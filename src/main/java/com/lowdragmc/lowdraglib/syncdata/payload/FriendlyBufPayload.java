package com.lowdragmc.lowdraglib.syncdata.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/FriendlyBufPayload.class */
public class FriendlyBufPayload extends ObjectTypedPayload<FriendlyByteBuf> {
    public static ITypedPayload<?> of(FriendlyByteBuf buf) {
        FriendlyBufPayload payload = new FriendlyBufPayload();
        payload.setPayload(buf);
        return payload;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        buf.m_130130_(buf.readableBytes());
        buf.writeBytes(buf);
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, net.minecraft.network.FriendlyByteBuf] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        ByteBuf directSliceBuffer = buf.readBytes(buf.m_130242_());
        ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(directSliceBuffer);
        directSliceBuffer.release();
        this.payload = new FriendlyByteBuf(copiedDataBuffer);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public Tag serializeNBT() {
        return new ByteArrayTag(Arrays.copyOfRange(((FriendlyByteBuf) this.payload).array(), 0, ((FriendlyByteBuf) this.payload).writerIndex()));
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [T, net.minecraft.network.FriendlyByteBuf] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag buf) {
        if (buf instanceof ByteArrayTag) {
            ByteArrayTag byteTags = (ByteArrayTag) buf;
            this.payload = new FriendlyByteBuf(Unpooled.copiedBuffer(byteTags.m_128227_()));
        }
    }
}
