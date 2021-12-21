package online.umbcraft.ml.functions.grouped;

import online.umbcraft.ml.functions.activations.ActivationFunction;

import java.util.Arrays;

public class SoftMaxFunction implements GroupedFunction {

    // returns [i -> e^xi / (1 + sum(e^xj) )]
    @Override
    public double[] result(double... weightedSum) {

        double total = 0;
        double[] indiv = new double[weightedSum.length];

        // calculate e^x for each x, and sum them up to get the total
        for(int i = 0; i < weightedSum.length; i++) {
            indiv[i] = Math.pow(Math.E, weightedSum[i]);
            total += indiv[i];
        }
        final double finalTotal = total;
        return Arrays.stream(indiv).map(i -> i/finalTotal).toArray();
    }

    @Override
    public double[] derivative(double... weightedSum){

        double[] softmaxed = result(weightedSum);
        double[] output = new double[weightedSum.length];

        for(int i = 0; i < weightedSum.length; i++) {
            for(int j = 0; j < weightedSum.length; j++) {
                if(i == j)
                    output[i] += softmaxed[i] * (1-softmaxed[j]);

                else
                    output[i] += -softmaxed[i] * softmaxed[j];
            }
        }
        return output;
    }

    @Override
    public String name(){
        return getClass().getName();
    }
}
