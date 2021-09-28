package edu.harvard.mcz.imagecapture.jobs;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException;
import edu.harvard.mcz.imagecapture.interfaces.ProgressListener;
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob;
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.serializer.nahima.Specimen2JSONSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

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

    @Override
    public void start() {
        this.start = new Date();
        // fetch specimen to be exported
        SpecimenLifeCycle sls = new SpecimenLifeCycle();
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("workFlowStatus", WorkFlowStatus.STAGE_CLEAN);
        queryParams.put("nahimaExported", false);
        List<Specimen> specimenToExport = sls.findBy(queryParams);
        nrOfSpecimenToProcess = specimenToExport.size();
        // initialize Nahima manager
        Properties properties = Singleton.getSingletonInstance().getProperties().getProperties();
        NahimaManager manager;
        try {
            manager = new NahimaManager(properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_URL), properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_USER), properties.getProperty(ImageCaptureProperties.KEY_NAHIMA_PASSWORD));
        } catch (Exception e) {
            lastError = e;
            log.error("Failed to instantiate NahimaManager", e);
            this.status = STATUS_NAHIMA_FAILED;
            return;
        }

        Specimen2JSONSerializer serializer = new Specimen2JSONSerializer(manager);

        for (Specimen specimen : specimenToExport) {
            currentIndex = currentIndex + 1;
            log.debug("Exporting specimen with id " + specimen.getSpecimenId() + " and barcode " + specimen.getBarcode());
            // upload images
            Object[] uploadedImages;
            try {
                uploadedImages = manager.uploadImagesForSpecimen(specimen);
            } catch (Exception e) {
                lastError = e;
                log.error("Failed to upload images", e);
                this.status = STATUS_NAHIMA_FAILED;
                return;
            }

            // map, associate where possible/needed
            JSONObject specimenJson = serializer.serialize2JSON(specimen);
            // add images
            JSONObject imageTarget = (JSONObject) specimenJson.get("entomologie");
            JSONArray mediaAssets = new JSONArray();
            for (Object image : uploadedImages) {
                JSONObject imageJsonObj = (JSONObject) image;
                Map<String, Object> reducedMediaAssetMap = new HashMap<>() {{
                    put("_pool", NahimaManager.defaultPool);
                    put("_id", JSONObject.NULL);
                    put("_version", 1);
                    put("datei", (new JSONArray()).put(new JSONObject(new HashMap<>() {{
                        put("preferred", true);
                        put("_id", imageJsonObj.get("_id"));
                    }})));
                    put("barcode", specimen.getBarcode());
                    put("urspruelicherdateiname", imageJsonObj.get("original_filename"));
                }};
                JSONObject reducedImageMediaAsset = new JSONObject(reducedMediaAssetMap);
                Map<String, Object> publicMediaAssetMap = new HashMap<>() {{
                    put("_objecttype", "mediaasset");
                    put("_mask", "mediaassetminimal");
                    put("_tags", new JSONArray());
                    put("mediaasset", reducedImageMediaAsset);
                }};
                JSONObject publicMediaAsset = new JSONObject(publicMediaAssetMap);

                Map<String, Object> mediaAssetMap = new HashMap<>() {{
                    put("_id", JSONObject.NULL);
                    put("source_reference", JSONObject.NULL);
                    put("__idx", 0);
                    put("_version", 1);
                    put("mediassetpublic", publicMediaAsset);
                }};
                JSONObject mediaAsset = new JSONObject(mediaAssetMap);
                mediaAssets.put(mediaAsset);
            }
            imageTarget.put("_reverse_nested:entomologie_mediaassetpublic:entomologie", mediaAssets);

            // create in Nahima
            try {
                JSONObject result = manager.createObjectInNahima(specimenJson, "entomologie");
                if (result.has("code") && result.getString("code").startsWith("error")) {
                    throw new RuntimeException("Failed to save specimen in nahima. Error code: " + result.get("code"));
                }
            } catch (IOException | InterruptedException | RuntimeException e) {
                lastError = e;
                log.error("Failed to create new Nahima specimen", e);
                this.status = STATUS_NAHIMA_FAILED;
                return;
            }

            // then, finally, mark specimen as exported
            specimen.setNahimaExported(true);
            try {
                sls.attachDirty(specimen);
                sls.persist(specimen);
            } catch (SaveFailedException | SpecimenExistsException e) {
                lastError = e;
                log.error("Failed to store Nahima export status", e);
                this.status = STATUS_DATASHOT_FAILED;
                return;
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
        start();
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
