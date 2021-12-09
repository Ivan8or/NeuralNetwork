package online.umbcraft.component.activationfunction;

public class SignFunction implements ActivationFunction {

    // returns 1 if x > 0, 0 if x = 0, -1 if x < 0
    @Override
    public double process(double weightedSum) {
        if (weightedSum > 0)
            return 1;
        if(weightedSum == 0)
            return 0;
        return -1;
    }
}
