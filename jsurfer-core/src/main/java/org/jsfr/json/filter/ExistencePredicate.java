package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

/**
 * Created by Leo on 2017/4/4.
 */
public class ExistencePredicate extends AbstractJsonPathFilter {

    public ExistencePredicate(JsonPath relativePath) {
        super(relativePath);
    }

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        return this.getRelativePath().matchFilterPath(jsonPosition) && primitiveHolder.getValue() != null;
    }

}
