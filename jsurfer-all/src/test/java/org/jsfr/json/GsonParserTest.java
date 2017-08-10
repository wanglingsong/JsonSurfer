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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.provider.GsonProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * Created by Leo on 2015/3/29.
 */
public class GsonParserTest extends JsonSurferTest {

    @Override
    @Before
    public void setUp() throws Exception {
        provider = GsonProvider.INSTANCE;
        surfer = new JsonSurfer(GsonParser.INSTANCE, provider);
    }

    @Test
    public void testLargeJsonRawGson() throws Exception {
        final AtomicLong counter = new AtomicLong();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(read("allthethings.json"));
        long start = System.currentTimeMillis();
        reader.beginObject();
        reader.nextName();
        // $.builders
        reader.beginObject();
        while (reader.hasNext()) {
            reader.nextName();
            Map element = gson.fromJson(reader, Map.class);
            Object value = ((Map) element.get("properties")).get("branch");
            counter.incrementAndGet();
            LOGGER.trace("JsonPath: {} value: {}", reader.getPath(), value);
        }
        LOGGER.info("Gson processes {} value in {} millisecond", counter.get(), System.currentTimeMillis() - start);
    }

    @Test
    public void testGsonTypeBindingOne() throws Exception {
        Reader reader = read("sample.json");
        Book book = surfer.collectOne(reader, Book.class, JsonPathCompiler.compile("$..book[1]"));
        assertEquals("Evelyn Waugh", book.getAuthor());
    }

    @Test
    public void testGsonTypeBindingCollection() throws Exception {
        Reader reader = read("sample.json");
        Collection<Book> book = surfer.collectAll(reader, Book.class, JsonPathCompiler.compile("$..book[0,1]"));
        assertEquals(2, book.size());
        assertEquals("Nigel Rees", book.iterator().next().getAuthor());
    }

    @Test
    public void testSurfingIterator() throws Exception {
        Iterator<Object> iterator = surfer.iterator(read("sample.json"), JsonPathCompiler.compile("$.store.book[*]"));
        int count = 0;
        while (iterator.hasNext()) {
            LOGGER.info("Iterator next: {}", iterator.next());
            count++;
        }
        assertEquals(4, count);
    }

    @Test
    public void testResumableParser() throws Exception {
        SurfingConfiguration config = surfer.configBuilder()
                .bind("$.store.book[0]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        LOGGER.info("First pause");
                        context.pause();
                    }
                })
                .bind("$.store.book[1]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        LOGGER.info("Second pause");
                        context.pause();
                    }
                }).build();
        ResumableParser parser = surfer.createResumableParser(read("sample.json"), config);
        assertFalse(parser.resume());
        LOGGER.info("Start parsing");
        parser.parse();
        LOGGER.info("Resume from the first pause");
        assertTrue(parser.resume());
        LOGGER.info("Resume from the second pause");
        assertTrue(parser.resume());
        LOGGER.info("Parsing stopped");
        assertFalse(parser.resume());
    }

}
