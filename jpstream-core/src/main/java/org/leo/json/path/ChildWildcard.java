package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ChildWildcard extends PathOperator {

    protected ChildWildcard() {
        super(Type.OBJECT);
    }

    @Override
    public String toString() {
        return ".*";
    }
}
