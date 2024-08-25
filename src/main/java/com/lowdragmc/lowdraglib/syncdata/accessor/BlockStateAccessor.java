package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/BlockStateAccessor.class */
public class BlockStateAccessor extends CustomObjectAccessor<BlockState> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ BlockState deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public BlockStateAccessor() {
        super(BlockState.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, BlockState value) {
        return NbtTagPayload.of(NbtUtils.m_129202_(value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public BlockState deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            return NbtUtils.m_129241_(nbtTagPayload.getPayload());
        }
        return null;
    }
}
