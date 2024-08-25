package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ReadOnlyManagedField;
import com.lowdragmc.lowdraglib.syncdata.managed.ReadOnlyManagedRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/ReadonlyAccessor.class */
public abstract class ReadonlyAccessor implements IAccessor {
    private byte defaultType = -1;

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public abstract ITypedPayload<?> readFromReadonlyField(AccessorOp accessorOp, Object obj);

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public abstract void writeToReadonlyField(AccessorOp accessorOp, Object obj, ITypedPayload<?> iTypedPayload);

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public byte getDefaultType() {
        return this.defaultType;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void setDefaultType(byte defaultType) {
        this.defaultType = defaultType;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean isManaged() {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readField(AccessorOp op, IRef field) {
        Object obj = field.readRaw();
        if (field instanceof ReadOnlyManagedRef) {
            ReadOnlyManagedRef managedRef = (ReadOnlyManagedRef) field;
            if (obj == null) {
                return PrimitiveTypedPayload.NullPayload.ofNull();
            }
            CompoundTag tag = new CompoundTag();
            tag.m_128365_("uid", managedRef.getReadOnlyField().serializeUid(obj));
            CompoundTag payloadTag = new CompoundTag();
            ITypedPayload<?> payload = readFromReadonlyField(op, obj);
            payloadTag.m_128344_("t", payload.getType());
            Tag data = payload.serializeNBT();
            if (data != null) {
                payloadTag.m_128365_("d", data);
            }
            tag.m_128365_("payload", payloadTag);
            return NbtTagPayload.of(tag);
        } else if (obj == null) {
            throw new IllegalArgumentException("readonly field %s has a null reference".formatted(new Object[]{field.getKey()}));
        } else {
            return readFromReadonlyField(op, obj);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeField(AccessorOp op, IRef field, ITypedPayload<?> payload) {
        Object obj = field.readRaw();
        if (field instanceof ReadOnlyManagedRef) {
            ReadOnlyManagedRef managedRef = (ReadOnlyManagedRef) field;
            ReadOnlyManagedField readOnlyField = managedRef.getReadOnlyField();
            if (payload instanceof PrimitiveTypedPayload) {
                PrimitiveTypedPayload<?> primitive = (PrimitiveTypedPayload) payload;
                if (primitive.isNull()) {
                    readOnlyField.set(null);
                    return;
                }
            }
            if (payload instanceof NbtTagPayload) {
                NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
                CompoundTag payload2 = nbtTagPayload.getPayload();
                if (payload2 instanceof CompoundTag) {
                    CompoundTag tag = payload2;
                    CompoundTag uid = tag.m_128469_("uid");
                    if (obj == null || !readOnlyField.serializeUid(obj).equals(uid)) {
                        obj = managedRef.getReadOnlyField().deserializeUid(uid);
                        readOnlyField.set(obj);
                    }
                    CompoundTag payloadTag = tag.m_128469_("payload");
                    byte id = payloadTag.m_128445_("t");
                    ITypedPayload<?> p = TypedPayloadRegistries.create(id);
                    p.deserializeNBT(payloadTag.m_128423_("d"));
                    writeToReadonlyField(op, obj, p);
                }
            }
        } else if (obj == null) {
            throw new IllegalArgumentException("readonly field %s has a null reference".formatted(new Object[]{field}));
        } else {
            writeToReadonlyField(op, obj, payload);
        }
    }
}
