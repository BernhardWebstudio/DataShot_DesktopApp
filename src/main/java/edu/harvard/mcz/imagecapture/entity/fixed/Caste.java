/**
 *
 */
package edu.harvard.mcz.imagecapture.entity.fixed;

/**
 * Caste provides an authority file of values for the part attribute Caste that
 * can be used to populate picklists.
 *
 *
 */
public class Caste {

    public static String[] getCasteValues() {
        String[] values = {"drone",
                "major",
                "male alate",
                "male - dealated",
                "male - wingless",
                "media",
                "minor",
                "other",
                "queen alate",
                "queen intermorph",
                "queen reproductive",
                "queen",
                "replete",
                "soldier",
                "unknown",
                "worker"};
        return values;
    }
}
