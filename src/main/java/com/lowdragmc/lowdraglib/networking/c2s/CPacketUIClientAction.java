package com.lowdragmc.lowdraglib.networking.c2s;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/c2s/CPacketUIClientAction.class */
public class CPacketUIClientAction implements IPacket {
    public int windowId;
    public FriendlyByteBuf updateData;

    public CPacketUIClientAction() {
    }

    public CPacketUIClientAction(int windowId, FriendlyByteBuf updateData) {
        this.windowId = windowId;
        this.updateData = updateData;
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void encode(FriendlyByteBuf buf) {
        buf.m_130130_(this.updateData.readableBytes());
        buf.writeBytes(this.updateData);
        buf.m_130130_(this.windowId);
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void decode(FriendlyByteBuf buf) {
        ByteBuf directSliceBuffer = buf.readBytes(buf.m_130242_());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        this.updateData = new FriendlyByteBuf(copiedDataBuffer);
        this.windowId = buf.m_130242_();
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void execute(IHandlerContext handler) {
        if (handler.getPlayer() != null) {
            AbstractContainerMenu openContainer = handler.getPlayer().f_36096_;
            if (openContainer instanceof ModularUIContainer) {
                ((ModularUIContainer) openContainer).handleClientAction(this);
            }
        }
    }
}
