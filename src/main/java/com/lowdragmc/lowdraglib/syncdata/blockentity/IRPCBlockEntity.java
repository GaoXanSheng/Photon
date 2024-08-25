package com.lowdragmc.lowdraglib.syncdata.blockentity;

import com.lowdragmc.lowdraglib.networking.LDLNetworking;
import com.lowdragmc.lowdraglib.networking.both.PacketRPCMethodPayload;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.field.RPCMethodMeta;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/blockentity/IRPCBlockEntity.class */
public interface IRPCBlockEntity extends IManagedBlockEntity {
    @Nullable
    default RPCMethodMeta getRPCMethod(IManaged managed, String methodName) {
        return managed.getFieldHolder().getRpcMethodMap().get(methodName);
    }

    default PacketRPCMethodPayload generateRpcPacket(IManaged managed, String methodName, Object... args) {
        return PacketRPCMethodPayload.of(managed, this, methodName, args);
    }

    @OnlyIn(Dist.CLIENT)
    default void rpcToServer(IManaged managed, String methodName, Object... args) {
        PacketRPCMethodPayload packet = generateRpcPacket(managed, methodName, args);
        LDLNetworking.NETWORK.sendToServer(packet);
    }

    default void rpcToPlayer(IManaged managed, ServerPlayer player, String methodName, Object... args) {
        PacketRPCMethodPayload packet = generateRpcPacket(managed, methodName, args);
        LDLNetworking.NETWORK.sendToPlayer(packet, player);
    }

    default void rpcToTracking(IManaged managed, ServerPlayer player, String methodName, Object... args) {
        PacketRPCMethodPayload packet = generateRpcPacket(managed, methodName, args);
        LDLNetworking.NETWORK.sendToTrackingChunk(packet, getSelf().m_58904_().m_46745_(getSelf().m_58899_()));
    }
}
