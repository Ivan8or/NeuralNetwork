package online.umbcraft.component.errorfunction;

import online.umbcraft.data.DataPoint;
import online.umbcraft.data.DataSet;

public class MeanAbsoluteError implements ErrorFunction {


    @Override
    public double error(DataSet predictions) {
        double cumErr = 0;
        for(DataPoint pred: predictions.getData()) {
            cumErr += Math.abs(pred.getFeatures().get(0) - pred.getLabel());
        }
        double error = cumErr / predictions.dimensions()[1];
        return error;
    }
}
