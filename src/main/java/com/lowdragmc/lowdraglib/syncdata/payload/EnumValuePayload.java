package com.lowdragmc.lowdraglib.syncdata.payload;

import javax.annotation.Nullable;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/EnumValuePayload.class */
public class EnumValuePayload extends ObjectTypedPayload<Payload> {

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/EnumValuePayload$Payload.class */
    public static class Payload {
        public int ordinal = -1;
        public String name;
    }

    public EnumValuePayload() {
        setPayload(new Payload());
    }

    public static EnumValuePayload of(String name, int ordinal) {
        EnumValuePayload payload = new EnumValuePayload();
        payload.getPayload().name = name;
        payload.getPayload().ordinal = ordinal;
        return payload;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        if (((Payload) this.payload).ordinal == -1) {
            throw new IllegalStateException("Did not find ordinal for enum");
        }
        buf.m_130130_(((Payload) this.payload).ordinal);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        ((Payload) this.payload).ordinal = buf.m_130242_();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    @Nullable
    public Tag serializeNBT() {
        if (((Payload) this.payload).name == null) {
            throw new IllegalStateException("Did not find name for enum");
        }
        return StringTag.m_129297_(((Payload) this.payload).name);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag tag) {
        if (tag instanceof IntTag) {
            ((Payload) this.payload).ordinal = ((IntTag) tag).m_7047_();
            return;
        }
        ((Payload) this.payload).name = tag.m_7916_();
    }
}
