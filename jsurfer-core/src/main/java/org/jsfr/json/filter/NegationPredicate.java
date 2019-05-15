package org.jsfr.json.filter;

import org.jsfr.json.provider.JsonProvider;

public class NegationPredicate extends AggregatePredicate {

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        return !this.getFilters().get(0).apply(jsonNode, jsonProvider);
    }

}
