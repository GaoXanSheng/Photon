package com.lowdragmc.lowdraglib.utils;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/BlockInfo.class */
public class BlockInfo {
    public static final BlockInfo EMPTY = new BlockInfo(Blocks.f_50016_);
    private BlockState blockState;
    private CompoundTag tag;
    private boolean hasBlockEntity;
    private final ItemStack itemStack;
    private BlockEntity lastEntity;
    private final Consumer<BlockEntity> postCreate;

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public void setTag(CompoundTag tag) {
        this.tag = tag;
    }

    public void setHasBlockEntity(boolean hasBlockEntity) {
        this.hasBlockEntity = hasBlockEntity;
    }

    public BlockInfo(Block block) {
        this(block.m_49966_());
    }

    public BlockInfo(BlockState blockState) {
        this(blockState, false);
    }

    public BlockInfo(BlockState blockState, boolean hasBlockEntity) {
        this(blockState, hasBlockEntity, null, null);
    }

    public BlockInfo(BlockState blockState, Consumer<BlockEntity> postCreate) {
        this(blockState, true, null, null);
    }

    public BlockInfo(BlockState blockState, boolean hasBlockEntity, ItemStack itemStack, Consumer<BlockEntity> postCreate) {
        this.blockState = blockState;
        this.hasBlockEntity = hasBlockEntity;
        this.itemStack = itemStack;
        this.postCreate = postCreate;
    }

    public static BlockInfo fromBlockState(BlockState state) {
        try {
            if (state.m_60734_() instanceof EntityBlock) {
                BlockEntity blockEntity = state.m_60734_().m_142194_(BlockPos.f_121853_, state);
                if (blockEntity != null) {
                    return new BlockInfo(state, true);
                }
            }
        } catch (Exception e) {
        }
        return new BlockInfo(state);
    }

    public static BlockInfo fromBlock(Block block) {
        return fromBlockState(block.m_49966_());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public boolean hasBlockEntity() {
        return this.hasBlockEntity;
    }

    public BlockEntity getBlockEntity(BlockPos pos) {
        if (this.hasBlockEntity) {
            EntityBlock m_60734_ = this.blockState.m_60734_();
            if (m_60734_ instanceof EntityBlock) {
                EntityBlock entityBlock = m_60734_;
                if (this.lastEntity != null && this.lastEntity.m_58899_().equals(pos)) {
                    return this.lastEntity;
                }
                this.lastEntity = entityBlock.m_142194_(pos, this.blockState);
                if (this.tag != null && this.lastEntity != null) {
                    CompoundTag compoundTag2 = this.lastEntity.m_187482_();
                    CompoundTag compoundTag3 = compoundTag2.m_6426_();
                    compoundTag2.m_128391_(this.tag);
                    if (!compoundTag2.equals(compoundTag3)) {
                        this.lastEntity.m_142466_(compoundTag2);
                    }
                }
                if (this.postCreate != null) {
                    this.postCreate.accept(this.lastEntity);
                }
                return this.lastEntity;
            }
            return null;
        }
        return null;
    }

    public BlockEntity getBlockEntity(Level level, BlockPos pos) {
        BlockEntity entity = getBlockEntity(pos);
        if (entity != null) {
            entity.m_142339_(level);
        }
        return entity;
    }

    public ItemStack getItemStackForm() {
        return this.itemStack == null ? new ItemStack(this.blockState.m_60734_()) : this.itemStack;
    }

    public ItemStack getItemStackForm(BlockAndTintGetter level, BlockPos pos) {
        return this.itemStack != null ? this.itemStack : this.blockState.m_60734_().m_7397_(new FacadeBlockAndTintGetter(level, pos, this.blockState, null), pos, this.blockState);
    }

    public void apply(Level world, BlockPos pos) {
        world.m_46597_(pos, this.blockState);
        BlockEntity blockEntity = getBlockEntity(pos);
        if (blockEntity != null) {
            world.m_151523_(blockEntity);
        }
    }

    public void clearBlockEntityCache() {
        this.lastEntity = null;
    }
}
