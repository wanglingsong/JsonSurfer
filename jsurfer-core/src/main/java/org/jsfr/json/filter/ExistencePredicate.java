package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public class ExistencePredicate implements JsonPathFilter {

    private JsonPath relativePath;

    public ExistencePredicate(JsonPath relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        return relativePath.resolve(jsonNode, jsonProvider) != null;
    }

    @Override
    public boolean notApply(Object jsonNode, JsonProvider jsonProvider) {
        return !apply(jsonNode, jsonProvider);
    }

    @Override
    public boolean couldApply(JsonPath jsonPath) {
        return jsonPath.isSubPathOf(relativePath);
    }

}
