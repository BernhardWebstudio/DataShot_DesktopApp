package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.interfaces.ProgressListener;
import edu.harvard.mcz.imagecapture.jobs.NahimaExportJob;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportApp implements ProgressListener {

    public ExportApp() {
        NahimaExportJob exportJob = new NahimaExportJob();
        exportJob.goAnon();
        exportJob.addProgressListener(this);
        exportJob.run();
        System.exit(exportJob.getStatus() == NahimaExportJob.STATUS_FINISHED ? 0 : 1);
    }

    public static void main(String[] args) {
        new ExportApp();
    }

    @Override
    public void progressChanged(int progress) {
        System.out.println("Progress: " + progress);
    }

    @Override
    public void currentWorkStatusChanged(String status) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " status: " + status);
    }
}
