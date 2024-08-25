package com.lowdragmc.lowdraglib.gui.editor.runtime;

import com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/runtime/ConfiguratorParser.class */
public class ConfiguratorParser {
    public static void createConfigurators(ConfiguratorGroup father, Map<String, Method> setters, Class<?> clazz, Object object) {
        Method[] methods;
        Field[] declaredFields;
        if (clazz == Object.class || clazz == null) {
            return;
        }
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(ConfigSetter.class)) {
                ConfigSetter configSetter = (ConfigSetter) method.getAnnotation(ConfigSetter.class);
                String name = configSetter.field();
                if (!setters.containsKey(name)) {
                    setters.put(name, method);
                }
            }
        }
        createConfigurators(father, setters, clazz.getSuperclass(), object);
        if (clazz.isAnnotationPresent(Configurable.class)) {
            Configurable configurable = (Configurable) clazz.getAnnotation(Configurable.class);
            String name2 = configurable.name().isEmpty() ? clazz.getSimpleName() : configurable.name();
            ConfiguratorGroup newGroup = new ConfiguratorGroup(name2, configurable.collapse());
            newGroup.setCanCollapse(configurable.canCollapse());
            newGroup.setTips(configurable.tips());
            father.addConfigurators(newGroup);
            father = newGroup;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Configurable.class)) {
                Configurable configurable2 = (Configurable) field.getAnnotation(Configurable.class);
                if (configurable2.subConfigurable()) {
                    Class<?> rawClass = ReflectionUtils.getRawType(field.getGenericType());
                    try {
                        field.setAccessible(true);
                        Object value = field.get(object);
                        if (value != null) {
                            String name3 = configurable2.name().isEmpty() ? clazz.getSimpleName() : configurable2.name();
                            ConfiguratorGroup newGroup2 = new ConfiguratorGroup(name3, configurable2.collapse());
                            newGroup2.setCanCollapse(configurable2.canCollapse());
                            newGroup2.setTips(configurable2.tips());
                            if (value instanceof IConfigurable) {
                                IConfigurable subConfigurable = (IConfigurable) value;
                                subConfigurable.buildConfigurator(newGroup2);
                            } else {
                                createConfigurators(newGroup2, new HashMap(), rawClass, value);
                            }
                            father.addConfigurators(newGroup2);
                        }
                    } catch (IllegalAccessException e) {
                    }
                } else {
                    IConfiguratorAccessor accessor = ConfiguratorAccessors.findByType(field.getGenericType());
                    field.setAccessible(true);
                    String name4 = configurable2.name().isEmpty() ? field.getName() : configurable2.name();
                    Method setter = setters.get(field.getName());
                    Configurator configurator = accessor.create(name4, () -> {
                        try {
                            return field.get(object);
                        } catch (IllegalAccessException e2) {
                            throw new RuntimeException(e2);
                        }
                    }, value2 -> {
                        try {
                            if (setter == null) {
                                field.set(object, value2);
                            } else {
                                setter.invoke(object, value2);
                            }
                        } catch (Exception e2) {
                            throw new RuntimeException(e2);
                        }
                    }, configurable2.forceUpdate(), field);
                    configurator.setTips(configurable2.tips());
                    father.addConfigurators(configurator);
                }
            }
        }
    }
}
