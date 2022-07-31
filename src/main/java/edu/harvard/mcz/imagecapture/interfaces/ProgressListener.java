package edu.harvard.mcz.imagecapture.interfaces;

public interface ProgressListener {
    void progressChanged(int progress);
    void currentWorkStatusChanged(String status);
}
