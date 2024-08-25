package com.lowdragmc.lowdraglib.syncdata.payload;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/ItemStackPayload.class */
public class ItemStackPayload extends ObjectTypedPayload<ItemStack> {
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        buf.m_130055_((ItemStack) this.payload);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, net.minecraft.world.item.ItemStack] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        this.payload = buf.m_130267_();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public Tag serializeNBT() {
        return ((ItemStack) this.payload).m_41739_(new CompoundTag());
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, net.minecraft.world.item.ItemStack] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag tag) {
        this.payload = ItemStack.m_41712_((CompoundTag) tag);
    }
}
