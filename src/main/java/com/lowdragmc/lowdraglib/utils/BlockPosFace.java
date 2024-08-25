package com.lowdragmc.lowdraglib.utils;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/BlockPosFace.class */
public class BlockPosFace {
    public final Direction facing;
    public final BlockPos pos;

    public BlockPosFace(BlockPos pos, Direction facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public boolean equals(@Nullable Object bp) {
        if (bp instanceof BlockPosFace) {
            return this.pos.equals(((BlockPosFace) bp).pos) && ((BlockPosFace) bp).facing == this.facing;
        }
        return super.equals(bp);
    }

    public int hashCode() {
        return this.pos.hashCode() + (this.facing == null ? 0 : this.facing.hashCode());
    }
}
