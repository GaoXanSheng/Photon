package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.utils.Range;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/RangeConfigurator.class */
public class RangeConfigurator extends ValueConfigurator<Range> {
    protected Number min;
    protected Number max;
    protected Number wheel;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RangeConfigurator.class.desiredAssertionStatus();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public RangeConfigurator(String name, Supplier<Range> supplier, Consumer<Range> onUpdate, Range defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        if (this.value == 0) {
            this.value = defaultValue;
        }
        boolean isDecimal = (((Range) this.value).getA() instanceof Double) || (((Range) this.value).getB() instanceof Double) || (((Range) this.value).getA() instanceof Float) || (((Range) this.value).getB() instanceof Float);
        setRange(Double.valueOf(Math.min(((Range) this.value).getA().doubleValue(), ((Range) this.value).getB().doubleValue())), Double.valueOf(Math.max(((Range) this.value).getA().doubleValue(), ((Range) this.value).getB().doubleValue())));
        if (isDecimal) {
            setWheel(Double.valueOf(0.1d));
        } else {
            setWheel(1);
        }
    }

    public RangeConfigurator setRange(Number min, Number max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public RangeConfigurator setWheel(Number wheel) {
        if (wheel.doubleValue() == 0.0d) {
            return this;
        }
        this.wheel = wheel;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(Range newValue) {
        if (newValue == null) {
            newValue = (Range) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((RangeConfigurator) newValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        if (!$assertionsDisabled && this.value == 0) {
            throw new AssertionError();
        }
        int w = ((width - this.leftWidth) - this.rightWidth) / 2;
        NumberConfigurator x = new NumberConfigurator("", () -> {
            return ((Range) this.value).getA();
        }, number -> {
            ((Range) this.value).setA(number);
            updateValue();
        }, ((Range) this.defaultValue).getA(), this.forceUpdate);
        x.setRange(this.min, this.max);
        x.setWheel(this.wheel);
        x.setConfigPanel(this.configPanel, this.tab);
        x.init(w);
        x.addSelfPosition(this.leftWidth, 0);
        addWidget(x);
        NumberConfigurator y = new NumberConfigurator("", () -> {
            return ((Range) this.value).getB();
        }, number2 -> {
            ((Range) this.value).setB(number2);
            updateValue();
        }, ((Range) this.defaultValue).getB(), this.forceUpdate);
        y.setRange(this.min, this.max);
        y.setWheel(this.wheel);
        y.setConfigPanel(this.configPanel, this.tab);
        y.init(w);
        y.addSelfPosition(this.leftWidth + w, 0);
        addWidget(y);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void updateValue() {
        super.updateValue();
    }
}
