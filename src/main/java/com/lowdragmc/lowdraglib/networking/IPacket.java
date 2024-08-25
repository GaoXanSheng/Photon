package com.lowdragmc.lowdraglib.networking;

import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/IPacket.class */
public interface IPacket {
    void encode(FriendlyByteBuf friendlyByteBuf);

    void decode(FriendlyByteBuf friendlyByteBuf);

    default void execute(IHandlerContext handler) {
    }
}
