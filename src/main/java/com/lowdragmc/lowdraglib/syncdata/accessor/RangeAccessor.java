package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.lowdraglib.utils.Range;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/RangeAccessor.class */
public class RangeAccessor extends CustomObjectAccessor<Range> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ Range deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public RangeAccessor() {
        super(Range.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, Range value) {
        CompoundTag tag = new CompoundTag();
        if ((value.getA() instanceof Float) || (value.getA() instanceof Double)) {
            tag.m_128350_("a", value.getA().floatValue());
        } else {
            tag.m_128405_("a", value.getA().intValue());
        }
        if ((value.getB() instanceof Float) || (value.getB() instanceof Double)) {
            tag.m_128350_("b", value.getB().floatValue());
        } else {
            tag.m_128405_("b", value.getB().intValue());
        }
        return NbtTagPayload.of(tag);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public Range deserialize(AccessorOp op, ITypedPayload<?> payload) {
        Number a;
        Number b;
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag payload2 = nbtTagPayload.getPayload();
            if (payload2 instanceof CompoundTag) {
                CompoundTag tag = payload2;
                if (tag.m_128425_("a", 3)) {
                    a = Integer.valueOf(tag.m_128451_("a"));
                } else {
                    a = Float.valueOf(tag.m_128457_("a"));
                }
                if (tag.m_128425_("b", 3)) {
                    b = Integer.valueOf(tag.m_128451_("b"));
                } else {
                    b = Float.valueOf(tag.m_128457_("b"));
                }
                return new Range(a, b);
            }
            return null;
        }
        return null;
    }
}
