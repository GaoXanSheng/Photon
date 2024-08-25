package com.lowdragmc.lowdraglib.side.fluid;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/IFluidStorage.class */
public interface IFluidStorage extends IFluidTransfer {
    @Nonnull
    FluidStack getFluid();

    void setFluid(FluidStack fluidStack);

    long getCapacity();

    boolean isFluidValid(FluidStack fluidStack);

    default long getFluidAmount() {
        return getFluid().getAmount();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    default int getTanks() {
        return 1;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    default FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    default void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        setFluid(fluidStack);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    default long getTankCapacity(int tank) {
        return getCapacity();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    default boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }
}
