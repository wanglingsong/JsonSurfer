package org.leo.json.parse;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/3/21.
 */
class Transformer implements ContentHandler {

    private JsonStructureFactory factory;
    private LinkedList<Object> valueStack;

    public void setFactory(JsonStructureFactory factory) {
        this.factory = factory;
    }

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
        String key = valueStack.pop().toString();
        factory.consumeObjectEntry(valueStack.peek(), key, value);
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
            if (factory.isArray(prev)) {
                factory.consumeArrayElement(prev, value);
            } else {
                valueStack.push(value);
            }
        }
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        consumeValue(factory.primitive(value));
        return true;
    }

    public boolean startArray() throws ParseException, IOException {
        Object array = factory.createArray();
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
        Object object = factory.createObject();
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

    public void clear() {
        if (valueStack != null) {
            this.valueStack.clear();
        }
    }
}
