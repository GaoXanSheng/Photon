package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.GuiTextureConfigurator;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/GuiTextureAccessor.class */
public class GuiTextureAccessor extends TypesAccessor<IGuiTexture> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public GuiTextureAccessor() {
        super(IGuiTexture.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public IGuiTexture defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new ResourceTexture(((DefaultValue) field.getAnnotation(DefaultValue.class)).stringValue()[0]);
        }
        return IGuiTexture.EMPTY;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<IGuiTexture> supplier, Consumer<IGuiTexture> consumer, boolean forceUpdate, Field field) {
        return new GuiTextureConfigurator(name, supplier, consumer, forceUpdate);
    }
}
