package com.lowdragmc.lowdraglib.gui.editor.runtime;

import com.lowdragmc.lowdraglib.gui.editor.accessors.ArrayConfiguratorAccessor;
import com.lowdragmc.lowdraglib.gui.editor.accessors.CollectionConfiguratorAccessor;
import com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/runtime/ConfiguratorAccessors.class */
public class ConfiguratorAccessors {
    private static final Map<Class<?>, IConfiguratorAccessor<?>> ACCESSOR_MAP = new ConcurrentHashMap();

    public static IConfiguratorAccessor<?> findByType(Type clazz) {
        if (clazz instanceof GenericArrayType) {
            GenericArrayType array = (GenericArrayType) clazz;
            Type componentType = array.getGenericComponentType();
            IConfiguratorAccessor<?> childAccessor = findByType(componentType);
            Class<?> rawType = ReflectionUtils.getRawType(componentType);
            return new ArrayConfiguratorAccessor(rawType == null ? Object.class : rawType, childAccessor);
        }
        Class<?> rawType2 = ReflectionUtils.getRawType(clazz);
        if (rawType2 != null) {
            if (rawType2.isArray()) {
                Class<?> componentType2 = rawType2.getComponentType();
                IConfiguratorAccessor<?> childAccessor2 = findByType(componentType2);
                return new ArrayConfiguratorAccessor(componentType2, childAccessor2);
            } else if (Collection.class.isAssignableFrom(rawType2)) {
                Type componentType3 = ((ParameterizedType) clazz).getActualTypeArguments()[0];
                IConfiguratorAccessor<?> childAccessor3 = findByType(componentType3);
                Class<?> rawComponentType = ReflectionUtils.getRawType(componentType3);
                return new CollectionConfiguratorAccessor(rawType2, rawComponentType == null ? Object.class : rawComponentType, childAccessor3);
            } else {
                return findByClass(rawType2);
            }
        }
        return IConfiguratorAccessor.DEFAULT;
    }

    public static IConfiguratorAccessor<?> findByClass(Class<?> clazz) {
        return ACCESSOR_MAP.computeIfAbsent(clazz, c -> {
            for (IConfiguratorAccessor<?> accessor : AnnotationDetector.CONFIGURATOR_ACCESSORS) {
                if (accessor.test(c)) {
                    return accessor;
                }
            }
            return IConfiguratorAccessor.DEFAULT;
        });
    }
}
