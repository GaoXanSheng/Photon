package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/FluidBlockTransfer.class */
public class FluidBlockTransfer implements IFluidTransfer {
    protected final LiquidBlock fluidBlock;
    protected final BlockState blockState;
    protected final Level world;
    protected final BlockPos blockPos;

    public FluidBlockTransfer(LiquidBlock fluidBlock, Level world, BlockPos blockPos) {
        this.fluidBlock = fluidBlock;
        this.world = world;
        this.blockPos = blockPos;
        this.blockState = world.m_8055_(blockPos);
    }

    public Fluid getFluid() {
        return this.fluidBlock.m_5888_(this.blockState).m_76152_();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public int getTanks() {
        return 1;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack getFluidInTank(int tank) {
        return tank == 0 ? FluidStack.create(getFluid(), FluidHelper.getBucket()) : FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long getTankCapacity(int tank) {
        return FluidHelper.getBucket();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return stack.getFluid() == getFluid();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        return 0L;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        if (!resource.isEmpty() && resource.getFluid() == getFluid() && resource.getAmount() >= getTankCapacity(0)) {
            FluidStack drained = getFluidInTank(0).copy();
            if (!simulate) {
                this.world.m_46597_(this.blockPos, Blocks.f_50016_.m_49966_());
            }
            return drained;
        }
        return FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsFill(int tank) {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsDrain(int tank) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public Object createSnapshot() {
        return new Object();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void restoreFromSnapshot(Object snapshot) {
    }
}
