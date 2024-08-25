package com.lowdragmc.lowdraglib.syncdata.payload;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/BlockPosPayload.class */
public class BlockPosPayload extends ObjectTypedPayload<BlockPos> {
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        buf.m_130064_((BlockPos) this.payload);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, net.minecraft.core.BlockPos] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        this.payload = buf.m_130135_();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public Tag serializeNBT() {
        return NbtUtils.m_129224_((BlockPos) this.payload);
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, net.minecraft.core.BlockPos] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag tag) {
        this.payload = NbtUtils.m_129239_((CompoundTag) tag);
    }
}
