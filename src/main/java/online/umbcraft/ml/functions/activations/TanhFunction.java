package online.umbcraft.ml.functions.activations;

public class TanhFunction implements ActivationFunction {

    // returns tanh(x)
    @Override
    public double result(double weightedSum) {
        return Math.tanh(weightedSum);
    }

    @Override
    public double derivative(double weightedSum){
        return 1 - Math.pow(result(weightedSum),2);
    }

    @Override
    public String name(){
        return "Tanh";
    }
}
