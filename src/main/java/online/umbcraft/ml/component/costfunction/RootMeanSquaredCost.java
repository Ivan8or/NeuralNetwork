package online.umbcraft.ml.component.costfunction;

import online.umbcraft.ml.data.DataPoint;
import online.umbcraft.ml.data.DataSet;

public class RootMeanSquaredCost implements CostFunction {

    @Override
    public double result(double prediction, double goal) {
        return Math.sqrt(Math.pow(prediction - goal, 2));
    }

    @Override
    public double derivative(double prediction, double goal) {
        throw new IllegalCallerException("Function does not have a derivative");
    }
}
