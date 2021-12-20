package online.umbcraft.ml.component;

import online.umbcraft.ml.activations.ActivationFunction;

import java.util.List;

public class ThresholdLogicUnit {

    final private List<Connection> inputs;
    final private ActivationFunction activationFunction;


    public ThresholdLogicUnit(List<Connection> inputs, ActivationFunction activationFunction) {
        this.inputs = inputs;
        this.activationFunction = activationFunction;
    }

    // returns a list of all the connection weights as an array of values
    public double[] getWeights() {
        double[] toReturn = new double[inputs.size()];
        for(int i = 0; i < inputs.size(); i++) {
            toReturn[i] = inputs.get(i).getWeight();
        }
        return toReturn;
    }

    // sets the weights for all connections according to an array of weight values
    public void setWeights(double[] weights) {
        if(weights.length != inputs.size())
            throw new IllegalArgumentException("invalid amount of weights!");

        for(int i = 0; i < inputs.size(); i++) {
            inputs.get(i).setWeight(weights[i]);
        }
    }

    public List<Connection> getInputs() {
        return inputs;
    }

    public double getWeightedSum() {
        double toReturn = 0;
        for (Connection c : inputs) {
            toReturn += c.getConnectionValue();
        }
        return toReturn;
    }

    public double evaluate() {
        return activationFunction.result(getWeightedSum());
    }
}
