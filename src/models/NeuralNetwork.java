package models;

import java.io.*;

public class NeuralNetwork implements Serializable {
    private int inputNodes;
    private int hiddenNodes;
    private int outputNodes;
    private float learningRate;

    private ArrayMap weightIh;
    private ArrayMap weightH0;
    private ArrayMap biasH;
    private ArrayMap biasO;

    private SigmoidActivationFunction sigmoid = new SigmoidActivationFunction();
    private TanhActivationFunction tanh = new TanhActivationFunction();

    public NeuralNetwork(int input_nodes, int hidden_nodes, int output_nodes) {
        this.inputNodes = input_nodes;
        this.hiddenNodes = hidden_nodes;
        this.outputNodes = output_nodes;

        this.weightIh = new ArrayMap(this.hiddenNodes, this.inputNodes);
        this.weightH0 = new ArrayMap(this.outputNodes, this.hiddenNodes);
        this.weightIh.randomize();
        this.weightH0.randomize();

        this.biasH = new ArrayMap(this.hiddenNodes, 1);
        this.biasO = new ArrayMap(this.outputNodes, 1);
        this.biasH.randomize();
        this.biasO.randomize();
        setLearningRate(0.1f);
    }

    public NeuralNetwork(NeuralNetwork old_brain) {
        this.inputNodes = old_brain.getInputNodes();
        this.hiddenNodes = old_brain.getHiddenNodes();
        this.outputNodes = old_brain.getOutputNodes();

        this.weightH0 = old_brain.getWeightH0().copy();
        this.weightIh = old_brain.getWeightIh().copy();

        this.biasH = old_brain.getBiasH().copy();
        this.biasO = old_brain.getBiasO().copy();
        setLearningRate(0.1f);
    }

    public int getInputNodes() {
        return this.inputNodes;
    }
    public int getHiddenNodes() {
        return hiddenNodes;
    }
    public int getOutputNodes() {
        return outputNodes;
    }
    public ArrayMap getBiasH() {
        return biasH;
    }
    public ArrayMap getBiasO() {
        return biasO;
    }
    public ArrayMap getWeightH0() {
        return weightH0;
    }
    public ArrayMap getWeightIh() {
        return weightIh;
    }

    public void setLearningRate(float rate) {
        this.learningRate = rate;
    }

    public double[] predict(double[] inputArray) {
        // Generating the hidden outputs
        ArrayMap inputs = ArrayMap.fromArray(inputArray);
        ArrayMap hiddens = ArrayMap.multiply(this.weightIh, inputs);
        hiddens.add(this.biasH);
        // Activation function
        hiddens.activateFunc(this.sigmoid);

        // Generating the output's output
        ArrayMap outputs = ArrayMap.multiply(this.weightH0, hiddens);
        outputs.add(this.biasO);
        outputs.activateFunc(this.sigmoid);

        return outputs.toArray();
    }

    public void train(double[] inputArr, double [] targetArr) {
        // Generating the hidden outputs
        ArrayMap inputs = ArrayMap.fromArray(inputArr);
        ArrayMap hiddens = ArrayMap.multiply(this.weightIh, inputs);
        hiddens.add(this.biasH);
        // Activation function
        hiddens.activateFunc(this.sigmoid);

        // Generating the output's output
        ArrayMap outputs = ArrayMap.multiply(this.weightH0, hiddens);
        outputs.add(this.biasO);
        outputs.activateFunc(this.sigmoid);

        // Convert array to matrix object
        ArrayMap targets = ArrayMap.fromArray(targetArr);

        // Calculate the error
        // ERROR = TARGETS - OUTPUTS
        ArrayMap output_error = ArrayMap.subsctract(targets, outputs);

        // Calculate gradient
        ArrayMap gradients = ArrayMap.activatedFunc(outputs, this.sigmoid);
        gradients.multiply(output_error);
        gradients.multiply(this.learningRate);

        // Calculate deltas
        ArrayMap hiddenT = ArrayMap.transpose(hiddens);
        ArrayMap weightHODeltas = ArrayMap.multiply(gradients, hiddenT);

        // Adjust the weight by deltas
        this.weightH0.add(weightHODeltas);
        // Adjust the bias by its deltas (gradients)
        this.biasO.add(gradients);

        // Calculate the hidden layer errors
        ArrayMap whoT = ArrayMap.activatedFunc(hiddens, this.sigmoid);
        ArrayMap hiddenErrors = ArrayMap.multiply(whoT, output_error);

        // Calculate hidden gradient
        ArrayMap hiddenGradients = ArrayMap.activatedFunc(hiddens, this.sigmoid);
        hiddenGradients.multiply(hiddenErrors);
        hiddenGradients.multiply(this.learningRate);

        // Calculate input -> hidden delta
        ArrayMap inputsT = ArrayMap.transpose(inputs);
        ArrayMap weightIHDeltas = ArrayMap.multiply(hiddenGradients, inputsT);

        this.weightIh.add(weightIHDeltas);
        this.biasH.add(hiddenGradients);
    }

    public void serialize() throws FileNotFoundException, IOException {
        FileOutputStream fout = new FileOutputStream(new File("brain.rl").getAbsolutePath());
        ObjectOutputStream objout = new ObjectOutputStream(fout);
        objout.writeObject(this);
        objout.close();
    }

    public NeuralNetwork copy() {
        return new NeuralNetwork(this);
    }

    public void mutate(double rate) {
        this.weightIh.mutate(rate);
        this.weightH0.mutate(rate);
        this.biasH.mutate(rate);
        this.biasO.mutate(rate);
    }
}
