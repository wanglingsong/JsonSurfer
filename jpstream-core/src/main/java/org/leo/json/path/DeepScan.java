package org.leo.json.path;

/**
 * Created by Administrator on 2015/3/25.
 */
public class DeepScan extends PathOperator {

    protected DeepScan() {
        super(Type.DEEP_SCAN);
    }

    @Override
    public boolean match(PathOperator pathOperator) {
        return true;
    }

    @Override
    public String toString() {
        return ".";
    }

}
