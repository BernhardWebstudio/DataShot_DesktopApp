package edu.harvard.mcz.imagecapture.jobs;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.SkipSpecimenException;
import edu.harvard.mcz.imagecapture.interfaces.ProgressListener;
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob;
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.serializer.nahima.Specimen2JSONSerializer;
import edu.harvard.mcz.imagecapture.utility.CastUtility;
import edu.harvard.mcz.imagecapture.utility.FileUtility;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NahimaExportJob implements RunnableJob, Runnable {
  private static final Logger log =
      LoggerFactory.getLogger(NahimaExportJob.class);
  public static int STATUS_NAHIMA_FAILED = 3;
  public static int STATUS_DATASHOT_FAILED = 1;
  public static int STATUS_RUNNING_FINE = 0;
  public static int STATUS_FINISHED = -1;
  private final List<ProgressListener> progressListener = new ArrayList<>();
  private Date start = null;
  private int nrOfSpecimenToProcess = 0;
  private final AtomicInteger currentIndex = new AtomicInteger(0);
  private volatile int status = STATUS_RUNNING_FINE;
  private volatile Exception lastError = null;
  private String oneSpecimenBarcode = null;
  private List<Long> specimenToExport = null;
  private boolean anon = false;
  private ExecutorService executorService;
  private static final int DEFAULT_THREAD_POOL_SIZE = 8;
  private final AtomicInteger processedCount = new AtomicInteger(0);
  private final AtomicInteger newlyExportedCount = new AtomicInteger(0);
  private final AtomicInteger updatedCount = new AtomicInteger(0);
  private final AtomicInteger skippedCount = new AtomicInteger(0);
  private final AtomicInteger failedCount = new AtomicInteger(0);
  private volatile String overallStatus = "Initializing...";

  // ThreadLocal instances for thread safety
  private ThreadLocal<NahimaManager> threadLocalNahimaManager;
  private ThreadLocal<SpecimenLifeCycle> threadLocalSpecimenLifeCycle;

  public void setOneSpecimenToExportBarcode(String oneSpecimenBarcode) {
    this.oneSpecimenBarcode = oneSpecimenBarcode;
  }

  public void setSpecimenToExport(List<Specimen> specimenToExport) {
    this.specimenToExport = specimenToExport.stream()
                                .map(Specimen::getSpecimenId)
                                .collect(Collectors.toList());
  }

  public void goAnon() { this.anon = true; }

  private int getConfiguredThreadPoolSize() {
    Properties properties =
        Singleton.getSingletonInstance().getProperties().getProperties();
    String threadPoolSizeStr = properties.getProperty(
        ImageCaptureProperties.KEY_NAHIMA_THREAD_POOL_SIZE,
        String.valueOf(DEFAULT_THREAD_POOL_SIZE));
    try {
      int size = Integer.parseInt(threadPoolSizeStr);
      return Math.max(1, size); // Ensure at least 1 thread
    } catch (NumberFormatException e) {
      log.warn("Invalid thread pool size configuration '{}', using default: {}",
               threadPoolSizeStr, DEFAULT_THREAD_POOL_SIZE);
      return DEFAULT_THREAD_POOL_SIZE;
    }
  }

  @Override
  public void start() throws Exception {
    this.start = new Date();
    // fetch specimen to be exported
    notifyWorkStatusChanged("Loading specimens to export.");
    SpecimenLifeCycle sls = new SpecimenLifeCycle();
    if (this.anon) {
      sls.goAnon();
    }
    if (specimenToExport == null) {
      if (this.oneSpecimenBarcode != null &&
          !this.oneSpecimenBarcode.isEmpty()) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("barcode", this.oneSpecimenBarcode);
        specimenToExport = sls.findBy(queryParams)
                               .stream()
                               .map(Specimen::getSpecimenId)
                               .collect(Collectors.toList());
      } else {
        specimenToExport = sls.findIdsToExport();
      }
    }
    Collections.shuffle(specimenToExport);
    nrOfSpecimenToProcess = specimenToExport.size();
    notifyProgressChanged();
    notifyWorkStatusChanged(
        "Loaded " + nrOfSpecimenToProcess +
        " specimen to export to Nahima. This can take a while.");

    // initialize Nahima manager and ThreadLocal instances
    Properties properties =
        Singleton.getSingletonInstance().getProperties().getProperties();

    // Initialize ThreadLocal instances for thread-safe access
    this.threadLocalNahimaManager = ThreadLocal.withInitial(() -> {
      try {
        NahimaManager manager = new NahimaManager(
            properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL),
            properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_USER),
            properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_PASSWORD),
            CastUtility.castToBoolean(properties.getOrDefault(
                ImageCaptureProperties.KEY_NAHIMA_INTERACTIVE, true)));
        return manager;
      } catch (IOException | InterruptedException e) {
        log.error("Failed to create thread-local NahimaManager", e);
        throw new RuntimeException(
            "Failed to create thread-local NahimaManager", e);
      }
    });

    this.threadLocalSpecimenLifeCycle = ThreadLocal.withInitial(() -> {
      SpecimenLifeCycle slc = new SpecimenLifeCycle();
      if (this.anon) {
        slc.goAnon();
      }
      return slc;
    });

    // Test the main thread manager creation to ensure configuration is valid
    try {
      NahimaManager testManager = this.threadLocalNahimaManager.get();
      // Validate that the manager was created successfully
      log.debug(
          "Successfully created NahimaManager for thread safety validation");
    } catch (Exception e) {
      lastError = e;
      log.error("Failed to instantiate NahimaManager", e);
      this.status = STATUS_NAHIMA_FAILED;
      throw e;
    }

    // Create thread pool for parallel processing
    int threadPoolSize = getConfiguredThreadPoolSize();
    threadPoolSize = Math.min(threadPoolSize, nrOfSpecimenToProcess);
    executorService = Executors.newFixedThreadPool(threadPoolSize);

    overallStatus = "Starting export with " + threadPoolSize + " threads...";
    notifyWorkStatusChanged(overallStatus);

    try {
      // Submit all specimen processing tasks
      List<Future<Void>> futures = new ArrayList<>();
      for (Long specimenId : specimenToExport) {
        Future<Void> future = executorService.submit(() -> {
          processSpecimen(specimenId);
          return null;
        });
        futures.add(future);
      }

      // Wait for all tasks to complete
      for (Future<Void> future : futures) {
        try {
          future.get();
        } catch (ExecutionException e) {
          Throwable cause = e.getCause();
          if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
          } else if (cause instanceof Exception) {
            throw (Exception)cause;
          } else {
            throw new RuntimeException(
                "Unexpected error during specimen processing (\"" +
                    cause.getLocalizedMessage() + "\")",
                cause);
          }
        }
      }

      this.status = STATUS_FINISHED;
      overallStatus = String.format(
          "Export completed! Newly Exported: %d, Updated: %d, Skipped: %d, "
              + "Failed: %d of %d total specimens",
          newlyExportedCount.get(), updatedCount.get(), skippedCount.get(),
          failedCount.get(), nrOfSpecimenToProcess);
      notifyWorkStatusChanged(overallStatus);
    } finally {
      // Clean up ThreadLocal instances to prevent memory leaks
      if (threadLocalNahimaManager != null) {
        threadLocalNahimaManager.remove();
      }
      if (threadLocalSpecimenLifeCycle != null) {
        threadLocalSpecimenLifeCycle.remove();
      }

      if (executorService != null) {
        executorService.shutdown();
        try {
          if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
          }
        } catch (InterruptedException e) {
          executorService.shutdownNow();
          Thread.currentThread().interrupt();
        }
      }
    }
  }

  private void processSpecimen(Long specimenId) throws Exception {
    // Use thread-local instances for thread safety
    NahimaManager threadLocalManager = this.threadLocalNahimaManager.get();
    SpecimenLifeCycle sls = this.threadLocalSpecimenLifeCycle.get();

    Specimen specimen = sls.findById(specimenId);
    int index = currentIndex.incrementAndGet();

    // check if it hasn't been exported by another instance already
    if (specimen.getDateLastNahimaUpdated() != null &&
        specimen.getDateLastNahimaUpdated().after(
            specimen.getDateLastUpdated()) &&
        this.oneSpecimenBarcode == null) {
      log.debug("Skipping export of specimen " + index + "/" +
                nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() +
                ".");
      skippedCount.incrementAndGet();
      updateOverallStatus();
      notifyProgressChanged();
      return;
    }

    log.debug("Exporting specimen " + index + "/" + nrOfSpecimenToProcess +
              " with id " + specimen.getSpecimenId() + " and barcode " +
              specimen.getBarcode());
    updateOverallStatus();

    // check if all images are present, otherwise, can skip anyway
    boolean allFound = true;
    for (ICImage image : specimen.getICImages()) {
      String imagePath = FileUtility.findValidFilepath(
          ImageCaptureProperties.assemblePathWithBase(image.getPath(),
                                                      image.getFilename()),
          image.getPath() + File.separator + image.getFilename(),
          image.getPath());
      if (!(new File(imagePath)).exists()) {
        log.warn("Image file {} was not found.", imagePath);
        allFound = false;
      }
    }
    if (!allFound) {
      log.debug("Skipping export of specimen " + index + "/" +
                nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() +
                " because not all images were found.");
      skippedCount.incrementAndGet();
      updateOverallStatus();
      notifyProgressChanged();
      return;
    }

    Specimen2JSONSerializer serializer =
        new Specimen2JSONSerializer(threadLocalManager);

    // map, associate where possible/needed
    JSONObject specimenJson;
    JSONObject existingExport = null;
    boolean isUpdate = false; // Track whether this is an update or new export
    try {
      if (specimen.getNahimaId() != null && !specimen.getNahimaId().isEmpty()) {
        try {
          existingExport = threadLocalManager.findObjectByGlobalObjectId(
              specimen.getNahimaId());
        } catch (IOException | InterruptedException e) {
          log.warn("NoExport: skip specimen exception when searching by "
                   + "global object");
          throw new SkipSpecimenException();
        }
        log.debug("Found specimen to update in Nahima");
        isUpdate = true;
        specimenJson = serializer.serialize2JSON(specimen, existingExport);
      } else {
        specimenJson = serializer.serialize2JSON(specimen);
      }
    } catch (SkipSpecimenException e) {
      log.debug("NoExport: skip specimen exception");
      skippedCount.incrementAndGet();
      updateOverallStatus();
      notifyProgressChanged();
      return;
    }

    if (specimenJson == null) {
      log.debug("NoExport: Specimen json null, cannot upload stuff");
      skippedCount.incrementAndGet();
      updateOverallStatus();
      notifyProgressChanged();
      return;
    }

    // upload images
    ArrayList<JSONObject> uploadedImages = new ArrayList<>();
    boolean uploadSucceeded = false;
    int attempts = 0;
    while (!uploadSucceeded) {
      attempts += 1;
      try {
        uploadedImages = threadLocalManager.uploadImagesForSpecimen(
            specimen, existingExport);
        uploadSucceeded = true;
      } catch (Exception e) {
        lastError = e;
        log.error("Failed to upload images", e);
        // Don't update UI for individual thread retries - too noisy
        try {
          Thread.sleep((long)attempts * 5 *
                       60000); // wait 5 minute before retrying
        } catch (InterruptedException e1) {
          log.error("Interrupted while waiting for retry", e1);
          Thread.currentThread().interrupt();
          throw new RuntimeException("Thread interrupted during retry wait",
                                     e1);
        }
        if (attempts > 10) {
          this.status = STATUS_NAHIMA_FAILED;
          failedCount.incrementAndGet();
          updateOverallStatus();
          throw new RuntimeException(
              "Failed to upload images – 10 attempts failed.", e);
        }
      }
    }

    log.debug("Uploaded " + uploadedImages.size() + " images for specimen " +
              index + "/" + nrOfSpecimenToProcess + " with id " +
              specimen.getSpecimenId() + " and barcode " +
              specimen.getBarcode());

    // add images
    JSONArray mediaAssets = new JSONArray();
    for (JSONObject imageJsonObj : uploadedImages) {
      JSONObject mediaAsset;
      if (imageJsonObj.has("mediaassetpublic")) {
        mediaAsset = imageJsonObj;
      } else {
        Map<String, Object> reducedMediaAssetMap = new HashMap<>() {
          {
            put("_pool", NahimaManager.entomologyPool);
            put("_id", JSONObject.NULL);
            put("_version", 1);
            put("datei", (new JSONArray()).put(new JSONObject(new HashMap<>() {
              {
                put("preferred", true);
                put("_id", imageJsonObj.get("_id"));
              }
            })));
            put("barcode", specimen.getBarcode());
            put("urspruelicherdateiname",
                imageJsonObj.get("original_filename"));
            // empty, potentially later relevant stuff
            put("mediaassettyp", JSONObject.NULL);
            put("dateiname", JSONObject.NULL);
            put("dateigroesse", JSONObject.NULL);
            put("titel", JSONObject.NULL);
            put("beschreibung", JSONObject.NULL);
            put("lizenz_dcterms_licence", JSONObject.NULL);
            put("rechteinhaber_person", JSONObject.NULL);
            put("rechteinhaber_koeperschaft", JSONObject.NULL);
            put("aufnahmedatum_dc_date", JSONObject.NULL);
            put("format_dc_format", JSONObject.NULL);
            put("_nested:mediaasset__urheber_dc_creator", new JSONArray());
            put("_nested:mediaasset__schlagworte", new JSONArray());
            put("_reverse_nested:entomologie_mediaassetpublic:mediaassetpublic",
                new JSONArray());
            put("_reverse_nested:entomologie_mediaassetnonpublic:"
                    + "mediaassetpublic",
                new JSONArray());
            put("_reverse_nested:entomologie_mediaassetdeleted:"
                    + "mediaassetdeleted",
                new JSONArray());
            put("_reverse_nested:erdwissenschaften_mediaassetpublic:"
                    + "mediaassetpublic",
                new JSONArray());
            put("_reverse_nested:erdwissenschaften_mediaassetnonpublic:"
                    + "mediaassetnonpublic",
                new JSONArray());
            put("_reverse_nested:erdwissenschaften_mediaassetdeleted:"
                    + "mediaassetdeleted",
                new JSONArray());
            put("_reverse_nested:herbarien_mediaassetpublic:mediaassetpublic",
                new JSONArray());
            put("_reverse_nested:herbarien_mediaassetnonpublic:"
                    + "mediaassetnonpublic",
                new JSONArray());
            put("_reverse_nested:herbarien_mediaassetdeleted:mediaassetdeleted",
                new JSONArray());
          }
        };
        JSONObject reducedImageMediaAsset =
            new JSONObject(reducedMediaAssetMap);
        Map<String, Object> publicMediaAssetMap = new HashMap<>() {
          {
            put("_objecttype", "mediaasset");
            put("_mask", "mediaassetminimal");
            put("_tags", new JSONArray());
            put("mediaasset", reducedImageMediaAsset);
          }
        };
        JSONObject publicMediaAsset = new JSONObject(publicMediaAssetMap);
        // create, reduce
        JSONObject createdMediaAsset = null;
        try {
          createdMediaAsset = threadLocalManager.createObjectInNahima(
              publicMediaAsset, "mediaasset");
          if (createdMediaAsset.has("code") &&
              createdMediaAsset.getString("code").startsWith("error")) {
            throw new RuntimeException(
                "Failed to save mediaasset in nahima. Error code: " +
                createdMediaAsset.get("code"));
          }
        } catch (IOException | InterruptedException e) {
          lastError = e;
          log.error("Failed to create new Nahima specimen", e);
          this.status = STATUS_NAHIMA_FAILED;
          throw new RuntimeException("Failed to create media asset", e);
        }

        Map<String, Object> mediaAssetMap = new HashMap<>() {
          {
            put("_id", JSONObject.NULL);
            put("source_reference", JSONObject.NULL);
            put("__idx", 0);
            put("_version", 1);
            put("mediaassetpublic", publicMediaAsset);
          }
        };
        mediaAsset = new JSONObject(mediaAssetMap);

        reducedImageMediaAsset.put(
            "_id", createdMediaAsset.getJSONObject("mediaasset").getInt("_id"));
        publicMediaAsset.put("mediaasset", reducedImageMediaAsset);
        mediaAsset.put("mediaassetpublic", publicMediaAsset);
      }

      mediaAssets.put(mediaAsset);
    }
    // attach media to specimen
    JSONObject imageTarget = (JSONObject)specimenJson.get("entomologie");
    imageTarget.put("_reverse_nested:entomologie_mediaassetpublic:entomologie",
                    mediaAssets);
    specimenJson.put("entomologie",
                     imageTarget); // overwrite loaded value again

    // create in Nahima
    JSONObject result = null;
    try {
      result =
          threadLocalManager.createObjectInNahima(specimenJson, "entomologie");
      if (result.has("code") && result.getString("code").startsWith("error")) {
        throw new RuntimeException(
            "Failed to save specimen in nahima. Error code: " +
            result.get("code"));
      }
    } catch (IOException | InterruptedException | RuntimeException e) {
      lastError = e;
      log.error("Failed to create new Nahima specimen", e);
      this.status = STATUS_NAHIMA_FAILED;
      failedCount.incrementAndGet();
      updateOverallStatus();
      throw e;
    }

    // then, finally, mark specimen as exported
    specimen.setNahimaExported(true);
    if (Objects.equals(specimen.getWorkFlowStatus(),
                       WorkFlowStatus.STAGE_CLEAN)) {
      specimen.setWorkFlowStatus(WorkFlowStatus.STAGE_DONE);
    }
    specimen.setNahimaId(result.getString("_global_object_id"));
    specimen.setDateLastNahimaUpdated(new Date());
    if (specimen.getSpecimenId() != null && specimen.getSpecimenId() > 0) {
      try {
        log.debug("Will update the specimen with id " +
                  specimen.getSpecimenId() + " in the database after export.");
        sls.attachDirty(specimen);
      } catch (SaveFailedException e) {
        lastError = e;
        log.error("Failed to store Nahima export status", e);
        this.status = STATUS_DATASHOT_FAILED;
        failedCount.incrementAndGet();
        updateOverallStatus();
        throw e;
      }
    } else {
      log.debug("No specimen id, will not update the database");
    }
    processedCount.incrementAndGet();
    // Increment the appropriate specific counter based on whether this was an
    // update or new export
    if (isUpdate) {
      updatedCount.incrementAndGet();
    } else {
      newlyExportedCount.incrementAndGet();
    }
    updateOverallStatus();
    notifyProgressChanged();
  }

  @Override
  public boolean stop() {
    // Clean up ThreadLocal instances to prevent memory leaks
    if (threadLocalNahimaManager != null) {
      threadLocalNahimaManager.remove();
    }
    if (threadLocalSpecimenLifeCycle != null) {
      threadLocalSpecimenLifeCycle.remove();
    }

    if (executorService != null && !executorService.isShutdown()) {
      executorService.shutdown();
      try {
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
          executorService.shutdownNow();
        }
      } catch (InterruptedException e) {
        executorService.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
    return true;
  }

  @Override
  public boolean cancel() {
    return stop();
  }

  @Override
  public int getStatus() {
    return this.status;
  }

  @Override
  public int percentComplete() {
    if (status == STATUS_FINISHED) {
      return 100;
    }
    // prevent division by 0 or undefined
    if (nrOfSpecimenToProcess < 1) {
      return 0;
    }
    return 100 * currentIndex.get() / nrOfSpecimenToProcess;
  }

  @Override
  public boolean registerListener(RunnerListener aJobListener) {
    return false;
  }

  @Override
  public String getName() {
    return "Export Specimen to Nahima";
  }

  @Override
  public Date getStartTime() {
    return this.start;
  }

  @Override
  public void run() {
    try {
      start();
    } catch (Exception e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException)e;
      }
      throw new RuntimeException("Runner has been interrupted due to error: " +
                                 e.getMessage());
    }
  }

  /**
   * Register a listener for progress
   *
   * @param listener
   */
  public void addProgressListener(ProgressListener listener) {
    this.progressListener.add(listener);
  }

  /**
   * Propagate the progress to all the listeners
   */
  public void notifyProgressChanged() {
    for (ProgressListener listener : progressListener) {
      listener.progressChanged(this.percentComplete());
    }
  }

  /**
   * Propagate the progress to all the listeners
   */
  public void notifyWorkStatusChanged(String status) {
    for (ProgressListener listener : progressListener) {
      listener.currentWorkStatusChanged(status);
    }
  }

  /**
   * Update the overall status message based on current progress
   */
  private synchronized void updateOverallStatus() {
    int total = processedCount.get() + skippedCount.get() + failedCount.get();
    overallStatus = String.format(
        "Progress: %d/%d specimens | Newly Exported: %d | Updated: %d | " +
        "Skipped: %d | Failed: %d",
        total, nrOfSpecimenToProcess, newlyExportedCount.get(),
        updatedCount.get(), skippedCount.get(), failedCount.get());
    notifyWorkStatusChanged(overallStatus);
  }

  /**
   * @return the last (and probably only) Exception encountered.
   */
  public Exception getLastException() { return lastError; }

  /**
   * @return the total nr. of specimen to process/export
   */
  public int getTotalCount() { return nrOfSpecimenToProcess; }

  /**
   * @return the number of specimen successfully processed
   */
  public int getProcessedCount() { return processedCount.get(); }

  /**
   * @return the number of specimen newly exported
   */
  public int getNewlyExportedCount() { return newlyExportedCount.get(); }

  /**
   * @return the number of specimen updated
   */
  public int getUpdatedCount() { return updatedCount.get(); }

  /**
   * @return the number of specimen skipped
   */
  public int getSkippedCount() { return skippedCount.get(); }

  /**
   * @return the number of specimen that failed to process
   */
  public int getFailedCount() { return failedCount.get(); }
}
