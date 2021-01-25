package models;

import models.structs.IActivationFunction;

import java.io.Serializable;

public class TanhActivationFunction implements IActivationFunction, Serializable {
    private static final long serialVersionUID = 4L;

    @Override
    public double func(double x) {
        return Math.tanh(x);
    }

    @Override
    public double dfunc(double y) {
        return 1 - (y * y);
    }
}
