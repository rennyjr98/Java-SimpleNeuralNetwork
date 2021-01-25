package models;

import java.io.Serializable;
import java.util.Random;

public class ArrayMap implements Serializable {
    private int rows;
    private int cols;
    private double[][] data;
    private static final long serialVersionUID = 4L;

    public ArrayMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public ArrayMap(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getData() {
        return data;
    }

    public ArrayMap copy() {
        double [][] c_data = new double[this.rows][this.cols];
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                c_data[i][j] = this.data[i][j];
            }
        }
        return new ArrayMap(this.data);
    }

    public static ArrayMap transpose(ArrayMap x) {
        ArrayMap result = new ArrayMap(x.getCols(), x.getRows());
        for(int i = 0; i < result.getRows(); i++) {
            for(int j = 0; j < result.getCols(); j++) {
                result.getData()[i][j] = x.getData()[j][i];
            }
        }
        return result;
    }

    public static ArrayMap fromArray(double[] arr) {
        ArrayMap newSet = new ArrayMap(arr.length, 1);
        for(int i = 0; i < newSet.getRows(); i++) {
            for(int j = 0; j < newSet.getCols(); j++) {
                newSet.getData()[i][j] = arr[i];
            }
        }
        return newSet;
    }

    public double[] toArray() {
        double[] arr = new double[this.rows * this.cols];
        int index = 0;
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                arr[index] = this.data[i][j];
                index++;
            }
        }
        return arr;
    }

    public void randomize() {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.data[i][j] = Math.random()*2 - 1;
            }
        }
    }

    public static ArrayMap subsctract(ArrayMap a, ArrayMap b) throws IndexOutOfBoundsException {
        if(a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
            new IndexOutOfBoundsException();
        }

        double [][] result = new double[a.getRows()][a.getCols()];
        for(int i = 0; i < a.getRows(); i++) {
            for(int j = 0; j < a.getCols(); j++) {
                result[i][j] = a.getData()[i][j] - b.getData()[i][j];
            }
        }
        return new ArrayMap(result);
    }

    public void add(ArrayMap a) {
        if(a.getRows() != this.rows || a.getCols() != this.cols) {
            new IndexOutOfBoundsException();
        }

        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.data[i][j] += a.getData()[i][j];
            }
        }
    }

    public static ArrayMap multiply(ArrayMap a, ArrayMap b) {
        if(a.getCols() != b.getRows()) {
            System.out.println("ERROR BITCH");
            return null;
        }

        ArrayMap result = new ArrayMap(a.getRows(), b.getCols());
        for(int i = 0; i < a.getRows(); i++) {
            for(int j = 0; j < b.getCols(); j++) {
                double sum = 0;
                for(int k = 0; k < a.getCols(); k++) {
                    sum += a.getData()[i][k] * b.getData()[k][j];
                }
                result.getData()[i][j] = sum;
            }
        }
        return result;
    }

    public void multiply(ArrayMap a) {
        for(int i = 0; i < a.getRows(); i++) {
            for(int j = 0; j < a.getCols(); j++) {
                this.data[i][j] *= a.getData()[i][j];
            }
        }
    }

    public void multiply(float a) {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.data[i][j] *= a;
            }
        }
    }

    public void activateFunc(SigmoidActivationFunction sigmoid) {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.data[i][j] = sigmoid.func(this.data[i][j]);
            }
        }
    }

    public static ArrayMap activatedFunc(ArrayMap x, SigmoidActivationFunction sigmoid) {
        double[][] data = x.getData();
        for(int i = 0; i < x.getRows(); i++) {
            for(int j = 0; j < x.getCols(); j++) {
                data[i][j] = sigmoid.dfunc(data[i][j]);
            }
        }
        return new ArrayMap(data);
    }

    public void mutate(double rate) {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                if(Math.random() < rate) {
                    Random rand = new Random();
                    this.data[i][j] += rand.nextGaussian();
                }
            }
        }
    }

    public void print() {
        System.out.print("[");
        for(int i = 0; i < this.rows; i++) {
            System.out.print("[ ");
            for(int j = 0;j < this.cols; j++) {
                System.out.print(this.data[i][j] + ", ");
            }
            System.out.print("]");
            if(i != this.rows-1)
                System.out.println();
        }
        System.out.println("]");
    }
}
