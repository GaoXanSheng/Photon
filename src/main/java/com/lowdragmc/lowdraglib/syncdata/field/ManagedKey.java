package com.lowdragmc.lowdraglib.syncdata.field;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.accessor.IArrayLikeAccessor;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayLikeRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedField;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ReadOnlyManagedField;
import com.lowdragmc.lowdraglib.syncdata.managed.ReadonlyArrayRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ReadonlyRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/field/ManagedKey.class */
public class ManagedKey {
    private final String name;
    private final boolean isDestSync;
    private final boolean isPersist;
    private final boolean isDrop;
    @Nullable
    private String persistentKey;
    private final boolean isLazy;
    private final Type contentType;
    private final Field rawField;
    private boolean isReadOnlyManaged;
    @Nullable
    private Method onDirtyMethod;
    @Nullable
    private Method serializeMethod;
    @Nullable
    private Method deserializeMethod;
    private IAccessor accessor;

    public String getName() {
        return this.name;
    }

    public boolean isDestSync() {
        return this.isDestSync;
    }

    public boolean isPersist() {
        return this.isPersist;
    }

    public boolean isDrop() {
        return this.isDrop;
    }

    @Nullable
    public String getPersistentKey() {
        return this.persistentKey;
    }

    public boolean isLazy() {
        return this.isLazy;
    }

    public Type getContentType() {
        return this.contentType;
    }

    public Field getRawField() {
        return this.rawField;
    }

    public boolean isReadOnlyManaged() {
        return this.isReadOnlyManaged;
    }

    @Nullable
    public Method getOnDirtyMethod() {
        return this.onDirtyMethod;
    }

    @Nullable
    public Method getSerializeMethod() {
        return this.serializeMethod;
    }

    @Nullable
    public Method getDeserializeMethod() {
        return this.deserializeMethod;
    }

    public void setPersistentKey(@Nullable String persistentKey) {
        this.persistentKey = persistentKey;
    }

    public void setRedOnlyManaged(Method onDirtyMethod, Method serializeMethod, Method deserializeMethod) {
        this.isReadOnlyManaged = true;
        this.onDirtyMethod = onDirtyMethod;
        this.serializeMethod = serializeMethod;
        this.deserializeMethod = deserializeMethod;
    }

    public ManagedKey(String name, boolean isDestSync, boolean isPersist, boolean isDrop, boolean isLazy, Type contentType, Field rawField) {
        this.name = name;
        this.isDestSync = isDestSync;
        this.isPersist = isPersist;
        this.isDrop = isDrop;
        this.isLazy = isLazy;
        this.contentType = contentType;
        this.rawField = rawField;
    }

    public IAccessor getAccessor() {
        if (this.accessor == null) {
            this.accessor = TypedPayloadRegistries.findByType(this.contentType);
        }
        return this.accessor;
    }

    public ITypedPayload<?> readSyncedField(IRef field, boolean force) {
        return getAccessor().readField(force ? AccessorOp.FORCE_SYNCED : AccessorOp.SYNCED, field);
    }

    public void writeSyncedField(IRef field, ITypedPayload<?> payload) {
        getAccessor().writeField(AccessorOp.SYNCED, field, payload);
    }

    public Tag readPersistedField(IRef field) {
        return getAccessor().readField(AccessorOp.PERSISTED, field).serializeNBT();
    }

    public void writePersistedField(IRef field, @NotNull Tag nbt) {
        byte payloadType = getAccessor().getDefaultType();
        ITypedPayload<?> payload = TypedPayloadRegistries.create(payloadType);
        payload.deserializeNBT(nbt);
        getAccessor().writeField(AccessorOp.PERSISTED, field, payload);
    }

    public IRef createRef(Object instance) {
        try {
            IAccessor accessor = getAccessor();
            if (accessor instanceof IArrayLikeAccessor) {
                IArrayLikeAccessor arrayLikeAccessor = (IArrayLikeAccessor) accessor;
                if (accessor.isManaged() || arrayLikeAccessor.getChildAccessor().isManaged()) {
                    return new ManagedArrayLikeRef(ManagedField.of(this.rawField, instance), this.isLazy).setKey(this);
                }
                try {
                    this.rawField.setAccessible(true);
                    return new ReadonlyArrayRef(this.isLazy, this.rawField.get(instance)).setKey(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (accessor.isManaged()) {
                return ManagedRef.create(ManagedField.of(this.rawField, instance), this.isLazy).setKey(this);
            } else {
                if (isReadOnlyManaged()) {
                    return ManagedRef.create(ReadOnlyManagedField.of(this.rawField, instance, this.onDirtyMethod, this.serializeMethod, this.deserializeMethod), this.isLazy).setKey(this);
                }
                try {
                    this.rawField.setAccessible(true);
                    return new ReadonlyRef(this.isLazy, this.rawField.get(instance)).setKey(this);
                } catch (IllegalAccessException e2) {
                    throw new RuntimeException(e2);
                }
            }
        } catch (Exception e3) {
            throw new IllegalStateException("Failed to create ref of " + this.name + " with type:" + this.rawField.getType().getCanonicalName(), e3);
        }
        throw new IllegalStateException("Failed to create ref of " + this.name + " with type:" + this.rawField.getType().getCanonicalName(), e3);
    }
}
