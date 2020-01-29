package edu.harvard.mcz.imagecapture.utility;

import edu.harvard.mcz.imagecapture.Singleton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class FileUtility {
    public static File askForDirectory(File startpoint) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setPreferredSize(new Dimension(800, 600));
        if (startpoint != null && startpoint.canRead()) {
            fileChooser.setCurrentDirectory(startpoint);
        }
        int returnValue = fileChooser.showOpenDialog(Singleton.getSingletonInstance().getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        } else {
            return null;
        }
    }

    public static File askForImageFile(File startpoint) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(startpoint);
        fileChooser.setPreferredSize(new Dimension(800, 600));
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("TIFF Images", "tif", "tiff");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "tif", "tiff", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(Singleton.getSingletonInstance().getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

}
