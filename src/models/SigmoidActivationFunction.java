package models;

import models.structs.IActivationFunction;

import java.io.Serializable;

public class SigmoidActivationFunction implements IActivationFunction, Serializable {
    private static final long serialVersionUID = 4L;

    @Override
    public double func(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double dfunc(double y) {
        return y * (1 - y);
    }
}
