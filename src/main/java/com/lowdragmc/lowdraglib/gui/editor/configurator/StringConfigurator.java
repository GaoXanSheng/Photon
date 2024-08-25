package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/StringConfigurator.class */
public class StringConfigurator extends ValueConfigurator<String> {
    protected TextFieldWidget textFieldWidget;
    protected boolean isResourceLocation;

    public void setResourceLocation(boolean isResourceLocation) {
        this.isResourceLocation = isResourceLocation;
    }

    public StringConfigurator(String name, Supplier<String> supplier, Consumer<String> onUpdate, @Nonnull String defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(String newValue) {
        if (newValue == null) {
            newValue = (String) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((StringConfigurator) newValue);
        this.textFieldWidget.setCurrentString(this.value == 0 ? this.defaultValue : this.value);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        ImageWidget image = new ImageWidget(this.leftWidth, 2, ((width - this.leftWidth) - 3) - this.rightWidth, 10, ColorPattern.T_GRAY.rectTexture().setRadius(5.0f));
        addWidget(image);
        image.setDraggingConsumer(o -> {
            return (!this.isResourceLocation && (o instanceof Number)) || (o instanceof String) || (o instanceof ResourceLocation);
        }, o2 -> {
            image.setImage(ColorPattern.GREEN.rectTexture().setRadius(5.0f));
        }, o3 -> {
            image.setImage(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f));
        }, o4 -> {
            if ((!this.isResourceLocation && (image instanceof Number)) || (image instanceof String) || (image instanceof ResourceLocation)) {
                onValueUpdate(image.toString());
                updateValue();
            }
            image.setImage(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f));
        });
        TextFieldWidget textFieldWidget = new TextFieldWidget(this.leftWidth + 3, 2, ((width - this.leftWidth) - 6) - this.rightWidth, 10, null, this::onStringUpdate);
        this.textFieldWidget = textFieldWidget;
        addWidget(textFieldWidget);
        this.textFieldWidget.setClientSideWidget();
        this.textFieldWidget.setCurrentString(this.value == 0 ? this.defaultValue : this.value);
        this.textFieldWidget.setBordered(false);
        if (this.isResourceLocation) {
            this.textFieldWidget.setResourceLocationOnly();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onStringUpdate(String s) {
        this.value = s;
        updateValue();
    }
}
