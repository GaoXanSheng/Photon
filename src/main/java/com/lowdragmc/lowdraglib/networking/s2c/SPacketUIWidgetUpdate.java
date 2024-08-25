package com.lowdragmc.lowdraglib.networking.s2c;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/s2c/SPacketUIWidgetUpdate.class */
public class SPacketUIWidgetUpdate implements IPacket {
    public int windowId;
    public FriendlyByteBuf updateData;

    public SPacketUIWidgetUpdate() {
    }

    public SPacketUIWidgetUpdate(int windowId, FriendlyByteBuf updateData) {
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
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            ModularUIGuiContainer modularUIGuiContainer = Minecraft.m_91087_().f_91080_;
            if (modularUIGuiContainer instanceof ModularUIGuiContainer) {
                modularUIGuiContainer.handleWidgetUpdate(this);
            }
        }
    }
}
