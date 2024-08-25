package com.lowdragmc.lowdraglib.utils.virtual;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/virtual/VirtualChunkSection.class */
public class VirtualChunkSection extends LevelChunkSection {
    public VirtualChunk owner;
    public final int xStart;
    public final int yStart;
    public final int zStart;

    public VirtualChunkSection(VirtualChunk owner, int yBase) {
        super(yBase, (Registry) owner.world.m_5962_().m_6632_(Registry.f_122885_).orElseThrow());
        this.owner = owner;
        this.xStart = owner.m_7697_().m_45604_();
        this.yStart = yBase;
        this.zStart = owner.m_7697_().m_45605_();
    }

    public BlockState m_62982_(int x, int y, int z) {
        return this.owner.world.getBlockState(x + this.xStart, y + this.yStart, z + this.zStart);
    }

    public BlockState m_62991_(int p_177484_1_, int p_177484_2_, int p_177484_3_, BlockState p_177484_4_, boolean p_177484_5_) {
        throw new IllegalStateException("Chunk sections should not be mutated in a fake world.");
    }
}
