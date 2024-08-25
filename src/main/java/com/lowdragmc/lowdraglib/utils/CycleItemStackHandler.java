package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/CycleItemStackHandler.class */
public class CycleItemStackHandler implements IItemTransfer {
    private List<List<ItemStack>> stacks;

    public CycleItemStackHandler(List<List<ItemStack>> stacks) {
        updateStacks(stacks);
    }

    public void updateStacks(List<List<ItemStack>> stacks) {
        this.stacks = stacks;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlots() {
        return this.stacks.size();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack getStackInSlot(int i) {
        List<ItemStack> stackList = this.stacks.get(i);
        return (stackList == null || stackList.isEmpty()) ? ItemStack.f_41583_ : stackList.get(Math.abs(((int) (System.currentTimeMillis() / 1000)) % stackList.size()));
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void setStackInSlot(int index, ItemStack stack) {
        if (index >= 0 && index < this.stacks.size()) {
            this.stacks.set(index, List.of(stack));
        }
    }

    public List<ItemStack> getStackList(int i) {
        return this.stacks.get(i);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack insertItem(int i, @Nonnull ItemStack itemStack, boolean b, boolean notifyChanges) {
        return itemStack;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack extractItem(int i, int i1, boolean b, boolean notifyChanges) {
        return ItemStack.f_41583_;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public Object createSnapshot() {
        return new Object();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void restoreFromSnapshot(Object snapshot) {
    }
}
