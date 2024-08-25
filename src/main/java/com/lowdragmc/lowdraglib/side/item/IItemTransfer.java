package com.lowdragmc.lowdraglib.side.item;

import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/item/IItemTransfer.class */
public interface IItemTransfer {
    int getSlots();

    @Nonnull
    ItemStack getStackInSlot(int i);

    @Nonnull
    ItemStack insertItem(int i, @Nonnull ItemStack itemStack, boolean z, boolean z2);

    @Nonnull
    ItemStack extractItem(int i, int i2, boolean z, boolean z2);

    int getSlotLimit(int i);

    boolean isItemValid(int i, @Nonnull ItemStack itemStack);

    @Nonnull
    @ApiStatus.Internal
    Object createSnapshot();

    @ApiStatus.Internal
    void restoreFromSnapshot(Object obj);

    default void setStackInSlot(int index, ItemStack stack) {
        extractItem(index, getStackInSlot(index).m_41613_(), false, false);
        insertItem(index, stack, false, false);
    }

    default ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return insertItem(slot, stack, simulate, !simulate);
    }

    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extractItem(slot, amount, simulate, !simulate);
    }

    default void onContentsChanged() {
    }
}
