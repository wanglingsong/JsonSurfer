package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.Objects;

public class EqualityBoolPredicate implements JsonPathFilter {

    private JsonPath relativePath;

    private boolean value;

    public EqualityBoolPredicate(JsonPath relativePath, boolean value) {
        this.relativePath = relativePath;
        this.value = value;
    }

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        Object candidate = relativePath.resolve(jsonNode, jsonProvider);
        return candidate != null && Objects.equals(candidate, jsonProvider.primitive(value));
    }

    @Override
    public boolean notApply(Object jsonNode, JsonProvider jsonProvider) {
        Object candidate = relativePath.resolve(jsonNode, jsonProvider);
        return candidate != null && !Objects.equals(candidate, jsonProvider.primitive(value));
    }

    @Override
    public boolean couldApply(JsonPath jsonPath) {
        return jsonPath.isSubPathOf(relativePath);
    }

}
