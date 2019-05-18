package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;

public abstract class AbstractJsonPathFilter implements JsonPathFilter {

    private JsonPath relativePath;

    public AbstractJsonPathFilter(JsonPath relativePath) {
        this.relativePath = relativePath;
    }

    public JsonPath getRelativePath() {
        return relativePath;
    }

}
