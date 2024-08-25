package com.lowdragmc.photon.client.emitter.data.number.color;

import com.lowdragmc.lowdraglib.gui.editor.configurator.ColorConfigurator;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.gui.editor.configurator.NumberFunctionConfigurator;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/color/Color.class */
public class Color extends Constant {
    public Color() {
        super((Number) (-1));
    }

    public Color(Number number) {
        super(number);
    }

    public Color(NumberFunctionConfig config) {
        super(config);
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.Constant, com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public void createConfigurator(WidgetGroup group, NumberFunctionConfigurator configurator) {
        ColorConfigurator widget = new ColorConfigurator("", () -> {
            return Integer.valueOf(getNumber().intValue());
        }, number -> {
            setNumber(configurator);
            configurator.updateValue();
        }, Integer.valueOf(getNumber().intValue()), true);
        widget.setConfigPanel(configurator.getConfigPanel(), configurator.getTab());
        widget.init(group.getSize().width);
        group.addWidget(widget);
    }
}
