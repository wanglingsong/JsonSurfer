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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Leo on 2015/3/29.
 */
public class JacksonParser implements JsonParserAdapter {

    @Override
    public void parse(Reader reader, SurfingContext context) {
        try {
            JsonFactory f = new JsonFactory();
            JsonParser jp = f.createParser(reader);
            context.startJSON();
            while (true) {
                JsonToken token = jp.nextToken();
                if (token == null) {
                    context.endJSON();
                    return;
                }
                switch (token) {
                    case NOT_AVAILABLE:
                        return;
                    case START_OBJECT:
                        if (!context.startObject()) {
                            return;
                        }
                        break;
                    case END_OBJECT:
                        if (!context.endObject()) {
                            return;
                        }
                        break;
                    case START_ARRAY:
                        if (!context.startArray()) {
                            return;
                        }
                        break;
                    case END_ARRAY:
                        if (!context.endArray()) {
                            return;
                        }
                        break;
                    case FIELD_NAME:
                        if (!context.startObjectEntry(jp.getCurrentName())) {
                            return;
                        }
                        break;
                    case VALUE_EMBEDDED_OBJECT:
                        throw new IllegalStateException("Unexpected token");
                    case VALUE_STRING:
                        if (!context.primitive(context.getJsonProvider().primitive(jp.getText()))) {
                            return;
                        }
                        break;
                    case VALUE_NUMBER_INT:
                        if (!context.primitive(context.getJsonProvider().primitive(jp.getIntValue()))) {
                            return;
                        }
                        break;
                    case VALUE_NUMBER_FLOAT:
                        if (!context.primitive(context.getJsonProvider().primitive(jp.getDoubleValue()))) {
                            return;
                        }
                        break;
                    case VALUE_TRUE:
                        if (!context.primitive(context.getJsonProvider().primitive(true))) {
                            return;
                        }
                        break;
                    case VALUE_FALSE:
                        if (!context.primitive(context.getJsonProvider().primitive(false))) {
                            return;
                        }
                        break;
                    case VALUE_NULL:
                        if (!context.primitive(context.getJsonProvider().primitiveNull())) {
                            return;
                        }
                        break;
                }
            }
        } catch (IOException e) {
            context.getErrorHandlingStrategy().handleParsingException(e);
        }
    }

}
