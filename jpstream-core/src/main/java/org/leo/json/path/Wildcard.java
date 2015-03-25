package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/25.
 */
public class Wildcard extends PathOperator {
    protected Wildcard() {
        super(Type.WILDCARD);
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }
}
