package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.Objects;

/**
 * Created by Leo on 2017/4/4.
 */
public class EqualityStrPredicate implements JsonPathFilter {

    private JsonPath relativePath;

    private String value;

    public EqualityStrPredicate(JsonPath relativePath, String value) {
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
