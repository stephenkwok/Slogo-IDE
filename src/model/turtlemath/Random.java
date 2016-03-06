package model.turtlemath;

public class Random extends TurtleMath {

    @Override
    public double calculate() {
        double value = getChildren().get(0).getValue();
        return Math.random() * value;
    }
}
