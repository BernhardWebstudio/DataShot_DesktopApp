package edu.harvard.mcz.imagecapture.utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONUtility {

    public static boolean areEqualIgnoringUnderscore(JSONArray a1, JSONArray a2) {
        if (a1.length() != a2.length()) {
            return false;
        }
        for (int i = 0; i < a1.length(); i++) {
            Object obj1 = a1.get(i);
            Object obj2 = a2.get(i);
            if (!objectsAreEqual(obj1, obj2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqualIgnoringUnderscore(JSONObject obj1, JSONObject obj2) {
        return areEqualIgnoringUnderscore(obj1, obj2, false);
    }

    private static boolean areEqualIgnoringUnderscore(JSONObject obj1, JSONObject obj2, boolean inverted) {
        assert(obj1 != null);
        Iterator<String> keys = obj1.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith("_")) {
                continue;
            }
            if (!obj2.has(key)) {
                return false;
            }
            Object val1 = obj1.get(key);
            Object val2 = obj2.get(key);
            if (!objectsAreEqual(val1, val2)) {
                return false;
            }
        }

        if (inverted) {
            return true;
        } else {
            return areEqualIgnoringUnderscore(obj2, obj1, true);
        }
    }

    private static boolean objectsAreEqual(Object val1, Object val2) {
        if (val1 instanceof JSONObject && val2 instanceof JSONObject) {
            return areEqualIgnoringUnderscore((JSONObject) val1, (JSONObject) val2);
        } else if (val1 instanceof JSONArray && val2 instanceof JSONArray) {
            return areEqualIgnoringUnderscore((JSONArray) val1, (JSONArray) val2);
        } //else {
        return val1.equals(val2);
    }
}
