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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

public class GsonParser implements JsonParserAdapter {

    public final static GsonParser INSTANCE = new GsonParser();

    @Override
    public void parse(Reader reader, SurfingContext context) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            // TODO to correct behavior

            context.startJSON();
            while (true) {
                JsonToken token = jsonReader.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        jsonReader.beginArray();
                        if (!context.startArray()) {
                            return;
                        }
                        break;
                    case END_ARRAY:
                        jsonReader.endArray();
                        if (!context.endArray()) {
                            return;
                        }
                        break;
                    case BEGIN_OBJECT:
                        jsonReader.beginObject();
                        if (!context.startObject()) {
                            return;
                        }
                        break;
                    case END_OBJECT:
                        jsonReader.endObject();
                        if (!context.endObject()) {
                            return;
                        }
                        break;
                    case NAME:
                        String name = jsonReader.nextName();
                        if (!context.startObjectEntry(name)) {
                            return;
                        }
                        break;
                    case STRING:
                        String s = jsonReader.nextString();
                        if (!context.primitive(context.getJsonProvider().primitive(s))) {
                            return;
                        }
                        break;
                    case NUMBER:
                        double n = jsonReader.nextDouble();
                        if (!context.primitive(context.getJsonProvider().primitive(n))) {
                            return;
                        }
                        break;
                    case BOOLEAN:
                        boolean b = jsonReader.nextBoolean();
                        if (!context.primitive(context.getJsonProvider().primitive(b))) {
                            return;
                        }
                        break;
                    case NULL:
                        jsonReader.nextNull();
                        if (!context.primitive(context.getJsonProvider().primitiveNull())) {
                            return;
                        }
                        break;
                    case END_DOCUMENT:
                        context.endJSON();
                        return;
                }
            }
        } catch (IOException e) {
            context.getErrorHandlingStrategy().handleParsingException(e);
        }
    }

}
