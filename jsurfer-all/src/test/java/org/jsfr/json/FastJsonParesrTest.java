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

import org.jsfr.json.provider.FastJsonProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Leo on 2017/3/31.
 */
public class FastJsonParesrTest extends JsonSurferTest {

    @Before
    public void setUp() throws Exception {
        provider = FastJsonProvider.INSTANCE;
        surfer = new JsonSurfer(FastJsonParser.INSTANCE, provider);
    }

    @Test
    public void testWithString() throws Exception {
        String json = "{\n" +
                "  \"store\": {\n" +
                "    \"book\": [\n" +
                "      {\n" +
                "        \"category\": \"reference\",\n" +
                "        \"author\": \"Nigel Rees\",\n" +
                "        \"title\": \"Sayings of the Century\",\n" +
                "        \"price\": 8.95\n" +
                "      },\n" +
                "      {\n" +
                "        \"category\": \"fiction\",\n" +
                "        \"author\": \"Evelyn Waugh\",\n" +
                "        \"title\": \"Sword of Honour\",\n" +
                "        \"price\": 12.99\n" +
                "      },\n" +
                "      {\n" +
                "        \"category\": \"fiction\",\n" +
                "        \"author\": \"Herman Melville\",\n" +
                "        \"title\": \"Moby Dick\",\n" +
                "        \"isbn\": \"0-553-21311-3\",\n" +
                "        \"price\": 8.99\n" +
                "      },\n" +
                "      {\n" +
                "        \"category\": \"fiction\",\n" +
                "        \"author\": \"J. R. R. Tolkien\",\n" +
                "        \"title\": \"The Lord of the Rings\",\n" +
                "        \"isbn\": \"0-395-19395-8\",\n" +
                "        \"price\": 22.99\n" +
                "      }\n" +
                "    ],\n" +
                "    \"car\": [\n" +
                "      \"ferrari\",\n" +
                "      \"lamborghini\"\n" +
                "    ],\n" +
                "    \"bicycle\": {\n" +
                "      \"color\": \"red\",\n" +
                "      \"price\": 19.95\n" +
                "    }\n" +
                "  },\n" +
                "  \"expensive\": 10\n" +
                "}";
        //surfer.configBuilder().bind("$.store.book[*].author", print).buildAndSurf(json);

        surfer.configBuilder().bind("$.store.book[?(@.author=='J. R. R. Tolkien')]", print).buildAndSurf(json);
    }

    @Test
    public void testWithSimpleString() throws Exception {
        String json = "{\"id\":5, \"name\":\"abc\", \"age\":50, \"company\":{\"name\":\"alibaba\", \"place\":\"america\"}}";
        surfer.configBuilder().bind("$.name", print).buildAndSurf(json);
    }

    @Test
    public void testQueryWithSimpleString() throws Exception {
        //String json = "{\"id\":5, \"name\":\"abc\", \"age\":50, \"company\":{\"name\":\"yahoo\", \"place\":\"america\"}}";
        String json = "{\"store\":[{\"id\":5, \"name\":\"abc\", \"age\":50, \"company\":{\"name\":\"yahoo\", \"place\":\"america\"}}]}";

        String bindString = "$.store[?(@.name=='abc')].name";
        JsonSurfer surfer = JsonSurferFastJson.INSTANCE;
        SurfingConfiguration config = surfer.configBuilder()
                .bind(bindString, new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        System.out.println(value);
                    }
                }).build();

        surfer.surf(json, config);
    }
}
