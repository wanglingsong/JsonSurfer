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
import org.jsfr.json.provider.JsonProvider;

import java.io.Reader;

public class GsonParser implements JsonParserAdapter {

    public final static GsonParser INSTANCE = new GsonParser();

    @Override
    public void parse(Reader reader, SurfingContext context) {
        try {
            final JsonReader jsonReader = new JsonReader(reader);
            final JsonProvider jsonProvider = context.getConfig().getJsonProvider();
            AbstractPrimitiveHolder stringHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws Exception {
                    return jsonProvider.primitive(jsonReader.nextString());
                }

                @Override
                public void doSkipValue() throws Exception {
                    jsonReader.skipValue();
                }
            };
            AbstractPrimitiveHolder numberHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws Exception {
                    return jsonProvider.primitive(jsonReader.nextDouble());
                }

                @Override
                public void doSkipValue() throws Exception {
                    jsonReader.skipValue();
                }
            };
            AbstractPrimitiveHolder booleanHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws Exception {
                    return jsonProvider.primitive(jsonReader.nextBoolean());
                }

                @Override
                public void doSkipValue() throws Exception {
                    jsonReader.skipValue();
                }
            };
            AbstractPrimitiveHolder nullHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws Exception {
                    jsonReader.nextNull();
                    return jsonProvider.primitiveNull();
                }

                @Override
                public void doSkipValue() throws Exception {
                    jsonReader.skipValue();
                }
            };
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
                        stringHolder.init();
                        if (!context.primitive(stringHolder)) {
                            return;
                        }
                        stringHolder.skipValue();
                        break;
                    case NUMBER:
                        numberHolder.init();
                        if (!context.primitive(numberHolder)) {
                            return;
                        }
                        numberHolder.skipValue();
                        break;
                    case BOOLEAN:
                        booleanHolder.init();
                        if (!context.primitive(booleanHolder)) {
                            return;
                        }
                        booleanHolder.skipValue();
                        break;
                    case NULL:
                        nullHolder.init();
                        if (!context.primitive(nullHolder)) {
                            return;
                        }
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
