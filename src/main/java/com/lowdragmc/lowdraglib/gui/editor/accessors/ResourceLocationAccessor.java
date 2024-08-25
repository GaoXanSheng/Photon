package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.StringConfigurator;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/ResourceLocationAccessor.class */
public class ResourceLocationAccessor extends TypesAccessor<ResourceLocation> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public ResourceLocationAccessor() {
        super(ResourceLocation.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public ResourceLocation defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new ResourceLocation(((DefaultValue) field.getAnnotation(DefaultValue.class)).stringValue()[0]);
        }
        return new ResourceLocation("ldlib", "default");
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<ResourceLocation> supplier, Consumer<ResourceLocation> consumer, boolean forceUpdate, Field field) {
        StringConfigurator configurator = new StringConfigurator(name, () -> {
            return ((ResourceLocation) supplier.get()).toString();
        }, s -> {
            consumer.accept(new ResourceLocation(s));
        }, defaultValue(field, String.class).toString(), forceUpdate);
        configurator.setResourceLocation(true);
        return configurator;
    }
}
