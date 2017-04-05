package org.jsfr.json;

import org.jsfr.json.filter.JsonPathFilter;

/**
 * Created by Leo on 2017/4/4.
 */
public class FilteredJsonPathListener implements JsonPathListener {

    private JsonPathFilter jsonPathFilter;

    private JsonPathListener underlyingListener;

    private SurfingConfiguration surfingConfiguration;

    public FilteredJsonPathListener(JsonPathFilter jsonPathFilter, JsonPathListener underlyingListener, SurfingConfiguration surfingConfiguration) {
        this.jsonPathFilter = jsonPathFilter;
        this.underlyingListener = underlyingListener;
        this.surfingConfiguration = surfingConfiguration;
    }

    @Override
    public void onValue(Object value, ParsingContext context) throws Exception {
        if (jsonPathFilter.apply(value, surfingConfiguration.getJsonProvider())) {
            underlyingListener.onValue(value, context);
        }
    }

}
