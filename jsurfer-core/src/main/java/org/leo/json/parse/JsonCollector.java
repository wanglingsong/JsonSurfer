/*
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

package org.leo.json.parse;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Administrator on 2015/3/21.
 */
public class JsonCollector extends JsonGenerator {

    private Collection<JsonPathListener> jsonPathListeners;
    private ParsingContext context;

    public JsonCollector(Collection<JsonPathListener> jsonPathListeners, ParsingContext context) {
        this.jsonPathListeners = jsonPathListeners;
        this.context = context;
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        super.endObject();
        if (getStackSize() == 1) {
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(getResult(), context);
                }
            }
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        super.endArray();
        if (getStackSize() == 1) {
            Object result = getResult();
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(result, context);
                }
            }
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        super.primitive(value);
        if (getStackSize() == 1) {
            Object result = getResult();
            for (JsonPathListener jsonPathListener : jsonPathListeners) {
                if (!context.isStopped()) {
                    jsonPathListener.onValue(result, context);
                }
            }
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        this.context = null;
        this.jsonPathListeners = null;
    }


}
