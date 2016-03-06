package model.turtlemath;

public class Sum extends TurtleMath {

    public double calculate() {
        double value1 = getChildren().get(0).getValue();
        double value2 = getChildren().get(1).getValue();
        return value1 + value2;
    }

    @Override
    public int getNumChildrenRequired() {
        return 2;
    }
}
