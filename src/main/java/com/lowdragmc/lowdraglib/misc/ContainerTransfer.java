package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/ContainerTransfer.class */
public class ContainerTransfer implements IItemTransfer {
    private final Container inv;

    public Container getInv() {
        return this.inv;
    }

    public ContainerTransfer(Container inv) {
        this.inv = inv;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContainerTransfer that = (ContainerTransfer) o;
        return getInv().equals(that.getInv());
    }

    public int hashCode() {
        return getInv().hashCode();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlots() {
        return getInv().m_6643_();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return getInv().m_8020_(slot);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate, boolean notifyChanges) {
        if (stack.m_41619_()) {
            return ItemStack.f_41583_;
        }
        ItemStack stackInSlot = getInv().m_8020_(slot);
        if (!stackInSlot.m_41619_()) {
            if (stackInSlot.m_41613_() >= Math.min(stackInSlot.m_41741_(), getSlotLimit(slot))) {
                return stack;
            }
            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
                return stack;
            }
            if (!getInv().m_7013_(slot, stack)) {
                return stack;
            }
            int m = Math.min(stack.m_41741_(), getSlotLimit(slot)) - stackInSlot.m_41613_();
            if (stack.m_41613_() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.m_41777_();
                    copy.m_41769_(stackInSlot.m_41613_());
                    getInv().m_6836_(slot, copy);
                    if (notifyChanges) {
                        onContentsChanged();
                    }
                    getInv().m_6596_();
                }
                return ItemStack.f_41583_;
            }
            ItemStack stack2 = stack.m_41777_();
            if (!simulate) {
                ItemStack copy2 = stack2.m_41620_(m);
                copy2.m_41769_(stackInSlot.m_41613_());
                getInv().m_6836_(slot, copy2);
                if (notifyChanges) {
                    onContentsChanged();
                }
                getInv().m_6596_();
                return stack2;
            }
            stack2.m_41774_(m);
            return stack2;
        } else if (!getInv().m_7013_(slot, stack)) {
            return stack;
        } else {
            int m2 = Math.min(stack.m_41741_(), getSlotLimit(slot));
            if (m2 < stack.m_41613_()) {
                ItemStack stack3 = stack.m_41777_();
                if (!simulate) {
                    getInv().m_6836_(slot, stack3.m_41620_(m2));
                    if (notifyChanges) {
                        onContentsChanged();
                    }
                    getInv().m_6596_();
                    return stack3;
                }
                stack3.m_41774_(m2);
                return stack3;
            }
            if (!simulate) {
                getInv().m_6836_(slot, stack);
                if (notifyChanges) {
                    onContentsChanged();
                }
                getInv().m_6596_();
            }
            return ItemStack.f_41583_;
        }
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
        if (amount == 0) {
            return ItemStack.f_41583_;
        }
        ItemStack stackInSlot = getInv().m_8020_(slot);
        if (stackInSlot.m_41619_()) {
            return ItemStack.f_41583_;
        }
        if (simulate) {
            if (stackInSlot.m_41613_() < amount) {
                return stackInSlot.m_41777_();
            }
            ItemStack copy = stackInSlot.m_41777_();
            copy.m_41764_(amount);
            return copy;
        }
        int m = Math.min(stackInSlot.m_41613_(), amount);
        ItemStack decrStackSize = getInv().m_7407_(slot, m);
        if (notifyChanges) {
            onContentsChanged();
        }
        getInv().m_6596_();
        return decrStackSize;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        getInv().m_6836_(slot, stack);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlotLimit(int slot) {
        return getInv().m_6893_();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return getInv().m_7013_(slot, stack);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public Object createSnapshot() {
        ItemStack[] copied = new ItemStack[this.inv.m_6643_()];
        for (int i = 0; i < this.inv.m_6643_(); i++) {
            copied[i] = this.inv.m_8020_(i).m_41777_();
        }
        return copied;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void restoreFromSnapshot(Object snapshot) {
        if (snapshot instanceof ItemStack[]) {
            ItemStack[] copied = (ItemStack[]) snapshot;
            if (copied.length == this.inv.m_6643_()) {
                for (int i = 0; i < this.inv.m_6643_(); i++) {
                    this.inv.m_6836_(i, copied[i]);
                }
            }
        }
    }
}
