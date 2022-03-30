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

import org.jsfr.json.SurfingConfiguration.Binding;
import org.jsfr.json.SurfingConfiguration.IndefinitePathBinding;
import org.jsfr.json.path.ArrayIndex;
import org.jsfr.json.path.ChildNode;
import org.jsfr.json.path.PathOperator;
import org.jsfr.json.path.PathOperator.Type;

import java.util.*;

/**
 * SurfingContext is not thread-safe
 */
public class SurfingContext implements ParsingContext, JsonSaxHandler {

    private boolean stopped = false;
    private boolean paused = false;
    private JsonPosition currentPosition;
    private ContentDispatcher dispatcher = new ContentDispatcher();
    private FilterVerifierDispatcher filterVerifierDispatcher;
    private SurfingConfiguration config;
    private Map<String, Object> transientMap;

    SurfingContext(SurfingConfiguration config) {
        this.config = config;
//        if (config.hasFilter()) {
        this.filterVerifierDispatcher = new FilterVerifierDispatcher();
//            this.dispatcher.addReceiver(this.filterVerifierDispatcher);
//        }
    }

    private void doMatching(PrimitiveHolder primitiveHolder) {

        // skip matching if "skipOverlappedPath" is enable
        if (!config.hasFilter() && config.isSkipOverlappedPath() && !dispatcher.isEmpty()) {
            return;
        }

        LinkedList<JsonPathListener> listeners = null;
        int currentDepth = currentPosition.pathDepth();

//        if (config.hasFilter()) {
//
//            // skip matching if "skipOverlappedPath" is enable
////            if (config.isSkipOverlappedPath() && dispatcher.size() > 1) {
////                return;
////            }
//            for (IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
//                if (binding.minimumPathDepth <= currentDepth) {
//                    listeners = doMatchingWithFilter(binding, primitiveHolder, listeners, false);
//                } else {
//                    break;
//                }
//            }
//            Binding[] bindings = config.getDefinitePathBind(currentDepth);
//            if (bindings != null) {
//                for (Binding binding : bindings) {
//                    listeners = doMatchingWithFilter(binding, primitiveHolder, listeners, true);
//                }
//            }

//            if (listeners != null) {
//
//                JsonFilterVerifier filterVerifier = (JsonFilterVerifier) this.filterVerifierDispatcher.getLastReceiver();
//                if (filterVerifier != null) {
//                    LinkedList<JsonPathListener> wrappedListeners = new LinkedList<>();
//                    for (JsonPathListener listener : listeners) {
//                        wrappedListeners.add(filterVerifier.addListener(listener));
//                    }
//                    listeners = wrappedListeners;
//                }
//
//            }

//        } else {

        for (IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
            if (binding.minimumPathDepth <= currentDepth) {
                listeners = doMatching(binding, primitiveHolder, listeners, false);
            } else {
                break;
            }
        }
        Binding[] bindings = config.getDefinitePathBind(currentDepth);
        if (bindings != null) {
            for (Binding binding : bindings) {
                listeners = doMatching(binding, primitiveHolder, listeners, true);
            }
        }

//        }

        if (listeners != null) {
            JsonCollector collector = new JsonCollector(listeners.size() == 1 ? Collections.singleton(listeners.getFirst()) : listeners, this, config);
            dispatcher.addReceiver(collector);
        }

    }

    private void doMatchingWithFilter(Binding binding, boolean definiteBinding) {
        if (binding.filter != null) {
            boolean matched = definiteBinding ? binding.jsonPath.match(currentPosition) : binding.jsonPath.matchWithDeepScan(currentPosition);
            if (matched) {
//            if (binding.filter != null) {
                // JsonPathFilter is stateful so clone is required
                // TODO not clone for stateless filter
                this.filterVerifierDispatcher.addVerifier(binding, new JsonFilterVerifier(currentPosition, config, binding));
//            } else {
//                if (primitiveHolder != null) {
//                    dispatchPrimitiveWithFilter(binding.getListeners(), primitiveHolder.getValue(), binding.dependency);
//                } else {
//                    return this.addListeners(binding, listeners, binding.dependency);
//                }
//            }
            }
        }
//        return listeners;
    }

    private LinkedList<JsonPathListener> doMatching(Binding binding, PrimitiveHolder primitiveHolder, LinkedList<JsonPathListener> listeners, boolean definiteBinding) {
        if (binding.filter != null) {
            return listeners;
        }

        if (binding.dependency != null) {
            if (primitiveHolder != null) {
                dispatchPrimitiveWithFilter(primitiveHolder.getValue(), binding);
            } else {
                return this.addListenersWithFilter(binding, listeners);
            }
        } else {
            boolean matched = definiteBinding ? binding.jsonPath.match(currentPosition) : binding.jsonPath.matchWithDeepScan(currentPosition);
            if (matched) {
                if (primitiveHolder != null) {
                    dispatchPrimitive(binding.getListeners(), primitiveHolder.getValue());
                } else {
                    return this.addListeners(binding, listeners);
                }
            }
        }
        return listeners;

    }

    private LinkedList<JsonPathListener> addListenersWithFilter(Binding binding, LinkedList<JsonPathListener> listeners) {
        LinkedList<JsonPathListener> listenersToAdd = listeners == null ? new LinkedList<JsonPathListener>() : listeners;
//        JsonPathListener[] bindingListeners = binding.getListeners();
//        Binding dependency = binding.dependency;
//        if (dependency != null) {
        List<JsonPathListener> buffered = filterVerifierDispatcher.dispatch(this.currentPosition, binding);
        listenersToAdd.addAll(buffered);
//        }
//        else {
//            for (JsonPathListener listener : bindingListeners) {
////            if (verifier != null) {
////                listenersToAdd.add(verifier.addListener(listener));
////            } else {
//                listenersToAdd.add(listener);
////            }
//            }
//        }
//        Collections.addAll(listenersToAdd, binding.getListeners());
        return listenersToAdd;
    }

    private LinkedList<JsonPathListener> addListeners(Binding binding, LinkedList<JsonPathListener> listeners) {
        LinkedList<JsonPathListener> listenersToAdd = listeners == null ? new LinkedList<JsonPathListener>() : listeners;
        Collections.addAll(listenersToAdd, binding.getListeners());
        return listenersToAdd;
    }

    private void dispatchPrimitiveWithFilter(Object primitive, Binding binding) {

//        Binding dependency = binding.dependency;

        //        JsonFilterVerifier filterVerifier = (JsonFilterVerifier) this.filterVerifierDispatcher.getLastReceiver();
//        if (dependency != null) {
        List<JsonPathListener> buffered = this.filterVerifierDispatcher.dispatch(this.currentPosition, binding);
//            JsonFilterVerifier filterVerifier = this.filterVerifierDispatcher.getVerifier(dependency);
        for (JsonPathListener listener : buffered) {
//                JsonPathListener newListener = filterVerifier.addListener(listener);
            listener.onValue(primitive, this);
        }
//        } else {
//            dispatchPrimitive(listeners, primitive);
//        }
    }

    private void dispatchPrimitive(JsonPathListener[] listeners, Object primitive) {
        DispatchUtil.dispatchValueToListeners(primitive, listeners, this, config.getErrorHandlingStrategy());
    }

    @Override
    public boolean startJSON() {
        currentPosition = JsonPosition.start();
        doMatching(null);
        dispatcher.startJSON();
        filterVerifierDispatcher.startJSON();
        return true;
    }

    @Override
    public boolean endJSON() {
        dispatcher.endJSON();
        filterVerifierDispatcher.endJSON();
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
                matchFilter();
                doMatching(null);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
//                startArrayElement();
                matchFilter();
                doMatching(null);
                break;
            case ROOT:
                break;
            default:
                throw new IllegalStateException();
        }
        currentPosition.stepIntoObject();
        dispatcher.startObject();
        filterVerifierDispatcher.startObject();
        return true;
    }

    private void matchFilter() {
        if (this.config.hasFilter()) {
            int currentDepth = currentPosition.pathDepth();

            for (IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
                if (binding.minimumPathDepth <= currentDepth) {
                    doMatchingWithFilter(binding, false);
                } else {
                    break;
                }
            }
            Binding[] bindings = config.getDefinitePathBind(currentDepth);
            if (bindings != null) {
                for (Binding binding : bindings) {
                    doMatchingWithFilter(binding, true);
                }
            }
        }
    }

    @Override
    public boolean endObject() {
        if (shouldBreak()) {
            return false;
        }
        currentPosition.stepOutObject();
        dispatcher.endObject();
        filterVerifierDispatcher.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        if (shouldBreak()) {
            return false;
        }
        currentPosition.updateObjectEntry(key);
        dispatcher.startObjectEntry(key);
        filterVerifierDispatcher.startObjectEntry(key);
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
                matchFilter();
                doMatching(null);
                break;
            case ARRAY:
                accumulateArrayIndex((ArrayIndex) currentNode);
//                startArrayElement();
                matchFilter();
                doMatching(null);
                break;
            case ROOT:
                break;
            default:
                throw new IllegalStateException();
        }

        currentPosition.stepIntoArray();
        dispatcher.startArray();
        filterVerifierDispatcher.startArray();
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
        filterVerifierDispatcher.endArray();
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
//                startArrayElement();
                doMatching(primitiveHolder);
                break;
            case ROOT:
                break;
            default:
                throw new IllegalStateException();
        }

        dispatcher.primitive(primitiveHolder);
        filterVerifierDispatcher.primitive(primitiveHolder);
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

}
