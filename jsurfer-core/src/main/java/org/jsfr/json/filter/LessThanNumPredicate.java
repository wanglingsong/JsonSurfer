package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.math.BigDecimal;

/**
 * Created by Leo on 2017/4/4.
 */
public class LessThanNumPredicate implements JsonPathFilter {

    private JsonPath relativePath;

    private BigDecimal value;

    public LessThanNumPredicate(JsonPath relativePath, BigDecimal value) {
        this.relativePath = relativePath;
        this.value = value;
    }

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        Object candidate = relativePath.resolve(jsonNode, jsonProvider);
        return new BigDecimal(candidate.toString()).compareTo(value) < 0;
    }

}
