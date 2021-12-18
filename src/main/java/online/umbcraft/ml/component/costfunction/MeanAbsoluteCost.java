package online.umbcraft.ml.component.costfunction;

public class MeanAbsoluteCost implements CostFunction {

    @Override
    public double result(double prediction, double goal) {
        return Math.abs(prediction - goal);
    }

    @Override
    public double derivative(double prediction, double goal) {
        throw new IllegalCallerException("Function does not have a derivative");
    }
}
