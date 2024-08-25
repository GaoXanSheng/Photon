package com.lowdragmc.lowdraglib.networking.s2c;

import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/s2c/SPacketUIOpen.class */
public class SPacketUIOpen implements IPacket {
    private ResourceLocation uiFactoryId;
    private FriendlyByteBuf serializedHolder;
    private int windowId;

    public SPacketUIOpen() {
    }

    public SPacketUIOpen(ResourceLocation uiFactoryId, FriendlyByteBuf serializedHolder, int windowId) {
        this.uiFactoryId = uiFactoryId;
        this.serializedHolder = serializedHolder;
        this.windowId = windowId;
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void encode(FriendlyByteBuf buf) {
        buf.m_130130_(this.serializedHolder.readableBytes());
        buf.writeBytes(this.serializedHolder);
        buf.m_130085_(this.uiFactoryId);
        buf.m_130130_(this.windowId);
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void decode(FriendlyByteBuf buf) {
        ByteBuf directSliceBuffer = buf.readBytes(buf.m_130242_());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        this.serializedHolder = new FriendlyByteBuf(copiedDataBuffer);
        this.uiFactoryId = buf.m_130281_();
        this.windowId = buf.m_130242_();
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        UIFactory<?> uiFactory;
        if (handler.isClient() && (uiFactory = UIFactory.FACTORIES.get(this.uiFactoryId)) != null) {
            uiFactory.initClientUI(this.serializedHolder, this.windowId);
        }
    }
}
