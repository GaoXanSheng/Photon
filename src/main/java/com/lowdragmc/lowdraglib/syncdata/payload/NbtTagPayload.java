package com.lowdragmc.lowdraglib.syncdata.payload;

import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/NbtTagPayload.class */
public class NbtTagPayload extends ObjectTypedPayload<Tag> {
    public static ITypedPayload<?> of(Tag tag) {
        NbtTagPayload payload = new NbtTagPayload();
        payload.setPayload(tag);
        return payload;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public Tag serializeNBT() {
        return (Tag) this.payload;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag tag) {
        this.payload = tag;
    }
}
