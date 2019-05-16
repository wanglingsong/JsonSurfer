package org.jsfr.json.path;

import org.jsfr.json.filter.JsonPathFilter;

public class ArrayFilter extends PathOperator {

    private JsonPathFilter jsonPathFilter;

    public ArrayFilter(JsonPathFilter jsonPathFilter) {
        this.jsonPathFilter = jsonPathFilter;
    }

//    @Override
//    public boolean match(PathOperator pathOperator) {
//        return pathOperator instanceof ArrayIndex; // match any array element, filtering is done in the JsonFilterVerifier
//    }

    public JsonPathFilter getJsonPathFilter() {
        return jsonPathFilter;
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public String toString() {
        return "[?(@...]"; // TODO write toString methods for filter path
    }

}
