package model.oneoperatormath;

import java.util.function.DoubleUnaryOperator;

public class ArcTangent extends OneOperatorMath {

    @Override
    protected double execute() {
        DoubleUnaryOperator func = (t -> (Math.atan(Math.toRadians(t))));
        return calculate(func);
    }
}