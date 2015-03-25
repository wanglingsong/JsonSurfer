package org.leo.json.path;

import java.util.HashSet;

/**
 * Created by Administrator on 2015/3/26.
 */
public class ChildrenNode extends PathOperator {

    private HashSet<String> children;

    public ChildrenNode(HashSet<String> children) {
        super(Type.OBJECT);
        this.children = children;
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return super.match(pathOperator) && children.contains(((ChildNode)pathOperator).getKey());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
