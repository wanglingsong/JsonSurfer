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

import org.jsfr.json.filter.CloneableJsonPathFilter;
import org.jsfr.json.filter.JsonPathFilter;

import java.util.ArrayList;
import java.util.Collection;

public class JsonFilterVerifier implements JsonSaxHandler {

    private final int startDepth;
    private SurfingConfiguration config;
    private SurfingConfiguration.Binding binding;
    private JsonPathFilter jsonPathFilter;
    private Collection<BufferedListener> bufferedListeners;
    private Collection<JsonFilterVerifier> dependencies;
    private JsonPosition currentPosition;
    private boolean verified = false;
    private int stackDepth = 0;

    public JsonFilterVerifier(JsonPosition currentPosition, SurfingConfiguration config, SurfingConfiguration.Binding binding) {
        this.currentPosition = currentPosition;
        this.startDepth = currentPosition.pathDepth();
        this.config = config;
        this.binding = binding;
        this.jsonPathFilter = (JsonPathFilter) ((CloneableJsonPathFilter) binding.filter).cloneMe();
//        this.dependency = dependency;
        this.bufferedListeners = new ArrayList<>();
    }

    public int getStartDepth() {
        return startDepth;
    }

    public void setDependencies(Collection<JsonFilterVerifier> dependencies) {
        this.dependencies = dependencies;
    }

    public JsonPathListener addListener(JsonPathListener listener) {
        BufferedListener newListener = new BufferedListener(this.config, listener);
        this.bufferedListeners.add(newListener);
        return newListener;
    }

    private void invokeBuffer() {
        if (dependencies != null) {
            for (JsonFilterVerifier dependency : dependencies) {
                dependency.bufferedListeners.addAll(this.bufferedListeners);
            }
        } else {
            for (BufferedListener buffer : this.bufferedListeners) {
                buffer.invokeBufferedValue();
            }
        }
    }

    @Override
    public boolean startJSON() {
        return true;
    }

    @Override
    public boolean endJSON() {
        return false;
    }

    @Override
    public boolean startObject() {
        this.stackDepth++;
        return true;
    }

    @Override
    public boolean startObjectEntry(String key) {
        if (!this.verified && this.jsonPathFilter.apply(this.currentPosition, this.startDepth, null, this.config.getJsonProvider())) {
            this.verified = true;
        }
        return true;
    }

    @Override
    public boolean endObject() {
        return this.endObjectOrArray();
    }

    @Override
    public boolean startArray() {
        this.stackDepth++;
        return true;
    }

    @Override
    public boolean endArray() {
        return this.endObjectOrArray();
    }

    private boolean endObjectOrArray() {
        this.stackDepth--;
        if (this.stackDepth == 0) {
            if (this.verified) {
                this.invokeBuffer();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        if (!this.verified && this.jsonPathFilter.apply(this.currentPosition, this.startDepth, primitiveHolder, this.config.getJsonProvider())) {
            this.verified = true;
        }
        return true;
    }

}
