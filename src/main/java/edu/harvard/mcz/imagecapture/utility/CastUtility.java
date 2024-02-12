package edu.harvard.mcz.imagecapture.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastUtility {

    private static final Logger log =
            LoggerFactory.getLogger(CastUtility.class);

    public static String castToString(Object anything) {
        if (anything instanceof String) {
            return (String) anything;
        }
        try {
            return anything.toString();
        } catch (NoSuchMethodError e) {
            // ignore
            log.error("Failed to cast object to string", e);
        }
        return "";
    }

    public static Boolean castToBoolean(Object anything) {
        if (anything instanceof String) {
            switch (((String) anything).trim().toLowerCase()) {
                case "true":
                case "yes":
                    return true;
                case "false":
                case "no":
                    return false;
            }
            Boolean.parseBoolean((String) anything);
        }
        if (anything instanceof Integer) {
            return ((Integer) anything) != 0;
        }
        if (anything instanceof Float) {
            return ((Float) anything) != 0.0;
        }
        try {
            return (Boolean) anything;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
