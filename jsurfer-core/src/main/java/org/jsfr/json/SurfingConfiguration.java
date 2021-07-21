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

import org.jsfr.json.filter.JsonPathFilter;
import org.jsfr.json.path.ArrayFilter;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.path.PathOperator;
import org.jsfr.json.provider.JsonProvider;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.jsfr.json.compiler.JsonPathCompiler.compile;

/**
 * SurfingConfiguration is immutable object that hold all JSONPath binding information
 */
public class SurfingConfiguration {

    public static class FilterConfig {
        JsonPath filterRootPath;
        JsonPathFilter filter;
    }

    public static Collection<FilterConfig> getFilterConfigs(JsonPath path) {
        ArrayList<FilterConfig> filterConfigs = new ArrayList<>();
        for (int i = 0; i < path.pathDepth(); i++) {
            PathOperator operator = path.get(i);
            if (operator instanceof ArrayFilter) {
                FilterConfig fc = new FilterConfig();
                fc.filter = ((ArrayFilter) operator).getJsonPathFilter();
                fc.filterRootPath = path.derivePath(i + 1);
                filterConfigs.add(fc);
            }
        }
        return filterConfigs;
    }

    public static Builder builder() {
        Builder builder = new Builder();
        builder.configuration = new SurfingConfiguration();
        return builder;
    }

    public static class Binding {

        Binding(JsonPath jsonPath, JsonPathListener[] listeners) {
            this.jsonPath = jsonPath;
            this.listeners = listeners;
        }

        JsonPath jsonPath;
        JsonPathFilter filter;
        Binding dependency;
        JsonPathListener[] listeners;
//        FilteredJsonPathListener[] filteredListeners;

        //        FilteredJsonPathListener[] wrapWithFilteredListener(ParsingContext context, SurfingConfiguration config) {
//            for (int i = 0; i < listeners.length; i++)
//                filteredListeners[i] = new FilteredJsonPathListener(getListeners()[i], context, config);
//            return filteredListeners;
//        }
//
        JsonPathListener[] getListeners() {
//            if (filteredListeners[0] == null) // if empty
            return listeners;
//            else
//                return filteredListeners;
        }
//
//        void unwrapListeners() {
//            for (int i = 0; i < filteredListeners.length; i++) {
//                if (filteredListeners[i].getUnderlyingListener() instanceof FilteredJsonPathListener) {
//                    FilteredJsonPathListener filteredListener = filteredListeners[i];
//                    filteredListeners[i] = (FilteredJsonPathListener) filteredListener.getUnderlyingListener();
//                    filteredListener.clear();
//                } else {
//                    filteredListeners[i] = null;
//                }
//            }
//        }

    }

    public static class IndefinitePathBinding extends Binding {

        int minimumPathDepth;

        IndefinitePathBinding(JsonPath jsonPath, JsonPathListener[] listeners, int minimumPathDepth) {
            super(jsonPath, listeners);
            this.minimumPathDepth = minimumPathDepth;
        }

    }

    public static class Builder {

        private JsonSurfer jsonSurfer;
        private SurfingConfiguration configuration;
        private Map<Integer, ArrayList<Binding>> definiteBindings = new HashMap<Integer, ArrayList<Binding>>();
        private ArrayList<IndefinitePathBinding> indefiniteBindings = new ArrayList<IndefinitePathBinding>();
        private boolean hasFilter = false;

        public SurfingConfiguration build() {
            if (!indefiniteBindings.isEmpty()) {
                Collections.sort(indefiniteBindings, INDEFINITE_BINDING_COMPARATOR);
                configuration.indefinitePathLookup = indefiniteBindings.toArray(new IndefinitePathBinding[0]);
            }
            if (!definiteBindings.isEmpty()) {
                configuration.definitePathLookup = new Binding[configuration.maxDepth - configuration.minDepth + 1][];
                for (Map.Entry<Integer, ArrayList<Binding>> entry : definiteBindings.entrySet()) {
                    configuration.definitePathLookup[entry.getKey() - configuration.minDepth] = entry.getValue().toArray(new Binding[0]);
                }
            }
            configuration.hasFilter = this.hasFilter;
            return configuration;
        }

        /**
         * Associated with a Charset
         *
         * @param charset charset
         * @return builder
         */
        public Builder withCharset(Charset charset) {
            configuration.parserCharset = charset;
            return this;
        }

        /**
         * Associated with a JsonSurfer
         *
         * @param jsonSurfer JsonSurfer
         * @return builder
         */
        public Builder withSurfer(JsonSurfer jsonSurfer) {
            this.jsonSurfer = jsonSurfer;
            return this;
        }

        /**
         * Build the configuration and then surf with it and the associated JsonSurfer
         *
         * @param json json
         */
        public void buildAndSurf(String json) {
            this.jsonSurfer.surf(json, this.build());
        }

        /**
         * Build the configuration and then surf with it and the associated JsonSurfer
         *
         * @param jsonReader jsonReader
         * @deprecated use {@link #buildAndSurf(InputStream)} instead
         */
        @Deprecated
        public void buildAndSurf(Reader jsonReader) {
            this.jsonSurfer.surf(jsonReader, this.build());
        }

        /**
         * Build the configuration and then surf with it and the associated JsonSurfer
         *
         * @param inputStream json
         */
        public void buildAndSurf(InputStream inputStream) {
            this.jsonSurfer.surf(inputStream, this.build());
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
                    public void onValue(Object value, ParsingContext parsingContext) {
                        typedListener.onTypedValue((T) configuration.jsonProvider.cast(value, tClass), parsingContext);
                    }
                };
            }
            bind(jsonPath, listeners);
            return this;
        }

        private ArrayList<Binding> getDefiniteBindings(int depth) {
            ArrayList<Binding> bindings = definiteBindings.get(depth);
            if (bindings == null) {
                bindings = new ArrayList<Binding>();
                definiteBindings.put(depth, bindings);
            }
            return bindings;
        }

        private void updateMinMaxDepth(int depth) {
            if (depth > configuration.maxDepth) {
                configuration.maxDepth = depth;
            }
            if (depth < configuration.minDepth) {
                configuration.minDepth = depth;
            }
        }

        public Builder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners) {
            Collection<FilterConfig> filterConfigs = getFilterConfigs(jsonPath);
            if (!filterConfigs.isEmpty()) {
                this.hasFilter = true;
            }

            Binding previous = null;
            for (FilterConfig fc : filterConfigs) {
                if (fc.filterRootPath.checkDefinite()) {
                    ArrayList<Binding> bindings = getDefiniteBindings(fc.filterRootPath.pathDepth());
                    Binding filterBiding = new Binding(fc.filterRootPath, null);
                    filterBiding.filter = fc.filter;
                    filterBiding.dependency = previous;
                    bindings.add(filterBiding);
                    updateMinMaxDepth(fc.filterRootPath.pathDepth());
                    previous = filterBiding;

                } else {
                    int minimumDepth = JsonPath.minimumPathDepth(fc.filterRootPath);
                    IndefinitePathBinding filterBiding = new IndefinitePathBinding(fc.filterRootPath, null, minimumDepth);
                    filterBiding.filter = fc.filter;
                    filterBiding.dependency = previous;
                    indefiniteBindings.add(filterBiding);
                    previous = filterBiding;
                }
            }
            if (!jsonPath.isDefinite()) {
                int minimumDepth = JsonPath.minimumPathDepth(jsonPath);
                IndefinitePathBinding binding = new IndefinitePathBinding(jsonPath, jsonPathListeners, minimumDepth);
                binding.dependency = previous;
                indefiniteBindings.add(binding);
            } else {
                int depth = jsonPath.pathDepth();
                updateMinMaxDepth(depth);
                ArrayList<Binding> bindings = getDefiniteBindings(depth);
                Binding binding = new Binding(jsonPath, jsonPathListeners);
                binding.dependency = previous;
                bindings.add(binding);
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

    private static final Comparator<IndefinitePathBinding> INDEFINITE_BINDING_COMPARATOR = new Comparator<IndefinitePathBinding>() {
        @Override
        public int compare(IndefinitePathBinding o1, IndefinitePathBinding o2) {
            return Integer.compare(o1.minimumPathDepth, o2.minimumPathDepth);
        }
    };

    private Charset parserCharset;
    private int minDepth = Integer.MAX_VALUE;
    private int maxDepth = -1;
    private boolean skipOverlappedPath = false;
    private boolean hasFilter = false;
    private boolean closeParserOnStop = true;

    private Binding[][] definitePathLookup;

    // sorted by minimum path depth
    private IndefinitePathBinding[] indefinitePathLookup = new IndefinitePathBinding[0];

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

    public boolean hasDefinitePath() {
        return definitePathLookup != null;
    }

    public IndefinitePathBinding[] getIndefinitePathLookup() {
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

    private boolean withinRange(int currentDepth) {
        return minDepth <= currentDepth && currentDepth <= maxDepth;
    }

    public Binding[] getDefinitePathBind(int currentDepth) {
        if (this.definitePathLookup != null && withinRange(currentDepth)) {
            return this.definitePathLookup[currentDepth - minDepth];
        } else {
            return null;
        }
    }

    public Binding[][] getDefinitePathBindings() {
        return this.definitePathLookup;
    }

    public Charset getParserCharset() {
        return parserCharset;
    }

    public void setParserCharset(Charset parserCharset) {
        this.parserCharset = parserCharset;
    }

    public boolean hasFilter() {
        return hasFilter;
    }

    public void setCloseParserOnStop(boolean closeParserOnStop) {
        this.closeParserOnStop = closeParserOnStop;
    }

    public boolean isCloseParserOnStop() {
        return closeParserOnStop;
    }

}
