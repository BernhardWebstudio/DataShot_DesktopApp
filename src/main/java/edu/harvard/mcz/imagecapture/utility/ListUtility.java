package edu.harvard.mcz.imagecapture.utility;

import java.util.List;

public class ListUtility {
    public static <T> boolean allEqual(List<T> list) {
        for (T s : list) {
            if (!s.equals(list.get(0)))
                return false;
        }
        return true;
    }
}
