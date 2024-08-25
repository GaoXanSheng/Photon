package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ColorConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.NumberConfigurator;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/NumberAccessor.class */
public class NumberAccessor extends TypesAccessor<Number> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public NumberAccessor() {
        super(Integer.TYPE, Float.TYPE, Byte.TYPE, Double.TYPE, Integer.class, Float.class, Byte.class, Double.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Number defaultValue(Field field, Class<?> type) {
        Number number = 0;
        if (field.isAnnotationPresent(DefaultValue.class)) {
            number = Double.valueOf(((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[0]);
        }
        if (field.isAnnotationPresent(NumberRange.class)) {
            number = Double.valueOf(((NumberRange) field.getAnnotation(NumberRange.class)).range()[0]);
        }
        if (type == Integer.TYPE || type == Integer.class) {
            return Integer.valueOf(number.intValue());
        }
        if (type == Byte.TYPE || type == Byte.class) {
            return Byte.valueOf(number.byteValue());
        }
        if (type == Long.TYPE || type == Long.class) {
            return Long.valueOf(number.longValue());
        }
        if (type == Float.TYPE || type == Float.class) {
            return Float.valueOf(number.floatValue());
        }
        if (type == Double.TYPE || type == Double.class) {
            return Double.valueOf(number.doubleValue());
        }
        return number;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Number> supplier, Consumer<Number> consumer, boolean forceUpdate, Field field) {
        if (field.isAnnotationPresent(NumberColor.class)) {
            return new ColorConfigurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
        }
        NumberConfigurator configurator = new NumberConfigurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = (NumberRange) field.getAnnotation(NumberRange.class);
            configurator = configurator.setRange(Double.valueOf(range.range()[0]), Double.valueOf(range.range()[1])).setWheel(Double.valueOf(range.wheel()));
        }
        return configurator;
    }
}
