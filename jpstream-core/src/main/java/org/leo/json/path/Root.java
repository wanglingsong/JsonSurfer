package org.leo.json.path;

public class Root extends PathOperator {

    private final static Root INSTANCE = new Root();

    public static Root instance() {
        return INSTANCE;
    }

    private Root() {
        super(Type.ROOT);
    }

    @Override
    public String toString() {
        return "$";
    }
}
