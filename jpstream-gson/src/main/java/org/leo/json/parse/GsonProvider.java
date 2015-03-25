package org.leo.json.parse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2015/3/25.
 */
public class GsonProvider implements JsonProvider<JsonObject, JsonArray, JsonElement> {

    private final static Gson DEFAULT_GSON = new Gson();

    @Override
    public JsonObject createObject() {
        return new JsonObject();
    }

    @Override
    public JsonArray createArray() {
        return new JsonArray();
    }

    @Override
    public boolean isObject(Object object) {
        return object instanceof JsonObject;
    }

    @Override
    public boolean isArray(Object array) {
        return array instanceof JsonArray;
    }

    @Override
    public void consumeObjectEntry(JsonObject object, String key, JsonElement value) {
        object.add(key, value);
    }

    @Override
    public void consumeArrayElement(JsonArray array, JsonElement value) {
        array.add(value);
    }

    @Override
    public JsonElement primitive(Object value) {
        return DEFAULT_GSON.toJsonTree(value);
    }
}
