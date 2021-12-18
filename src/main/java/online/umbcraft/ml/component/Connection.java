package online.umbcraft.ml.component;

import online.umbcraft.NeuralNetwork;
import online.umbcraft.ml.component.input.Input;

public class Connection {

    final private Input input;
    private double weight;

    public Connection(Input input) {
        this.input = input;
        weight = (NeuralNetwork.RANDOM.nextDouble() - 0.5);
    }

    public Connection(Input input, double weight) {
        this.input = input;
        this.weight = weight;
    }

    public Input getInput() {
        return input;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        this.weight = newWeight;
    }

    public void incrementWeight(double increment) {
        this.weight += increment;
    }

    public double getConnectionValue() {
        return weight * input.getInput();
    }

    public double getRawValue() {
        return input.getInput();
    }
}
