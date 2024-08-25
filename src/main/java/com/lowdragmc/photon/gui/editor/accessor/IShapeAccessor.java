package com.lowdragmc.photon.gui.editor.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.photon.client.emitter.data.shape.IShape;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/accessor/IShapeAccessor.class */
public class IShapeAccessor extends CustomObjectAccessor<IShape> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ IShape deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public IShapeAccessor() {
        super(IShape.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, IShape value) {
        return NbtTagPayload.of(IShape.serializeWrapper(value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public IShape deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag payload2 = nbtTagPayload.getPayload();
            if (payload2 instanceof CompoundTag) {
                CompoundTag tag = payload2;
                return IShape.deserializeWrapper(tag);
            }
            return null;
        }
        return null;
    }
}
