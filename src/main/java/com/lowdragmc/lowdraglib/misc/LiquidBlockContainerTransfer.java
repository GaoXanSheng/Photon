package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/LiquidBlockContainerTransfer.class */
public class LiquidBlockContainerTransfer implements IFluidTransfer {
    protected final LiquidBlockContainer liquidContainer;
    protected final Level world;
    protected final BlockPos blockPos;

    public LiquidBlockContainerTransfer(LiquidBlockContainer liquidContainer, Level world, BlockPos blockPos) {
        this.liquidContainer = liquidContainer;
        this.world = world;
        this.blockPos = blockPos;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public int getTanks() {
        return 1;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @Nonnull
    public FluidStack getFluidInTank(int tank) {
        return FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        fill(0, fluidStack, false, false);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long getTankCapacity(int tank) {
        return 2147483647L;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @Nonnull
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        return FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        if (resource.getAmount() >= FluidHelper.getBucket()) {
            BlockState state = this.world.m_8055_(this.blockPos);
            if (this.liquidContainer.m_6044_(this.world, this.blockPos, state, resource.getFluid())) {
                if (simulate || this.liquidContainer.m_7361_(this.world, this.blockPos, state, resource.getFluid().m_76145_())) {
                    return FluidHelper.getBucket();
                }
                return 0L;
            }
            return 0L;
        }
        return 0L;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsFill(int tank) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsDrain(int tank) {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public Object createSnapshot() {
        return this.world.m_8055_(this.blockPos);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void restoreFromSnapshot(Object snapshot) {
        if (snapshot instanceof BlockState) {
            BlockState state = (BlockState) snapshot;
            this.world.m_46597_(this.blockPos, state);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/LiquidBlockContainerTransfer$BlockWrapper.class */
    public static class BlockWrapper implements IFluidTransfer {
        protected final BlockState state;
        protected final Level world;
        protected final BlockPos blockPos;

        public BlockWrapper(BlockState state, Level world, BlockPos blockPos) {
            this.state = state;
            this.world = world;
            this.blockPos = blockPos;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public int getTanks() {
            return 1;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        @Nonnull
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.empty();
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
            fill(0, fluidStack, false, false);
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public long getTankCapacity(int tank) {
            return 2147483647L;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return true;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        @Nonnull
        public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
            return FluidStack.empty();
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
            if (resource.getAmount() < FluidHelper.getBucket()) {
                return 0L;
            }
            if (!simulate) {
                FluidTransferHelper.destroyBlockOnFluidPlacement(this.world, this.blockPos);
                this.world.m_7731_(this.blockPos, this.state, 11);
            }
            return FluidHelper.getBucket();
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public boolean supportsFill(int tank) {
            return true;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public boolean supportsDrain(int tank) {
            return false;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        @NotNull
        public Object createSnapshot() {
            return this.state;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public void restoreFromSnapshot(Object snapshot) {
            this.world.m_46597_(this.blockPos, this.state);
        }
    }
}
