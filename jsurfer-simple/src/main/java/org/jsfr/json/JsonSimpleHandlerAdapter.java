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

/**
 * Created by Leo on 2015/4/3.
 */
public class JsonSimpleHandlerAdapter implements ContentHandler {

    private JsonSaxHandler jsonSaxHandler;
    private StaticPrimitiveHolder staticPrimitiveHolder = new StaticPrimitiveHolder();

    public JsonSimpleHandlerAdapter(JsonSaxHandler jsonSaxHandler) {
        this.jsonSaxHandler = jsonSaxHandler;
    }

    @Override
    public void startJSON() throws ParseException, IOException {
        jsonSaxHandler.startJSON();
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        jsonSaxHandler.endJSON();
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        return jsonSaxHandler.startObject();
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        return jsonSaxHandler.endObject();
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        return jsonSaxHandler.startObjectEntry(key);
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        return true;
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        return jsonSaxHandler.startArray();
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        return jsonSaxHandler.endArray();
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        return jsonSaxHandler.primitive(staticPrimitiveHolder.withValue(value));
    }
}
