package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.BooleanConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/BooleanAccessor.class */
public class BooleanAccessor extends TypesAccessor<Boolean> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public BooleanAccessor() {
        super(Boolean.class, Boolean.TYPE);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Boolean defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return Boolean.valueOf(((DefaultValue) field.getAnnotation(DefaultValue.class)).booleanValue()[0]);
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Boolean> supplier, Consumer<Boolean> consumer, boolean forceUpdate, Field field) {
        return new BooleanConfigurator(name, supplier, consumer, defaultValue(field, Boolean.TYPE), forceUpdate);
    }
}
