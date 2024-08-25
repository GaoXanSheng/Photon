package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/BooleanConfigurator.class */
public class BooleanConfigurator extends ValueConfigurator<Boolean> {
    protected SwitchWidget switchWidget;

    /* JADX WARN: Multi-variable type inference failed */
    public BooleanConfigurator(String name, Supplier<Boolean> supplier, Consumer<Boolean> onUpdate, @Nonnull Boolean defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        if (this.value == 0) {
            this.value = defaultValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(Boolean newValue) {
        if (newValue == null) {
            newValue = (Boolean) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((BooleanConfigurator) newValue);
        this.switchWidget.setPressed(newValue.booleanValue());
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        SwitchWidget switchWidget = new SwitchWidget(this.leftWidth, 2, 10, 10, cd, pressed -> {
            this.value = pressed;
            updateValue();
        });
        this.switchWidget = switchWidget;
        addWidget(switchWidget);
        this.switchWidget.setPressed(((Boolean) this.value).booleanValue());
        this.switchWidget.setTexture(new ColorBorderTexture(-1, -1).setRadius(5.0f), new GuiTextureGroup(new ColorBorderTexture(-1, -1).setRadius(5.0f), new ColorRectTexture(-1).setRadius(5.0f).scale(0.5f)));
    }
}
