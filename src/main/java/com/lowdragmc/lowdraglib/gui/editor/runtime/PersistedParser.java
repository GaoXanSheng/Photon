package com.lowdragmc.lowdraglib.gui.editor.runtime;

import com.google.common.base.Strings;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import com.lowdragmc.lowdraglib.utils.TagUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/runtime/PersistedParser.class */
public class PersistedParser {
    /* JADX WARN: Removed duplicated region for block: B:47:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0157 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void serializeNBT(net.minecraft.nbt.CompoundTag r4, java.lang.Class<?> r5, java.lang.Object r6) {
        /*
            Method dump skipped, instructions count: 350
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser.serializeNBT(net.minecraft.nbt.CompoundTag, java.lang.Class, java.lang.Object):void");
    }

    public static void deserializeNBT(CompoundTag tag, Map<String, Method> setters, Class<?> clazz, Object object) {
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
        deserializeNBT(tag, setters, clazz.getSuperclass(), object);
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String key = field.getName();
                if (field.isAnnotationPresent(Configurable.class)) {
                    Configurable configurable = (Configurable) field.getAnnotation(Configurable.class);
                    if (!configurable.persisted()) {
                        continue;
                    } else if (!Strings.isNullOrEmpty(configurable.key())) {
                        key = configurable.key();
                    }
                } else if (field.isAnnotationPresent(Persisted.class)) {
                    Persisted persisted = (Persisted) field.getAnnotation(Persisted.class);
                    if (!Strings.isNullOrEmpty(persisted.key())) {
                        key = persisted.key();
                    }
                } else {
                    continue;
                }
                CompoundTag tagExtended = TagUtils.getTagExtended(tag, key);
                if (tagExtended == null) {
                    continue;
                } else if ((field.isAnnotationPresent(Configurable.class) && ((Configurable) field.getAnnotation(Configurable.class)).subConfigurable()) || (field.isAnnotationPresent(Persisted.class) && ((Persisted) field.getAnnotation(Persisted.class)).subPersisted())) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(object);
                        if (value != null) {
                            if (value instanceof ITagSerializable) {
                                ITagSerializable serializable = (ITagSerializable) value;
                                serializable.deserializeNBT(tagExtended);
                            } else if (tagExtended instanceof CompoundTag) {
                                CompoundTag compoundTag = tagExtended;
                                deserializeNBT(compoundTag, new HashMap(), ReflectionUtils.getRawType(field.getGenericType()), value);
                            }
                        }
                    } catch (IllegalAccessException e) {
                    }
                } else {
                    ManagedKey managedKey = ManagedFieldUtils.createKey(field);
                    managedKey.writePersistedField(managedKey.createRef(object), tagExtended);
                    Method setter = setters.get(field.getName());
                    if (setter != null) {
                        field.setAccessible(true);
                        try {
                            setter.invoke(object, field.get(object));
                        } catch (IllegalAccessException | InvocationTargetException e2) {
                            throw new RuntimeException(e2);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
