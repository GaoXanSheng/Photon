package com.lowdragmc.lowdraglib.syncdata;

import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/IBytebufSerializable.class */
public interface IBytebufSerializable {
    void toBytes(FriendlyByteBuf friendlyByteBuf);

    void fromBytes(FriendlyByteBuf friendlyByteBuf);
}
