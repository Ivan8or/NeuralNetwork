package online.umbcraft.data.inputs;

import java.util.Arrays;
import java.util.List;

public class DataPoint {
    final double[] features;
    final double[] labels;

    // populate datapoint with features and label
    public DataPoint(double[] features, double[] labels) {
        this.features = features;
        this.labels = labels;
    }

    // populate datapoint with features; exclude label
    public DataPoint(double[] features) {
        this.features = features;
        this.labels = null;
    }

    // return number of features in datapoint
    public int dimension() {
        return features.length;
    }

    // return each feature in the datapoint
    public double[] getFeatures() {
        return features;
    }

    // return the datapoint label
    public double[] getLabels() {
        return labels;
    }

    public String toString() {
        String toReturn = "";
        toReturn+= "features: "+ Arrays.toString(features)+"\t";
        toReturn+= "labels: "+ Arrays.toString(labels);
        return toReturn;
    }
}
