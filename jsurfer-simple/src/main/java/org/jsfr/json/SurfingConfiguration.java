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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.jsfr.json.compiler.JsonPathCompiler.compile;

/**
 * SurfingConfiguration is immutable object that hold all JSONPath binding information
 */
public class SurfingConfiguration {

    public static Builder builder() {
        Builder builder = new Builder();
        builder.configuration = new SurfingConfiguration();
        return builder;
    }

    public static class Binding {

        public Binding(JsonPath jsonPath, JsonPathListener[] listeners) {
            this.jsonPath = jsonPath;
            this.listeners = listeners;
        }

        JsonPath jsonPath;
        JsonPathListener[] listeners;

    }

    public static class IndefinitePathBinding extends Binding implements Comparable<IndefinitePathBinding> {

        int minimumPathDepth;

        public IndefinitePathBinding(JsonPath jsonPath, JsonPathListener[] listeners, int minimumPathDepth) {
            super(jsonPath, listeners);
            this.minimumPathDepth = minimumPathDepth;
        }

        @Override
        public int compareTo(IndefinitePathBinding o) {
            if (minimumPathDepth < o.minimumPathDepth) {
                return -1;
            } else if (minimumPathDepth > o.minimumPathDepth) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    public static class Builder {

        private SurfingConfiguration configuration;
        private Map<Integer, ArrayList<Binding>> definiteBindings = new HashMap<Integer, ArrayList<Binding>>();
        private ArrayList<IndefinitePathBinding> indefiniteBindings = new ArrayList<IndefinitePathBinding>();

        public SurfingConfiguration build() {
            if (!indefiniteBindings.isEmpty()) {
                Collections.sort(indefiniteBindings);
                configuration.indefinitePathLookup = indefiniteBindings.toArray(new IndefinitePathBinding[indefiniteBindings.size()]);
            }
            if (!definiteBindings.isEmpty()) {
                configuration.definitePathLookup = new Binding[configuration.maxDepth - configuration.minDepth + 1][];
                for (Map.Entry<Integer, ArrayList<Binding>> entry : definiteBindings.entrySet()) {
                    configuration.definitePathLookup[entry.getKey() - configuration.minDepth] = entry.getValue().toArray(new Binding[entry.getValue().size()]);
                }
            }
            return configuration;
        }

        public Builder bind(String path, JsonPathListener... jsonPathListeners) {
            return bind(compile(path), jsonPathListeners);
        }

        public Builder bind(JsonPath.Builder builder, JsonPathListener... jsonPathListeners) {
            return bind(builder.build(), jsonPathListeners);
        }

        public <T> Builder bind(String jsonPath, final Class<T> tClass, TypedJsonPathListener<T>... typedListeners) {
            bind(compile(jsonPath), tClass, typedListeners);
            return this;
        }

        public <T> Builder bind(JsonPath jsonPath, final Class<T> tClass, TypedJsonPathListener<T>... typedListeners) {
            JsonPathListener[] listeners = new JsonPathListener[typedListeners.length];
            int i = 0;
            for (final TypedJsonPathListener<T> typedListener : typedListeners) {
                listeners[i++] = new JsonPathListener() {
                    @Override
                    public void onValue(Object value, ParsingContext parsingContext) throws Exception {
                        typedListener.onTypedValue((T) configuration.jsonProvider.cast(value, tClass), parsingContext);
                    }
                };
            }
            bind(jsonPath, listeners);
            return this;
        }

        public Builder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners) {
            if (!jsonPath.isDefinite()) {
                int minimumDepth = jsonPath.minimumPathDepth();
                indefiniteBindings.add(new IndefinitePathBinding(jsonPath, jsonPathListeners, minimumDepth));
            } else {
                int depth = jsonPath.pathDepth();
                if (depth > configuration.maxDepth) {
                    configuration.maxDepth = depth;
                }
                if (depth < configuration.minDepth) {
                    configuration.minDepth = depth;
                }
                ArrayList<Binding> bindings = definiteBindings.get(depth);
                if (bindings == null) {
                    bindings = new ArrayList<Binding>();
                    definiteBindings.put(depth, bindings);
                }
                bindings.add(new Binding(jsonPath, jsonPathListeners));
            }
            return this;
        }

        public Builder skipOverlappedPath() {
            configuration.skipOverlappedPath = true;
            return this;
        }

        public Builder withJsonProvider(JsonProvider provider) {
            configuration.jsonProvider = provider;
            return this;
        }

        public Builder withErrorStrategy(ErrorHandlingStrategy errorHandlingStrategy) {
            configuration.errorHandlingStrategy = errorHandlingStrategy;
            return this;
        }

    }

    private int minDepth = Integer.MAX_VALUE;
    private int maxDepth = -1;
    private boolean skipOverlappedPath = false;

    private Binding[][] definitePathLookup;

    // sorted by minimum path depth
    private IndefinitePathBinding[] indefinitePathLookup;

    private JsonProvider jsonProvider;
    private ErrorHandlingStrategy errorHandlingStrategy;

    public int getMinDepth() {
        return minDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public boolean isSkipOverlappedPath() {
        return skipOverlappedPath;
    }

    // TODO remove it
    Binding[][] getDefinitePathLookup() {
        return definitePathLookup;
    }

    // TODO remove it
    IndefinitePathBinding[] getIndefinitePathLookup() {
        return indefinitePathLookup;
    }

    public JsonProvider getJsonProvider() {
        return jsonProvider;
    }

    public void setJsonProvider(JsonProvider jsonProvider) {
        this.jsonProvider = jsonProvider;
    }

    public ErrorHandlingStrategy getErrorHandlingStrategy() {
        return errorHandlingStrategy;
    }

    void setErrorHandlingStrategy(ErrorHandlingStrategy errorHandlingStrategy) {
        this.errorHandlingStrategy = errorHandlingStrategy;
    }

    public boolean withinRange(int currentDepth) {
        // TODO
        return false;
    }

    public Binding[] getDefinitePathBind(int currentDepth) {
        // TODO
        return null;
    }
}
