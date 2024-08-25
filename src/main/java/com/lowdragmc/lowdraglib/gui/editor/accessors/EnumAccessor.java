package com.lowdragmc.lowdraglib.gui.editor.accessors;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSelector;
import com.lowdragmc.lowdraglib.gui.editor.annotation.DefaultValue;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.SelectorConfigurator;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.ArrayUtils;

@ConfigAccessor
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/EnumAccessor.class */
public class EnumAccessor implements IConfiguratorAccessor<Enum> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public /* bridge */ /* synthetic */ Enum defaultValue(Field field, Class cls) {
        return defaultValue(field, (Class<?>) cls);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public boolean test(Class<?> type) {
        return type.isEnum();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Enum defaultValue(Field field, Class<?> type) {
        Object[] enumConstants;
        if (field.isAnnotationPresent(DefaultValue.class)) {
            String name = ((DefaultValue) field.getAnnotation(DefaultValue.class)).stringValue()[0];
            for (Object value : type.getEnumConstants()) {
                String enumName = getEnumName((Enum) value);
                if (enumName.equals(name)) {
                    return (Enum) value;
                }
            }
        }
        return (Enum) type.getEnumConstants()[0];
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public Configurator create(String name, Supplier<Enum> supplier, Consumer<Enum> consumer, boolean forceUpdate, Field field) {
        Class<?> type = ReflectionUtils.getRawType(field.getGenericType());
        if (type.isEnum()) {
            Stream stream = Arrays.stream(type.getEnumConstants());
            Objects.requireNonNull(Enum.class);
            Stream<Enum> candidates = stream.map(this::cast);
            if (field.isAnnotationPresent(ConfigSelector.class)) {
                ConfigSelector configSelector = (ConfigSelector) field.getAnnotation(ConfigSelector.class);
                String[] candidate = configSelector.candidate();
                if (candidate.length > 0) {
                    candidates.filter(e -> {
                        return ArrayUtils.contains(candidate, getEnumName(e));
                    });
                }
            }
            return new SelectorConfigurator(name, supplier, consumer, defaultValue(field, type), forceUpdate, candidates.toList(), EnumAccessor::getEnumName);
        }
        return super.create(name, supplier, consumer, forceUpdate, field);
    }

    public static String getEnumName(Enum enumValue) {
        if (enumValue instanceof StringRepresentable) {
            StringRepresentable provider = (StringRepresentable) enumValue;
            return provider.m_7912_();
        }
        return enumValue.name();
    }
}
