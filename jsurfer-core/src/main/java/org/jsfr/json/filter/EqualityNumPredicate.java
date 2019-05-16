package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Leo on 2017/4/4.
 */
public class EqualityNumPredicate implements JsonPathFilter {

    private JsonPath relativePath;

    private BigDecimal value;

    public EqualityNumPredicate(JsonPath relativePath, BigDecimal value) {
        this.relativePath = relativePath;
        this.value = value;
    }

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        Object candidate = relativePath.resolve(jsonNode, jsonProvider);
        return candidate != null && Objects.equals(candidate, jsonProvider.primitive(value.doubleValue()));
    }

}
