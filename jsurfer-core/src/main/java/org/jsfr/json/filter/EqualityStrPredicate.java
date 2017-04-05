package org.jsfr.json.filter;

import com.sun.org.apache.bcel.internal.util.Objects;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

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
        return Objects.equals(candidate, jsonProvider.primitive(value));
    }

}
