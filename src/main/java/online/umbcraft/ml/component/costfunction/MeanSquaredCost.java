package online.umbcraft.ml.component.costfunction;

public class MeanSquaredCost implements CostFunction {

    @Override
    public double result(double prediction, double goal) {
        return Math.pow(prediction - goal, 2);
    }

    @Override
    public double derivative(double prediction, double goal) {
        return 2 * (prediction - goal);
    }
}
