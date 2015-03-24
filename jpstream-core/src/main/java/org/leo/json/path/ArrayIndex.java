package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/22.
 */
public class ArrayIndex extends PathOperator {

    private int arrayIndex = -1;

    public ArrayIndex() {
        super(Type.ARRAY);
    }

    public ArrayIndex(int arrayIndex) {
        this();
        this.arrayIndex = arrayIndex;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public void increaseArrayIndex() {
        this.arrayIndex++;
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return super.match(pathOperator) && arrayIndex == ((ArrayIndex) pathOperator).arrayIndex;
    }

    @Override
    public String toString() {
        return "[" + arrayIndex + "]";
    }
}
