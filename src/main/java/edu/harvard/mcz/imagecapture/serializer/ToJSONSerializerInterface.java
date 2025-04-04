package edu.harvard.mcz.imagecapture.serializer;

import edu.harvard.mcz.imagecapture.exceptions.SkipSpecimenException;
import org.json.JSONObject;

public interface ToJSONSerializerInterface {
    JSONObject serialize2JSON(Object target) throws SkipSpecimenException;
    JSONObject serialize2JSON(Object target, JSONObject existing) throws SkipSpecimenException;

    boolean supportsSerializationOf(Object target);
}
