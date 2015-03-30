/*
 * The MIT License
 *
 * Copyright (c) 2015 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jsfr.json;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/3/21.
 */
class JsonGenerator implements ContentHandler {

    private JsonProvider provider;
    private LinkedList<Object> valueStack;

    public void setProvider(JsonProvider provider) {
        this.provider = provider;
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
        provider.consumeObjectEntry(valueStack.peek(), key, value);
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
            if (provider.isArray(prev)) {
                provider.consumeArrayElement(prev, value);
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
        Object array = provider.createArray();
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
        Object object = provider.createObject();
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
