package online.umbcraft.ml.functions.activations;

public class SigmoidFunction implements ActivationFunction {

    // returns 1 / (1 + e^-x)
    @Override
    public double result(double weightedSum) {
        return 1 / (1+Math.pow(Math.E, -1*weightedSum));
    }

    @Override
    public double derivative(double weightedSum){
        return result(weightedSum)*(1 - result(weightedSum));
    }

    @Override
    public String name(){
        return getClass().getName();
    }
}
