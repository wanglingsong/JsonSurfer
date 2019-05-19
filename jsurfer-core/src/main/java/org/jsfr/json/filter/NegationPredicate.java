package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

public class NegationPredicate extends AggregatePredicate {

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        return !this.getFilters().get(0).apply(jsonPosition, primitiveHolder, jsonProvider);
    }

}
