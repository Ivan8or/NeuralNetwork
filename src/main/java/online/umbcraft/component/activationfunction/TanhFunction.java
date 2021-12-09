package online.umbcraft.component.activationfunction;

public class TanhFunction implements ActivationFunction {

    // returns tanh(x)
    @Override
    public double process(double weightedSum) {
        return Math.tanh(weightedSum);
    }
}
