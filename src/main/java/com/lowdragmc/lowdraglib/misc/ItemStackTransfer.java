package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/ItemStackTransfer.class */
public class ItemStackTransfer implements IItemTransfer, ITagSerializable<CompoundTag>, IContentChangeAware {
    protected NonNullList<ItemStack> stacks;
    private Runnable onContentsChanged;
    private Function<ItemStack, Boolean> filter;

    @Override // com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
    public Runnable getOnContentsChanged() {
        return this.onContentsChanged;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
    public void setOnContentsChanged(Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }

    public void setFilter(Function<ItemStack, Boolean> filter) {
        this.filter = filter;
    }

    public ItemStackTransfer() {
        this(1);
    }

    public ItemStackTransfer(int size) {
        this.onContentsChanged = () -> {
        };
        this.stacks = NonNullList.m_122780_(size, ItemStack.f_41583_);
    }

    public ItemStackTransfer(NonNullList<ItemStack> stacks) {
        this.onContentsChanged = () -> {
        };
        this.stacks = stacks;
    }

    public ItemStackTransfer(ItemStack stack) {
        this(NonNullList.m_122783_(ItemStack.f_41583_, new ItemStack[]{stack}));
    }

    public void setSize(int size) {
        this.stacks = NonNullList.m_122780_(size, ItemStack.f_41583_);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);
        this.stacks.set(slot, stack);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlots() {
        return this.stacks.size();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return (ItemStack) this.stacks.get(slot);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate, boolean notifyChanges) {
        if (stack.m_41619_()) {
            return ItemStack.f_41583_;
        }
        if (!isItemValid(slot, stack)) {
            return stack;
        }
        validateSlotIndex(slot);
        ItemStack existing = (ItemStack) this.stacks.get(slot);
        int limit = getStackLimit(slot, stack);
        if (!existing.m_41619_()) {
            if (!ItemTransferHelper.canItemStacksStack(stack, existing)) {
                return stack;
            }
            limit -= existing.m_41613_();
        }
        if (limit <= 0) {
            return stack;
        }
        boolean reachedLimit = stack.m_41613_() > limit;
        if (!simulate) {
            if (existing.m_41619_()) {
                this.stacks.set(slot, reachedLimit ? ItemTransferHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.m_41769_(reachedLimit ? limit : stack.m_41613_());
            }
            if (notifyChanges) {
                onContentsChanged(slot);
            }
        }
        return reachedLimit ? ItemTransferHelper.copyStackWithSize(stack, stack.m_41613_() - limit) : ItemStack.f_41583_;
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
        if (amount == 0) {
            return ItemStack.f_41583_;
        }
        validateSlotIndex(slot);
        ItemStack existing = (ItemStack) this.stacks.get(slot);
        if (existing.m_41619_()) {
            return ItemStack.f_41583_;
        }
        int toExtract = Math.min(amount, existing.m_41741_());
        if (existing.m_41613_() <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, ItemStack.f_41583_);
                if (notifyChanges) {
                    onContentsChanged(slot);
                }
                return existing;
            }
            return existing.m_41777_();
        }
        if (!simulate) {
            this.stacks.set(slot, ItemTransferHelper.copyStackWithSize(existing, existing.m_41613_() - toExtract));
            if (notifyChanges) {
                onContentsChanged(slot);
            }
        }
        return ItemTransferHelper.copyStackWithSize(existing, toExtract);
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public int getSlotLimit(int slot) {
        return 64;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.m_41741_());
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.filter == null || this.filter.apply(stack).booleanValue();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.stacks.size(); i++) {
            if (!((ItemStack) this.stacks.get(i)).m_41619_()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.m_128405_("Slot", i);
                ((ItemStack) this.stacks.get(i)).m_41739_(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.m_128365_("Items", nbtTagList);
        nbt.m_128405_("Size", this.stacks.size());
        return nbt;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        setSize(nbt.m_128425_("Size", 3) ? nbt.m_128451_("Size") : this.stacks.size());
        ListTag tagList = nbt.m_128437_("Items", 10);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.m_128728_(i);
            int slot = itemTags.m_128451_("Slot");
            if (slot >= 0 && slot < this.stacks.size()) {
                this.stacks.set(slot, ItemStack.m_41712_(itemTags));
            }
        }
        onLoad();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.stacks.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.stacks.size() + ")");
        }
    }

    protected void onLoad() {
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void onContentsChanged() {
        this.onContentsChanged.run();
    }

    public void onContentsChanged(int slot) {
        onContentsChanged();
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    @NotNull
    public Object createSnapshot() {
        return this.stacks.stream().map((v0) -> {
            return v0.m_41777_();
        }).toArray(x$0 -> {
            return new ItemStack[x$0];
        });
    }

    @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
    public void restoreFromSnapshot(Object snapshot) {
        if (snapshot instanceof ItemStack[]) {
            ItemStack[] copied = (ItemStack[]) snapshot;
            if (copied.length == this.stacks.size()) {
                for (int i = 0; i < this.stacks.size(); i++) {
                    this.stacks.set(i, copied[i].m_41777_());
                }
            }
        }
    }

    public ItemStackTransfer copy() {
        NonNullList<ItemStack> copiedStack = NonNullList.m_122780_(this.stacks.size(), ItemStack.f_41583_);
        for (int i = 0; i < this.stacks.size(); i++) {
            copiedStack.set(i, ((ItemStack) this.stacks.get(i)).m_41777_());
        }
        ItemStackTransfer copied = new ItemStackTransfer(copiedStack);
        copied.setFilter(this.filter);
        return copied;
    }
}
