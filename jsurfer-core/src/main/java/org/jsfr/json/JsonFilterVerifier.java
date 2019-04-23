package org.jsfr.json;

import org.jsfr.json.filter.JsonPathFilter;

import java.util.Collection;

public class JsonFilterVerifier extends JsonDomBuilder {

    private JsonPathFilter filter;
    private Collection<FilteredJsonPathListener> listeners;
    private SurfingConfiguration.Binding binding;
    private ParsingContext context;

    public JsonFilterVerifier(JsonPathFilter filter, Collection<FilteredJsonPathListener> listeners, SurfingConfiguration.Binding binding, ParsingContext context) {
        this.filter = filter;
        this.listeners = listeners;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public boolean endObject() {
        super.endObject();
        if (filter.notApply(rootValue(), getProvider()) || isInRoot()) {
            for (FilteredJsonPathListener listener : listeners) {
                if (!context.isStopped())
                    listener.filterVerified(false);
            }
            binding.unwrapListeners();
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean endArray() {
        super.endArray();
        if (filter.notApply(rootValue(), getProvider()) || isInRoot()) {
            for (FilteredJsonPathListener listener : listeners) {
                if (!context.isStopped())
                    listener.filterVerified(false);
            }
            binding.unwrapListeners();
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean primitive(PrimitiveHolder primitiveHolder) {
        super.primitive(primitiveHolder);
        if (filter.apply(rootValue(), getProvider())) {
            for (FilteredJsonPathListener listener : listeners) {
                if (!context.isStopped())
                    listener.filterVerified(true);
            }
            binding.unwrapListeners();
            this.clear();
            return false;
        }
        return true;
    }

    @Override
    boolean shouldSkip() {
        return !filter.couldApply(currentPosition); // we can skip if the filter can not apply
    }

    @Override
    public void clear() {
        super.clear();
        this.filter = null;
        this.listeners = null;
        this.binding = null;
        this.context = null;
    }
}
