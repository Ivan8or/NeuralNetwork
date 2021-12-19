package online.umbcraft.ml.activations;

public class TanhFunction implements ActivationFunction {

    // returns tanh(x)
    @Override
    public double result(double weightedSum) {
        return Math.tanh(weightedSum);
    }
    public double derivative(double weightedSum){
        return 1 - Math.pow(result(weightedSum),2);
    }
}
