/*
 * The MIT License
 *
 * Copyright (c) 2017 WANG Lingsong
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
import org.jsfr.json.provider.JsonProvider;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class GsonParser implements JsonParserAdapter {

    private static class GsonResumableParser implements ResumableParser {
        private JsonReader jsonReader;
        private SurfingContext context;
        private AbstractPrimitiveHolder stringHolder;
        private AbstractPrimitiveHolder numberHolder;
        private AbstractPrimitiveHolder booleanHolder;
        private AbstractPrimitiveHolder nullHolder;

        public GsonResumableParser(JsonReader jsonReader, SurfingContext context, AbstractPrimitiveHolder stringHolder, AbstractPrimitiveHolder numberHolder, AbstractPrimitiveHolder booleanHolder, AbstractPrimitiveHolder nullHolder) {
            this.jsonReader = jsonReader;
            this.context = context;
            this.stringHolder = stringHolder;
            this.numberHolder = numberHolder;
            this.booleanHolder = booleanHolder;
            this.nullHolder = nullHolder;
        }

        @Override
        public void parse() {
            context.startJSON();
            doParse();
        }

        @Override
        public boolean resume() {
            try {
                if (context.isStopped() || !context.isPaused()) {
                    return false;
                }
                context.resume();
                doParse();
                return true;
            } catch (Exception e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
                return false;
            }
        }

        private void doParse() {
            try {
                while (!context.isStopped() && !context.isPaused()) {
                    JsonToken token = jsonReader.peek();
                    switch (token) {
                        case BEGIN_ARRAY:
                            jsonReader.beginArray();
                            context.startArray();
                            break;
                        case END_ARRAY:
                            jsonReader.endArray();
                            context.endArray();
                            break;
                        case BEGIN_OBJECT:
                            jsonReader.beginObject();
                            context.startObject();
                            break;
                        case END_OBJECT:
                            jsonReader.endObject();
                            context.endObject();
                            break;
                        case NAME:
                            String name = jsonReader.nextName();
                            context.startObjectEntry(name);
                            break;
                        case STRING:
                            stringHolder.init();
                            context.primitive(stringHolder);
                            stringHolder.skipValue();
                            break;
                        case NUMBER:
                            numberHolder.init();
                            context.primitive(numberHolder);
                            numberHolder.skipValue();
                            break;
                        case BOOLEAN:
                            booleanHolder.init();
                            context.primitive(booleanHolder);
                            booleanHolder.skipValue();
                            break;
                        case NULL:
                            nullHolder.init();
                            context.primitive(nullHolder);
                            nullHolder.skipValue();
                            break;
                        case END_DOCUMENT:
                            context.endJSON();
                            return;
                    }
                }
            } catch (Exception e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            }
        }
    }

    public final static GsonParser INSTANCE = new GsonParser();

    private GsonParser() {
    }

    @Override
    public ResumableParser parse(Reader reader, SurfingContext context) {
        ResumableParser parser = createParser(reader, context);
        parser.parse();
        return parser;

    }

    @Override
    public ResumableParser parse(String json, SurfingContext context) {
        ResumableParser parser = createParser(json, context);
        parser.parse();
        return parser;
    }

    @Override
    public ResumableParser createParser(Reader reader, SurfingContext context) {
        final JsonReader jsonReader = new JsonReader(reader);
        final JsonProvider jsonProvider = context.getConfig().getJsonProvider();

        AbstractPrimitiveHolder stringHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                return jsonProvider.primitive(jsonReader.nextString());
            }

            @Override
            public void doSkipValue() throws IOException {
                jsonReader.skipValue();
            }
        };
        AbstractPrimitiveHolder numberHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                return jsonProvider.primitive(jsonReader.nextDouble());
            }

            @Override
            public void doSkipValue() throws IOException {
                jsonReader.skipValue();
            }
        };
        AbstractPrimitiveHolder booleanHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                return jsonProvider.primitive(jsonReader.nextBoolean());
            }

            @Override
            public void doSkipValue() throws IOException {
                jsonReader.skipValue();
            }
        };
        AbstractPrimitiveHolder nullHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                jsonReader.nextNull();
                return jsonProvider.primitiveNull();
            }

            @Override
            public void doSkipValue() throws IOException {
                jsonReader.skipValue();
            }
        };
        return new GsonResumableParser(jsonReader, context, stringHolder, numberHolder, booleanHolder, nullHolder);
    }

    @Override
    public ResumableParser createParser(String json, SurfingContext context) {
        return createParser(new StringReader(json), context);
    }

}
