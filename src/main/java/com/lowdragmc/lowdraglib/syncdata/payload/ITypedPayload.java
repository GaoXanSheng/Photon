package com.lowdragmc.lowdraglib.syncdata.payload;

import javax.annotation.Nullable;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/ITypedPayload.class */
public interface ITypedPayload<T> {
    byte getType();

    void writePayload(FriendlyByteBuf friendlyByteBuf);

    void readPayload(FriendlyByteBuf friendlyByteBuf);

    @Nullable
    Tag serializeNBT();

    void deserializeNBT(Tag tag);

    T getPayload();

    boolean isPrimitive();
}
