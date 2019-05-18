package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.math.BigDecimal;

/**
 * Created by Leo on 2017/4/4.
 */
public class EqualityNumPredicate extends AbstractJsonPathFilter {

    private BigDecimal value;

    public EqualityNumPredicate(JsonPath relativePath, BigDecimal value) {
        super(relativePath);
        this.value = value;
    }

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        if (this.getRelativePath().matchFilterPath(jsonPosition)) {
            Object candidate = primitiveHolder.getValue();
            return candidate != null && new BigDecimal(candidate.toString()).compareTo(value) == 0;
        } else {
            return false;
        }
    }

//    @Override
//    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
//        Object candidate = relativePath.resolve(jsonNode, jsonProvider);
//        return candidate != null && Objects.equals(candidate, jsonProvider.primitive(value.doubleValue()));
//    }

}
