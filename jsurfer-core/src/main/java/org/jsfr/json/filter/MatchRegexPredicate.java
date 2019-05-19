package org.jsfr.json.filter;

import org.jsfr.json.PrimitiveHolder;
import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.regex.Pattern;

/**
 * Created by Leo on 2017/4/4.
 */
public class MatchRegexPredicate extends AbstractJsonPathFilter {

    private Pattern regex;

    public MatchRegexPredicate(JsonPath relativePath, Pattern regex) {
        super(relativePath);
        this.regex = regex;
    }

    @Override
    public boolean apply(JsonPath jsonPosition, PrimitiveHolder primitiveHolder, JsonProvider jsonProvider) {
        if (this.getRelativePath().matchFilterPath(jsonPosition)) {
            Object candidate = primitiveHolder.getValue();
            String string = (String) jsonProvider.cast(candidate, String.class);
            return regex.matcher(string).find();
        }
        return false;
    }

}
