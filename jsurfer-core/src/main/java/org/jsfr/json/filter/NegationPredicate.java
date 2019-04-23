package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

public class NegationPredicate extends AggregatePredicate {

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        return !this.getFilters().get(0).apply(jsonNode, jsonProvider);
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
