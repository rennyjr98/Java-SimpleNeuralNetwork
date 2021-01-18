package models;

import models.structs.IActivationFunction;

public class TanhActivationFunction implements IActivationFunction {
    @Override
    public double func(double x) {
        return Math.tanh(x);
    }

    @Override
    public double dfunc(double y) {
        return 1 - (y * y);
    }
}
