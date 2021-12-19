package online.umbcraft.ml.costs;

public interface ErrorFunction {

    double result(double prediction, double goal);

    double derivative(double prediction, double goal);
}
