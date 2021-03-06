package model.twooperatormath;

import model.treenode.CommandNode;

import java.util.function.DoubleBinaryOperator;

/**
 * Created by rhondusmithwick on 3/5/16.
 *
 * @author Rhondu Smithwick
 */
public abstract class TwoOperatorMath extends CommandNode {

    private final DoubleBinaryOperator func;

    protected TwoOperatorMath(DoubleBinaryOperator func) {
        this.func = func;
    }

    @Override
    public double execute() {
        double value1 = getChildren().get(0).getValue();
        double value2 = getChildren().get(1).getValue();
        return func.applyAsDouble(value1, value2);
    }

    @Override
    protected int getNumChildrenRequired() {
        return 2;
    }
}
