package online.umbcraft.ml.component.nodes;

public class BiasNode implements Node {

    // always returns 1
    @Override
    public double getValue() {
        return 1;
    }
}
