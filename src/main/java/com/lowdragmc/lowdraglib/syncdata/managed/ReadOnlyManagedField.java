package com.lowdragmc.lowdraglib.syncdata.managed;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ReadOnlyManagedField.class */
public class ReadOnlyManagedField extends ManagedField {
    protected final Method onDirtyMethod;
    protected final Method serializeMethod;
    protected final Method deserializeMethod;

    protected ReadOnlyManagedField(Field field, Object instance, Method onDirtyMethod, Method serializeMethod, Method deserializeMethod) {
        super(field, instance);
        this.onDirtyMethod = onDirtyMethod;
        this.serializeMethod = serializeMethod;
        this.deserializeMethod = deserializeMethod;
    }

    public static ReadOnlyManagedField of(Field field, Object instance, Method onDirtyMethod, Method serializeMethod, Method deserializeMethod) {
        return new ReadOnlyManagedField(field, instance, onDirtyMethod, serializeMethod, deserializeMethod);
    }

    public boolean isDirty(Object obj) {
        try {
            return ((Boolean) this.onDirtyMethod.invoke(this.instance, obj)).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public CompoundTag serializeUid(Object obj) {
        try {
            return (CompoundTag) this.serializeMethod.invoke(this.instance, obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object deserializeUid(CompoundTag uid) {
        try {
            return this.deserializeMethod.invoke(this.instance, uid);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
