package online.umbcraft.data.offsets;

import java.util.Arrays;

public class OffsetVector {

    private double biasOffset;
    private double[] weightOffsets;
    private double[] nodeOffsets;

    public OffsetVector() {
    }

    public OffsetVector(double biasOffset, double[] weightOffsets, double[] nodeOffsets) {
        this.biasOffset = biasOffset;
        this.weightOffsets = weightOffsets;
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

    public double[] weightOffsets() {
        return weightOffsets;
    }

    public double[] nodeOffsets() {
        return nodeOffsets;
    }

    public String toString() {
        String toReturn = "";
        toReturn += "weight offsets: \t"+Arrays.toString(weightOffsets)+"\n";
        toReturn += "node offsets: \t"+Arrays.toString(nodeOffsets)+"\n";
        toReturn += "bias offset: \t"+biasOffset;
        return toReturn;
    }

    public OffsetVector add(OffsetVector other) {
        double[] newWeights = weightOffsets.clone();
        for(int i = 0; i < newWeights.length; i++)
            newWeights[i] += other.weightOffsets[i];

        double[] newNodes = nodeOffsets.clone();
        for(int i = 0; i < newNodes.length; i++)
            newNodes[i] += other.nodeOffsets[i];

        double newBias = biasOffset + other.biasOffset;

        return new OffsetVector(newBias, newWeights, newNodes);
    }
}

