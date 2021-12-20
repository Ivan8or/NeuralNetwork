package online.umbcraft.ml.functions.costs;

import online.umbcraft.ml.functions.Function;

public interface ErrorFunction extends Function {

    double result(double prediction, double goal);

    double derivative(double prediction, double goal);

}
