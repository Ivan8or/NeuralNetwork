package online.umbcraft.ml.component.input;

public class BiasInput implements Input {

    // always returns 1
    @Override
    public double getInput() {
        return 1;
    }
}
