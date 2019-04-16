package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public class AndPredicate extends AggregatePredicate {

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        for (JsonPathFilter filter : this.getFilters()) {
            if (!filter.apply(jsonNode, jsonProvider)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean notApply(Object jsonNode, JsonProvider jsonProvider) {
        return !apply(jsonNode, jsonProvider);
    }

    @Override
    public boolean couldApply(JsonPath jsonPath) {
        return true;
    }

}
