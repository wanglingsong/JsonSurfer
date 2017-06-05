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

import org.jsfr.json.path.ArrayIndex;
import org.jsfr.json.path.ChildNode;
import org.jsfr.json.path.PathOperator;
import org.jsfr.json.path.PathOperator.Type;

import java.util.Collections;
import java.util.LinkedList;

/**
 * SurfingContext is not thread-safe
 */
class SurfingContext implements ParsingContext, JsonSaxHandler {

    private boolean stopped = false;
    private JsonPosition currentPosition;
    private ContentDispatcher dispatcher = new ContentDispatcher();
    private SurfingConfiguration config;
    private PrimitiveHolder currentValue;

    public SurfingContext(SurfingConfiguration config) {
        this.config = config;
    }

    private void doMatching(PrimitiveHolder primitiveHolder) {

        // skip matching if "skipOverlappedPath" is enable
        if (config.isSkipOverlappedPath() && !dispatcher.isEmpty()) {
            return;
        }
        LinkedList<JsonPathListener> listeners = null;

        int currentDepth = currentPosition.pathDepth();
        for (SurfingConfiguration.IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
            if (binding.minimumPathDepth <= currentDepth) {
                listeners = doMatching(binding, primitiveHolder, listeners);
            } else {
                break;
            }
        }
        SurfingConfiguration.Binding[] bindings = config.getDefinitePathBind(currentDepth);
        if (bindings != null) {
            for (SurfingConfiguration.Binding binding : bindings) {
                listeners = doMatching(binding, primitiveHolder, listeners);
            }
        }

        if (listeners != null) {
            JsonCollector collector = new JsonCollector(listeners.size() == 1 ? Collections.singleton(listeners.getFirst()) : listeners, this, config.getErrorHandlingStrategy());
            collector.setProvider(config.getJsonProvider());
            dispatcher.addReceiver(collector);
        }
    }

    private LinkedList<JsonPathListener> doMatching(SurfingConfiguration.Binding binding, PrimitiveHolder primitiveHolder, LinkedList<JsonPathListener> listeners) {
        if (binding.jsonPath.match(currentPosition)) {
            if (primitiveHolder != null) {
                dispatchPrimitive(binding, primitiveHolder.getValue());
            } else {
                LinkedList<JsonPathListener> listenersToAdd = listeners == null ? new LinkedList<JsonPathListener>() : listeners;
                if (binding.jsonPath.getJsonPathFilter() != null) {
                    for (JsonPathListener jsonPathListener : binding.listeners) {
                        listenersToAdd.add(new FilteredJsonPathListener(binding.jsonPath.getJsonPathFilter(), jsonPathListener, config));
                    }
                } else {
                    Collections.addAll(listenersToAdd, binding.listeners);
                }
                return listenersToAdd;
            }
        }
        return listeners;
    }

    private void dispatchPrimitive(SurfingConfiguration.Binding binding, Object primitive) {
        for (JsonPathListener listener : binding.listeners) {
            if (isStopped()) {
                break;
            }
            try {
                // TODO apply filter?
                listener.onValue(primitive, this);
            } catch (Exception e) {
                config.getErrorHandlingStrategy().handleExceptionFromListener(e, this);
            }
        }
    }

    @Override
    public boolean startJSON() {
        if (stopped) {
            return true;
        }
        currentPosition = JsonPosition.start();
        doMatching(null);
        dispatcher.startJSON();
        return true;
    }

    @Override
    public boolean endJSON() {
        if (stopped) {
            return true;
        }
        dispatcher.endJSON();
        // clear resources
        currentPosition.clear();
        currentPosition = null;
        return true;
    }

    @Override
    public boolean startObject() {
        if (stopped) {
            return false;
        }
        PathOperator currentNode = currentPosition.peek();
        switch (currentNode.getType()) {
            case OBJECT:
                doMatching(null);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
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
        if (stopped) {
            return false;
        }
        currentPosition.stepOutObject();
        dispatcher.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        if (stopped) {
            return false;
        }
        currentPosition.updateObjectEntry(key);
        dispatcher.startObjectEntry(key);
        return true;
    }

    @Override
    public boolean startArray() {
        if (stopped) {
            return false;
        }
        PathOperator currentNode = currentPosition.peek();
        switch (currentNode.getType()) {
            case OBJECT:
                doMatching(null);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
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
        if (stopped) {
            return false;
        }
        currentPosition.stepOutArray();
        dispatcher.endArray();
        return true;
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        if (stopped) {
            return false;
        }
        this.currentValue = primitiveHolder;
        PathOperator currentNode = currentPosition.peek();
        switch (currentNode.getType()) {
            case OBJECT:
                doMatching(primitiveHolder);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
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
    public PrimitiveHolder getCurrentValue() {
        return this.currentValue;
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
    public void stopParsing() {
        this.stopped = true;
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    public SurfingConfiguration getConfig() {
        return config;
    }
}
