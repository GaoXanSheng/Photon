package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/FluidTransferList.class */
public class FluidTransferList implements IFluidTransfer, ITagSerializable<CompoundTag> {
    public final IFluidTransfer[] transfers;
    protected Predicate<FluidStack> filter = fluid -> {
        return true;
    };

    public void setFilter(Predicate<FluidStack> filter) {
        this.filter = filter;
    }

    public FluidTransferList(IFluidTransfer... transfers) {
        this.transfers = transfers;
    }

    public FluidTransferList(List<IFluidTransfer> transfers) {
        this.transfers = (IFluidTransfer[]) transfers.toArray(x$0 -> {
            return new IFluidTransfer[x$0];
        });
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public int getTanks() {
        return Arrays.stream(this.transfers).mapToInt((v0) -> {
            return v0.getTanks();
        }).sum();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack getFluidInTank(int tank) {
        IFluidTransfer[] iFluidTransferArr;
        int index = 0;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank - index < transfer.getTanks()) {
                return transfer.getFluidInTank(tank - index);
            }
            index += transfer.getTanks();
        }
        return FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        IFluidTransfer[] iFluidTransferArr;
        int index = 0;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank - index < transfer.getTanks()) {
                transfer.setFluidInTank(tank - index, fluidStack);
                return;
            }
            index += transfer.getTanks();
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long getTankCapacity(int tank) {
        IFluidTransfer[] iFluidTransferArr;
        int index = 0;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank - index < transfer.getTanks()) {
                return transfer.getTankCapacity(tank - index);
            }
            index += transfer.getTanks();
        }
        return 0L;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        IFluidTransfer[] iFluidTransferArr;
        if (!this.filter.test(stack)) {
            return false;
        }
        int index = 0;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank - index < transfer.getTanks()) {
                return transfer.isFluidValid(tank - index, stack);
            }
            index += transfer.getTanks();
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        IFluidTransfer[] iFluidTransferArr;
        int index = 0;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank - index < transfer.getTanks()) {
                return transfer.fill(tank - index, resource, simulate, notifyChanges);
            }
            index += transfer.getTanks();
        }
        return 0L;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(FluidStack resource, boolean simulate, boolean notifyChange) {
        IFluidTransfer[] iFluidTransferArr;
        if (resource.isEmpty() || !this.filter.test(resource)) {
            return 0L;
        }
        FluidStack copied = resource.copy();
        for (IFluidTransfer transfer : this.transfers) {
            FluidStack candidate = copied.copy();
            copied.shrink(transfer.fill(candidate, simulate, notifyChange));
            if (copied.isEmpty()) {
                break;
            }
        }
        return resource.getAmount() - copied.getAmount();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        IFluidTransfer[] iFluidTransferArr;
        int index = 0;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank - index < transfer.getTanks()) {
                return transfer.drain(tank - index, resource, simulate, notifyChanges);
            }
            index += transfer.getTanks();
        }
        return FluidStack.empty();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(FluidStack resource, boolean simulate, boolean notifyChange) {
        IFluidTransfer[] iFluidTransferArr;
        if (resource.isEmpty() || !this.filter.test(resource)) {
            return FluidStack.empty();
        }
        FluidStack copied = resource.copy();
        for (IFluidTransfer transfer : this.transfers) {
            FluidStack candidate = copied.copy();
            copied.shrink(transfer.drain(candidate, simulate, notifyChange).getAmount());
            if (copied.isEmpty()) {
                break;
            }
        }
        copied.setAmount(resource.getAmount() - copied.getAmount());
        return copied;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(long maxDrain, boolean simulate, boolean notifyChange) {
        IFluidTransfer[] iFluidTransferArr;
        if (maxDrain == 0) {
            return FluidStack.empty();
        }
        FluidStack totalDrained = null;
        for (IFluidTransfer storage : this.transfers) {
            if (totalDrained == null || totalDrained.isEmpty()) {
                totalDrained = storage.drain(maxDrain, simulate, notifyChange);
                if (totalDrained.isEmpty()) {
                    totalDrained = null;
                } else {
                    maxDrain -= totalDrained.getAmount();
                }
            } else {
                FluidStack copy = totalDrained.copy();
                copy.setAmount(maxDrain);
                FluidStack drain = storage.drain(copy, simulate, notifyChange);
                totalDrained.grow(drain.getAmount());
                maxDrain -= drain.getAmount();
            }
            if (maxDrain <= 0) {
                break;
            }
        }
        return totalDrained == null ? FluidStack.empty() : totalDrained;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public final void onContentsChanged() {
        IFluidTransfer[] iFluidTransferArr;
        for (IFluidTransfer transfer : this.transfers) {
            transfer.onContentsChanged();
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public Object createSnapshot() {
        return Arrays.stream(this.transfers).map((v0) -> {
            return v0.createSnapshot();
        }).toArray(x$0 -> {
            return new Object[x$0];
        });
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void restoreFromSnapshot(Object snapshot) {
        if (snapshot instanceof Object[]) {
            Object[] array = (Object[]) snapshot;
            if (array.length == this.transfers.length) {
                for (int i = 0; i < array.length; i++) {
                    this.transfers[i].restoreFromSnapshot(array[i]);
                }
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        IFluidTransfer[] iFluidTransferArr;
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (IFluidTransfer transfer : this.transfers) {
            if (transfer instanceof ITagSerializable) {
                ITagSerializable<?> serializable = (ITagSerializable) transfer;
                list.add(serializable.mo129serializeNBT());
            } else {
                LDLib.LOGGER.warn("[FluidTransferList] internal tank doesn't support serialization");
            }
        }
        tag.m_128365_("tanks", list);
        tag.m_128344_("type", list.m_7264_());
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        ListTag list = nbt.m_128437_("tanks", nbt.m_128445_("type"));
        for (int i = 0; i < list.size(); i++) {
            IFluidTransfer iFluidTransfer = this.transfers[i];
            if (iFluidTransfer instanceof ITagSerializable) {
                ITagSerializable serializable = (ITagSerializable) iFluidTransfer;
                serializable.deserializeNBT(list.get(i));
            } else {
                LDLib.LOGGER.warn("[FluidTransferList] internal tank doesn't support serialization");
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsFill(int tank) {
        IFluidTransfer[] iFluidTransferArr;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank >= transfer.getTanks()) {
                tank -= transfer.getTanks();
            } else {
                return transfer.supportsFill(tank);
            }
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsDrain(int tank) {
        IFluidTransfer[] iFluidTransferArr;
        for (IFluidTransfer transfer : this.transfers) {
            if (tank >= transfer.getTanks()) {
                tank -= transfer.getTanks();
            } else {
                return transfer.supportsDrain(tank);
            }
        }
        return false;
    }
}
