package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.NumberConfigurator;
import com.lowdragmc.lowdraglib.utils.Position;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/PositionAccessor.class */
public class PositionAccessor extends TypesAccessor<Position> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public PositionAccessor() {
        super(Position.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Position defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new Position((int) ((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[0], (int) ((DefaultValue) field.getAnnotation(DefaultValue.class)).numberValue()[1]);
        }
        return Position.ORIGIN;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Position> supplier, Consumer<Position> consumer, boolean forceUpdate, Field field) {
        ConfiguratorGroup group = new ConfiguratorGroup(name);
        group.addConfigurators(new NumberConfigurator("x", () -> {
            return Integer.valueOf(((Position) supplier.get()).x);
        }, number -> {
            consumer.accept(new Position(number.intValue(), ((Position) supplier.get()).y));
        }, 0, forceUpdate).setRange(Integer.MIN_VALUE, Integer.MAX_VALUE));
        group.addConfigurators(new NumberConfigurator("y", () -> {
            return Integer.valueOf(((Position) supplier.get()).y);
        }, number2 -> {
            consumer.accept(new Position(((Position) supplier.get()).x, number2.intValue()));
        }, 0, forceUpdate).setRange(Integer.MIN_VALUE, Integer.MAX_VALUE));
        return group;
    }
}
