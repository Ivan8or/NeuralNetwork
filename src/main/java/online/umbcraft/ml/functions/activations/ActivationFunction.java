package online.umbcraft.ml.functions.activations;

import online.umbcraft.ml.functions.Function;

public interface ActivationFunction extends Function {

    double result(double weightedSum);

    double derivative(double weightedSum);

}
