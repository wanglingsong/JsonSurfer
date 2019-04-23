package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public interface JsonPathFilter {

    /**
     * Returns whether jsonNode satisfies the filter.
     */
    boolean apply(Object jsonNode, JsonProvider jsonProvider);

    /**
     * Returns whether jsonNode surely does not satisfy the filter. This is not simply the same as !apply because it
     * makes sure that the required elements have been resolved.
     */
    boolean notApply(Object jsonNode, JsonProvider jsonProvider);

    /**
     * Returns whether given jsonPath could satisfy the filter. For example if jsonPath is a sub path of relativePath.
     */
    boolean couldApply(JsonPath jsonPath);

}
