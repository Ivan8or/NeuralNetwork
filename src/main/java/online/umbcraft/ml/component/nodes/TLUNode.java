package online.umbcraft.ml.component.nodes;

import online.umbcraft.ml.component.ThresholdLogicUnit;

public class TLUNode implements Node {

    private ThresholdLogicUnit tlu;

    public TLUNode(ThresholdLogicUnit tlu) {
        this.tlu = tlu;
    }

    @Override
    public double getValue() {
        return tlu.evaluate();
    }

    public ThresholdLogicUnit getTLU() {
        return tlu;
    }
}
