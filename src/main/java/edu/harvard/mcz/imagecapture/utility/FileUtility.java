package edu.harvard.mcz.imagecapture.utility;

import edu.harvard.mcz.imagecapture.Singleton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class FileUtility {
    public static File askForDirectory(File startingPoint) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setPreferredSize(new Dimension(800, 600));
        if (startingPoint != null && startingPoint.exists() && startingPoint.canRead()) {
            fileChooser.setCurrentDirectory(startingPoint);
        }
        int returnValue = fileChooser.showOpenDialog(Singleton.getSingletonInstance().getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        } else {
            return null;
        }
    }

    public static File askForImageFile(File startingPoint) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600));
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("TIFF Images", "tif", "tiff");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "tif", "tiff", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        if (startingPoint != null && startingPoint.exists() && startingPoint.canRead()) {
            fileChooser.setCurrentDirectory(startingPoint);
        }
        int returnValue = fileChooser.showOpenDialog(Singleton.getSingletonInstance().getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static File askForCSVFile(File startingPoint) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600));
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("TIFF Images", "tif", "tiff");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fileChooser.setFileFilter(filter);
        if (startingPoint != null && startingPoint.exists() && startingPoint.canRead()) {
            fileChooser.setCurrentDirectory(startingPoint);
        }
        int returnValue = fileChooser.showOpenDialog(Singleton.getSingletonInstance().getMainFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static String findValidFilepath(String... paths) {
        for (String path : paths) {
            File f = new File(path);
            if (f.exists() && !f.isDirectory()) {
                return path;
            }
        }
        return paths[0];
    }
}
