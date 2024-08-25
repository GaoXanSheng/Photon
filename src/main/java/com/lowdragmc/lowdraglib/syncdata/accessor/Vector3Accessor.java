package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.lowdraglib.utils.Vector3;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/Vector3Accessor.class */
public class Vector3Accessor extends CustomObjectAccessor<Vector3> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ Vector3 deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public Vector3Accessor() {
        super(Vector3.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, Vector3 value) {
        CompoundTag tag = new CompoundTag();
        tag.m_128347_("x", value.x);
        tag.m_128347_("y", value.y);
        tag.m_128347_("z", value.z);
        return NbtTagPayload.of(tag);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public Vector3 deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag tag = nbtTagPayload.getPayload();
            return new Vector3(tag.m_128459_("x"), tag.m_128459_("y"), tag.m_128459_("z"));
        }
        return null;
    }
}
