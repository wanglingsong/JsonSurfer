/*
 * MIT License
 *
 * Copyright (c) 2020 WANG Lingsong
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

import org.jsfr.json.compiler.JsonPathCompiler;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.io.InputStream;
import java.util.Collection;

public class Collector {

    private SurfingConfiguration.Builder builder;

    private JsonProvider jsonProvider;

    private String jsonString;

    private InputStream jsonInputStream;

    Collector(SurfingConfiguration.Builder builder, JsonProvider jsonProvider, String jsonString, InputStream jsonInputStream) {
        this.builder = builder;
        this.jsonProvider = jsonProvider;
        this.jsonString = jsonString;
        this.jsonInputStream = jsonInputStream;
    }


    /**
     * Collect single value into box
     *
     * @param jsonPath JsonPath
     * @param <T>      target class
     * @return value box
     */
    public <T> ValueBox<T> collectOne(JsonPath jsonPath, final Class<T> tClass) {
        final CollectOneListener listener = new CollectOneListener();
        this.builder.bind(jsonPath, listener);
        return new ValueBox<T>() {
            @Override
            public T get() {
                return (T) jsonProvider.cast(listener.getValue(), tClass);
            }
        };
    }

    /**
     * Collect single value into box
     *
     * @param jsonPath JsonPath
     * @param <T>      target class
     * @return value box
     */
    public <T> ValueBox<T> collectOne(String jsonPath, final Class<T> tClass) {
        return this.collectOne(JsonPathCompiler.compile(jsonPath), tClass);
    }

    /**
     * Collect single value into box
     *
     * @param jsonPath JsonPath
     * @return value box
     */
    public ValueBox<Object> collectOne(String jsonPath) {
        return this.collectOne(jsonPath, Object.class);
    }

    /**
     * Collect multiple value into box
     *
     * @param jsonPath JsonPath
     * @param <T>      target class
     * @return value box
     */
    public <T> ValueBox<Collection<T>> collectAll(JsonPath jsonPath, final Class<T> tClass) {
        final CollectAllListener<T> listener = new CollectAllListener<>(this.jsonProvider, tClass);
        this.builder.bind(jsonPath, listener);
        return new ValueBox<Collection<T>>() {
            @Override
            public Collection<T> get() {
                return listener.getCollection();
            }
        };
    }

    /**
     * Collect multiple value into box
     *
     * @param jsonPath JsonPath
     * @param <T>      target class
     * @return value box
     */
    public <T> ValueBox<Collection<T>> collectAll(String jsonPath, final Class<T> tClass) {
        return this.collectAll(JsonPathCompiler.compile(jsonPath), tClass);
    }

    /**
     * Collect multiple value into box
     *
     * @param jsonPath JsonPath
     * @return value box
     */
    public ValueBox<Collection<Object>> collectAll(String jsonPath) {
        return this.collectAll(jsonPath, Object.class);
    }

    /**
     * Collector can only be executed once
     */
    public void exec() {
        if (this.jsonString != null) {
            this.builder.buildAndSurf(this.jsonString);
        } else if (this.jsonInputStream != null) {
            this.builder.buildAndSurf(this.jsonInputStream);
        }
    }

}
