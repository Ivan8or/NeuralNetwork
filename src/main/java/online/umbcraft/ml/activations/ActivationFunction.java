package online.umbcraft.ml.activations;

public interface ActivationFunction {

    double result(double weightedSum);

    double derivative(double weightedSum);
}
