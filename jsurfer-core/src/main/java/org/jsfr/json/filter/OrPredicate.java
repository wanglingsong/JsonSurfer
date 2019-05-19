package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public class OrPredicate extends AggregatePredicate {

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        for (JsonPathFilter filter : this.getFilters()) {
            if (filter.apply(jsonPosition, primitiveHolder, jsonProvider)) {
                return true;
            }
        }
        return false;
    }

}
