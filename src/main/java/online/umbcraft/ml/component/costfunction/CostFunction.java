package online.umbcraft.ml.component.costfunction;

public interface CostFunction {

    double result(double prediction, double goal);

    double derivative(double prediction, double goal);
}
