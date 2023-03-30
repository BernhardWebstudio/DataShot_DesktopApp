package edu.harvard.mcz.imagecapture.utility;

public class NullHandlingUtility {

    public static String joinNonNull(String delim, String ... strings) {
        StringBuilder result = new StringBuilder();
        for (String str : strings) {
            if (str != null && !str.equals("")) {
                if (!result.toString().equals("")) {
                    result.append(delim);
                }
                result.append(str);
            }
        }
        return result.toString();
    }
}
