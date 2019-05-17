package org.jsfr.json.filter;

import org.jsfr.json.path.JsonPath;
import org.jsfr.json.provider.JsonProvider;

import java.util.regex.Pattern;

/**
 * Created by Leo on 2017/4/4.
 */
public class MatchRegexPredicate implements JsonPathFilter {

    private JsonPath relativePath;

    private Pattern regex;

    public MatchRegexPredicate(JsonPath relativePath, Pattern regex) {
        this.relativePath = relativePath;
        this.regex = regex;
    }

    @Override
    public boolean apply(Object jsonNode, JsonProvider jsonProvider) {
        Object candidate = relativePath.resolve(jsonNode, jsonProvider);
        if (candidate != null) {
            String string = (String) jsonProvider.cast(candidate, String.class);
            return regex.matcher(string).find();
        }
        return false;
    }

}
