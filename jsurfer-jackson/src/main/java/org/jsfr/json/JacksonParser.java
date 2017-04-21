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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.jsfr.json.provider.JsonProvider;

import java.io.IOException;
import java.io.Reader;

public class JacksonParser implements JsonParserAdapter {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    public static final JacksonParser INSTANCE = new JacksonParser(JSON_FACTORY);

    private JsonFactory factory;

    public JacksonParser(JsonFactory factory) {
      this.factory = factory;
    }

    @Override
    public void parse(Reader reader, final SurfingContext context) {
        try {
            JsonParser jp = this.factory.createParser(reader);
            doPare(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
        }
    }

    @Override
    public void parse(String json, SurfingContext context) {
        try {
            JsonParser jp = this.factory.createParser(json);
            doPare(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
        }
    }

    private void doPare(final JsonParser jp, SurfingContext context) throws IOException {
        final JsonProvider jsonProvider = context.getConfig().getJsonProvider();

        AbstractPrimitiveHolder stringHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                return jsonProvider.primitive(jp.getText());
            }

            @Override
            public void doSkipValue() throws IOException {
            }
        };
        AbstractPrimitiveHolder longHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                return jsonProvider.primitive(jp.getLongValue());
            }

            @Override
            public void doSkipValue() throws IOException {
            }
        };
        AbstractPrimitiveHolder doubleHolder = new AbstractPrimitiveHolder(context.getConfig()) {
            @Override
            public Object doGetValue() throws IOException {
                return jsonProvider.primitive(jp.getDoubleValue());
            }

            @Override
            public void doSkipValue() throws IOException {
            }
        };
        StaticPrimitiveHolder staticPrimitiveHolder = new StaticPrimitiveHolder();

        context.startJSON();
        while (!context.isStopped()) {
            JsonToken token = jp.nextToken();
            if (token == null) {
                context.endJSON();
                return;
            }
            switch (token) {
                case NOT_AVAILABLE:
                    return;
                case START_OBJECT:
                    context.startObject();
                    break;
                case END_OBJECT:
                    context.endObject();
                    break;
                case START_ARRAY:
                    context.startArray();
                    break;
                case END_ARRAY:
                    context.endArray();
                    break;
                case FIELD_NAME:
                    context.startObjectEntry(jp.getCurrentName());
                    break;
                case VALUE_STRING:
                    stringHolder.init();
                    context.primitive(stringHolder);
                    stringHolder.skipValue();
                    break;
                case VALUE_NUMBER_INT:
                    longHolder.init();
                    context.primitive(longHolder);
                    longHolder.skipValue();
                    break;
                case VALUE_NUMBER_FLOAT:
                    doubleHolder.init();
                    context.primitive(doubleHolder);
                    doubleHolder.skipValue();
                    break;
                case VALUE_TRUE:
                    context.primitive(staticPrimitiveHolder.withValue(jsonProvider.primitive(true)));
                    break;
                case VALUE_FALSE:
                    context.primitive(staticPrimitiveHolder.withValue(jsonProvider.primitive(false)));
                    break;
                case VALUE_NULL:
                    context.primitive(staticPrimitiveHolder.withValue(jsonProvider.primitiveNull()));
                    break;
                case VALUE_EMBEDDED_OBJECT:
                default:
                    throw new IllegalStateException("Unexpected token");
            }
        }
    }

}
