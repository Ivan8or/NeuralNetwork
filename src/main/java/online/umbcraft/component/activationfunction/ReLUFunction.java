package online.umbcraft.component.activationfunction;

public class ReLUFunction implements ActivationFunction {

    // returns max of 0, x
    @Override
    public double process(double weightedSum) {
        return Math.max(0,weightedSum);
    }
}
