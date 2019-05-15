package org.jsfr.json.filter;

import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public interface JsonPathFilter {

    /**
     * Returns whether jsonNode satisfies the filter.
     */
    boolean apply(Object jsonNode, JsonProvider jsonProvider);

}
