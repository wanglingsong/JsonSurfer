package org.jsfr.json.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/4/5.
 */
public abstract class AggregatePredicate implements JsonPathFilter {

    private List<JsonPathFilter> filters = new ArrayList<JsonPathFilter>();

    public List<JsonPathFilter> getFilters() {
        return filters;
    }

    public void addFilter(JsonPathFilter filter) {
        this.filters.add(filter);
    }

}
