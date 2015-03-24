package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ArrayWildcard extends PathOperator {

    protected ArrayWildcard() {
        super(Type.ARRAY);
    }

    @Override
    public String toString() {
        return "[*]";
    }
}
