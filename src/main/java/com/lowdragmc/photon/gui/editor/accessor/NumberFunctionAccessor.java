package com.lowdragmc.photon.gui.editor.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/accessor/NumberFunctionAccessor.class */
public class NumberFunctionAccessor extends CustomObjectAccessor<NumberFunction> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ NumberFunction deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public NumberFunctionAccessor() {
        super(NumberFunction.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, NumberFunction value) {
        return NbtTagPayload.of(NumberFunction.serializeWrapper(value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public NumberFunction deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag payload2 = nbtTagPayload.getPayload();
            if (payload2 instanceof CompoundTag) {
                CompoundTag tag = payload2;
                return NumberFunction.deserializeWrapper(tag);
            }
            return null;
        }
        return null;
    }
}
