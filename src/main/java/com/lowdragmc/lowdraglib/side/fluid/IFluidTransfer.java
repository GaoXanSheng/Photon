package com.lowdragmc.lowdraglib.side.fluid;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/IFluidTransfer.class */
public interface IFluidTransfer {
    public static final IFluidTransfer EMPTY = new IFluidTransfer() { // from class: com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer.1
        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public int getTanks() {
            return 0;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        @NotNull
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.empty();
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public long getTankCapacity(int tank) {
            return 0L;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return false;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
            return 0L;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public boolean supportsFill(int tank) {
            return false;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        @NotNull
        public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
            return FluidStack.empty();
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public boolean supportsDrain(int tank) {
            return false;
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        @NotNull
        public Object createSnapshot() {
            return new Object();
        }

        @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
        public void restoreFromSnapshot(Object snapshot) {
        }
    };

    int getTanks();

    @Nonnull
    FluidStack getFluidInTank(int i);

    @ApiStatus.Internal
    void setFluidInTank(int i, @Nonnull FluidStack fluidStack);

    long getTankCapacity(int i);

    boolean isFluidValid(int i, @Nonnull FluidStack fluidStack);

    @ApiStatus.Internal
    long fill(int i, FluidStack fluidStack, boolean z, boolean z2);

    boolean supportsFill(int i);

    @Nonnull
    @ApiStatus.Internal
    FluidStack drain(int i, FluidStack fluidStack, boolean z, boolean z2);

    boolean supportsDrain(int i);

    @Nonnull
    @ApiStatus.Internal
    Object createSnapshot();

    @ApiStatus.Internal
    void restoreFromSnapshot(Object obj);

    default long fill(FluidStack resource, boolean simulate, boolean notifyChanges) {
        if (resource.isEmpty()) {
            return 0L;
        }
        long filled = 0;
        for (int i = 0; i < getTanks(); i++) {
            filled += fill(i, resource.copy(resource.getAmount() - filled), simulate, false);
            if (filled == resource.getAmount()) {
                break;
            }
        }
        if (notifyChanges && filled > 0 && !simulate) {
            onContentsChanged();
        }
        return filled;
    }

    default long fill(FluidStack resource, boolean simulate) {
        return fill(resource, simulate, !simulate);
    }

    @Nonnull
    default FluidStack drain(FluidStack resource, boolean simulate, boolean notifyChanges) {
        if (resource.isEmpty()) {
            return FluidStack.empty();
        }
        long drained = 0;
        for (int i = 0; i < getTanks(); i++) {
            drained += drain(i, resource.copy(resource.getAmount() - drained), simulate, false).getAmount();
            if (drained == resource.getAmount()) {
                break;
            }
        }
        if (notifyChanges && drained > 0 && !simulate) {
            onContentsChanged();
        }
        return resource.copy(drained);
    }

    default FluidStack drain(FluidStack resource, boolean simulate) {
        return drain(resource, simulate, !simulate);
    }

    @Nonnull
    default FluidStack drain(long maxDrain, boolean simulate, boolean notifyChanges) {
        if (maxDrain == 0) {
            return FluidStack.empty();
        }
        FluidStack totalDrained = null;
        for (int i = 0; i < getTanks(); i++) {
            FluidStack handler = getFluidInTank(i);
            if (!handler.isEmpty()) {
                if (totalDrained == null) {
                    totalDrained = handler.copy();
                    totalDrained.setAmount(Math.min(maxDrain, totalDrained.getAmount()));
                    maxDrain -= totalDrained.getAmount();
                    if (!simulate) {
                        handler.shrink(totalDrained.getAmount());
                        if (notifyChanges) {
                            onContentsChanged();
                        }
                    }
                } else if (totalDrained.isFluidEqual(handler)) {
                    long toDrain = Math.min(maxDrain, handler.getAmount());
                    maxDrain -= toDrain;
                    totalDrained.grow(toDrain);
                    if (!simulate) {
                        handler.shrink(toDrain);
                        if (notifyChanges) {
                            onContentsChanged();
                        }
                    }
                }
                if (maxDrain <= 0) {
                    break;
                }
            }
        }
        return totalDrained == null ? FluidStack.empty() : totalDrained;
    }

    default FluidStack drain(long maxDrain, boolean simulate) {
        return drain(maxDrain, simulate, !simulate);
    }

    default void onContentsChanged() {
    }
}
