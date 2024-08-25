package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.StringConfigurator;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/StringAccessor.class */
public class StringAccessor extends TypesAccessor<String> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public StringAccessor() {
        super(String.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public String defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return ((DefaultValue) field.getAnnotation(DefaultValue.class)).stringValue()[0];
        }
        return "";
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<String> supplier, Consumer<String> consumer, boolean forceUpdate, Field field) {
        return new StringConfigurator(name, supplier, consumer, defaultValue(field, String.class), forceUpdate);
    }
}
