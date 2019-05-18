package org.jsfr.json;

import org.jsfr.json.filter.JsonPathFilter;

import java.util.ArrayList;
import java.util.Collection;

public class JsonFilterVerifier implements JsonSaxHandler {

    private SurfingConfiguration config;
    private JsonPathFilter jsonPathFilter;
    private Collection<BufferedListener> bufferedListeners;
    private JsonFilterVerifier dependency;
    private JsonPosition currentPosition;
    private boolean verified = false;
    private int stackDepth = 0;

    public JsonFilterVerifier(JsonPosition currentPosition, SurfingConfiguration config, JsonPathFilter jsonPathFilter, JsonFilterVerifier dependency) {
        this.currentPosition = currentPosition;
        this.config = config;
        this.jsonPathFilter = jsonPathFilter;
        this.dependency = dependency;
        this.bufferedListeners = new ArrayList<>();
    }

    public JsonPathListener addListener(JsonPathListener listener) {
        BufferedListener newListener = new BufferedListener(this.config, listener);
        this.bufferedListeners.add(newListener);
        return newListener;
    }

    private void invokeBuffer() {
        if (dependency != null) {
            dependency.bufferedListeners.addAll(this.bufferedListeners);
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
        if (!this.verified && this.jsonPathFilter.apply(this.currentPosition, primitiveHolder, this.config.getJsonProvider())) {
            this.verified = true;
        }
        return true;
    }

}
