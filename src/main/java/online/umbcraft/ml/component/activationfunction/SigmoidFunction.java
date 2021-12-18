package online.umbcraft.ml.component.activationfunction;

public class SigmoidFunction implements ActivationFunction {

    // returns 1 / (1 + e^-x)
    @Override
    public double result(double weightedSum) {
        return 1 / (1+Math.pow(Math.E, -1*weightedSum));
    }

    public double derivative(double weightedSum){
        return result(weightedSum)*(1 - result(weightedSum));
    }
}
