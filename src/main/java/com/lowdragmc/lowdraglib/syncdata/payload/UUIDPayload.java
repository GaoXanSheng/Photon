package com.lowdragmc.lowdraglib.syncdata.payload;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/UUIDPayload.class */
public class UUIDPayload extends ObjectTypedPayload<UUID> {
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        buf.m_130077_((UUID) this.payload);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.util.UUID] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        this.payload = buf.m_130259_();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    @Nullable
    public Tag serializeNBT() {
        return NbtUtils.m_129226_((UUID) this.payload);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, java.util.UUID] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag tag) {
        this.payload = NbtUtils.m_129233_(tag);
    }
}
