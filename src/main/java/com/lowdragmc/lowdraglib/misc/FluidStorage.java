package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidStorage;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/FluidStorage.class */
public class FluidStorage implements IFluidStorage, IContentChangeAware, ITagSerializable<CompoundTag> {
    private Runnable onContentsChanged;
    protected Predicate<FluidStack> validator;
    @Nonnull
    protected FluidStack fluid;
    protected long capacity;

    @Override // com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
    public Runnable getOnContentsChanged() {
        return this.onContentsChanged;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
    public void setOnContentsChanged(Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }

    public void setValidator(Predicate<FluidStack> validator) {
        this.validator = validator;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidStorage
    @Nonnull
    public FluidStack getFluid() {
        return this.fluid;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidStorage
    public long getCapacity() {
        return this.capacity;
    }

    public FluidStorage(long capacity) {
        this(capacity, e -> {
            return true;
        });
    }

    public FluidStorage(long capacity, Predicate<FluidStack> validator) {
        this.onContentsChanged = () -> {
        };
        this.fluid = FluidStack.empty();
        this.capacity = capacity;
        this.validator = validator;
    }

    public FluidStorage(FluidStack fluidStack) {
        this(fluidStack.getAmount());
        this.fluid = fluidStack;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidStorage
    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
        onContentsChanged();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidStorage
    public boolean isFluidValid(FluidStack stack) {
        return this.validator.test(stack);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChange) {
        if (tank >= getTanks() || resource.isEmpty() || !isFluidValid(resource)) {
            return 0L;
        }
        if (simulate) {
            if (this.fluid.isEmpty()) {
                return Math.min(this.capacity, resource.getAmount());
            }
            if (!this.fluid.isFluidEqual(resource)) {
                return 0L;
            }
            return Math.min(this.capacity - this.fluid.getAmount(), resource.getAmount());
        } else if (this.fluid.isEmpty()) {
            this.fluid = FluidStack.create(resource, Math.min(this.capacity, resource.getAmount()));
            if (notifyChange) {
                onContentsChanged();
            }
            return this.fluid.getAmount();
        } else if (!this.fluid.isFluidEqual(resource)) {
            return 0L;
        } else {
            long filled = this.capacity - this.fluid.getAmount();
            if (resource.getAmount() < filled) {
                this.fluid.grow(resource.getAmount());
                filled = resource.getAmount();
            } else {
                this.fluid.setAmount(this.capacity);
            }
            if (filled > 0 && notifyChange) {
                onContentsChanged();
            }
            return filled;
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChange) {
        if (tank >= getTanks() || resource.isEmpty() || !resource.isFluidEqual(this.fluid)) {
            return FluidStack.empty();
        }
        return drain(resource.getAmount(), simulate, notifyChange);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void onContentsChanged() {
        this.onContentsChanged.run();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public Object createSnapshot() {
        return this.fluid.copy();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void restoreFromSnapshot(Object snapshot) {
        if (snapshot instanceof FluidStack) {
            FluidStack stack = (FluidStack) snapshot;
            this.fluid = stack.copy();
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        return this.fluid.saveToTag(new CompoundTag());
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        setFluid(FluidStack.loadFromTag(nbt));
    }

    public FluidStorage copy() {
        FluidStorage storage = new FluidStorage(this.capacity, this.validator);
        storage.setFluid(this.fluid.copy());
        return storage;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsFill(int tank) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsDrain(int tank) {
        return true;
    }
}
