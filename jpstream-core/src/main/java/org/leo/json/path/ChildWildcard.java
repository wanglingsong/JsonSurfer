package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ChildWildcard extends PathOperator {

    private final static ChildWildcard INSTANCE = new ChildWildcard();

    public static ChildWildcard instance() {
        return INSTANCE;
    }

    private ChildWildcard() {
        super(Type.OBJECT);
    }

    @Override
    public String toString() {
        return ".*";
    }
}
