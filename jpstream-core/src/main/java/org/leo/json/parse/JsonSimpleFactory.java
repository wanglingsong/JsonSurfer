package org.leo.json.parse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Administrator on 2015/3/25.
 */
public class JsonSimpleFactory implements JsonStructureFactory<JSONObject, JSONArray, Object> {
    @Override
    public JSONObject createObject() {
        return new JSONObject();
    }

    @Override
    public JSONArray createArray() {
        return new JSONArray();
    }

    @Override
    public boolean isObject(Object object) {
        return object instanceof JSONObject;
    }

    @Override
    public boolean isArray(Object array) {
        return array instanceof JSONArray;
    }

    @Override
    public void consumeObjectEntry(JSONObject object, String key, Object value) {
        object.put(key, value);
    }

    @Override
    public void consumeArrayElement(JSONArray array, Object value) {
        array.add(value);
    }

    @Override
    public Object primitive(Object value) {
        return value;
    }
}
