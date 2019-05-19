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

import com.google.common.io.Resources;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.provider.JavaCollectionProvider;
import org.jsfr.json.provider.JsonProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class JsonSurferTest {

    protected final static Logger LOGGER = LoggerFactory.getLogger(JsonSurferTest.class);

    protected JsonSurfer surfer;
    protected JsonProvider provider;
    private JsonPathListener print = new JsonPathListener() {
        @Override
        public void onValue(Object value, ParsingContext context) {
            LOGGER.debug("Received value: {}", value);
        }
    };

    protected InputStream read(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    protected String readAsString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), surfer.getParserCharset());
    }

    @Test
    public void testTypeCasting() throws Exception {
        surfer.configBuilder()
                .bind("$.store.book[*]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertNotNull(context.cast(value, Book.class));
                    }
                })
                .bind("$.expensive", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals(10L, context.cast(value, Long.class).longValue());
                    }
                })
                .bind("$.store.book[0].price", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals(8.95d, context.cast(value, Double.class), 0);
                    }
                })
                .bind("$.store.book[1].title", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals("Sword of Honour", context.cast(value, String.class));
                    }
                }).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testWildcardAtRoot() throws Exception {
        Collection<Object> collection = surfer.collectAll("[\n" +
                "    {\n" +
                "      \"type\"  : \"iPhone\",\n" +
                "      \"number\": \"0123-4567-8888\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\"  : \"home\",\n" +
                "      \"number\": \"0123-4567-8910\"\n" +
                "    }\n" +
                "  ]", JsonPathCompiler.compile("$.*"));
        LOGGER.debug("Collect all at root - {}", collection);
        assertEquals(2, collection.size());
    }

    @Test
    public void testTypeBindingOne() throws Exception {
        Book book = surfer.collectOne(read("sample.json"), Book.class, JsonPathCompiler.compile("$..book[1]"));
        assertEquals("Evelyn Waugh", book.getAuthor());
    }

    @Test
    public void testTypeBindingOneWithFilter() throws Exception {
        Book book = surfer.collectOne(read("sample.json"), Book.class, JsonPathCompiler.compile("$..book[?(@.category=='fiction')]"), JsonPathCompiler.compile("$..book[?(@.price>9)]"));
        assertEquals("Evelyn Waugh", book.getAuthor());
    }

    @Test
    public void testTypeBindingCollection() throws Exception {
        Collection<Book> book = surfer.collectAll(read("sample.json"), Book.class, JsonPathCompiler.compile("$..book[*]"));
        assertEquals(4, book.size());
        assertEquals("Nigel Rees", book.iterator().next().getAuthor());
    }

    @Test
    public void testSurfingIterator() throws Exception {
        Iterator<Object> iterator = surfer.iterator(read("sample.json"), JsonPathCompiler.compile("$.store.book[*]"));
        int count = 0;
        while (iterator.hasNext()) {
            LOGGER.debug("Iterator next: {}", iterator.next());
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
                        LOGGER.debug("First pause");
                        context.pause();
                    }
                })
                .bind("$.store.book[1]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        LOGGER.debug("Second pause");
                        context.pause();
                    }
                }).build();
        ResumableParser parser = surfer.createResumableParser(read("sample.json"), config);
        assertFalse(parser.resume());
        LOGGER.debug("Start parsing");
        parser.parse();
        LOGGER.debug("Resume from the first pause");
        assertTrue(parser.resume());
        LOGGER.debug("Resume from the second pause");
        assertTrue(parser.resume());
        LOGGER.debug("Parsing stopped");
        assertFalse(parser.resume());
    }

    @Test
    public void testTransientMap() throws Exception {
        surfer.configBuilder().bind("$.store.book[1]", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                context.save("foo", "bar");
            }
        }).bind("$.store.book[2]", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                assertEquals("bar", context.load("foo", String.class));
            }
        }).bind("$.store.book[0]", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                assertNull(context.load("foo", String.class));
            }
        }).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testJsonPathFilterEqualBoolean() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.marked==true)]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Moby Dick").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterEqualNumber() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.price==8.95)]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sayings of the Century").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterGreaterThan() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.price>10)]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sword of Honour").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Lord of the Rings").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathFilterLessThan() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.price<10)]", mockListener)
                .buildAndSurf(read("sample.json"));

        Object book1 = provider.createObject();
        provider.put(book1, "category", provider.primitive("reference"));
        provider.put(book1, "author", provider.primitive("Nigel Rees"));
        provider.put(book1, "title", provider.primitive("Sayings of the Century"));
        provider.put(book1, "price", provider.primitive(8.95));
        verify(mockListener).onValue(eq(book1), any(ParsingContext.class));

        Object book2 = provider.createObject();
        provider.put(book2, "category", provider.primitive("fiction"));
        provider.put(book2, "author", provider.primitive("Herman Melville"));
        provider.put(book2, "title", provider.primitive("Moby Dick"));
        provider.put(book2, "isbn", provider.primitive("0-553-21311-3"));
        provider.put(book2, "price", provider.primitive(8.99));
        verify(mockListener).onValue(eq(book2), any(ParsingContext.class));
        verify(mockListener, times(2)).onValue(any(), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterEqualString1() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.description.year=='2010')]", mockListener)
                .buildAndSurf(read("sample_filter.json"));
        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("Test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sword of Honour").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterEqualString() throws Exception {

        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.category=='fiction')]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sword of Honour").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Lord of the Rings").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Moby Dick").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathFilterExistence() throws Exception {

        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.isbn)]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Lord of the Rings").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Moby Dick").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathFilterNegation() throws Exception {

        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(!(@.isbn))]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sayings of the Century").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sword of Honour").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathFilterAggregate() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.price < 10 || @.category && @.isbn && !(@.price<10))]", mockListener)
                .buildAndSurf(read("sample_filter.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Sayings of the Century").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Lord of the Rings").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Moby Dick").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathFilterThenChild() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.description.year=='2010')].author", mockListener)
                .buildAndSurf(read("sample_filter2.json"));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("Nigel Rees")), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterThenChildDeepScan() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.price==8.95)]..year", mockListener)
                .buildAndSurf(read("sample_filter2.json"));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("2010")), any(ParsingContext.class));
        verify(mockListener, times(2)).onValue(eq(provider.primitive("1997")), any(ParsingContext.class));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("1998")), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterAfterDeepScanAndThenChildDeepScan() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$..book[?(@.price==8.95)]..year", mockListener)
                .buildAndSurf(read("sample_filter2.json"));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("2010")), any(ParsingContext.class));
        verify(mockListener, times(2)).onValue(eq(provider.primitive("1997")), any(ParsingContext.class));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("1998")), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterAggregateThenChild() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.author=='Nigel Rees'||@.description.year=='2010')].title", mockListener)
                .buildAndSurf(read("sample_filter2.json"));
        verify(mockListener, times(2)).onValue(eq(provider.primitive("Sayings of the Century")), any(ParsingContext.class));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("Sword of Honour")), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathDoubleFilter() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.category=='fiction')].volumes[?(@.year=='1954')]", mockListener)
                .buildAndSurf(read("sample_filter2.json"));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Fellowship of the Ring").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Two Towers").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

        verify(mockListener, times(0)).onValue(argThat(new CustomMatcher<Object>("test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Return of the King").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));

    }

    @Test
    public void testJsonPathDoubleFilterThenChild() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.category=='fiction')].volumes[?(@.year=='1954')].title", mockListener)
                .buildAndSurf(read("sample_filter2.json"));

        verify(mockListener, times(1)).onValue(eq(provider.primitive("The Fellowship of the Ring")), any(ParsingContext.class));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("The Two Towers")), any(ParsingContext.class));
        verify(mockListener, times(0)).onValue(eq(provider.primitive("The Return of the King")), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterNotMatch() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$..book[?(@.category=='comic')]", mockListener)
                .buildAndSurf(read("sample_filter2.json"));
        verify(mockListener, times(0)).onValue(any(), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterNotMatch2() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book.title[?(@.title=='comic')]", mockListener)
                .buildAndSurf(read("sample_filter2.json"));
        verify(mockListener, times(0)).onValue(any(), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathDoubleFilterThenChildWithDeepscan() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$..book[?(@.category=='fiction' && @.volumes[2].year=='1955')]..[?(@.year=='1954')]..title", mockListener)
                .buildAndSurf(read("sample_filter2.json"));

        verify(mockListener, times(1)).onValue(eq(provider.primitive("The Fellowship of the Ring")), any(ParsingContext.class));
        verify(mockListener, times(1)).onValue(eq(provider.primitive("The Two Towers")), any(ParsingContext.class));
        verify(mockListener, times(0)).onValue(eq(provider.primitive("The Return of the King")), any(ParsingContext.class));
    }

    @Test
    public void testSampleJson() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[0].category", mockListener)
                .bind("$.store.book[0]", mockListener)
                .bind("$.store.car", mockListener)
                .bind("$.store.bicycle", mockListener)
                .buildAndSurf(read("sample.json"));

        Object book = provider.createObject();
        provider.put(book, "category", provider.primitive("reference"));
        provider.put(book, "author", provider.primitive("Nigel Rees"));
        provider.put(book, "title", provider.primitive("Sayings of the Century"));
        provider.put(book, "price", provider.primitive(8.95));
        verify(mockListener).onValue(eq(book), any(ParsingContext.class));

        verify(mockListener).onValue(eq(provider.primitive("reference")), any(ParsingContext.class));

        Object cars = provider.createArray();
        provider.add(cars, provider.primitive("ferrari"));
        provider.add(cars, provider.primitive("lamborghini"));
        verify(mockListener).onValue(eq(cars), any(ParsingContext.class));

        Object bicycle = provider.createObject();
        provider.put(bicycle, "color", provider.primitive("red"));
        provider.put(bicycle, "price", provider.primitive(19.95d));
        verify(mockListener).onValue(eq(bicycle), any(ParsingContext.class));
    }

    @Test
    public void testSample2() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder()
                .bind("$[0].aiRuleEditorOriginal.+.barrierLevel", mockListener)
                .buildAndSurf(read("sample2.json"));
        verify(mockListener).onValue(eq(provider.primitive("0.8065")), any(ParsingContext.class));
    }

    @Test
    public void testStoppableParsing() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        doNothing().when(mockListener)
                .onValue(anyObject(), argThat(new TypeSafeMatcher<ParsingContext>() {

                    @Override
                    public boolean matchesSafely(ParsingContext parsingContext) {
                        parsingContext.stop();
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));

        surfer.configBuilder()
                .bind("$.store.book[0,1,2]", mockListener)
                .bind("$.store.book[3]", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener, times(1))
                .onValue(anyObject(), any(ParsingContext.class));

    }

    @Test
    public void testChildNodeWildcard() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder()
                .bind("$.store.*", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener, times(3))
                .onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testAnyIndex() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder()
                .bind("$.store.book[*]", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener, times(4))
                .onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testWildcardCombination() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[*].*", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener, times(18)).onValue(anyObject(),
                any(ParsingContext.class));
    }

    @Test
    public void testArraySlicing() throws Exception {
        JsonPathListener mock1 = mock(JsonPathListener.class);
        JsonPathListener mock2 = mock(JsonPathListener.class);
        JsonPathListener mock3 = mock(JsonPathListener.class);
        JsonPathListener mock4 = mock(JsonPathListener.class);
        surfer.configBuilder()
                .bind("$[:2]", mock1)
                .bind("$[0:2]", mock2)
                .bind("$[2:]", mock3)
                .bind("$[:]", mock4)
                .buildAndSurf(read("array.json"));
        verify(mock1, times(2)).onValue(anyObject(), any(ParsingContext.class));
        verify(mock2, times(2)).onValue(anyObject(), any(ParsingContext.class));
        verify(mock3, times(3)).onValue(anyObject(), any(ParsingContext.class));
        verify(mock4, times(5)).onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testParsingArray() throws Exception {
        JsonPathListener wholeArray = mock(JsonPathListener.class);
        JsonPathListener stringElement = mock(JsonPathListener.class);
        JsonPathListener numberElement = mock(JsonPathListener.class);
        JsonPathListener booleanElement = mock(JsonPathListener.class);
        JsonPathListener nullElement = mock(JsonPathListener.class);
        JsonPathListener objectElement = mock(JsonPathListener.class);

        surfer.configBuilder().bind("$", wholeArray)
                .bind("$[0]", stringElement)
                .bind("$[1]", numberElement)
                .bind("$[2]", booleanElement)
                .bind("$[3]", nullElement)
                .bind("$[4]", objectElement)
                .buildAndSurf(read("array.json"));

        Object object = provider.createObject();
        provider.put(object, "key", provider.primitive("value"));
        Object array = provider.createArray();
        provider.add(array, provider.primitive("abc"));
        provider.add(array, provider.primitive(8.88));
        provider.add(array, provider.primitive(true));
        provider.add(array, provider.primitiveNull());
        provider.add(array, object);
        verify(wholeArray).onValue(eq(array), any(ParsingContext.class));
        verify(stringElement).onValue(eq(provider.primitive("abc")), any(ParsingContext.class));
        verify(numberElement).onValue(eq(provider.primitive(8.88)), any(ParsingContext.class));
        verify(booleanElement).onValue(eq(provider.primitive(true)), any(ParsingContext.class));
        verify(nullElement).onValue(eq(provider.primitiveNull()), any(ParsingContext.class));
        verify(objectElement).onValue(eq(object), any(ParsingContext.class));

    }

    @Test
    public void testDeepScan() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$..author", mockListener)
                .bind("$..store..bicycle..color", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener).onValue(eq(provider.primitive("Nigel Rees")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Herman Melville")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("J. R. R. Tolkien")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("red")), any(ParsingContext.class));

    }

    @Test
    public void testDeepScan2() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$..store..price", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener).onValue(eq(provider.primitive(8.95)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(12.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(8.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(22.99)), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(19.95)), any(ParsingContext.class));
    }

    @Test
    public void testAny() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store..bicycle..*", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener).onValue(eq(provider.primitive("red")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive(19.95)), any(ParsingContext.class));
    }

    @Test
    public void testFindEverything() throws Exception {
        surfer.configBuilder()
                .bind("$..*", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        LOGGER.trace("value: {}", value);
                    }
                })
                .buildAndSurf(read("sample.json"));
    }

    @Test
    public void testIndexesAndChildrenOperator() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$..book[1,3]['author','title']", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener).onValue(eq(provider.primitive("Evelyn Waugh")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("Sword of Honour")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("J. R. R. Tolkien")), any(ParsingContext.class));
        verify(mockListener).onValue(eq(provider.primitive("The Lord of the Rings")), any(ParsingContext.class));
    }

    @Test
    public void testCollectAllRaw() throws Exception {
        Collection<Object> values = surfer.collectAll(read("sample.json"), "$..book[1,3]['author','title']");
        assertEquals(4, values.size());
        Iterator<Object> itr = values.iterator();
        itr.next();
        assertEquals("Sword of Honour", itr.next());
    }

    @Test
    public void testCollectOneRaw() throws Exception {
        Object value = surfer.collectOne(read("sample.json"), "$..book[1,3]['author','title']");
        assertEquals("Evelyn Waugh", value);
    }

    @Test
    public void testCollectAll() throws Exception {
        Collection<String> values = surfer.collectAll(read("sample.json"), String.class, JsonPathCompiler.compile("$..book[1,3]['author', 'title']"));
        assertEquals(4, values.size());
        assertEquals("Evelyn Waugh", values.iterator().next());
    }

    @Test
    public void testCollectAllFromString() throws Exception {
        Collection<Object> values = surfer.collectAll(readAsString("sample.json"), "$..book[1,3]['author', 'title']");
        assertEquals(4, values.size());
        assertEquals("Evelyn Waugh", values.iterator().next());
    }

    @Test
    public void testCollectOne() throws Exception {
        String value = surfer.collectOne(read("sample.json"), String.class, JsonPathCompiler.compile("$..book[1,3]['author','title']"));
        assertEquals("Evelyn Waugh", value);
    }

    @Test
    public void testCollectOneFromString() throws Exception {
        Object value = surfer.collectOne(readAsString("sample.json"), "$..book[1,3]['author','title']");
        assertEquals("Evelyn Waugh", value);
    }

    @Test
    public void testGetCurrentFieldName() throws Exception {
        surfer.configBuilder()
                .bind("$.store.book[0].title", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals(context.getCurrentFieldName(), "title");
                    }
                })
                .bind("$.store.book[0]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertNull(context.getCurrentFieldName());
                    }
                })
                .bind("$.store.bicycle", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals(context.getCurrentFieldName(), "bicycle");
                    }
                })
                .buildAndSurf(read("sample.json"));
    }

    @Test
    public void testGetCurrentArrayIndex() throws Exception {
        surfer.configBuilder()
                .bind("$.store.book[3]", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals(context.getCurrentArrayIndex(), 3);
                    }
                })
                .bind("$.store", new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext context) {
                        assertEquals(context.getCurrentArrayIndex(), -1);
                    }
                })
                .buildAndSurf(read("sample.json"));
    }

    @Test
    public void testExample1() throws Exception {
        surfer.configBuilder().bind("$.store.book[*].author", print).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testExample2() throws Exception {
        surfer.configBuilder().bind("$..author", print).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testExample3() throws Exception {
        surfer.configBuilder().bind("$.store.*", print).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testExample4() throws Exception {
        surfer.configBuilder().bind("$.store..price", print).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testExample5() throws Exception {
        surfer.configBuilder().bind("$..book[2]", print).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testExample6() throws Exception {
        surfer.configBuilder().bind("$..book[0,1]", print).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testStoppable() throws Exception {
        surfer.configBuilder().bind("$..book[0,1]", new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext parsingContext) {
                parsingContext.stop();
                //System.out.println(value);
            }
        }).buildAndSurf(read("sample.json"));
    }

    @Test
    public void testPlugableProvider() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().withJsonProvider(JavaCollectionProvider.INSTANCE)
                .bind("$.store", mockListener)
                .buildAndSurf(read("sample.json"));
        verify(mockListener).onValue(isA(HashMap.class), any(ParsingContext.class));
    }

    @Test
    public void testErrorStrategySuppressException() throws Exception {

        JsonPathListener mock = mock(JsonPathListener.class);
        doNothing().doThrow(Exception.class).doThrow(Exception.class).when(mock).onValue(anyObject(), any(ParsingContext.class));

        surfer.configBuilder().bind("$.store.book[*]", mock)
                .withErrorStrategy(new ErrorHandlingStrategy() {
                    @Override
                    public void handleParsingException(Exception e) {
                        // suppress exception
                    }

                    @Override
                    public void handleExceptionFromListener(Exception e, ParsingContext context) {
                        // suppress exception
                    }
                })
                .buildAndSurf(read("sample.json"));
        verify(mock, times(4)).onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testErrorStrategyThrowException() throws Exception {

        JsonPathListener mock = mock(JsonPathListener.class);
        doNothing().doThrow(Exception.class).doThrow(Exception.class).when(mock).onValue(anyObject(), any(ParsingContext.class));
        try {
            surfer.configBuilder().bind("$.store.book[*]", mock).buildAndSurf(read("sample.json"));
        } catch (Exception e) {
            // catch mock exception
        }
        verify(mock, times(2)).onValue(anyObject(), any(ParsingContext.class));
    }

    @Test
    public void testCollectOneFoundNothing() throws Exception {
        String jsonPathFoundNothing = "$..authors";
        Object expireNull = surfer.collectOne(read("sample.json"), jsonPathFoundNothing);
        assertNull(expireNull);
    }

    @Test
    public void testJsonPathFilterMatchRegex() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.isbn=~/\\d-\\d\\d\\d-21311-\\d/)]", mockListener)
                .buildAndSurf(read("sample_filter.json"));
        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("Test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("Moby Dick").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));
    }

    @Test
    public void testJsonPathFilterMatchRegexFlags() throws Exception {
        JsonPathListener mockListener = mock(JsonPathListener.class);
        surfer.configBuilder().bind("$.store.book[?(@.author =~ /tolkien/i)]", mockListener) // we assume other flags work too
                .buildAndSurf(read("sample_filter.json"));
        verify(mockListener, times(1)).onValue(argThat(new CustomMatcher<Object>("Test filter") {
            @Override
            public boolean matches(Object o) {
                return provider.primitive("The Lord of the Rings").equals(provider.resolve(o, "title"));
            }
        }), any(ParsingContext.class));
    }

}
