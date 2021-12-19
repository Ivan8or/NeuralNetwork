package online.umbcraft.ml.activations;

public class ReLUFunction implements ActivationFunction {

    // returns max of 0, x
    @Override
    public double result(double weightedSum) {
        return Math.max(0,weightedSum);
    }

    public double derivative(double weightedSum){
        if(weightedSum > 0)
            return 1;
        return 0;
    }
}
