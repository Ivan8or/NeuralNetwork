package online.umbcraft.ml.functions.costs;

public class RootMeanSquaredError implements ErrorFunction {

    @Override
    public double result(double prediction, double goal) {
        return Math.sqrt(Math.pow(prediction - goal, 2));
    }

    @Override
    public double derivative(double prediction, double goal) {
        throw new IllegalCallerException("Function does not have a derivative");
    }

    @Override
    public String name(){
        return getClass().getName();
    }
}
