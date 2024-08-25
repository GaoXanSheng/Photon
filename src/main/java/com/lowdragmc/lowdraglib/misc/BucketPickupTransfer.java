package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import javax.annotation.Nonnull;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/BucketPickupTransfer.class */
public class BucketPickupTransfer implements IFluidTransfer {
    protected final BucketPickup bucketPickupHandler;
    protected final Level world;
    protected final BlockPos blockPos;

    public BucketPickupTransfer(BucketPickup bucketPickupHandler, Level world, BlockPos blockPos) {
        this.bucketPickupHandler = bucketPickupHandler;
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
        if (tank == 0) {
            FluidState fluidState = this.world.m_6425_(this.blockPos);
            if (!fluidState.m_76178_()) {
                return FluidStack.create(fluidState.m_76152_(), FluidHelper.getBucket());
            }
        }
        return FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long getTankCapacity(int tank) {
        return FluidHelper.getBucket();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        return 0L;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @Nonnull
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        if (!resource.isEmpty() && FluidHelper.getBucket() <= resource.getAmount()) {
            FluidState fluidState = this.world.m_6425_(this.blockPos);
            if (!fluidState.m_76178_() && resource.getFluid() == fluidState.m_76152_()) {
                if (!simulate) {
                    ItemStack itemStack = this.bucketPickupHandler.m_142598_(this.world, this.blockPos, this.world.m_8055_(this.blockPos));
                    if (itemStack != ItemStack.f_41583_) {
                        BucketItemAccessor m_41720_ = itemStack.m_41720_();
                        if (m_41720_ instanceof BucketItemAccessor) {
                            BucketItemAccessor bucket = m_41720_;
                            FluidStack extracted = FluidStack.create(bucket.fabric_getFluid(), FluidHelper.getBucket());
                            if (!resource.isFluidEqual(extracted)) {
                                return FluidStack.empty();
                            }
                            return extracted;
                        }
                    }
                } else {
                    FluidStack extracted2 = FluidStack.create(fluidState.m_76152_(), FluidHelper.getBucket());
                    if (resource.isFluidEqual(extracted2)) {
                        return extracted2;
                    }
                }
            }
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
