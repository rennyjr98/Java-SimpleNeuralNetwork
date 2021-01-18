package models;

import models.structs.IActivationFunction;

public class SigmoidActivationFunction implements IActivationFunction {
    @Override
    public double func(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double dfunc(double y) {
        return y * (1 - y);
    }
}
