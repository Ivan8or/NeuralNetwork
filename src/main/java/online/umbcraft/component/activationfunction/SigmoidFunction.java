package online.umbcraft.component.activationfunction;

public class SigmoidFunction implements ActivationFunction {

    // returns 1 / (1 + e^-x)
    @Override
    public double process(double weightedSum) {

        return 1 / (1+Math.pow(Math.E, -1*weightedSum));
    }
}
