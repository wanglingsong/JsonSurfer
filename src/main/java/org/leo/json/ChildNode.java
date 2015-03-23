package org.leo.json;

import java.util.Objects;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ChildNode extends PathOperator {

    private String key;

    public ChildNode(String key) {
        super(Type.OBJECT);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return super.match(pathOperator) && Objects.equals(key, ((ChildNode) pathOperator).key);
    }

    @Override
    public String toString() {
        return "." + key;
    }
}
