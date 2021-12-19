package online.umbcraft.ml.component;

import online.umbcraft.ml.activations.ActivationFunction;
import online.umbcraft.ml.component.Connection;

import java.util.List;

public class ThresholdLogicUnit {

    final private List<Connection> inputs;
    final private ActivationFunction activationFunction;


    public ThresholdLogicUnit(List<Connection> inputs, ActivationFunction activationFunction) {
        this.inputs = inputs;
        this.activationFunction = activationFunction;
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

    public double trainEvaluate(final double label, final double STEP_SIZE) {
        double solution = evaluate();
        for (Connection c : inputs) {
            double rawValue = c.getRawValue();
            double originalWeight = c.getWeight();
            double newWeight = originalWeight + STEP_SIZE * rawValue *
                    (label - solution);
            c.setWeight(newWeight);
        }
        return solution;
    }

    public double evaluate() {
        return activationFunction.result(getWeightedSum());
    }
}
