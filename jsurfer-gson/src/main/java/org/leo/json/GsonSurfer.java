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

package org.leo.json;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;
import org.leo.json.exception.JsonSurfingException;
import org.leo.json.parse.GsonProvider;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class GsonSurfer implements JsonSurfer {

    private enum EntryType {
        OBJECT,
        ARRAY,
        PRIMITIVE
    }

    public static ContentHandlerBuilder start() {
        // TODO Implement gson parsing context
        return BuilderFactory.start().setJsonStructureFactory(new GsonProvider());
    }

    @Override
    public void surf(Reader reader, ContentHandler contentHandler) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            LinkedList<EntryType> entryStack = Lists.newLinkedList();
            // TODO to correct behavior

            contentHandler.startJSON();
            while (true) {
                JsonToken token = jsonReader.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        jsonReader.beginArray();
                        if (!contentHandler.startArray()) {
                            return;
                        }
                        break;
                    case END_ARRAY:
                        jsonReader.endArray();
                        if (!contentHandler.endArray()) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.ARRAY) {
                            if (!contentHandler.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case BEGIN_OBJECT:
                        jsonReader.beginObject();
                        if (!contentHandler.startObject()) {
                            return;
                        }
                        break;
                    case END_OBJECT:
                        jsonReader.endObject();
                        if (!contentHandler.endObject()) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.OBJECT) {
                            if (!contentHandler.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case NAME:
                        String name = jsonReader.nextName();
                        if (!contentHandler.startObjectEntry(name)) {
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
                        if (!contentHandler.primitive(s)) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!contentHandler.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case NUMBER:
                        String n = jsonReader.nextString();
                        if (!contentHandler.primitive(Double.parseDouble(n))) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!contentHandler.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case BOOLEAN:
                        boolean b = jsonReader.nextBoolean();
                        if (!contentHandler.primitive(b)) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!contentHandler.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case NULL:
                        jsonReader.nextNull();
                        if (!contentHandler.primitive(null)) {
                            return;
                        }
                        if (entryStack.peek() == EntryType.PRIMITIVE) {
                            if (!contentHandler.endObjectEntry()) {
                                return;
                            }
                            entryStack.pop();
                        }
                        break;
                    case END_DOCUMENT:
                        contentHandler.endJSON();
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
