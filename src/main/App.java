package main;

import models.NeuralNetwork;

public class App {
    public static void main(String ... args) {
        NeuralNetwork nn = new NeuralNetwork(4, 4, 1);
        for(int i = 0; i < 1000; i++) {
            nn.train(new double[]{1, 1, 1, 1}, new double[]{1});
            nn.train(new double[]{0, 0, 0, 0}, new double[]{0});
        }

        System.out.print("The prediction is : ");
        System.out.println(nn.predict(new double[]{1,1,1,1})[0]);
    }
}
