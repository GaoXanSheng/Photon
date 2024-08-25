package com.lowdragmc.lowdraglib.syncdata.payload;

import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/ArrayPayload.class */
public class ArrayPayload extends ObjectTypedPayload<ITypedPayload<?>[]> {
    public static ArrayPayload of(ITypedPayload<?>[] payload) {
        return (ArrayPayload) new ArrayPayload().setPayload(payload);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    @Nullable
    public Tag serializeNBT() {
        ITypedPayload<?>[] payload;
        ListTag list = new ListTag();
        for (ITypedPayload<?> payload2 : getPayload()) {
            CompoundTag compound = new CompoundTag();
            compound.m_128344_("t", payload2.getType());
            Tag nbt = payload2.serializeNBT();
            if (nbt != null) {
                compound.m_128365_("p", nbt);
            }
            list.add(compound);
        }
        return list;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [T, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload[]] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void deserializeNBT(Tag input) {
        if (!(input instanceof ListTag)) {
            throw new IllegalArgumentException("Tag %s is not ListTag".formatted(new Object[]{input}));
        }
        ListTag list = (ListTag) input;
        this.payload = new ITypedPayload[list.size()];
        for (int i = 0; i < list.size(); i++) {
            CompoundTag compoundTag = list.get(i);
            if (!(compoundTag instanceof CompoundTag)) {
                throw new IllegalArgumentException("Tag %s is not CompoundTag".formatted(new Object[]{compoundTag}));
            }
            CompoundTag compound = compoundTag;
            byte type = compound.m_128445_("t");
            Tag tag = compound.m_128423_("p");
            ((ITypedPayload[]) this.payload)[i] = TypedPayloadRegistries.create(type);
            ((ITypedPayload[]) this.payload)[i].deserializeNBT(tag);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void writePayload(FriendlyByteBuf buf) {
        ITypedPayload<?>[] iTypedPayloadArr;
        buf.m_130130_(((ITypedPayload[]) this.payload).length);
        for (ITypedPayload<?> payload : (ITypedPayload[]) this.payload) {
            buf.writeByte(payload.getType());
            payload.writePayload(buf);
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [T, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload[]] */
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public void readPayload(FriendlyByteBuf buf) {
        this.payload = new ITypedPayload[buf.m_130242_()];
        for (int i = 0; i < ((ITypedPayload[]) this.payload).length; i++) {
            byte type = buf.readByte();
            ((ITypedPayload[]) this.payload)[i] = TypedPayloadRegistries.create(type);
            ((ITypedPayload[]) this.payload)[i].readPayload(buf);
        }
    }
}
