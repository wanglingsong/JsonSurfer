package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.math.BigDecimal;

/**
 * Created by Leo on 2017/4/4.
 */
public class LessThanNumPredicate extends AbstractJsonPathFilter {

    private BigDecimal value;

    public LessThanNumPredicate(JsonPath relativePath, BigDecimal value) {
        super(relativePath);
        this.value = value;
    }

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        if (this.getRelativePath().matchFilterPath(jsonPosition)) {
            Object candidate = primitiveHolder.getValue();
            return candidate != null && new BigDecimal(candidate.toString()).compareTo(value) < 0;
        } else {
            return false;
        }
    }

}
