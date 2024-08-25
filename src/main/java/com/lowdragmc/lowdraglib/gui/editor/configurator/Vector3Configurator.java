package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.utils.Vector3;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/Vector3Configurator.class */
public class Vector3Configurator extends ValueConfigurator<Vector3> {
    protected Number min;
    protected Number max;
    protected Number wheel;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Vector3Configurator.class.desiredAssertionStatus();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Vector3Configurator(String name, Supplier<Vector3> supplier, Consumer<Vector3> onUpdate, @NotNull Vector3 defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        if (this.value == 0) {
            this.value = defaultValue;
        }
        setWheel(Float.valueOf(0.1f));
    }

    public Vector3Configurator setRange(Number min, Number max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public Vector3Configurator setWheel(Number wheel) {
        if (wheel.doubleValue() == 0.0d) {
            return this;
        }
        this.wheel = wheel;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(Vector3 newValue) {
        if (newValue == null) {
            newValue = (Vector3) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((Vector3Configurator) newValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        if (!$assertionsDisabled && this.value == 0) {
            throw new AssertionError();
        }
        int w = ((width - this.leftWidth) - this.rightWidth) / 3;
        NumberConfigurator x = new NumberConfigurator("x", () -> {
            return Double.valueOf(((Vector3) this.value).x);
        }, number -> {
            ((Vector3) this.value).x = number.doubleValue();
            updateValue();
        }, Double.valueOf(((Vector3) this.defaultValue).x), this.forceUpdate);
        x.setRange(this.min, this.max);
        x.setWheel(this.wheel);
        x.setConfigPanel(this.configPanel, this.tab);
        x.init(w);
        x.addSelfPosition(this.leftWidth, 0);
        addWidget(x);
        NumberConfigurator y = new NumberConfigurator("y", () -> {
            return Double.valueOf(((Vector3) this.value).y);
        }, number2 -> {
            ((Vector3) this.value).y = number2.doubleValue();
            updateValue();
        }, Double.valueOf(((Vector3) this.defaultValue).y), this.forceUpdate);
        y.setRange(this.min, this.max);
        y.setWheel(this.wheel);
        y.setConfigPanel(this.configPanel, this.tab);
        y.init(w);
        y.addSelfPosition(this.leftWidth + w, 0);
        addWidget(y);
        NumberConfigurator z = new NumberConfigurator("z", () -> {
            return Double.valueOf(((Vector3) this.value).z);
        }, number3 -> {
            ((Vector3) this.value).z = number3.doubleValue();
            updateValue();
        }, Double.valueOf(((Vector3) this.defaultValue).z), this.forceUpdate);
        z.setRange(this.min, this.max);
        z.setWheel(this.wheel);
        z.setConfigPanel(this.configPanel, this.tab);
        z.init(w);
        z.addSelfPosition(this.leftWidth + (w * 2), 0);
        addWidget(z);
    }
}
