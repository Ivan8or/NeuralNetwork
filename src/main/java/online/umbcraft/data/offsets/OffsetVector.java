package online.umbcraft.data.offsets;

public class OffsetVector {

    private double biasOffset;
    private double[] weightOffsets;
    private double[] nodeOffsets;

    public OffsetVector() {
    }

    public OffsetVector(double biasOffset, double[] connectionOffsets, double[] nodeOffsets) {
        this.biasOffset = biasOffset;
        this.weightOffsets = connectionOffsets;
        this.nodeOffsets = nodeOffsets;
    }

    public void setBiasOffset(double offset) {
        biasOffset = offset;
    }

    public void setWeightOffsets(double... offsets) {
        weightOffsets = offsets;
    }

    public void setNodeOffsets(double... offsets) {
        nodeOffsets = offsets;
    }

    public double biasOffset() {
        return biasOffset;
    }

    public double[] weigthOffsets() {
        return weightOffsets;
    }

    public double[] nodeOffsets() {
        return nodeOffsets;
    }
}

