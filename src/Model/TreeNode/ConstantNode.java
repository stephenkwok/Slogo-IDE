package Model.TreeNode;

import java.util.concurrent.TimeUnit;

/**
 * Created by rhondusmithwick on 2/25/16.
 *
 * @author Rhondu Smithwick
 */
public class ConstantNode extends TreeNode {

    private final double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s with value: %f", getClass().getSimpleName(), value);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return 0L;
    }
}
