package online.umbcraft.ml.costs;

public class MeanAbsoluteError implements ErrorFunction {

    @Override
    public double result(double prediction, double goal) {
        return Math.abs(prediction - goal);
    }

    @Override
    public double derivative(double prediction, double goal) {
        throw new IllegalCallerException("Function does not have a derivative");
    }
}
