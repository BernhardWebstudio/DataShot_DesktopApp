package edu.harvard.mcz.imagecapture.jobs;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.RunStatus;
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob;
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ThumbnailBuilderJob implements Runnable, RunnableJob {

  private static final Logger log =
      LoggerFactory.getLogger(ThumbnailBuilderJob.class);
  File startPoint;
  AtomicInteger thumbnailCounter;
  String thumbHeight;
  String thumbWidth;
  private Date thumbStartTime = null;
  private int thumbRunStatus = RunStatus.STATUS_NEW;
  private int thumbPercentComplete = 0;
  private ArrayList<RunnerListener> thumbListeners = null;

  public ThumbnailBuilderJob(File aStartPoint, AtomicInteger counter) {
    thumbnailCounter = counter;
    startPoint = aStartPoint;
    thumbHeight = Singleton.getSingletonInstance()
                      .getProperties()
                      .getProperties()
                      .getProperty(ImageCaptureProperties.KEY_THUMBNAIL_HEIGHT);
    thumbWidth = Singleton.getSingletonInstance()
                     .getProperties()
                     .getProperties()
                     .getProperty(ImageCaptureProperties.KEY_THUMBNAIL_WIDTH);
    thumbInit();
  }

  public ThumbnailBuilderJob(File aStartPoint, int thumbHeightPixels,
                             int thumbWidthPixels, AtomicInteger counter) {
    thumbnailCounter = counter;
    startPoint = aStartPoint;
    thumbHeight = Integer.toString(thumbHeightPixels);
    thumbWidth = Integer.toString(thumbWidthPixels);
    if (thumbHeightPixels < 10) {
      thumbHeight =
          Singleton.getSingletonInstance()
              .getProperties()
              .getProperties()
              .getProperty(ImageCaptureProperties.KEY_THUMBNAIL_HEIGHT);
    }
    if (thumbWidthPixels < 10) {
      thumbWidth = Singleton.getSingletonInstance()
                       .getProperties()
                       .getProperties()
                       .getProperty(ImageCaptureProperties.KEY_THUMBNAIL_WIDTH);
    }
    thumbInit();
  }

  protected void thumbInit() {
    thumbListeners = new ArrayList<RunnerListener>();
  }

  @Override
  public void run() {
    thumbStartTime = new Date();
    thumbRunStatus = RunStatus.STATUS_RUNNING;
    setThumbPercentComplete(0);
    Singleton.getSingletonInstance().getJobList().addJob(this);
    // mkdir thumbs ; mogrify -path thumbs -resize 80x120 *.JPG
    if (startPoint.isDirectory() && (!startPoint.getName().equals("thumbs"))) {
      File thumbsDir =
          new File(startPoint.getPath() + File.separator + "thumbs");
      // log.debug("Debug {}", thumbsDir.getPath());
      if (!thumbsDir.exists()) {
        thumbsDir.mkdir();
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
            "Creating " + thumbsDir.getPath());
      }
      // Runtime executes mogrify directly, not through a shell, thus expand
      // list of files to pass rather than passing *.JPG
      File[] potentialFilesToThumb = startPoint.listFiles();
      StringBuffer filesToThumb = new StringBuffer();
      int filesToThumbCount = 0;
      for (int i = 0; i < potentialFilesToThumb.length; i++) {
        if (potentialFilesToThumb[i].getName().endsWith(".JPG")) {
          filesToThumb.append(potentialFilesToThumb[i].getName()).append(" ");
          filesToThumbCount++;
        }
      }
      if (filesToThumbCount > 0) {
        boolean makeWithJava = false;

        String mogrify =
            Singleton.getSingletonInstance()
                .getProperties()
                .getProperties()
                .getProperty(ImageCaptureProperties.KEY_MOGRIFY_EXECUTABLE);
        if (mogrify == null || mogrify.trim().length() > 0) {
          makeWithJava = true;
        } else {
          String runCommand = mogrify + " -path thumbs -resize " + thumbWidth +
                              "x" + thumbHeight + " " + filesToThumb.toString();

          Runtime r = Runtime.getRuntime();
          // log.debug("Debug {}", runCommand);
          try {
            String[] env = {""};
            Process proc = r.exec(runCommand, env, startPoint);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isrstderr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isrstderr);
            String line = null;
            while ((line = br.readLine()) != null) {
              log.debug("stderr:" + line);
            }
            int exitVal = proc.waitFor();
            log.debug("Mogrify Process exitValue: " + exitVal);
            if (exitVal == 0) {
              this.thumbnailCounter.incrementAndGet();
              String message =
                  "Finished creating thumbnails in: " + startPoint.getPath();
              Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                  message);
              log.debug("Debug {}", message);
            } else {
              log.error("Error returned running " + runCommand);
              makeWithJava = true;
            }
          } catch (IOException e) {
            log.error("Error running: " + runCommand);
            e.printStackTrace();
            Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                "Error creating thumbnails " + e.getMessage());
            makeWithJava = true;
          } catch (InterruptedException e) {
            log.error("Mogrify process interupted");
            e.printStackTrace();
          }
        }

        if (makeWithJava) {
          for (int i = 0; i < potentialFilesToThumb.length; i++) {
            try {
              log.debug("Attempting thumbnail generation with java in " +
                        startPoint.getPath());
              log.debug("Attempting thumbnail generation with java to " +
                        thumbsDir.getPath());
              ArrayList<String> makeFrom = new ArrayList<String>();
              List<File> files = Arrays.asList(startPoint.listFiles());
              Iterator<File> it = files.iterator();
              int creationCounter = 0;
              int totalFiles = files.size();
              while (it.hasNext() &&
                     thumbRunStatus != RunStatus.STATUS_CANCEL_REQUESTED) {
                File file = it.next();
                if (!file.isDirectory() && file.exists() && file.canRead()) {
                  // file must exist and be readable to make thumbnail
                  if (file.getName().matches(
                          Singleton.getSingletonInstance()
                              .getProperties()
                              .getProperties()
                              .getProperty(
                                  ImageCaptureProperties.KEY_IMAGEREGEX))) {
                    // only try to make thumbnails of files that match the image
                    // file pattern.
                    makeFrom.add(file.getPath());
                    // log.debug("Debug {}", file.getPath());
                    File target = new File(thumbsDir.getPath() +
                                           File.separatorChar + file.getName());
                    // log.debug("Debug {}", target.getPath());
                    if (!target.exists()) {
                      BufferedImage img =
                          new BufferedImage(Integer.parseInt(thumbWidth),
                                            Integer.parseInt(thumbHeight),
                                            BufferedImage.TYPE_INT_RGB);
                      img.createGraphics().drawImage(
                          ImageIO.read(file).getScaledInstance(
                              80, 120, Image.SCALE_SMOOTH),
                          0, 0, null);
                      ImageIO.write(img, "jpg", target);
                      creationCounter++;
                    }
                  }
                  setThumbPercentComplete(
                      (int)(((float)creationCounter / totalFiles) * 100));
                }
              }
              if (creationCounter > 0) {
                String message = "Finished creating thumbnails (" +
                                 creationCounter +
                                 ") in: " + startPoint.getPath();
                Singleton.getSingletonInstance()
                    .getMainFrame()
                    .setStatusMessage(message);
              }
            } catch (IOException e) {
              log.error(
                  "Thumbnail generation with thumbnailator library failed");
              log.error(e.getMessage());
            }
          }
        }

      } else {
        String message = "No *.JPG files found in " + startPoint.getPath();
        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
            message);
        log.debug("Message: " + message);
      }
    }
    String message = "Thumbnail Generation Complete.";
    Singleton.getSingletonInstance().getMainFrame().setStatusMessage(message);
    Singleton.getSingletonInstance().getJobList().removeJob(this);
  }

  /**
   * @see RunnableJob#start()
   */
  @Override
  public void start() {
    run();
  }

  /**
   * @see RunnableJob#stop()
   */
  @Override
  public boolean stop() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see RunnableJob#cancel()
   */
  @Override
  public boolean cancel() {
    thumbRunStatus = RunStatus.STATUS_CANCEL_REQUESTED;
    return false;
  }

  /**
   * @see RunnableJob#getStatus()
   */
  @Override
  public int getStatus() {
    return thumbRunStatus;
  }

  /**
   * @see RunnableJob#percentComplete()
   */
  @Override
  public int percentComplete() {
    return thumbPercentComplete;
  }

  protected void setThumbPercentComplete(int aPercentage) {
    // set value
    thumbPercentComplete = aPercentage;
    log.debug("Debug {}", thumbPercentComplete);
    // notify listeners
    Iterator<RunnerListener> i = thumbListeners.iterator();
    while (i.hasNext()) {
      i.next().notifyListener(thumbPercentComplete, this);
    }
  }

  /**
   * @see RunnableJob#registerListener(RunnerListener)
   */
  @Override
  public boolean registerListener(RunnerListener aJobListener) {
    if (thumbListeners == null) {
      thumbInit();
    }
    return thumbListeners.add(aJobListener);
  }

  /**
   * @see RunnableJob#getName()
   */
  @Override
  public String getName() {
    return "Thumbnail Generation in: " + startPoint;
  }

  /**
   * @see RunnableJob#getStartTime()
   */
  @Override
  public Date getStartTime() {
    return thumbStartTime;
  }

} // end class ThumbnailBuilder
