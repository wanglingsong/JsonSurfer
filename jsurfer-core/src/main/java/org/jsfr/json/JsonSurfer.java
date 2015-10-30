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

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.GsonProvider;
import org.jsfr.json.provider.JacksonProvider;
import org.jsfr.json.provider.JsonProvider;
import org.jsfr.json.provider.JsonSimpleProvider;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

import static org.jsfr.json.SurfingConfiguration.builder;
import static org.jsfr.json.compiler.JsonPathCompiler.compile;


/**
 * JsonSurfer traverses the whole json DOM tree, compare the path of each node with registered JsonPath
 * and return the matched value immediately. JsonSurfer is fully streaming which means that it doesn't construct the DOM tree in memory.
 */
public class JsonSurfer {

    private JsonProvider jsonProvider;
    private JsonParserAdapter jsonParserAdapter;
    private ErrorHandlingStrategy errorHandlingStrategy;

    /**
     * @return New JsonSurfer using JsonSimple parser and provider. JsonSimple dependency is included by default.
     */
    public static JsonSurfer simple() {
        return new JsonSurfer(JsonSimpleParser.INSTANCE, JsonSimpleProvider.INSTANCE);
    }

    /**
     * @return New JsonSurfer using Gson parser and provider. You need to explicitly declare gson dependency.
     */
    public static JsonSurfer gson() {
        return new JsonSurfer(GsonParser.INSTANCE, GsonProvider.INSTANCE);
    }

    /**
     * @return New JsonSurfer using Jackson parser and provider. You need to explicitly declare Jackson dependency.
     */
    public static JsonSurfer jackson() {
        return new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);
    }

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

    public void surf(String json, SurfingConfiguration configuration) {
        surf(new StringReader(json), configuration);
    }

    /**
     * @param reader        Json source
     * @param configuration SurfingConfiguration that holds JsonPath binding
     */
    public void surf(Reader reader, SurfingConfiguration configuration) {
        ensureSetting(configuration);
        jsonParserAdapter.parse(reader, new SurfingContext(configuration));
    }

    public Collection<Object> collectAll(String json, JsonPath... paths) {
        return collectAll(new StringReader(json), paths);
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

    public <T> Collection<T> collectAll(String json, Class<T> tClass, JsonPath... paths) {
        return collectAll(new StringReader(json), tClass, paths);
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
        SurfingConfiguration.Builder builder = builder();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(reader, builder.build());
        return listener.getCollection();
    }

    public <T> Collection<T> collectAll(String json, Class<T> tClass, String... paths) {
        return collectAll(new StringReader(json), tClass, paths);
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
    public <T> Collection<T> collectAll(Reader reader, Class<T> tClass, String... paths) {
        return collectAll(reader, tClass, compile(paths));
    }

    public Collection<Object> collectAll(String json, String... paths) {
        return collectAll(new StringReader(json), paths);
    }

    /**
     * Collect all matched value into a collection
     *
     * @param reader Json reader
     * @param paths  JsonPath
     * @return values
     */
    public Collection<Object> collectAll(Reader reader, String... paths) {
        return collectAll(reader, Object.class, paths);
    }

    public Object collectOne(String json, JsonPath... paths) {
        return collectOne(new StringReader(json), paths);
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

    public <T> T collectOne(String json, Class<T> tClass, JsonPath... paths) {
        return collectOne(new StringReader(json), tClass, paths);
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
        SurfingConfiguration.Builder builder = builder().skipOverlappedPath();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(reader, builder.build());
        Object value = listener.getValue();
        return tClass.cast(jsonProvider.cast(value, tClass));
    }

    public <T> T collectOne(String json, Class<T> tClass, String... paths) {
        return collectOne(new StringReader(json), tClass, paths);
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
    public <T> T collectOne(Reader reader, Class<T> tClass, String... paths) {
        return collectOne(reader, tClass, compile(paths));
    }

    public Object collectOne(String json, String... paths) {
        return collectOne(new StringReader(json), paths);
    }

    /**
     * Collect the first matched value and stop parsing immediately
     *
     * @param reader Json reader
     * @param paths  JsonPath
     * @return Value
     */
    public Object collectOne(Reader reader, String... paths) {
        return collectOne(reader, Object.class, paths);
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
