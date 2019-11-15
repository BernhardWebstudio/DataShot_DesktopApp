/**
 *
 */
package edu.harvard.mcz.imagecapture.entity.fixed;

/**
 * Features, values to populate picklist of possible features of specimens.
 * @deprecated Likely to be highly variable with low repetition, should probably
 * use free text fields instead.
 *
 *
 */
public class Features {

    public static String[] getFeaturesValues() {
        String[] features = {
                "", "abberation", "melanic", "eclosion defect",
                "runt", "deformed", "faded colours", "scales stripped for venation",
                "greasy", "stained", "psocid damaged"};
        return features;
    }
}
