package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/ItemTransferList.class */
public class ItemTransferList implements IItemTransfer, ITagSerializable<CompoundTag> {
    public final IItemTransfer[] transfers;
    protected Predicate<ItemStack> filter = item -> {
        return true;
    };

    public void setFilter(Predicate<ItemStack> filter) {
        this.filter = filter;
    }

    public ItemTransferList(IItemTransfer... transfers) {
        this.transfers = transfers;
    }

    public ItemTransferList(List<IItemTransfer> transfers) {
        this.transfers = (IItemTransfer[]) transfers.toArray(x$0 -> {
            return new IItemTransfer[x$0];
        });
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlots() {
        return Arrays.stream(this.transfers).mapToInt((v0) -> {
            return v0.getSlots();
        }).sum();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        IItemTransfer[] iItemTransferArr;
        int index = 0;
        for (IItemTransfer transfer : this.transfers) {
            if (slot - index < transfer.getSlots()) {
                return transfer.getStackInSlot(slot - index);
            }
            index += transfer.getSlots();
        }
        return ItemStack.f_41583_;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void setStackInSlot(int slot, ItemStack stack) {
        IItemTransfer[] iItemTransferArr;
        int index = 0;
        for (IItemTransfer transfer : this.transfers) {
            if (slot - index < transfer.getSlots()) {
                transfer.setStackInSlot(slot - index, stack);
                return;
            }
            index += transfer.getSlots();
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate, boolean notifyChanges) {
        IItemTransfer[] iItemTransferArr;
        if (!this.filter.test(stack)) {
            return stack;
        }
        int index = 0;
        for (IItemTransfer transfer : this.transfers) {
            if (slot - index < transfer.getSlots()) {
                return transfer.insertItem(slot - index, stack, simulate, notifyChanges);
            }
            index += transfer.getSlots();
        }
        return stack;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
        IItemTransfer[] iItemTransferArr;
        int index = 0;
        for (IItemTransfer transfer : this.transfers) {
            if (slot - index < transfer.getSlots()) {
                return transfer.extractItem(slot - index, amount, simulate, notifyChanges);
            }
            index += transfer.getSlots();
        }
        return ItemStack.f_41583_;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlotLimit(int slot) {
        IItemTransfer[] iItemTransferArr;
        int index = 0;
        for (IItemTransfer transfer : this.transfers) {
            if (slot - index < transfer.getSlots()) {
                return transfer.getSlotLimit(slot - index);
            }
            index += transfer.getSlots();
        }
        return 0;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        IItemTransfer[] iItemTransferArr;
        if (!this.filter.test(stack)) {
            return false;
        }
        int index = 0;
        for (IItemTransfer transfer : this.transfers) {
            if (slot - index < transfer.getSlots()) {
                return transfer.isItemValid(slot - index, stack);
            }
            index += transfer.getSlots();
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public final void onContentsChanged() {
        IItemTransfer[] iItemTransferArr;
        for (IItemTransfer transfer : this.transfers) {
            transfer.onContentsChanged();
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public Object createSnapshot() {
        return Arrays.stream(this.transfers).map((v0) -> {
            return v0.createSnapshot();
        }).toArray(x$0 -> {
            return new Object[x$0];
        });
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
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
        IItemTransfer[] iItemTransferArr;
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (IItemTransfer transfer : this.transfers) {
            if (transfer instanceof ITagSerializable) {
                ITagSerializable<?> serializable = (ITagSerializable) transfer;
                list.add(serializable.mo129serializeNBT());
            } else {
                LDLib.LOGGER.warn("[ItemTransferList] internal container doesn't support serialization");
            }
        }
        tag.m_128365_("slots", list);
        tag.m_128344_("type", list.m_7264_());
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        ListTag list = nbt.m_128437_("slots", nbt.m_128445_("type"));
        for (int i = 0; i < list.size(); i++) {
            IItemTransfer iItemTransfer = this.transfers[i];
            if (iItemTransfer instanceof ITagSerializable) {
                ITagSerializable serializable = (ITagSerializable) iItemTransfer;
                serializable.deserializeNBT(list.get(i));
            } else {
                LDLib.LOGGER.warn("[ItemTransferList] internal container doesn't support serialization");
            }
        }
    }
}
