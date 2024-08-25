package com.lowdragmc.photon.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3Config;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/configurator/NumberFunction3Configurator.class */
public class NumberFunction3Configurator extends ValueConfigurator<NumberFunction3> {
    private NumberFunction3Config config;
    private NumberFunctionConfigurator x;
    private NumberFunctionConfigurator y;
    private NumberFunctionConfigurator z;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NumberFunction3Configurator.class.desiredAssertionStatus();
    }

    public NumberFunction3Config getConfig() {
        return this.config;
    }

    public NumberFunction3Configurator(String name, Supplier<NumberFunction3> supplier, Consumer<NumberFunction3> onUpdate, boolean forceUpdate, NumberFunction3Config config) {
        super(name, supplier, onUpdate, new NumberFunction3(config.xyz().length > 0 ? NumberFunction.constant(Float.valueOf(config.xyz()[0].defaultValue())) : NumberFunction.constant(Float.valueOf(config.common().defaultValue())), config.xyz().length > 1 ? NumberFunction.constant(Float.valueOf(config.xyz()[1].defaultValue())) : NumberFunction.constant(Float.valueOf(config.common().defaultValue())), config.xyz().length > 2 ? NumberFunction.constant(Float.valueOf(config.xyz()[2].defaultValue())) : NumberFunction.constant(Float.valueOf(config.common().defaultValue()))), forceUpdate);
        this.config = config;
        if (this.value == 0) {
            this.value = this.defaultValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(NumberFunction3 newValue) {
        if (newValue == null) {
            newValue = (NumberFunction3) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((NumberFunction3Configurator) newValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        if (!$assertionsDisabled && this.value == 0) {
            throw new AssertionError();
        }
        int w = ((width - this.leftWidth) - this.rightWidth) / 3;
        this.x = new NumberFunctionConfigurator("x", () -> {
            return ((NumberFunction3) this.value).x;
        }, number -> {
            ((NumberFunction3) this.value).x = number;
            updateValue();
        }, this.forceUpdate, this.config.xyz().length > 0 ? this.config.xyz()[0] : this.config.common());
        this.x.setConfigPanel(this.configPanel, this.tab);
        this.x.init(w);
        this.x.addSelfPosition(this.leftWidth, 0);
        addWidget(this.x);
        this.y = new NumberFunctionConfigurator("y", () -> {
            return ((NumberFunction3) this.value).y;
        }, number2 -> {
            ((NumberFunction3) this.value).y = number2;
            updateValue();
        }, this.forceUpdate, this.config.xyz().length > 1 ? this.config.xyz()[1] : this.config.common());
        this.y.setConfigPanel(this.configPanel, this.tab);
        this.y.init(w);
        this.y.addSelfPosition(this.leftWidth + w, 0);
        addWidget(this.y);
        this.z = new NumberFunctionConfigurator("z", () -> {
            return ((NumberFunction3) this.value).z;
        }, number3 -> {
            ((NumberFunction3) this.value).z = number3;
            updateValue();
        }, this.forceUpdate, this.config.xyz().length > 2 ? this.config.xyz()[2] : this.config.common());
        this.z.setConfigPanel(this.configPanel, this.tab);
        this.z.init(w);
        this.z.addSelfPosition(this.leftWidth + (w * 2), 0);
        addWidget(this.z);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void computeHeight() {
        super.computeHeight();
        Size size = getSize();
        this.x.computeHeight();
        this.y.computeHeight();
        this.z.computeHeight();
        setSize(new Size(size.width, Math.max(15, Math.max(this.x.getSize().height, Math.max(this.y.getSize().height, this.z.getSize().height)))));
    }
}
