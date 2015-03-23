package org.leo.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/21.
 */
class Transformer implements ContentHandler {
    private LinkedList<Object> valueStack;

    public Object getResult() {
        if (valueStack == null || valueStack.size() == 0)
            return null;
        return valueStack.peek();
    }

    public boolean endArray() throws ParseException, IOException {
        trackBack();
        return true;
    }

    public void endJSON() throws ParseException, IOException {
    }

    public boolean endObject() throws ParseException, IOException {
        trackBack();
        return true;
    }

    public boolean endObjectEntry() throws ParseException, IOException {
        Object value = valueStack.pop();
        Object key = valueStack.pop();
        Map parent = (Map) valueStack.peek();
        parent.put(key, value);
        return true;
    }

    private void trackBack() {
        if (valueStack.size() > 1) {
            Object value = valueStack.pop();
            Object prev = valueStack.peek();
            if (prev instanceof String) {
                valueStack.push(value);
            }
        }
    }

    private void consumeValue(Object value) {
        if (valueStack.size() == 0)
            valueStack.push(value);
        else {
            Object prev = valueStack.peek();
            if (prev instanceof List) {
                List array = (List) prev;
                array.add(value);
            } else {
                valueStack.push(value);
            }
        }
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        consumeValue(value);
        return true;
    }

    public boolean startArray() throws ParseException, IOException {
        List array = new JSONArray();
        consumeValue(array);
        valueStack.push(array);
        return true;
    }

    public void startJSON() throws ParseException, IOException {
        if (valueStack == null) {
            valueStack = new LinkedList<Object>();
        } else {
            valueStack.clear();
        }
    }

    public boolean startObject() throws ParseException, IOException {
        Map object = new JSONObject();
        consumeValue(object);
        valueStack.push(object);
        return true;
    }

    public boolean startObjectEntry(String key) throws ParseException, IOException {
        valueStack.push(key);
        return true;
    }

    public int getStackSize() {
        return this.valueStack.size();
    }

    public void reset() {
        if (valueStack != null) {
            this.valueStack.clear();
        }
    }
}