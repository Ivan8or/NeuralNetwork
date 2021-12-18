package online.umbcraft.ml.component.activationfunction;

public interface ActivationFunction {

    double result(double weightedSum);

    double derivative(double weightedSum);
}
