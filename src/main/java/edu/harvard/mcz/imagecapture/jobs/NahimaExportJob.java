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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NahimaExportJob implements RunnableJob, Runnable {
    private static final Logger log = LoggerFactory.getLogger(NahimaExportJob.class);
    public static int STATUS_NAHIMA_FAILED = 3;
    public static int STATUS_DATASHOT_FAILED = 1;
    public static int STATUS_RUNNING_FINE = 0;
    public static int STATUS_FINISHED = -1;
    private final List<ProgressListener> progressListener = new ArrayList<>();
    private Date start = null;
    private int nrOfSpecimenToProcess = 0;
    private int currentIndex = 0;
    private int status = STATUS_RUNNING_FINE;
    private Exception lastError = null;
    private String oneSpecimenBarcode = null;
    private List<Long> specimenToExport = null;
    private boolean anon = false;

    public void setOneSpecimenToExportBarcode(String oneSpecimenBarcode) {
        this.oneSpecimenBarcode = oneSpecimenBarcode;
    }

    public void setSpecimenToExport(List<Specimen> specimenToExport) {
        this.specimenToExport = specimenToExport.stream().map(Specimen::getSpecimenId).collect(Collectors.toList());
    }

    public void goAnon() {
        this.anon = true;
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
            if (this.oneSpecimenBarcode != null && !this.oneSpecimenBarcode.isEmpty()) {
                Map<String, Object> queryParams = new HashMap<>();
                queryParams.put("barcode", this.oneSpecimenBarcode);
                specimenToExport = sls.findBy(queryParams).stream().map(Specimen::getSpecimenId).collect(Collectors.toList());
            } else {
                specimenToExport = sls.findIdsToExport();
            }
        }
        Collections.shuffle(specimenToExport);
        nrOfSpecimenToProcess = specimenToExport.size();
        notifyProgressChanged();
        notifyWorkStatusChanged("Loaded " + nrOfSpecimenToProcess + " specimen to export to Nahima. This can take a while.");
        // initialize Nahima manager
        Properties properties = Singleton.getSingletonInstance().getProperties().getProperties();
        NahimaManager manager;
        try {
            manager = new NahimaManager(
                    properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL),
                    properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_USER),
                    properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_PASSWORD),
                    CastUtility.castToBoolean(properties.getOrDefault(ImageCaptureProperties.KEY_NAHIMA_INTERACTIVE, true))
            );
        } catch (Exception e) {
            lastError = e;
            log.error("Failed to instantiate NahimaManager", e);
            this.status = STATUS_NAHIMA_FAILED;
            throw e;
        }

        Specimen2JSONSerializer serializer = new Specimen2JSONSerializer(manager);

        for (Long specimenId : specimenToExport) {
            Specimen specimen = sls.findById(specimenId);
            currentIndex = currentIndex + 1;
            // check if it hasn't been exported by another instance already
            if (specimen.getDateLastNahimaUpdated() != null && specimen.getDateLastNahimaUpdated().after(specimen.getDateLastUpdated()) && this.oneSpecimenBarcode == null) {
                log.debug("Skipping export of specimen " + currentIndex + "/" + nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() + ".");
                continue;
            }

            log.debug("Exporting specimen " + currentIndex + "/" + nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() + " and barcode " + specimen.getBarcode());
            notifyWorkStatusChanged("Exporting specimen " + currentIndex + "/" + nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() + " and barcode " + specimen.getBarcode());

            // check if all images are present, otherwise, can skip anyway
            boolean allFound = true;
            for (ICImage image : specimen.getICImages()) {
                String imagePath = FileUtility.findValidFilepath(
                        ImageCaptureProperties.assemblePathWithBase(image.getPath(), image.getFilename()),
                        image.getPath() + File.separator + image.getFilename(),
                        image.getPath()
                );
                if (!(new File(imagePath)).exists()) {
                    log.warn("Image file {} was not found.", imagePath);
                    allFound = false;
                }
            }
            if (!allFound) {
                log.debug("Skipping export of specimen " + currentIndex + "/" + nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() + " because not all images were found.");
                continue;
            }

            // map, associate where possible/needed
            JSONObject specimenJson;
            JSONObject existingExport = null;
            try {
                if (specimen.getNahimaId() != null && !specimen.getNahimaId().isEmpty()) {
                    try {
                        existingExport = manager.findObjectByGlobalObjectId(specimen.getNahimaId());
                    } catch (IOException | InterruptedException e) {
                        log.warn("NoExport: skip specimen exception when searching by global object");
                        throw new SkipSpecimenException();
                    }
                    log.debug("Found specimen to update in Nahima");
                    specimenJson = serializer.serialize2JSON(specimen, existingExport);
                } else {
                    specimenJson = serializer.serialize2JSON(specimen);
                }
            } catch (SkipSpecimenException e) {
                log.debug("NoExport: skip specimen exception");
                continue;
            }

            if (specimenJson == null) {
                // might want to add some checks
                // to make sure this only happens when the SkipSpecimenException is thrown
                log.debug("NoExport: Specimen json null, cannot upload stuff");
                continue;
            }

            // upload images
            ArrayList<JSONObject> uploadedImages = new ArrayList<>();
            boolean uploadSucceeded = false;
            int attempts = 0;
            while (!uploadSucceeded) {
                attempts += 1;
                try {
                    uploadedImages = manager.uploadImagesForSpecimen(specimen, existingExport);
                    uploadSucceeded = true;
                } catch (Exception e) {
                    lastError = e;
                    log.error("Failed to upload images", e);
                    notifyWorkStatusChanged("Failed to upload images for specimen " + currentIndex + "/"
                            + nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() + " and barcode " + specimen.getBarcode()
                            + " (attempt " + attempts + "). "
                            + "Retrying in " + 5 * attempts + " minutes...\n"
                            + "Error message: " + e.getMessage());
                    try {
                        Thread.sleep((long) attempts * 5 * 60000); // wait 5 minute before retrying
                    } catch (InterruptedException e1) {
                        log.error("Interrupted while waiting for retry", e1);
                    }
                    if (attempts > 10) {
                        this.status = STATUS_NAHIMA_FAILED;
                        throw new RuntimeException("Failed to upload images – 10 attempts failed.", e);
                    }
                }
            }

            notifyWorkStatusChanged("Uploaded " + uploadedImages.size() + " images for specimen " + currentIndex + "/" + nrOfSpecimenToProcess + " with id " + specimen.getSpecimenId() + " and barcode " + specimen.getBarcode());

            // add images
            JSONArray mediaAssets = new JSONArray();
            for (JSONObject imageJsonObj : uploadedImages) {
                JSONObject mediaAsset;
                if (imageJsonObj.has("mediaassetpublic")) {
                    mediaAsset = imageJsonObj;
                } else {
                    Map<String, Object> reducedMediaAssetMap = new HashMap<>() {{
                        put("_pool", NahimaManager.entomologyPool);
                        put("_id", JSONObject.NULL);
                        put("_version", 1);
                        put("datei", (new JSONArray()).put(new JSONObject(new HashMap<>() {{
                            put("preferred", true);
                            put("_id", imageJsonObj.get("_id"));
                        }})));
                        put("barcode", specimen.getBarcode());
                        put("urspruelicherdateiname", imageJsonObj.get("original_filename"));
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
                        put("_reverse_nested:entomologie_mediaassetpublic:mediaassetpublic", new JSONArray());
                        put("_reverse_nested:entomologie_mediaassetnonpublic:mediaassetpublic", new JSONArray());
                        put("_reverse_nested:entomologie_mediaassetdeleted:mediaassetdeleted", new JSONArray());
                        put("_reverse_nested:erdwissenschaften_mediaassetpublic:mediaassetpublic", new JSONArray());
                        put("_reverse_nested:erdwissenschaften_mediaassetnonpublic:mediaassetnonpublic", new JSONArray());
                        put("_reverse_nested:erdwissenschaften_mediaassetdeleted:mediaassetdeleted", new JSONArray());
                        put("_reverse_nested:herbarien_mediaassetpublic:mediaassetpublic", new JSONArray());
                        put("_reverse_nested:herbarien_mediaassetnonpublic:mediaassetnonpublic", new JSONArray());
                        put("_reverse_nested:herbarien_mediaassetdeleted:mediaassetdeleted", new JSONArray());
                    }};
                    JSONObject reducedImageMediaAsset = new JSONObject(reducedMediaAssetMap);
                    Map<String, Object> publicMediaAssetMap = new HashMap<>() {{
                        put("_objecttype", "mediaasset");
                        put("_mask", "mediaassetminimal");
                        put("_tags", new JSONArray());
                        put("mediaasset", reducedImageMediaAsset);
                    }};
                    JSONObject publicMediaAsset = new JSONObject(publicMediaAssetMap);
                    // create, reduce
                    JSONObject createdMediaAsset = null;
                    try {
                        createdMediaAsset = manager.createObjectInNahima(publicMediaAsset, "mediaasset");
                        if (createdMediaAsset.has("code") && createdMediaAsset.getString("code").startsWith("error")) {
                            throw new RuntimeException("Failed to save mediaasset in nahima. Error code: " + createdMediaAsset.get("code"));
                        }
                    } catch (IOException | InterruptedException e) {
                        lastError = e;
                        log.error("Failed to create new Nahima specimen", e);
                        this.status = STATUS_NAHIMA_FAILED;
                        return;
                    }

                    Map<String, Object> mediaAssetMap = new HashMap<>() {{
                        put("_id", JSONObject.NULL);
                        put("source_reference", JSONObject.NULL);
                        put("__idx", 0);
                        put("_version", 1);
                        put("mediaassetpublic", publicMediaAsset);
                    }};
                    mediaAsset = new JSONObject(mediaAssetMap);

                    reducedImageMediaAsset.put("_id", createdMediaAsset.getJSONObject("mediaasset").getInt("_id"));
                    publicMediaAsset.put("mediaasset", reducedImageMediaAsset);
                    mediaAsset.put("mediaassetpublic", publicMediaAsset);
                }

                mediaAssets.put(mediaAsset);
            }
            // attach media to specimen
            JSONObject imageTarget = (JSONObject) specimenJson.get("entomologie");
            imageTarget.put("_reverse_nested:entomologie_mediaassetpublic:entomologie", mediaAssets);
            specimenJson.put("entomologie", imageTarget); // overwrite loaded value again

            // create in Nahima
            JSONObject result = null;
            try {
                result = manager.createObjectInNahima(specimenJson, "entomologie");
                if (result.has("code") && result.getString("code").startsWith("error")) {
                    throw new RuntimeException("Failed to save specimen in nahima. Error code: " + result.get("code"));
                }
            } catch (IOException | InterruptedException | RuntimeException e) {
                lastError = e;
                log.error("Failed to create new Nahima specimen", e);
                this.status = STATUS_NAHIMA_FAILED;
                throw e;
            }

            // then, finally, mark specimen as exported
            specimen.setNahimaExported(true);
            if (Objects.equals(specimen.getWorkFlowStatus(), WorkFlowStatus.STAGE_CLEAN)) {
                specimen.setWorkFlowStatus(WorkFlowStatus.STAGE_DONE);
            }
            specimen.setNahimaId(result.getString("_global_object_id"));
            specimen.setDateLastNahimaUpdated(new Date());
            if (specimen.getSpecimenId() != null && specimen.getSpecimenId() > 0) {
                try {
                    log.debug("Will update the specimen with id " + specimen.getSpecimenId() + " in the database after export.");
                    sls.attachDirty(specimen);
                } catch (SaveFailedException e) {
                    lastError = e;
                    log.error("Failed to store Nahima export status", e);
                    this.status = STATUS_DATASHOT_FAILED;
                    throw e;
                }
            } else {
                log.debug("No specimen id, will not update the database");
            }
            notifyProgressChanged();
        }
        this.status = STATUS_FINISHED;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public boolean cancel() {
        return false;
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
        return 100 * currentIndex / nrOfSpecimenToProcess;
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
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Runner has been interrupted due to error: " + e.getMessage());
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
     * @return the last (and probably only) Exception encountered.
     */
    public Exception getLastException() {
        return lastError;
    }

    /**
     * @return the total nr. of specimen to process/export
     */
    public int getTotalCount() {
        return nrOfSpecimenToProcess;
    }
}
