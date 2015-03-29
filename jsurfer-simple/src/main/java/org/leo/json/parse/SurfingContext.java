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
        private Map<Integer, ArrayList<Binding>> definiteBindings = new HashMap<Integer, ArrayList<Binding>>();
        private ArrayList<IndefinitePathBinding> indefiniteBindings = new ArrayList<IndefinitePathBinding>();


        public static Builder builder() {
            Builder builder = new Builder();
            builder.context = new SurfingContext();
            return builder;
        }

        public SurfingContext build() {
            if (!context.built) {
                if (context.jsonProvider == null) {
                    context.jsonProvider = new JavaCollectionProvider();
                }
                if (!indefiniteBindings.isEmpty()) {
                    Collections.sort(indefiniteBindings);
                    context.indefinitePathMap = indefiniteBindings.toArray(new IndefinitePathBinding[indefiniteBindings.size()]);
                }
                if (!definiteBindings.isEmpty()) {
                    context.definitePathMap = new HashMap<Integer, Binding[]>(definiteBindings.size());
                    for (Map.Entry<Integer, ArrayList<Binding>> entry : definiteBindings.entrySet()) {
                        context.definitePathMap.put(entry.getKey(), entry.getValue().toArray(new Binding[entry.getValue().size()]));
                    }
                }
                context.built = true;
            }
            return context;
        }

        public Builder bind(JsonPath.Builder builder, JsonPathListener... jsonPathListeners) {
            return bind(builder.build(), jsonPathListeners);
        }

        private void check() {
            if (context.built) {
                throw new IllegalStateException("The JsonSurfer is already built");
            }
        }

        public Builder bind(JsonPath jsonPath, JsonPathListener... jsonPathListeners) {
            check();
            if (!jsonPath.isDefinite()) {
                int minimumDepth = jsonPath.minimumPathDepth();
                indefiniteBindings.add(new IndefinitePathBinding(jsonPath, jsonPathListeners, minimumDepth));
            } else {
                int depth = jsonPath.pathDepth();
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
            check();
            context.skipOverlappedPath = true;
            return this;
        }

        public Builder withJsonProvider(JsonProvider provider) {
            check();
            context.jsonProvider = provider;
            return this;
        }

    }

    private static class IndefinitePathBinding extends Binding implements Comparable<IndefinitePathBinding> {

        private int minimumPathDepth;

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

    private static class Binding {

        public Binding(JsonPath jsonPath, JsonPathListener[] listeners) {
            this.jsonPath = jsonPath;
            this.listeners = listeners;
        }

        protected JsonPath jsonPath;
        protected JsonPathListener[] listeners;

    }

    private boolean built = false;
    private boolean stopped = false;
    private boolean skipOverlappedPath = false;
    private JsonProvider jsonProvider;
    private JsonPosition currentPosition;
    private Map<Integer, Binding[]> definitePathMap;

    // sorted by minimum path depth
    private IndefinitePathBinding[] indefinitePathMap;

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

        if (indefinitePathMap != null) {
            for (IndefinitePathBinding binding : indefinitePathMap) {
                if (binding.minimumPathDepth <= currentPosition.pathDepth()) {
                    if (binding.jsonPath.match(currentPosition)) {
                        if (listeners == null) {
                            listeners = new LinkedList<JsonPathListener>();
                        }
                        Collections.addAll(listeners, binding.listeners);
                    }
                } else {
                    break;
                }
            }
        }
        if (definitePathMap != null) {
            Binding[] bindings = definitePathMap.get(currentPosition.pathDepth());
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
