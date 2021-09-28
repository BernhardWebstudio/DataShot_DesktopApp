package edu.harvard.mcz.imagecapture.serializer;

import org.json.JSONObject;

public interface ToJSONSerializerInterface {
    JSONObject serialize2JSON(Object target);

    boolean supportsSerializationOf(Object target);
}
