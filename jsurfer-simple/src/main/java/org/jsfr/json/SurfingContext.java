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

    public SurfingContext(SurfingConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean startJSON() {
        if (stopped) {
            return true;
        }
        currentPosition = JsonPosition.start();
        doMatching(false, null);
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
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(true, null);
        }
        dispatcher.startObject();
        return true;
    }

    private void doMatching(boolean initializeCollector, PrimitiveHolder primitiveHolder) {

        // TODO Refactor matching logic

        // skip matching if "skipOverlappedPath" is enable
        if (config.isSkipOverlappedPath() && !this.dispatcher.isEmpty()) {
            return;
        }
        LinkedList<JsonPathListener> listeners = null;

        int currentDepth = currentPosition.pathDepth();
        for (SurfingConfiguration.IndefinitePathBinding binding : config.getIndefinitePathLookup()) {
            if (binding.minimumPathDepth <= currentDepth) {
                if (binding.jsonPath.match(currentPosition)) {
                    if (primitiveHolder != null) {
                        dispatchPrimitive(binding, primitiveHolder.getValue());
                    } else {
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
        SurfingConfiguration.Binding[] bindings = config.getDefinitePathBind(currentDepth);
        if (bindings != null) {
            for (SurfingConfiguration.Binding binding : bindings) {
                if (binding.jsonPath.match(currentPosition)) {
                    if (primitiveHolder != null) {
                        dispatchPrimitive(binding, primitiveHolder.getValue());
                    } else {
                        if (listeners == null) {
                            listeners = new LinkedList<JsonPathListener>();
                        }
                        Collections.addAll(listeners, binding.listeners);
                    }
                }
            }
        }

        if (listeners != null) {
            JsonCollector collector = new JsonCollector(listeners.toArray(new JsonPathListener[listeners.size()]), this, config.getErrorHandlingStrategy());
            collector.setProvider(config.getJsonProvider());
            if (initializeCollector) {
                collector.startJSON();
            }
            dispatcher.addReceiver(collector);
        }
    }

    private void dispatchPrimitive(SurfingConfiguration.Binding binding, Object primitive) {
        for (JsonPathListener listener : binding.listeners) {
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
    public boolean endObject() {
        if (stopped) {
            return false;
        }
        if (currentPosition.peekType() == Type.OBJECT) {
            currentPosition.stepOut();
        }
        dispatcher.endObject();
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        if (stopped) {
            return false;
        }
        currentPosition.stepIntoChild(key);
        dispatcher.startObjectEntry(key);
        doMatching(true, null);
        return true;
    }


    @Override
    public boolean startArray() {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(true, null);
        }
        currentPosition.stepIntoArray();
        dispatcher.startArray();
        return true;
    }

    @Override
    public boolean endArray() {
        if (stopped) {
            return false;
        }
        currentPosition.stepOut();
        if (currentPosition.peekType() == Type.OBJECT) {
            currentPosition.stepOut();
        }
        dispatcher.endArray();
        return true;
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        if (stopped) {
            return false;
        }
        PathOperator top = currentPosition.peek();
        if (top.getType() == Type.ARRAY) {
            ((ArrayIndex) top).increaseArrayIndex();
            doMatching(true, primitiveHolder);
        } else if (top.getType() == Type.OBJECT) {
            currentPosition.stepOut();
        }
        dispatcher.primitive(primitiveHolder);
        return true;
    }

    @Override
    public String getJsonPath() {
        return this.currentPosition.toString();
    }

    @Override
    public String getKey() {
        PathOperator peek = currentPosition.peek();
        if (peek.getType() == Type.OBJECT) {
            return ((ChildNode) peek).getKey();
        }
        return null;
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
