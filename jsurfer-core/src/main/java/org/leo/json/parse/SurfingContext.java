/*
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

package org.leo.json.parse;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;
import org.leo.json.path.ArrayIndex;
import org.leo.json.path.JsonPath;
import org.leo.json.path.PathOperator;
import org.leo.json.path.PathOperator.Type;

import java.io.IOException;
import java.util.*;

public class SurfingContext implements ParsingContext, ContentHandler {

    public static class Builder {

        private SurfingContext context;

        public static Builder builder() {
            Builder builder = new Builder();
            builder.context = new SurfingContext();
            return builder;
        }

        public ContentHandler build() {
            if (context.jsonProvider == null) {
                context.jsonProvider = new JavaCollectionProvider();
            }
            if (!context.indefinitePathMap.isEmpty()) {
                LinkedHashMap<Integer, List<Binding>> sortedMap = new LinkedHashMap<Integer, List<Binding>>(context.indefinitePathMap.size());
                ArrayList<Integer> keys = new ArrayList<Integer>(context.indefinitePathMap.keySet());
                Collections.sort(keys);
                for (Integer key : keys) {
                    sortedMap.put(key, context.indefinitePathMap.get(key));
                }
                context.indefinitePathMap = sortedMap;
            }
            context.built = true;
            return context;
        }

        public Builder bind(JsonPath.Builder builder, JsonPathListener... jsonPathListeners) {
            return bind(builder.build(), jsonPathListeners);
        }

        public Builder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners) {
            if (context.built) {
                throw new IllegalStateException("The JsonSurfer is already built");
            }
            if (!jsonPath.isDefinite()) {
                int minimumDepth = jsonPath.minimumPathDepth();
                List<Binding> bindings = context.indefinitePathMap.get(minimumDepth);
                if (bindings == null) {
                    bindings = new ArrayList<Binding>();
                    context.indefinitePathMap.put(minimumDepth, bindings);
                }
                bindings.add(new Binding(jsonPath, jsonPathListeners));
            } else {
                int depth = jsonPath.pathDepth();
                List<Binding> bindings = context.definitePathMap.get(depth);
                if (bindings == null) {
                    bindings = new ArrayList<Binding>();
                    context.definitePathMap.put(depth, bindings);
                }
                bindings.add(new Binding(jsonPath, jsonPathListeners));
            }
            return this;
        }

        public Builder skipOverlappedPath() {
            context.skipOverlappedPath = true;
            return this;
        }

        public Builder withJsonProvider(JsonProvider structureFactory) {
            context.jsonProvider = structureFactory;
            return this;
        }

    }

    private static class Binding {

        public Binding(JsonPath jsonPath, JsonPathListener[] listeners) {
            this.jsonPath = jsonPath;
            this.listeners = listeners;
        }

        private JsonPath jsonPath;
        private JsonPathListener[] listeners;

    }

    private boolean built = false;
    private boolean stopped = false;
    private boolean skipOverlappedPath = false;
    private JsonProvider jsonProvider;
    private JsonPosition currentPosition;
    private Map<Integer, List<Binding>> definitePathMap = new HashMap<Integer, List<Binding>>();
    private LinkedHashMap<Integer, List<Binding>> indefinitePathMap = new LinkedHashMap<Integer, List<Binding>>();
    private ContentDispatcher dispatcher = new ContentDispatcher();

    @Override
    public void startJSON() throws ParseException, IOException {
        if (stopped) {
            return;
        }
        currentPosition = JsonPosition.start();
        doMatching(false);
        dispatcher.startJSON();
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        if (stopped) {
            return;
        }
        dispatcher.endJSON();
        // clear resources
        currentPosition.clear();
        currentPosition = null;
        indefinitePathMap = null;
        definitePathMap = null;
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(true);
        }
        dispatcher.startObject();
        return true;
    }

    private void doMatching(boolean initializeCollector) throws IOException, ParseException {
        // skip matching if "skipOverlappedPath" is enable
        if (skipOverlappedPath && !this.dispatcher.isEmpty()) {
            return;
        }
        LinkedList<JsonPathListener> listeners = null;

        if (!indefinitePathMap.isEmpty()) {
            for (Map.Entry<Integer, List<Binding>> entry : indefinitePathMap.entrySet()) {
                if (entry.getKey() <= currentPosition.minimumPathDepth()) {
                    for (Binding binding : entry.getValue()) {
                        if (binding.jsonPath.match(currentPosition)) {
                            if (listeners == null) {
                                listeners = new LinkedList<JsonPathListener>();
                            }
                            Collections.addAll(listeners, binding.listeners);
                        }
                    }
                } else {
                    break;
                }
            }
        }

        List<Binding> bindings = definitePathMap.get(currentPosition.pathDepth());
        if (bindings != null) {
            for (Binding binding : bindings) {
                if (binding.jsonPath.match(currentPosition)) {
                    if (listeners == null) {
                        listeners = new LinkedList<JsonPathListener>();
                    }
                    Collections.addAll(listeners, binding.listeners);
                }
            }
        }

        if (listeners != null) {
            JsonCollector collector = new JsonCollector(listeners, this);
            collector.setProvider(jsonProvider);
            if (initializeCollector) {
                collector.startJSON();
            }
            dispatcher.addReceiver(collector);
        }
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        dispatcher.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        currentPosition.stepInObject(key);
        dispatcher.startObjectEntry(key);
        doMatching(true);
        return true;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        currentPosition.stepOut();
        dispatcher.endObjectEntry();
        return true;
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(true);
        }
        currentPosition.stepInArray();
        dispatcher.startArray();
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        currentPosition.stepOut();
        dispatcher.endArray();
        return true;
    }

    @Override
    public boolean primitive(final Object value) throws ParseException, IOException {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(true);
        }
        dispatcher.primitive(value);
        return true;
    }

    @Override
    public String getPath() {
        return this.currentPosition.toString();
    }


    @Override
    public void stopParsing() {
        this.stopped = true;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

}
