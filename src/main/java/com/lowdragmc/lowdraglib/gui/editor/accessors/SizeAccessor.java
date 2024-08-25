package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.NumberConfigurator;
import com.lowdragmc.lowdraglib.utils.Size;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/SizeAccessor.class */
public class SizeAccessor extends TypesAccessor<Size> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public SizeAccessor() {
        super(Size.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Size defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new Size((int) ((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[0], (int) ((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[1]);
        }
        return Size.ZERO;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Size> supplier, Consumer<Size> consumer, boolean forceUpdate, Field field) {
        ConfiguratorGroup group = new ConfiguratorGroup(name);
        group.addConfigurators(new NumberConfigurator("width", () -> {
            return Integer.valueOf(((Size) supplier.get()).width);
        }, number -> {
            consumer.accept(new Size(number.intValue(), ((Size) supplier.get()).height));
        }, 0, forceUpdate).setRange(0, Integer.MAX_VALUE));
        group.addConfigurators(new NumberConfigurator("height", () -> {
            return Integer.valueOf(((Size) supplier.get()).height);
        }, number2 -> {
            consumer.accept(new Size(((Size) supplier.get()).width, number2.intValue()));
        }, 0, forceUpdate).setRange(0, Integer.MAX_VALUE));
        return group;
    }
}
