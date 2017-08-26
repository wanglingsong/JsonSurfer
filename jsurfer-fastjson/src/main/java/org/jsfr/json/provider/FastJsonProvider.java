package org.jsfr.json.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Leo on 2017/4/1.
 */
public class FastJsonProvider implements JsonProvider<JSONObject, JSONArray, Object> {

    public final static FastJsonProvider INSTANCE = new FastJsonProvider();

    private FastJsonProvider() {}

    @Override
    public Object resolve(JSONObject object, String key) {
        return object.get(key);
    }

    @Override
    public Object resolve(JSONArray array, int index) {
        return array.get(index);
    }

    @Override
    public JSONObject createObject() {
        return new JSONObject();
    }

    @Override
    public JSONArray createArray() {
        return new JSONArray();
    }

    @Override
    public void put(JSONObject object, String key, Object value) {
        object.put(key, value);
    }

    @Override
    public void add(JSONArray array, Object value) {
        array.add(value);
    }

    @Override
    public Object primitive(boolean value) {
        return value;
    }

    @Override
    public Object primitive(int value) {
        return value;
    }

    @Override
    public Object primitive(double value) {
        return value;
    }

    @Override
    public Object primitive(long value) {
        return value;
    }

    @Override
    public Object primitive(String value) {
        return value;
    }

    @Override
    public Object primitiveNull() {
        return null;
    }

    @Override
    public <T> T cast(Object value, Class<T> tClass) {
        if (value instanceof JSON) {
            return JSON.toJavaObject((JSON) value, tClass);
        } else {
            return tClass.cast(value);
        }
    }

}
