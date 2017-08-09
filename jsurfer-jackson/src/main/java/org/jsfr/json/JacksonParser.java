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

    private static class JacksonResumableParser implements ResumableParser {

        private JsonParser jsonParser;
        private SurfingContext context;
        private AbstractPrimitiveHolder stringHolder;
        private AbstractPrimitiveHolder longHolder;
        private AbstractPrimitiveHolder doubleHolder;
        private StaticPrimitiveHolder staticHolder;

        public JacksonResumableParser(JsonParser jsonParser, SurfingContext context, AbstractPrimitiveHolder stringHolder, AbstractPrimitiveHolder longHolder, AbstractPrimitiveHolder doubleHolder, StaticPrimitiveHolder staticHolder) {
            this.jsonParser = jsonParser;
            this.context = context;
            this.stringHolder = stringHolder;
            this.longHolder = longHolder;
            this.doubleHolder = doubleHolder;
            this.staticHolder = staticHolder;
        }

        @Override
        public boolean resume() {
            try {
                if (context.isStopped() || !context.isPaused()) {
                    return false;
                }
                context.resume();
                doPare();
                return true;
            } catch (Exception e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
                return false;
            }
        }

        @Override
        public void parse() {
            context.startJSON();
            try {
                doPare();
            } catch (Exception e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            }
        }

        private void doPare() throws IOException {
            final JsonProvider jsonProvider = context.getConfig().getJsonProvider();

            while (!context.isStopped() && !context.isPaused()) {
                JsonToken token = jsonParser.nextToken();
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
                        context.startObjectEntry(jsonParser.getCurrentName());
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
                        context.primitive(staticHolder.withValue(jsonProvider.primitive(true)));
                        break;
                    case VALUE_FALSE:
                        context.primitive(staticHolder.withValue(jsonProvider.primitive(false)));
                        break;
                    case VALUE_NULL:
                        context.primitive(staticHolder.withValue(jsonProvider.primitiveNull()));
                        break;
                    case VALUE_EMBEDDED_OBJECT:
                    default:
                        throw new IllegalStateException("Unexpected token");
                }
            }
        }

    }

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    public static final JacksonParser INSTANCE = new JacksonParser(JSON_FACTORY);

    private JsonFactory factory;

    public JacksonParser(JsonFactory factory) {
        this.factory = factory;
    }

    @Override
    public void parse(Reader reader, final SurfingContext context) {
        createParser(reader, context).parse();
    }

    @Override
    public void parse(String json, SurfingContext context) {
        createParser(json, context).parse();
    }

    @Override
    public ResumableParser createParser(Reader reader, SurfingContext context) {
        try {
            final JsonParser jp = this.factory.createParser(reader);
            return createParser(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            return null;
        }
    }

    @Override
    public ResumableParser createParser(String json, SurfingContext context) {
        try {
            final JsonParser jp = this.factory.createParser(json);
            return createParser(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            return null;
        }
    }

    private ResumableParser createParser(final JsonParser jp, SurfingContext context) {
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
        return new JacksonResumableParser(jp, context, stringHolder, longHolder, doubleHolder, staticPrimitiveHolder);
    }

}
