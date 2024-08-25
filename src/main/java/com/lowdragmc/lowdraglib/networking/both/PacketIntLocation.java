package com.lowdragmc.lowdraglib.networking.both;

import com.lowdragmc.lowdraglib.networking.IPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/both/PacketIntLocation.class */
public abstract class PacketIntLocation implements IPacket {
    protected BlockPos pos;

    public PacketIntLocation() {
    }

    public PacketIntLocation(BlockPos pos) {
        this.pos = pos;
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void encode(FriendlyByteBuf buf) {
        buf.m_130064_(this.pos);
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void decode(FriendlyByteBuf buf) {
        this.pos = buf.m_130135_();
    }
}
