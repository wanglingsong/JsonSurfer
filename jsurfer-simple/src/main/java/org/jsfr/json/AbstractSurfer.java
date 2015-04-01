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

import org.jsfr.json.SurfingContext.Builder;
import org.jsfr.json.path.JsonPath;

import java.io.Reader;
import java.util.Collection;

import static org.jsfr.json.SurfingContext.Builder.builder;
import static org.jsfr.json.compiler.JsonPathCompiler.compile;


/**
 * Created by Leo on 2015/3/30.
 */
public abstract class AbstractSurfer implements JsonSurfer {

    protected JsonProvider jsonProvider;

    private ErrorHandlingStrategy errorHandlingStrategy = new DefaultErrorHandlingStrategy();

    public AbstractSurfer(JsonProvider jsonProvider) {
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Collection<Object> collectAll(Reader reader, JsonPath... paths) {
        return collectAll(reader, Object.class, paths);
    }

    @Override
    public Object collectOne(Reader reader, JsonPath... paths) {
        return collectOne(reader, Object.class, paths);
    }

    @Override
    public <T> Collection<T> collectAll(Reader reader, Class<T> tClass, JsonPath... paths) {
        CollectAllListener<T> listener = new CollectAllListener<T>(jsonProvider, tClass);
        Builder builder = builder().withJsonProvider(jsonProvider).skipOverlappedPath();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(reader, builder.build());
        return listener.getCollection();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T collectOne(Reader reader, Class<T> tClass, JsonPath... paths) {
        CollectOneListener listener = new CollectOneListener();
        Builder builder = builder().withJsonProvider(jsonProvider).skipOverlappedPath();
        for (JsonPath jsonPath : paths) {
            builder.bind(jsonPath, listener);
        }
        surf(reader, builder.build());
        Object value = listener.getValue();
        return tClass.cast(jsonProvider.cast(value, tClass));
    }

    @Override
    public <T> Collection<T> collectAll(Reader reader, Class<T> tClass, String... paths) {
        return collectAll(reader, tClass, compile(paths));
    }

    @Override
    public <T> T collectOne(Reader reader, Class<T> tClass, String... paths) {
        return collectOne(reader, tClass, compile(paths));
    }

    @Override
    public Collection<Object> collectAll(Reader reader, String... paths) {
        return collectAll(reader, Object.class, paths);
    }

    @Override
    public Object collectOne(Reader reader, String... paths) {
        return collectOne(reader, Object.class, paths);
    }

    protected void ensureSetting(SurfingContext context) {
        if (context.getJsonProvider() == null) {
            context.setJsonProvider(jsonProvider);
        }
        if (context.getErrorHandlingStrategy() == null) {
            context.setErrorHandlingStrategy(errorHandlingStrategy);
        }
    }

    public ErrorHandlingStrategy getErrorHandlingStrategy() {
        return errorHandlingStrategy;
    }

    public void setErrorHandlingStrategy(ErrorHandlingStrategy errorHandlingStrategy) {
        this.errorHandlingStrategy = errorHandlingStrategy;
    }
}
