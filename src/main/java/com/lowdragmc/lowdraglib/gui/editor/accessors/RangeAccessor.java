package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.RangeConfigurator;
import com.lowdragmc.lowdraglib.utils.Range;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/RangeAccessor.class */
public class RangeAccessor extends TypesAccessor<Range> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public RangeAccessor() {
        super(Range.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Range defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = (NumberRange) field.getAnnotation(NumberRange.class);
            if (range.range().length > 1) {
                return new Range(Double.valueOf(range.range()[0]), Double.valueOf(range.range()[1]));
            }
        }
        return new Range(Float.valueOf(0.0f), Float.valueOf(1.0f));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Range> supplier, Consumer<Range> consumer, boolean forceUpdate, Field field) {
        RangeConfigurator configurator = new RangeConfigurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = (NumberRange) field.getAnnotation(NumberRange.class);
            configurator = configurator.setRange(Double.valueOf(range.range()[0]), Double.valueOf(range.range()[1])).setWheel(Double.valueOf(range.wheel()));
        }
        return configurator;
    }
}
