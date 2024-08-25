package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/IConfiguratorAccessor.class */
public interface IConfiguratorAccessor<T> {
    public static final IConfiguratorAccessor<?> DEFAULT = type -> {
        return true;
    };

    boolean test(Class<?> cls);

    default T defaultValue(Field field, Class<?> type) {
        return null;
    }

    default Configurator create(String name, Supplier<T> supplier, Consumer<T> consumer, boolean forceUpdate, Field field) {
        return new Configurator(name);
    }
}
