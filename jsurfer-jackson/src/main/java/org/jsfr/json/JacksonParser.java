/*
 * MIT License
 *
 * Copyright (c) 2019 WANG Lingsong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jsfr.json;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import org.jsfr.json.provider.JsonProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class JacksonParser implements JsonParserAdapter {

    private static class JacksonNonblockingParser extends JacksonResumableParser implements NonBlockingParser {

        private NonBlockingJsonParser nonBlockingJsonParser;

        JacksonNonblockingParser(NonBlockingJsonParser jsonParser, SurfingContext context) {
            super(jsonParser, context);
            this.nonBlockingJsonParser = jsonParser;
        }

        @Override
        public boolean feed(byte[] bytes, int start, int end) {
            try {
                if (nonBlockingJsonParser.needMoreInput() && !context.isStopped()) {
                    nonBlockingJsonParser.feedInput(bytes, start, end);
                    if (context.isPaused()) {
                        context.resume();
                        doPare();
                    } else {
                        parse();
                    }
                    return true;
                }
            } catch (IOException e) {
                context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            }
            return false;
        }

        @Override
        public void endOfInput() {
            nonBlockingJsonParser.endOfInput();
            resume();
        }

    }

    private static class JacksonResumableParser implements ResumableParser {

        private JsonParser jsonParser;
        SurfingContext context;
        private AbstractPrimitiveHolder stringHolder;
        private AbstractPrimitiveHolder longHolder;
        private AbstractPrimitiveHolder doubleHolder;
        private StaticPrimitiveHolder staticHolder;

        JacksonResumableParser(final JsonParser jsonParser, SurfingContext context) {
            this.jsonParser = jsonParser;
            this.context = context;
            final JsonProvider jsonProvider = context.getConfig().getJsonProvider();
            this.stringHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws IOException {
                    return jsonProvider.primitive(jsonParser.getText());
                }

                @Override
                public void doSkipValue() throws IOException {
                }
            };
            this.longHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws IOException {
                    return jsonProvider.primitive(jsonParser.getLongValue());
                }

                @Override
                public void doSkipValue() throws IOException {
                }
            };
            this.doubleHolder = new AbstractPrimitiveHolder(context.getConfig()) {
                @Override
                public Object doGetValue() throws IOException {
                    return jsonProvider.primitive(jsonParser.getDoubleValue());
                }

                @Override
                public void doSkipValue() throws IOException {
                }
            };
            this.staticHolder = new StaticPrimitiveHolder();
        }

        @Override
        public boolean resume() {
            try {
                if (!context.isPaused()) {
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

        void doPare() throws IOException {
            JsonProvider jsonProvider = context.getConfig().getJsonProvider();
            while (!context.shouldBreak()) {
                JsonToken token = jsonParser.nextToken();
                if (token == null) {
                    context.endJSON();
                    break;
                }
                switch (token) {
                    case NOT_AVAILABLE:
                        context.pause();
                        break;
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
            if (context.isStopped()) {
                this.jsonParser.close();
            }
        }

    }

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    public static final JacksonParser INSTANCE = new JacksonParser(JSON_FACTORY);

    private JsonFactory factory;
    private FormatSchema formatSchema;

    public JacksonParser(JsonFactory factory) {
        this.factory = factory;
    }

    public JacksonParser(JsonFactory factory, FormatSchema formatSchema) {
        this.factory = factory;
        this.formatSchema = formatSchema;
    }
    @Override
    public void parse(Reader reader, final SurfingContext context) {
        createResumableParser(reader, context).parse();
    }

    @Override
    public void parse(String json, SurfingContext context) {
        createResumableParser(json, context).parse();
    }

    @Override
    public void parse(InputStream inputStream, SurfingContext context) {
        createResumableParser(inputStream, context).parse();
    }

    @Override
    public ResumableParser createResumableParser(Reader reader, SurfingContext context) {
        try {
            final JsonParser jp = this.factory.createParser(reader);
            return createResumableParser(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            return null;
        }
    }

    @Override
    public ResumableParser createResumableParser(String json, SurfingContext context) {
        try {
            final JsonParser jp = this.factory.createParser(json);
            return createResumableParser(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            return null;
        }
    }

    @Override
    public ResumableParser createResumableParser(InputStream json, SurfingContext context) {
        try {
            final JsonParser jp = this.factory.createParser(json);
            return createResumableParser(jp, context);
        } catch (Exception e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
            return null;
        }
    }

    @Override
    public NonBlockingParser createNonBlockingParser(SurfingContext context) {
        try {
            NonBlockingJsonParser jp = (NonBlockingJsonParser) factory.createNonBlockingByteArrayParser();
            if (formatSchema != null) {
                jp.setSchema(formatSchema);
            }
            return new JacksonNonblockingParser(jp, context);
        } catch (IOException e) {
            context.getConfig().getErrorHandlingStrategy().handleParsingException(e);
        }
        return null;
    }

    private JacksonResumableParser createResumableParser(final JsonParser jp, SurfingContext context) {
        if (this.formatSchema != null) {
            jp.setSchema(formatSchema);
        }
        return new JacksonResumableParser(jp, context);
    }

}
