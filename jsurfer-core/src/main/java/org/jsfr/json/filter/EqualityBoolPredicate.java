package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.Objects;

public class EqualityBoolPredicate extends AbstractJsonPathFilter {

    private boolean value;

    public EqualityBoolPredicate(JsonPath relativePath, boolean value) {
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
