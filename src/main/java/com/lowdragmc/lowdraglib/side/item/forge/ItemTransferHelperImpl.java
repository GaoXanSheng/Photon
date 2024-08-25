package com.lowdragmc.lowdraglib.side.item.forge;

import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/item/forge/ItemTransferHelperImpl.class */
public class ItemTransferHelperImpl {
    public static IItemHandler toItemHandler(final IItemTransfer itemTransfer) {
        return new IItemHandler() { // from class: com.lowdragmc.lowdraglib.side.item.forge.ItemTransferHelperImpl.1
            public int getSlots() {
                return IItemTransfer.this.getSlots();
            }

            @NotNull
            public ItemStack getStackInSlot(int slot) {
                return IItemTransfer.this.getStackInSlot(slot);
            }

            @NotNull
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return IItemTransfer.this.insertItem(slot, stack, simulate, !simulate);
            }

            @NotNull
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return IItemTransfer.this.extractItem(slot, amount, simulate, !simulate);
            }

            public int getSlotLimit(int slot) {
                return IItemTransfer.this.getSlotLimit(slot);
            }

            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return IItemTransfer.this.isItemValid(slot, stack);
            }
        };
    }

    public static IItemTransfer toItemTransfer(final IItemHandler handler) {
        return new IItemTransfer() { // from class: com.lowdragmc.lowdraglib.side.item.forge.ItemTransferHelperImpl.2
            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            public int getSlots() {
                return handler.getSlots();
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            @NotNull
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            public void setStackInSlot(int index, ItemStack stack) {
                if (handler instanceof IItemHandlerModifiable) {
                    IItemHandlerModifiable modifiable = handler;
                    modifiable.setStackInSlot(index, stack);
                    return;
                }
                super.setStackInSlot(index, stack);
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            @NotNull
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate, boolean notifyChanges) {
                return handler.insertItem(slot, stack, simulate);
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            @NotNull
            public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
                return handler.extractItem(slot, amount, simulate);
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return handler.isItemValid(slot, stack);
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            @NotNull
            public Object createSnapshot() {
                return new Object();
            }

            @Override // com.lowdragmc.lowdraglib.side.item.IItemTransfer
            public void restoreFromSnapshot(Object snapshot) {
            }
        };
    }

    public static IItemTransfer getItemTransfer(Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity;
        if (level.m_8055_(pos).m_155947_() && (blockEntity = level.m_7702_(pos)) != null) {
            Optional<IItemHandler> cap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).resolve();
            if (cap.isPresent()) {
                return toItemTransfer(cap.get());
            }
            return null;
        }
        return null;
    }

    public static void exportToTarget(IItemTransfer source, int maxAmount, Predicate<ItemStack> predicate, Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity;
        if (level.m_8055_(pos).m_155947_() && (blockEntity = level.m_7702_(pos)) != null) {
            Optional<IItemHandler> cap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).resolve();
            if (cap.isPresent()) {
                IItemHandler target = cap.get();
                for (int srcIndex = 0; srcIndex < source.getSlots(); srcIndex++) {
                    ItemStack sourceStack = source.extractItem(srcIndex, Integer.MAX_VALUE, true, false);
                    if (!sourceStack.m_41619_() && predicate.test(sourceStack)) {
                        ItemStack remainder = insertItem(target, sourceStack, true);
                        int amountToInsert = sourceStack.m_41613_() - remainder.m_41613_();
                        if (amountToInsert > 0) {
                            insertItem(target, source.extractItem(srcIndex, Math.min(maxAmount, amountToInsert), false, true), false);
                            maxAmount -= Math.min(maxAmount, amountToInsert);
                            if (maxAmount <= 0) {
                                return;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    public static ItemStack insertItem(IItemHandler handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.m_41619_()) {
            return stack;
        }
        if (!stack.m_41753_()) {
            return insertToEmpty(handler, stack, simulate);
        }
        IntArrayList intArrayList = new IntArrayList();
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.m_41619_()) {
                intArrayList.add(i);
            }
            if (ItemHandlerHelper.canItemStacksStackRelaxed(stack, slotStack)) {
                stack = handler.insertItem(i, stack, simulate);
                if (stack.m_41619_()) {
                    return ItemStack.f_41583_;
                }
            }
        }
        IntListIterator it = intArrayList.iterator();
        while (it.hasNext()) {
            int slot = ((Integer) it.next()).intValue();
            stack = handler.insertItem(slot, stack, simulate);
            if (stack.m_41619_()) {
                return ItemStack.f_41583_;
            }
        }
        return stack;
    }

    public static ItemStack insertToEmpty(IItemHandler handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.m_41619_()) {
            return stack;
        }
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.m_41619_()) {
                stack = handler.insertItem(i, stack, simulate);
                if (stack.m_41619_()) {
                    return ItemStack.f_41583_;
                }
            }
        }
        return stack;
    }

    public static void importToTarget(IItemTransfer target, int maxAmount, Predicate<ItemStack> predicate, Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity;
        if (level.m_8055_(pos).m_155947_() && (blockEntity = level.m_7702_(pos)) != null) {
            Optional<IItemHandler> cap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).resolve();
            if (cap.isPresent()) {
                IItemHandler source = cap.get();
                for (int srcIndex = 0; srcIndex < source.getSlots(); srcIndex++) {
                    ItemStack sourceStack = source.extractItem(srcIndex, Integer.MAX_VALUE, true);
                    if (!sourceStack.m_41619_() && predicate.test(sourceStack)) {
                        ItemStack remainder = insertItem(target, sourceStack, true);
                        int amountToInsert = sourceStack.m_41613_() - remainder.m_41613_();
                        if (amountToInsert > 0) {
                            insertItem(target, source.extractItem(srcIndex, Math.min(maxAmount, amountToInsert), false), false);
                            maxAmount -= Math.min(maxAmount, amountToInsert);
                        }
                        if (maxAmount <= 0) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static ItemStack insertItem(IItemTransfer handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.m_41619_()) {
            return stack;
        }
        if (!stack.m_41753_()) {
            return insertToEmpty(handler, stack, simulate);
        }
        IntArrayList intArrayList = new IntArrayList();
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.m_41619_()) {
                intArrayList.add(i);
            }
            if (ItemHandlerHelper.canItemStacksStackRelaxed(stack, slotStack)) {
                stack = handler.insertItem(i, stack, simulate, !simulate);
                if (stack.m_41619_()) {
                    return ItemStack.f_41583_;
                }
            }
        }
        IntListIterator it = intArrayList.iterator();
        while (it.hasNext()) {
            int slot = ((Integer) it.next()).intValue();
            stack = handler.insertItem(slot, stack, simulate, !simulate);
            if (stack.m_41619_()) {
                return ItemStack.f_41583_;
            }
        }
        return stack;
    }

    public static ItemStack insertToEmpty(IItemTransfer handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.m_41619_()) {
            return stack;
        }
        int slots = handler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.m_41619_()) {
                stack = handler.insertItem(i, stack, simulate, !simulate);
                if (stack.m_41619_()) {
                    return ItemStack.f_41583_;
                }
            }
        }
        return stack;
    }
}
