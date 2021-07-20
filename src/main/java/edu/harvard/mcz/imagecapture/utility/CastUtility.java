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
}
