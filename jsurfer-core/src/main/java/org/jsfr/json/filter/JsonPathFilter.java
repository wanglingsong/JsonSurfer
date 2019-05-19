package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public interface JsonPathFilter {

    /**
     * Returns whether json position satisfies the filter.
     */
    boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider);

}
