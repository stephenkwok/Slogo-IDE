package model.turtlemath;

public class Cosine extends TurtleMath {

    @Override
    public double calculate() {
        double value = Math.toRadians(getChildren().get(0).getValue());
        return Math.cos(value);
    }
}
