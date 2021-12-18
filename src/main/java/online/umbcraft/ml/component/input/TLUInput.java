package online.umbcraft.ml.component.input;

import online.umbcraft.ml.component.ThresholdLogicUnit;

public class TLUInput implements Input {

    private ThresholdLogicUnit tlu;

    public TLUInput(ThresholdLogicUnit tlu) {
        this.tlu = tlu;
    }

    @Override
    public double getInput() {
        return tlu.evaluate();
    }
}
