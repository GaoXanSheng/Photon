package com.lowdragmc.lowdraglib.syncdata.payload;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/FluidStackPayload.class */
public class FluidStackPayload extends ObjectTypedPayload<FluidStack> {
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        ((FluidStack) this.payload).writeToBuf(buf);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, com.lowdragmc.lowdraglib.side.fluid.FluidStack] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        this.payload = FluidStack.readFromBuf(buf);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public Tag serializeNBT() {
        return ((FluidStack) this.payload).saveToTag(new CompoundTag());
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, com.lowdragmc.lowdraglib.side.fluid.FluidStack] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag tag) {
        this.payload = FluidStack.loadFromTag((CompoundTag) tag);
    }
}
