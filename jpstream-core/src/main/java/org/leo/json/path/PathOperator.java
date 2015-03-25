package org.leo.json.path;

public class PathOperator {

    public enum Type {
        ROOT,
        OBJECT,
        ARRAY,
        WILDCARD,
        DEEP_SCAN
    }

    private Type type;

    protected PathOperator(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean match(PathOperator pathOperator) {
        return type == pathOperator.type;
    }

}
