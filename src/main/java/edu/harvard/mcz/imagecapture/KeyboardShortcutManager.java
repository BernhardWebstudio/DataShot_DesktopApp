package edu.harvard.mcz.imagecapture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Filesystem persistence and retrieval of keyboard shortcuts.
 * This class provides the properties for the keyboard shortcuts
 * as well as an easy way to access/get/add them
 * <p>
 * Syntax for a shortcut: see https://javadoc.scijava.org/Java7/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)
 */
public class KeyboardShortcutManager extends AbstractTableModel {

    private static final Log log = LogFactory.getLog(KeyboardShortcutManager.class);
    private static KeyboardShortcutManager instance;
    private Properties properties = null;
    private String propertiesFilename = null;
    private StringBuffer propertiesFilePath = null;

    private KeyboardShortcutManager() {
        propertiesFilename = "imagecapture-shortcuts.properties";
        propertiesFilePath = new StringBuffer(System.getProperty("user.dir"));
        propertiesFilePath.append(System.getProperty("file.separator"));
        propertiesFilePath.append(propertiesFilename);
        try {
            properties = new Properties();
            FileInputStream propertiesStream = null;
            log.debug("Opening properties file: " + propertiesFilePath.toString());
            try {
                propertiesStream = new FileInputStream(propertiesFilePath.toString());
                properties.load(propertiesStream);
            } catch (FileNotFoundException e) {
                System.out.println("Error: Properties file not found.");
                throw e;
            } catch (Exception ex) {
                System.out.println("Error loading properties.");
                System.out.println(ex.getMessage());
                throw ex;
            } finally {
                if (propertiesStream != null) {
                    propertiesStream.close();
                }
            }
        } catch (Exception e) {
            // thrown if properties can't be loaded, create default properties.
            properties = new Properties();
            System.out.println("Failed loading properties. Error message: " + e.getMessage());
        }
    }

    public static synchronized KeyboardShortcutManager getInstance() {
        if (instance == null) {
            instance = new KeyboardShortcutManager();
        }
        return instance;
    }

    /**
     * Get the KeyStroke needed
     *
     * @param name          a unique ID for the action, used as key in the properties file
     * @param defaultStroke the KeyStroke to use otherwise
     * @return the Keystroke corresponding to the action identified by `name`
     */
    public KeyStroke getShortcut(String name, String defaultStroke) {
        String keyCombo = properties.getProperty(name, defaultStroke);

        if (!properties.containsKey(name) && defaultStroke != null) {
            properties.setProperty(name, defaultStroke);
            saveProperties();
        }

        try {
            return KeyStroke.getKeyStroke(keyCombo);
        } catch (Exception e) {
            KeyboardShortcutManager.log.error("Failed to return keyboard shortcut. Format them as described in https://javadoc.scijava.org/Java7/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)", e);
            System.out.println("...!");
            return null;
        }
    }

    public KeyStroke getShortcut(String name) {
        return getShortcut(name, null);
    }

    public void saveProperties() {
        FileOutputStream propertiesStream = null;
        try {
            System.out.println("Saving properties file: " + propertiesFilePath.toString());
            propertiesStream = new FileOutputStream(propertiesFilePath.toString());
            String comment = ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.getAppVersion() + " Properties";
            comment += "\nFormat: https://javadoc.scijava.org/Java7/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)";
            properties.store(propertiesStream, comment);
            propertiesStream.close();
        } catch (Exception e) {
            System.out.println("Error saving properties.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (propertiesStream != null) {
                try {
                    propertiesStream.close();
                } catch (Exception e) {
                    // oh well. let's just – well. ignore this case.
                    // keyboard shortcuts are not that important to require all cases handled
                }
            }
        }
    }

    @Override
    public int getRowCount() {
        return properties.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String value = "";
        Enumeration<Object> p = properties.keys();
        int element = 0;
        while (p.hasMoreElements()) {
            String e = (String) p.nextElement();
            if (element == rowIndex) {
                if (columnIndex == 0) {
                    value = e;
                } else {
                    value = properties.getProperty(e);
                }
            }
            element++;
        }
        return value;
    }

}
