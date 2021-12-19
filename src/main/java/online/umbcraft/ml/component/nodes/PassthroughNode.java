package online.umbcraft.ml.component.nodes;

public class PassthroughNode implements Node {

    private double input;

    // returns the defined input
    @Override
    public double getValue() {
        return input;
    }

    public void setInput(double toSet) {
        input = toSet;
    }
}
