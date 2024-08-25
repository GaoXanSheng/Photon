package com.lowdragmc.photon.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.accessors.TypesAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/configurator/NumberFunctionAccessor.class */
public class NumberFunctionAccessor extends TypesAccessor<NumberFunction> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    public NumberFunctionAccessor() {
        super(NumberFunction.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public NumberFunction defaultValue(Field field, Class<?> type) {
        return NumberFunction.constant(0);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<NumberFunction> supplier, Consumer<NumberFunction> consumer, boolean forceUpdate, Field field) {
        return new NumberFunctionConfigurator(name, supplier, consumer, forceUpdate, (NumberFunctionConfig) field.getAnnotation(NumberFunctionConfig.class));
    }
}
