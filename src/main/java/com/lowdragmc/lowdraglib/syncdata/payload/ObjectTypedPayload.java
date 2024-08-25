package com.lowdragmc.lowdraglib.syncdata.payload;

import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/ObjectTypedPayload.class */
public abstract class ObjectTypedPayload<T> implements ITypedPayload<T> {
    protected T payload;

    public ITypedPayload<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public byte getType() {
        return TypedPayloadRegistries.getId(getClass());
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public T getPayload() {
        return this.payload;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public boolean isPrimitive() {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        CompoundTag serializeNBT = serializeNBT();
        if (serializeNBT instanceof CompoundTag) {
            buf.writeBoolean(true);
            buf.m_130079_(serializeNBT);
            return;
        }
        buf.writeBoolean(false);
        CompoundTag root = new CompoundTag();
        if (serializeNBT != null) {
            root.m_128365_("d", serializeNBT);
        }
        buf.m_130079_(root);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            CompoundTag nbt = buf.m_130260_();
            deserializeNBT(nbt);
            return;
        }
        CompoundTag root = buf.m_130260_();
        Objects.requireNonNull(root);
        Tag nbt2 = root.m_128423_("d");
        deserializeNBT(nbt2);
    }
}
