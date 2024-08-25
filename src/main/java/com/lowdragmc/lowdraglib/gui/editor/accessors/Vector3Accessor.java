package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Vector3Configurator;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import com.lowdragmc.lowdraglib.utils.Vector3;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/Vector3Accessor.class */
public class Vector3Accessor extends TypesAccessor<Vector3> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public Vector3Accessor() {
        super(Vector3.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Vector3 defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new Vector3(((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[0], ((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[1], ((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[2]);
        }
        return Vector3.ZERO.copy();
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Vector3> supplier, Consumer<Vector3> consumer, boolean forceUpdate, Field field) {
        Vector3Configurator configurator = new Vector3Configurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = (NumberRange) field.getAnnotation(NumberRange.class);
            configurator = configurator.setRange(Double.valueOf(range.range()[0]), Double.valueOf(range.range()[1])).setWheel(Double.valueOf(range.wheel()));
        }
        return configurator;
    }
}
