package online.umbcraft.ml.functions.activations;

public class SignFunction implements ActivationFunction {

    // returns 1 if x > 0, 0 if x = 0, -1 if x < 0
    @Override
    public double result(double weightedSum) {
        if (weightedSum > 0)
            return 1;
        if(weightedSum == 0)
            return 0;
        return -1;
    }

    @Override
    public double derivative(double weightedSum){
        throw new IllegalCallerException("Function does not have a derivative");
    }

    @Override
    public String name(){
        return getClass().getName();
    }
}
