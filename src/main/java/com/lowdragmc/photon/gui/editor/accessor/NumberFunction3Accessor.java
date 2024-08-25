package com.lowdragmc.photon.gui.editor.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/accessor/NumberFunction3Accessor.class */
public class NumberFunction3Accessor extends CustomObjectAccessor<NumberFunction3> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ NumberFunction3 deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public NumberFunction3Accessor() {
        super(NumberFunction3.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, NumberFunction3 value) {
        CompoundTag tag = new CompoundTag();
        tag.m_128365_("x", NumberFunction.serializeWrapper(value.x));
        tag.m_128365_("y", NumberFunction.serializeWrapper(value.y));
        tag.m_128365_("z", NumberFunction.serializeWrapper(value.z));
        return NbtTagPayload.of(tag);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public NumberFunction3 deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag payload2 = nbtTagPayload.getPayload();
            if (payload2 instanceof CompoundTag) {
                CompoundTag tag = payload2;
                if (tag.m_128435_("x") == 5) {
                    return new NumberFunction3(Float.valueOf(tag.m_128457_("x")), Float.valueOf(tag.m_128457_("y")), Float.valueOf(tag.m_128457_("z")));
                }
                return new NumberFunction3(NumberFunction.deserializeWrapper(tag.m_128469_("x")), NumberFunction.deserializeWrapper(tag.m_128469_("y")), NumberFunction.deserializeWrapper(tag.m_128469_("z")));
            }
            return null;
        }
        return null;
    }
}
