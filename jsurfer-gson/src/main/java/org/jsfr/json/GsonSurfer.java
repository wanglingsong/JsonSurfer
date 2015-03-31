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

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import org.jsfr.json.exception.JsonSurfingException;
import org.json.simple.parser.ParseException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class GsonSurfer extends AbstractSurfer {

    private enum EntryType {
        ROOT,
        OBJECT,
        ARRAY,
        PRIMITIVE
    }

    public GsonSurfer() {
        super(new GsonProvider());
    }

    public GsonSurfer(JsonProvider jsonProvider) {
        super(jsonProvider);
    }

    @Override
    public void surf(Reader reader, SurfingContext context) {
        ensureJsonProvider(context);
        try {
            JsonReader jsonReader = new JsonReader(reader);
            Stack<EntryType> entryStack = new Stack<EntryType>();
            entryStack.push(EntryType.ROOT);
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
                        if (entryStack.peek() == EntryType.ARRAY) {
                            if (!context.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
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
                        if (entryStack.peek() == EntryType.OBJECT) {
                            if (!context.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case NAME:
                        String name = jsonReader.nextName();
                        if (!context.startObjectEntry(name)) {
                            return;
                        }
                        JsonToken peek = jsonReader.peek();
                        if (peek == JsonToken.STRING || peek == JsonToken.BOOLEAN || peek == JsonToken.NUMBER
                            || peek == JsonToken.NULL) {
                            entryStack.push(EntryType.PRIMITIVE);
                        } else if (peek == JsonToken.BEGIN_OBJECT) {
                            entryStack.push(EntryType.OBJECT);
                        } else if (peek == JsonToken.BEGIN_ARRAY) {
                            entryStack.push(EntryType.ARRAY);
                        }
                        break;
                    case STRING:
                        String s = jsonReader.nextString();
                        if (!context.primitive(jsonProvider.primitive(s))) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!context.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case NUMBER:
                        double n = jsonReader.nextDouble();
                        if (!context.primitive(jsonProvider.primitive(n))) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!context.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case BOOLEAN:
                        boolean b = jsonReader.nextBoolean();
                        if (!context.primitive(jsonProvider.primitive(b))) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!context.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case NULL:
                        jsonReader.nextNull();
                        if (!context.primitive(jsonProvider.primitiveNull())) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!context.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case END_DOCUMENT:
                        context.endJSON();
                        entryStack.clear();
                        return;
                }
            }
        } catch (IOException e) {
            throw new JsonSurfingException(e);
        } catch (ParseException e) {
            throw new JsonSurfingException(e);
        }
    }

}
