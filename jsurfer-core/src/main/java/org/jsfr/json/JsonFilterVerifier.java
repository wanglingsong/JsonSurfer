package org.jsfr.json;

import org.jsfr.json.filter.JsonPathFilter;

import java.util.ArrayList;
import java.util.Collection;

public class JsonFilterVerifier extends JsonDomBuilder {

    private SurfingConfiguration config;
    private JsonPathFilter jsonPathFilter;
    private Collection<BufferedListener> bufferedListeners;
    private JsonFilterVerifier dependency;

    public JsonFilterVerifier(SurfingConfiguration config, JsonPathFilter jsonPathFilter, JsonFilterVerifier dependency) {
        super(config.getJsonProvider());
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

    private void onObjectCollected(Object obj) {
        if (jsonPathFilter.apply(obj, config.getJsonProvider())) {
            if (dependency != null) {
                dependency.bufferedListeners.addAll(this.bufferedListeners);
            } else {
                for (BufferedListener buffer : this.bufferedListeners) {
                    buffer.invokeBufferedValue();
                }
            }
        }
    }

    @Override
    public boolean endObject() {
        super.endObject();
        if (isInRoot()) {
            Object result = rootValue();
            onObjectCollected(result);
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean endArray() {
        super.endArray();
        if (isInRoot()) {
            Object result = rootValue();
            onObjectCollected(result);
            this.clear();
            return false;
        }
        return true;
    }

}
