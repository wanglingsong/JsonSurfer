package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ArrayWildcard extends PathOperator {

    private final static ArrayWildcard INSTANCE = new ArrayWildcard();

    public static ArrayWildcard instance() {
        return INSTANCE;
    }

    private ArrayWildcard() {
        super(Type.ARRAY);
    }

    @Override
    public String toString() {
        return "[*]";
    }
}
