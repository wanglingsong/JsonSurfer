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

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.jsfr.json.compiler.JsonPathCompiler.compile;


/**
 * JsonSurfer traverses the whole json DOM tree, compare the path of each node with registered JsonPath
 * and return the matched value immediately. JsonSurfer is fully streaming which means that it doesn't construct the DOM tree in memory.
 */
public class JsonSurfer {

    private static final String KEY_HAS_MATCH = "_JSURFER_INTERNAL_HAS_MATCH_";
    private static final String KEY_MATCH = "_JSURFER_INTERNAL_MATCH_";

    private JsonProvider jsonProvider;
    private JsonParserAdapter jsonParserAdapter;
    private ErrorHandlingStrategy errorHandlingStrategy;

    /**
     * @param jsonParserAdapter jsonParserAdapter
     * @param jsonProvider      jsonProvider
     */
    public JsonSurfer(JsonParserAdapter jsonParserAdapter, JsonProvider jsonProvider) {
        this(jsonParserAdapter, jsonProvider, new DefaultErrorHandlingStrategy());
    }

    /**
     * @param jsonParserAdapter     jsonParserAdapter
     * @param jsonProvider          jsonProvider
     * @param errorHandlingStrategy errorHandlingStrategy
     */
    public JsonSurfer(JsonParserAdapter jsonParserAdapter, JsonProvider jsonProvider, ErrorHandlingStrategy errorHandlingStrategy) {
        this.jsonProvider = jsonProvider;
        this.jsonParserAdapter = jsonParserAdapter;
        this.errorHandlingStrategy = errorHandlingStrategy;
    }

    /**
     * Create SurfingConfiguration builder associated with this surfer
     *
     * @return SurfingConfiguration builder
     */
    public SurfingConfiguration.Builder configBuilder() {
        return SurfingConfiguration.builder().withSurfer(this);
    }

    /**
     * Create a streaming iterator which can pull matched value one by one according to provided JsonPath. Internally, only one matched value stored in memory
     *
     * @param reader   Json source
     * @param jsonPath JsonPath
     * @return Streaming iterator
     */
    public Iterator iterator(Reader reader, JsonPath jsonPath) {
        SurfingContext context = createIteratorContext(jsonPath);
        final ResumableParser resumableParser = jsonParserAdapter.createParser(reader, context);
        resumableParser.parse();
        return createIterator(context, resumableParser);
    }

    /**
     * Create a streaming iterator which can pull matched value one by one according to provided JsonPath. Internally, only one matched value stored in memory
     *
     * @param json     Json source
     * @param jsonPath JsonPath
     * @return Streaming iterator
     */
    public Iterator iterator(String json, JsonPath jsonPath) {
        SurfingContext context = createIteratorContext(jsonPath);
        final ResumableParser resumableParser = jsonParserAdapter.createParser(json, context);
        resumableParser.parse();
        return createIterator(context, resumableParser);
    }

    private Iterator<Object> createIterator(final SurfingContext context, final ResumableParser resumableParser) {
        return new Iterator<Object>() {

            @Override
            public boolean hasNext() {
                return context.load(KEY_HAS_MATCH, Boolean.class) || resumableParser.resume() && context.load(KEY_HAS_MATCH, Boolean.class);
            }

            @Override
            public Object next() {
                if (hasNext()) {
                    Object match = context.load(KEY_MATCH, Object.class);
                    context.save(KEY_HAS_MATCH, false);
                    context.save(KEY_MATCH, null);
                    return match;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove unsupported");
            }
        };
    }

    private SurfingContext createIteratorContext(JsonPath jsonPath) {
        final SurfingConfiguration config = SurfingConfiguration.builder().bind(jsonPath, new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                context.save(KEY_MATCH, value);
                context.save(KEY_HAS_MATCH, true);
                context.pause();
            }
        }).build();

        ensureSetting(config);

        final SurfingContext context = new SurfingContext(config);
        context.save(KEY_HAS_MATCH, false);
        context.pause();
        return context;
    }

    /**
     * @param json          json
     * @param configuration SurfingConfiguration that holds JsonPath binding
     */
    public void surf(String json, SurfingConfiguration configuration) {
        ensureSetting(configuration);
        jsonParserAdapter.parse(json, new SurfingContext(configuration));
    }

    /**
     * @param reader        Json source
     * @param configuration SurfingConfiguration that holds JsonPath binding
     */
    public void surf(Reader reader, SurfingConfiguration configuration) {
        ensureSetting(configuration);
        jsonParserAdapter.parse(reader, new SurfingContext(configuration));
    }

    /**
     * Collect all matched value into a collection
     *
     * @param json  Json string
     * @param paths JsonPath
     * @return collection
     */
    public Collection<Object> collectAll(String json, String... paths) {
        return collectAll(json, compile(paths));
    }

    /**
     * Collect all matched value into a collection
     *
     * @param json  Json string
     * @param paths JsonPath
     * @return collection
     */
    public Collection<Object> collectAll(String json, JsonPath... paths) {
        return collectAll(json, Object.class, paths);
    }

    /**
     * @param json   json
     * @param tClass target class
     * @param paths  JsonPath
     * @param <T>    target class
     * @return typed value
     */
    public <T> Collection<T> collectAll(String json, Class<T> tClass, JsonPath... paths) {
        CollectAllListener<T> listener = new CollectAllListener<T>(jsonProvider, tClass);
        SurfingConfiguration.Builder builder = configBuilder();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(json, builder.build());
        return listener.getCollection();
    }

    /**
     * Collect all matched value into a collection
     *
     * @param reader Json reader
     * @param paths  JsonPath
     * @return values
     */
    public Collection<Object> collectAll(Reader reader, String... paths) {
        return collectAll(reader, compile(paths));
    }

    /**
     * Collect all matched value into a collection
     *
     * @param reader Json reader
     * @param paths  JsonPath
     * @return All matched value
     */
    public Collection<Object> collectAll(Reader reader, JsonPath... paths) {
        return collectAll(reader, Object.class, paths);
    }

    /**
     * Collect all matched value into a collection
     *
     * @param reader Json reader
     * @param tClass type
     * @param paths  JsonPath
     * @param <T>    type
     * @return typed value
     */
    public <T> Collection<T> collectAll(Reader reader, Class<T> tClass, JsonPath... paths) {
        CollectAllListener<T> listener = new CollectAllListener<T>(jsonProvider, tClass);
        SurfingConfiguration.Builder builder = configBuilder();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(reader, builder.build());
        return listener.getCollection();
    }

    /**
     * Collect the first matched value and stop parsing immediately
     *
     * @param json  json
     * @param paths JsonPath
     * @return value
     */
    public Object collectOne(String json, String... paths) {
        return collectOne(json, compile(paths));
    }

    /**
     * Collect the first matched value and stop parsing immediately
     *
     * @param json  json
     * @param paths JsonPath
     * @return value
     */
    public Object collectOne(String json, JsonPath... paths) {
        return collectOne(json, Object.class, paths);
    }

    /**
     * @param json   json
     * @param tClass type
     * @param paths  JsonPath
     * @param <T>    type
     * @return value
     */
    public <T> T collectOne(String json, Class<T> tClass, JsonPath... paths) {
        CollectOneListener listener = new CollectOneListener(true);
        SurfingConfiguration.Builder builder = configBuilder().skipOverlappedPath();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(json, builder.build());
        Object value = listener.getValue();
        return tClass.cast(jsonProvider.cast(value, tClass));
    }

    /**
     * Collect the first matched value and stop parsing immediately
     *
     * @param reader Json reader
     * @param paths  JsonPath
     * @return Value
     */
    public Object collectOne(Reader reader, String... paths) {
        return collectOne(reader, compile(paths));
    }

    /**
     * Collect the first matched value and stop parsing immediately
     *
     * @param reader json reader
     * @param paths  JsonPath
     * @return Matched value
     */
    public Object collectOne(Reader reader, JsonPath... paths) {
        return collectOne(reader, Object.class, paths);
    }

    /**
     * Collect the first matched value and stop parsing immediately
     *
     * @param reader Json reader
     * @param tClass type
     * @param paths  JsonPath
     * @param <T>    type
     * @return typed value
     */
    @SuppressWarnings("unchecked")
    public <T> T collectOne(Reader reader, Class<T> tClass, JsonPath... paths) {
        CollectOneListener listener = new CollectOneListener(true);
        SurfingConfiguration.Builder builder = configBuilder().skipOverlappedPath();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(reader, builder.build());
        Object value = listener.getValue();
        return tClass.cast(jsonProvider.cast(value, tClass));
    }

    private void ensureSetting(SurfingConfiguration configuration) {
        if (configuration.getJsonProvider() == null) {
            configuration.setJsonProvider(jsonProvider);
        }
        if (configuration.getErrorHandlingStrategy() == null) {
            configuration.setErrorHandlingStrategy(errorHandlingStrategy);
        }
    }

}
