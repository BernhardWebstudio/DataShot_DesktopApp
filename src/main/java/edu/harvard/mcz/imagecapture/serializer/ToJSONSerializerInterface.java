package edu.harvard.mcz.imagecapture.serializer;

import org.json.JSONObject;

public interface ToJSONSerializerInterface {
    public JSONObject serialize2JSON(Object target);
    public boolean supportsSerializationOf(Object target);
}
