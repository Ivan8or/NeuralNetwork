package online.umbcraft.ml.component.input;

public class PassthroughInput implements Input {

    private double input;

    // returns the defined input
    @Override
    public double getInput() {
        return input;
    }

    public void setInput(double toSet) {
        input = toSet;
    }
}
