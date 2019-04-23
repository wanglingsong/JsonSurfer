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

import org.jsfr.json.SurfingConfiguration.Binding;
import org.jsfr.json.SurfingConfiguration.IndefinitePathBinding;
import org.jsfr.json.filter.JsonPathFilter;
import org.jsfr.json.path.ArrayFilter;
import org.jsfr.json.path.ArrayIndex;
import org.jsfr.json.path.ChildNode;
import org.jsfr.json.path.PathOperator;
import org.jsfr.json.path.PathOperator.Type;

import java.util.*;

/**
 * SurfingContext is not thread-safe
 */
class SurfingContext implements ParsingContext, JsonSaxHandler {

    private boolean stopped = false;
    private boolean paused = false;
    private JsonPosition currentPosition;
    private ContentDispatcher dispatcher = new ContentDispatcher();
    private SurfingConfiguration config;
    private Map<String, Object> transientMap;

    SurfingContext(SurfingConfiguration config) {
        this.config = config;
    }

    private void doMatching(PrimitiveHolder primitiveHolder) {

        // skip matching if "skipOverlappedPath" is enable
        if (config.isSkipOverlappedPath() && !dispatcher.isEmpty()) {
            return;
        }
        LinkedList<JsonPathListener> listeners = null;

        int currentDepth = currentPosition.pathDepth();
        for (IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
            if (binding.minimumPathDepth <= currentDepth) {
                listeners = doMatching(binding, primitiveHolder, listeners);
            } else {
                break;
            }
        }
        Binding[] bindings = config.getDefinitePathBind(currentDepth);
        if (bindings != null) {
            for (Binding binding : bindings) {
                listeners = doMatching(binding, primitiveHolder, listeners);
            }
        }

        if (listeners != null) {
            JsonCollector collector = new JsonCollector(listeners.size() == 1 ? Collections.singleton(listeners.getFirst()) : listeners, this, config);
            collector.setProvider(config.getJsonProvider());
            dispatcher.addReceiver(collector);
        }
    }

    private LinkedList<JsonPathListener> doMatching(Binding binding, PrimitiveHolder primitiveHolder, LinkedList<JsonPathListener> listeners) {
        if (binding.jsonPath.match(currentPosition)) {
            if (primitiveHolder != null) {
                dispatchPrimitive(binding, primitiveHolder.getValue());
            } else {
                LinkedList<JsonPathListener> listenersToAdd = listeners == null ? new LinkedList<JsonPathListener>() : listeners;
                Collections.addAll(listenersToAdd, binding.getListeners());
                return listenersToAdd;
            }
        }
        return listeners;
    }

    private void dispatchPrimitive(Binding binding, Object primitive) {
        for (JsonPathListener listener : binding.getListeners()) {
            if (isStopped()) {
                break;
            }
            try {
                listener.onValue(primitive, this);
            } catch (Exception e) {
                config.getErrorHandlingStrategy().handleExceptionFromListener(e, this);
            }
        }
    }

    @Override
    public boolean startJSON() {
        currentPosition = JsonPosition.start();
        doMatching(null);
        dispatcher.startJSON();
        return true;
    }

    @Override
    public boolean endJSON() {
        dispatcher.endJSON();
        // clear resources
        currentPosition.clear();
        currentPosition = null;
        this.stop();
        return true;
    }

    @Override
    public boolean startObject() {
        if (shouldBreak()) {
            return false;
        }
        PathOperator currentNode = currentPosition.peek();
        switch (currentNode.getType()) {
            case OBJECT:
                doMatching(null);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
                startArrayElement();
                doMatching(null);
                break;
            case ROOT:
                break;
            default:
                throw new IllegalStateException();
        }
        currentPosition.stepIntoObject();
        dispatcher.startObject();
        return true;
    }

    @Override
    public boolean endObject() {
        if (shouldBreak()) {
            return false;
        }
        currentPosition.stepOutObject();
        dispatcher.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        if (shouldBreak()) {
            return false;
        }
        currentPosition.updateObjectEntry(key);
        dispatcher.startObjectEntry(key);
        return true;
    }

    @Override
    public boolean startArray() {
        if (shouldBreak()) {
            return false;
        }
        PathOperator currentNode = currentPosition.peek();
        switch (currentNode.getType()) {
            case OBJECT:
                doMatching(null);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
                startArrayElement();
                doMatching(null);
                break;
            case ROOT:
                break;
            default:
                throw new IllegalStateException();
        }

        currentPosition.stepIntoArray();
        dispatcher.startArray();
        return true;
    }

    private void accumulateArrayIndex(ArrayIndex arrayIndex) {
        arrayIndex.increaseArrayIndex();
    }

    @Override
    public boolean endArray() {
        if (shouldBreak()) {
            return false;
        }
        currentPosition.stepOutArray();
        dispatcher.endArray();
        return true;
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        if (shouldBreak()) {
            return false;
        }
        PathOperator currentNode = currentPosition.peek();
        switch (currentNode.getType()) {
            case OBJECT:
                doMatching(primitiveHolder);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
                startArrayElement();
                doMatching(primitiveHolder);
                break;
            case ROOT:
                break;
            default:
                throw new IllegalStateException();
        }

        dispatcher.primitive(primitiveHolder);
        return true;
    }

    @Override
    public String getJsonPath() {
        return this.currentPosition.toString();
    }

    @Override
    public String getCurrentFieldName() {
        PathOperator top = this.currentPosition.peek();
        if (top instanceof ChildNode) {
            return ((ChildNode) top).getKey();
        } else {
            return null;
        }
    }

    @Override
    public int getCurrentArrayIndex() {
        PathOperator top = this.currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            return ((ArrayIndex) top).getArrayIndex();
        } else {
            return -1;
        }
    }

    @Override
    public void save(String key, Object value) {
        if (this.transientMap == null) {
            this.transientMap = new HashMap<String, Object>();
        }
        this.transientMap.put(key, value);
    }

    @Override
    public <T> T load(String key, Class<T> tClass) {
        return this.transientMap != null ? tClass.cast(this.transientMap.get(key)) : null;
    }

    @Override
    public <T> T cast(Object object, Class<T> tClass) {
        return (T) this.config.getJsonProvider().cast(object, tClass);
    }

    public boolean shouldBreak() {
        return this.stopped || this.paused;
    }

    @Override
    public void stop() {
        this.stopped = true;
        this.paused = false;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    public SurfingConfiguration getConfig() {
        return config;
    }

    private void startArrayElement() {
        if (config.getDefinitePathBindings() != null) {
            for (Binding[] bindingsPerDepth : config.getDefinitePathBindings()) {
                if (bindingsPerDepth != null) {
                    for (Binding binding : bindingsPerDepth) {
                        if (shouldCreateElementVerifier(currentPosition, binding))
                            createElementVerifier(binding);
                    }
                }
            }
        }

        for (IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
            if (shouldCreateElementVerifier(currentPosition, binding))
                createElementVerifier(binding);
        }
    }

    private boolean shouldCreateElementVerifier(JsonPosition currentPosition, Binding binding) {
        PathOperator parentOperator = binding.jsonPath.get(currentPosition.pathDepth() - 1);
        return currentPosition.isSubPathOf(binding.jsonPath)
                && parentOperator instanceof ArrayFilter;
    }

    private void createElementVerifier(Binding binding) {
        PathOperator parentOperator = binding.jsonPath.get(currentPosition.pathDepth() - 1);
        JsonPathFilter filter = ((ArrayFilter) parentOperator).getJsonPathFilter();
        FilteredJsonPathListener[] filteredListeners = binding.wrapWithFilteredListener(this, config);
        JsonFilterVerifier jsonFilterVerifier = new JsonFilterVerifier(filter, Arrays.asList(filteredListeners), binding, this);
        jsonFilterVerifier.setProvider(config.getJsonProvider());
        dispatcher.addReceiver(jsonFilterVerifier);
    }

}
