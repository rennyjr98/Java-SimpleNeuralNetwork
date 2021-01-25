package models;

public class NeuralDoctor {
    private float minRate;
    private double[][] inputs;
    private double[][] targets;

    private NeuralNetwork nn;

    public NeuralDoctor(NeuralNetwork nn) {
        this.nn = nn;
    }

    public NeuralDoctor(NeuralNetwork nn, float minRate) {
        this.nn = nn;
        this.minRate = minRate;
    }

    public void setInputs(double[][] inputs) {
        this.inputs = inputs;
    }
    public void setTargets(double[][] targets) {
        this.targets = targets;
    }
    public void setMinRate(float minRate) {
        this.minRate = minRate;
    }

    public float accuracy() {
        return accuracy(this.inputs, this.targets, this.minRate);
    }

    public float accuracy(float minRate) {
        return accuracy(this.inputs, this.targets, minRate);
    }

    public float accuracy(double[][] inputs, double[][] targets) {
        return accuracy(inputs, targets, this.minRate);
    }

    public float accuracy(double [][] inputs, double [][] targets, float minRate) {
        float positives = 0;
        for(int i = 0; i < inputs.length; i++) {
            double [] prediction = this.nn.predict(inputs[i]);
            if(prediction.length > 1) {
                positives += (multiTargetAccuracyHandler(prediction, targets[i], minRate)) ? 1 :0;
            } else {
                boolean inMinRate = prediction[0] >= minRate;
                boolean inUpRate = targets[i][0] == 1;
                positives += ((inMinRate && inUpRate) ||(!inMinRate && !inUpRate)) ? 1 : 0;
            }
        }
        return positives/inputs.length;
    }

    private boolean multiTargetAccuracyHandler(double [] prediction, double [] target, float minRate) {
        double maxValue = 0;
        int maxValueIndex = 0;
        for(int i = 0; i < prediction.length; i++) {
            if(prediction[i] > maxValue) {
                maxValue = prediction[i];
                maxValueIndex = i;
            }
        }

        boolean inMinRate = maxValue >= minRate;
        boolean inUpRate = target[maxValueIndex] == 1;
        return ((inMinRate && inUpRate) || (!inMinRate && !inUpRate));
    }

    public float dataQuality(double[] data, double[] targets) {
        float inRange01Sum = 0;
        for(int i = 0; i < data.length; i++) {
            inRange01Sum += (data[i] >= 0 && data[i] <= 1) ? 1 : 0;
        }

        float inRange01Mean = inRange01Sum/data.length;
        float dataSizeMean = data.length/800;
        return (inRange01Mean+dataSizeMean)/2;
    }
}
