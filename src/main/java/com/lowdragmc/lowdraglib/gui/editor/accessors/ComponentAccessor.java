package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.StringConfigurator;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/ComponentAccessor.class */
public class ComponentAccessor implements IConfiguratorAccessor<Component> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Component defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public boolean test(Class<?> type) {
        return type == Component.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Component defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return Component.m_130674_(((DefaultValue) field.getAnnotation(DefaultValue.class)).stringValue()[0]);
        }
        return Component.m_237119_();
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Component> supplier, Consumer<Component> consumer, boolean forceUpdate, Field field) {
        return new StringConfigurator(name, () -> {
            Component component = (Component) supplier.get();
            return component.getString();
        }, s -> {
            consumer.accept(Component.m_237115_(s));
        }, "", forceUpdate);
    }
}
