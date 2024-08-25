package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/ITagSerializableAccessor.class */
public class ITagSerializableAccessor extends ReadonlyAccessor {
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean hasPredicate() {
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
    public boolean test(Class<?> type) {
        return ITagSerializable.class.isAssignableFrom(type);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj) {
        if (!(obj instanceof ITagSerializable)) {
            throw new IllegalArgumentException("Field %s is not ITagSerializable".formatted(new Object[]{obj}));
        }
        ITagSerializable<?> serializable = (ITagSerializable) obj;
        Tag nbt = serializable.mo129serializeNBT();
        return new NbtTagPayload().setPayload(nbt);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload) {
        if (!(obj instanceof ITagSerializable)) {
            throw new IllegalArgumentException("Field %s is not ITagSerializable".formatted(new Object[]{obj}));
        }
        if (!(payload instanceof NbtTagPayload)) {
            throw new IllegalArgumentException("Payload %s is not NbtTagPayload".formatted(new Object[]{payload}));
        }
        NbtTagPayload nbtPayload = (NbtTagPayload) payload;
        ((ITagSerializable) obj).deserializeNBT(nbtPayload.getPayload());
    }
}
