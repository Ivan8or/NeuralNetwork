package online.umbcraft.component;

import online.umbcraft.component.activationfunction.ActivationFunction;
import online.umbcraft.component.input.BiasInput;
import online.umbcraft.component.input.Input;

import java.util.List;

public class ThresholdLogicUnit implements Input {

    final private List<Connection> inputs;
    final private ActivationFunction activationFunction;


    public ThresholdLogicUnit(List<Connection> inputs, ActivationFunction activationFunction) {
        this.inputs = inputs;
        this.activationFunction = activationFunction;
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

            if(c.getInput() instanceof ThresholdLogicUnit) {
                ThresholdLogicUnit feed = (ThresholdLogicUnit) c.getInput();
                double feedSolution = feed.evaluate();
                feed.trainEvaluate(
                        solution
                        , STEP_SIZE);
            }

        }

        return solution;
    }

    public double evaluate() {
        return activationFunction.process(getWeightedSum());
    }

    @Override
    public double getInput() {
        return evaluate();
    }
}
