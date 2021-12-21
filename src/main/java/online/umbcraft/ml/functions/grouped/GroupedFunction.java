package online.umbcraft.ml.functions.grouped;

import online.umbcraft.ml.functions.Function;

public interface GroupedFunction extends Function {

    double[] result(double[] weightedSum);

    double[] derivative(double[] weightedSum);

}
