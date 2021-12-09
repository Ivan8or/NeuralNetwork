package online.umbcraft.data;

import java.util.List;

public class DataPoint {
    final List<Double> features;
    final Double label;

    // populate datapoint with features and label
    public DataPoint(List<Double> features, double label) {
        this.features = features;
        this.label = label;
    }

    // populate datapoint with features; exclude label
    public DataPoint(List<Double> features) {
        this.features = features;
        this.label = null;
    }

    // return number of features in datapoint
    public int dimension() {
        return features.size();
    }

    // return each feature in the datapoint
    public List<Double> getFeatures() {
        return features;
    }

    // return the datapoint label
    public Double getLabel() {
        return label;
    }
}
