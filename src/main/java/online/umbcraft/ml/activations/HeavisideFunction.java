package online.umbcraft.ml.activations;

public class HeavisideFunction implements ActivationFunction {

    // returns 1 if x > 0, 0 if x <= 0
    @Override
    public double result(double weightedSum) {
        if (weightedSum > 0)
            return 1;
        return 0;
    }

    public double derivative(double weightedSum){
        throw new IllegalCallerException("Function does not have a derivative");
    }
}
