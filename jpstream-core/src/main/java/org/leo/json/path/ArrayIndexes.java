package org.leo.json.path;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Administrator on 2015/3/25.
 */
public class ArrayIndexes extends PathOperator {

    private HashSet<Integer> indexes;

    protected ArrayIndexes(HashSet<Integer> indexes) {
        super(Type.ARRAY);
        this.indexes = indexes;
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return super.match(pathOperator) && indexes.contains(((ArrayIndex)pathOperator).getArrayIndex());
    }

    @Override
    public String toString() {
        return String.valueOf(indexes);
    }
}
