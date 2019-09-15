package com.rockbite.tools.talos.runtime.modules;

import com.rockbite.tools.talos.runtime.values.NumericalValue;

public class StaticValueModule extends Module {

    public static final int OUTPUT = 0;

    private NumericalValue staticValue = new NumericalValue();
    private NumericalValue outputValue;

    @Override
    protected void defineSlots() {
        outputValue = createOutputSlot(OUTPUT);
    }

    @Override
    public void processValues() {
        outputValue.set(staticValue);
    }

    public void setStaticValue(float val) {
        staticValue.set(val);
    }
}
