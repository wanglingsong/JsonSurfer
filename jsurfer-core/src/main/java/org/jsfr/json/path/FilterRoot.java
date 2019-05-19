package org.jsfr.json.path;

import org.jsfr.json.resolver.DocumentResolver;

public class FilterRoot extends PathOperator {

    private final static FilterRoot INSTANCE = new FilterRoot();

    public static FilterRoot instance() {
        return INSTANCE;
    }

    private FilterRoot() {
    }

    @Override
    public Object resolve(Object document, DocumentResolver resolver) {
        return document;
    }

    @Override
    public Type getType() {
        return Type.FILTER_ROOT;
    }

    @Override
    public String toString() {
        return "@";
    }

}
