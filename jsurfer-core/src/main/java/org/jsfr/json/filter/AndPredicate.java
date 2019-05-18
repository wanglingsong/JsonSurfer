package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.Iterator;

/**
 * Created by Leo on 2017/4/4.
 */
public class AndPredicate extends AggregatePredicate {

    // AndPredicate becomes stateful

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        Iterator<JsonPathFilter> itr = this.getFilters().iterator();
        while (itr.hasNext()) {
            JsonPathFilter filter = itr.next();
            if (filter.apply(jsonPosition, primitiveHolder, jsonProvider)) {
                itr.remove();
            }
        }
        return this.getFilters().isEmpty();
    }

}
