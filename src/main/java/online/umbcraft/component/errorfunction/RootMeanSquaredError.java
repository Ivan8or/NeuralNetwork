package online.umbcraft.component.errorfunction;

import online.umbcraft.data.DataPoint;
import online.umbcraft.data.DataSet;

public class RootMeanSquaredError implements ErrorFunction {


    @Override
    public double error(DataSet predictions) {
        double cumErr = 0;
        for(DataPoint pred: predictions.getData()) {
            cumErr += Math.pow(pred.getFeatures().get(0) - pred.getLabel(), 2);
        }
        double error = Math.sqrt(cumErr / predictions.dimensions()[1]);
        return error;
    }
}
