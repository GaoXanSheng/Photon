package com.lowdragmc.lowdraglib.syncdata.managed;

import java.util.Map;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedHolder.class */
public class ManagedHolder<T> implements IManagedVar<T> {
    private static Map<Class<?>, Class<?>> primitiveToWrapper = Map.of(Integer.TYPE, Integer.class, Long.TYPE, Long.class, Float.TYPE, Float.class, Double.TYPE, Double.class, Boolean.TYPE, Boolean.class, Byte.TYPE, Byte.class, Character.TYPE, Character.class, Short.TYPE, Short.class);
    private T value;
    private final Class<T> type;

    public ManagedHolder(T value, Class<T> type) {
        this.type = type;
        set(value);
    }

    public static <T> ManagedHolder<T> of(T value) {
        return new ManagedHolder<>(value, value.getClass());
    }

    public static <T> ManagedHolder<T> ofType(Class<T> type) {
        if (type.isPrimitive()) {
            type = primitiveToWrapper.get(type);
        }
        return new ManagedHolder<>(null, type);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public T value() {
        return this.value;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public void set(T value) {
        if (value != null && !this.type.isInstance(value)) {
            throw new IllegalArgumentException("Value is not of type " + this.type);
        }
        this.value = value;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public boolean isPrimitive() {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public Class<T> getType() {
        return this.type;
    }
}
