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

import com.google.common.io.Resources;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jsfr.json.SurfingContext.Builder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import static org.jsfr.json.BuilderFactory.context;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JsonSurferTest {

    protected final static Logger LOGGER = LoggerFactory.getLogger(JsonSurferTest.class);

    protected JsonSurfer surfer;
    protected JsonProvider provider;
    private JsonPathListener print = new JsonPathListener() {
        @Override
        public void onValue(Object value, ParsingContext context) {
            System.out.println(value);
        }
    };

    @Before
    public void setUp() throws Exception {
        provider = new JsonSimpleProvider();
        surfer = new JsonSimpleSurfer(provider);
    }

    @Test
    public void testSampleJson() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$.store.book[0].category", mockListener)
                .bind("$.store.book[0]", mockListener)
                .bind("$.store.car", mockListener)
                .bind("$.store.bicycle", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());

        Object book = provider.createObject();
        provider.consumeObjectEntry(book, "category", provider.primitive("reference"));
        provider.consumeObjectEntry(book, "author", provider.primitive("Nigel Rees"));
        provider.consumeObjectEntry(book, "title", provider.primitive("Sayings of the Century"));
        provider.consumeObjectEntry(book, "price", provider.primitive(8.95));
        verify(mockListener).onValue(eq(book), any(ParsingContext.class));

        verify(mockListener).onValue(eq(provider.primitive("reference")), any(ParsingContext.class));

        Object cars = provider.createArray();
        provider.consumeArrayElement(cars, provider.primitive("ferrari"));
        provider.consumeArrayElement(cars, provider.primitive("lamborghini"));
        verify(mockListener).onValue(eq(cars), any(ParsingContext.class));

        Object bicycle = provider.createObject();
        provider.consumeObjectEntry(bicycle, "color", provider.primitive("red"));
        provider.consumeObjectEntry(bicycle, "price", provider.primitive(19.95d));
        verify(mockListener).onValue(eq(bicycle), any(ParsingContext.class));
    }

    @Test
    public void testStoppableParsing() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$.store.book[0,1,2]", mockListener)
                .bind("$.store.book[3]", mockListener);

        doNothing().when(mockListener)
                .onValue(anyObject(), argThat(new TypeSafeMatcher<ParsingContext>() {

                    @Override
                    public boolean matchesSafely(ParsingContext parsingContext) {
                        parsingContext.stopParsing();
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));

        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(1))
                .onValue(anyObject(), any(ParsingContext.class));

    }

    @Test
    public void testChildNodeWildcard() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$.store.*", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(3))
                .onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testAnyIndex() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$.store.book[*]", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(4))
                .onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testWildcardCombination() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$.store.book[*].*", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener, times(18)).onValue(anyObject(),
                any(ParsingContext.class));
    }

    @Test
    public void testParsingArray() throws Exception {
        Builder builder = context();
        JsonPathListener wholeArray = mock(JsonPathListener.class);
        JsonPathListener stringElement = mock(JsonPathListener.class);
        JsonPathListener numberElement = mock(JsonPathListener.class);
        JsonPathListener booleanElement = mock(JsonPathListener.class);
        JsonPathListener nullElement = mock(JsonPathListener.class);
        JsonPathListener objectElement = mock(JsonPathListener.class);

        builder.bind("$", wholeArray);
        builder.bind("$[0]", stringElement);
        builder.bind("$[1]", numberElement);
        builder.bind("$[2]", booleanElement);
        builder.bind("$[3]", nullElement);
        builder.bind("$[4]", objectElement);
        surfer.surf(new InputStreamReader(Resources.getResource("array.json").openStream()), builder.build());
        Object object = provider.createObject();
        provider.consumeObjectEntry(object, "key", provider.primitive("value"));
        Object array = provider.createArray();
        provider.consumeArrayElement(array, provider.primitive("abc"));
        provider.consumeArrayElement(array, provider.primitive(8.88));
        provider.consumeArrayElement(array, provider.primitive(true));
        provider.consumeArrayElement(array, provider.primitiveNull());
        provider.consumeArrayElement(array, object);
        verify(wholeArray).onValue(eq(array), any(ParsingContext.class));
        verify(stringElement).onValue(eq(provider.primitive("abc")), any(ParsingContext.class));
        verify(numberElement).onValue(eq(provider.primitive(8.88)), any(ParsingContext.class));
        verify(booleanElement).onValue(eq(provider.primitive(true)), any(ParsingContext.class));
        verify(nullElement).onValue(eq(provider.primitiveNull()), any(ParsingContext.class));
        verify(objectElement).onValue(eq(object), any(ParsingContext.class));

    }

    @Test
    public void testDeepScan() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$..author", mockListener);
        builder.bind("$..store..bicycle..color", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive("Nigel Rees")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Herman Melville")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("J. R. R. Tolkien")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("red")), any(ParsingContext.class));

    }

    @Test
    public void testDeepScan2() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$..store..price", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive(8.95)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(12.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(8.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(22.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(19.95)), any(ParsingContext.class));
    }

    @Test
    public void testAny() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$.store..bicycle..*", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive("red")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(19.95)), any(ParsingContext.class));
    }

    @Test
    public void testFindEverything() throws Exception {
        Builder builder = context();
        builder.bind("$..*", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                LOGGER.trace(value.toString());
            }
        });
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testIndexesAndChildrenOperator() throws Exception {
        Builder builder = context();
        JsonPathListener mockListener = mock(JsonPathListener.class);
        builder.bind("$..book[1,3][author,title]", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Sword of Honour")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("J. R. R. Tolkien")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("The Lord of the Rings")), any(ParsingContext.class));
    }

    @Test
    public void testCollectAllRaw() throws Exception {
        Collection<Object> values = surfer.collectAll(new InputStreamReader(Resources.getResource("sample.json").openStream()), "$..book[1,3][author,title]");
        assertEquals(4, values.size());
        Iterator<Object> itr = values.iterator();
        itr.next();
        assertEquals("Sword of Honour", itr.next());
    }

    @Test
    public void testCollectOneRaw() throws Exception {
        Object value = surfer.collectOne(new InputStreamReader(Resources.getResource("sample.json").openStream()), "$..book[1,3][author,title]");
        assertEquals("Evelyn Waugh", value);
    }

    @Test
    public void testCollectAll() throws Exception {
        Collection<String> values = surfer.collectAll(new InputStreamReader(Resources.getResource("sample.json").openStream()), String.class, "$..book[1,3][author, title]");
        assertEquals(4, values.size());
        assertEquals("Evelyn Waugh", values.iterator().next());
    }

    @Test
    public void testCollectOne() throws Exception {
        String value = surfer.collectOne(new InputStreamReader(Resources.getResource("sample.json").openStream()), String.class, "$..book[1,3][author,title]");
        assertEquals("Evelyn Waugh", value);
    }

    @Test
    public void testExample1() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$.store.book[*].author", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample2() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$..author", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample3() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$.store.*", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample4() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$.store..price", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample5() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$..book[2]", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testExample6() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$..book[0,1]", print);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testStoppable() throws Exception {
        Builder builder = BuilderFactory.context();
        builder.bind("$..book[0,1]", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext parsingContext) {
                parsingContext.stopParsing();
                System.out.println(value);
            }
        });
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
    }

    @Test
    public void testPlugableProvider() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        Builder builder = BuilderFactory.context().withJsonProvider(new JavaCollectionProvider());
        builder.bind("$.store", mockListener);
        surfer.surf(new InputStreamReader(Resources.getResource("sample.json").openStream()), builder.build());
        verify(mockListener).onValue(isA(HashMap.class), any(ParsingContext.class));
    }

    @Test
    public void performanceTest() throws Exception {
//        for (int i=0;i < 1000;i++) {
        Builder builder = context();
        final AtomicLong counter = new AtomicLong();
        JsonPathListener printListener = new JsonPathListener() {

            @Override
            public void onValue(Object value, ParsingContext context) {
                counter.incrementAndGet();
                LOGGER.trace("value: {}", value);
            }
        };
        builder.bind("$.builders.*.properties", printListener).skipOverlappedPath();
        long start = System.currentTimeMillis();
        surfer.surf(new InputStreamReader(Resources.getResource("allthethings.json").openStream()), builder.build());
        LOGGER.info(surfer.getClass().getSimpleName() + " processes {} value in {} millisecond", counter.get(), System.currentTimeMillis()
                - start);
//        }
    }

    @Test
    public void performanceTestWithDeepScanOperator() throws Exception {
//        for (int i=0;i < 1000;i++) {
        Builder builder = context();
        final AtomicLong counter = new AtomicLong();
        JsonPathListener printListener = new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                counter.incrementAndGet();
                LOGGER.trace("value: {}", value);
            }
        };
        builder.bind("$.builders..properties", printListener);
        long start = System.currentTimeMillis();
        surfer.surf(new InputStreamReader(Resources.getResource("allthethings.json").openStream()), builder.build());
        LOGGER.info(surfer.getClass().getSimpleName() + " with deep scan processes {} value in {} millisecond", counter.get(), System.currentTimeMillis() - start);
//        }
    }

}
