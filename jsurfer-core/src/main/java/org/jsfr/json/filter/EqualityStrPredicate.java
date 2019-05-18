package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.Objects;

/**
 * Created by Leo on 2017/4/4.
 */
public class EqualityStrPredicate extends AbstractJsonPathFilter {

    private String value;

    public EqualityStrPredicate(JsonPath relativePath, String value) {
        super(relativePath);
        this.value = value;
    }

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        if (this.getRelativePath().matchFilterPath(jsonPosition)) {
            Object candidate = primitiveHolder.getValue();
            return candidate != null && Objects.equals(candidate, jsonProvider.primitive(value));
        } else {
            return false;
        }
    }

}
