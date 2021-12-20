package online.umbcraft.ml.functions.costs;

public class MeanSquaredError implements ErrorFunction {

    @Override
    public double result(double prediction, double goal) {
        return Math.pow(prediction - goal, 2);
    }

    @Override
    public double derivative(double prediction, double goal) {
        return 2 * (prediction - goal);
    }

    @Override
    public String name(){
        return getClass().getName();
    }
}
