package com.lowdragmc.lowdraglib.side.item;

import com.lowdragmc.lowdraglib.misc.ContainerTransfer;
import com.lowdragmc.lowdraglib.side.item.forge.ItemTransferHelperImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/item/ItemTransferHelper.class */
public class ItemTransferHelper {
    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static IItemTransfer getItemTransfer(Level level, BlockPos pos, @Nullable Direction direction) {
        return ItemTransferHelperImpl.getItemTransfer(level, pos, direction);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static void exportToTarget(IItemTransfer source, int maxAmount, Predicate<ItemStack> predicate, Level level, BlockPos pos, @Nullable Direction direction) {
        ItemTransferHelperImpl.exportToTarget(source, maxAmount, predicate, level, pos, direction);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static void importToTarget(IItemTransfer target, int maxAmount, Predicate<ItemStack> predicate, Level level, BlockPos pos, @Nullable Direction direction) {
        ItemTransferHelperImpl.importToTarget(target, maxAmount, predicate, level, pos, direction);
    }

    public static boolean canItemStacksStack(ItemStack first, ItemStack second) {
        if (!first.m_41619_() && first.m_41656_(second) && first.m_41782_() == second.m_41782_()) {
            return !first.m_41782_() || first.m_41783_().equals(second.m_41783_());
        }
        return false;
    }

    public static ItemStack copyStackWithSize(ItemStack stack, int size) {
        if (size == 0) {
            return ItemStack.f_41583_;
        }
        ItemStack copy = stack.m_41777_();
        copy.m_41764_(size);
        return copy;
    }

    @Nonnull
    public static ItemStack insertItem(IItemTransfer dest, @Nonnull ItemStack stack, boolean simulate) {
        if (dest == null || stack.m_41619_()) {
            return stack;
        }
        for (int i = 0; i < dest.getSlots(); i++) {
            stack = dest.insertItem(i, stack, simulate);
            if (stack.m_41619_()) {
                return ItemStack.f_41583_;
            }
        }
        return stack;
    }

    @Nonnull
    public static ItemStack insertItemStacked(IItemTransfer inventory, ItemStack stack, boolean simulate) {
        if (inventory == null || stack.m_41619_()) {
            return stack;
        }
        if (!stack.m_41753_()) {
            return insertItem(inventory, stack, simulate);
        }
        int sizeInventory = inventory.getSlots();
        for (int i = 0; i < sizeInventory; i++) {
            ItemStack slot = inventory.getStackInSlot(i);
            if (canItemStacksStackRelaxed(slot, stack)) {
                stack = inventory.insertItem(i, stack, simulate);
                if (stack.m_41619_()) {
                    break;
                }
            }
        }
        if (!stack.m_41619_()) {
            for (int i2 = 0; i2 < sizeInventory; i2++) {
                if (inventory.getStackInSlot(i2).m_41619_()) {
                    stack = inventory.insertItem(i2, stack, simulate);
                    if (stack.m_41619_()) {
                        break;
                    }
                }
            }
        }
        return stack;
    }

    public static boolean canItemStacksStackRelaxed(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (!a.m_41619_() && !b.m_41619_() && a.m_41720_() == b.m_41720_() && a.m_41753_() && a.m_41782_() == b.m_41782_()) {
            return !a.m_41782_() || a.m_41783_().equals(b.m_41783_());
        }
        return false;
    }

    public static void giveItemToPlayer(Player player, ItemStack stack) {
        giveItemToPlayer(player, stack, -1);
    }

    public static void giveItemToPlayer(Player player, @Nonnull ItemStack stack, int preferredSlot) {
        if (stack.m_41619_()) {
            return;
        }
        IItemTransfer inventory = new ContainerTransfer(player.m_150109_());
        Level level = player.f_19853_;
        ItemStack remainder = stack;
        if (preferredSlot >= 0 && preferredSlot < inventory.getSlots()) {
            remainder = inventory.insertItem(preferredSlot, stack, false);
        }
        if (!remainder.m_41619_()) {
            remainder = insertItemStacked(inventory, remainder, false);
        }
        if (remainder.m_41619_() || remainder.m_41613_() != stack.m_41613_()) {
            level.m_6263_((Player) null, player.m_20185_(), player.m_20186_() + 0.5d, player.m_20189_(), SoundEvents.f_12019_, SoundSource.PLAYERS, 0.2f, (((level.f_46441_.m_188501_() - level.f_46441_.m_188501_()) * 0.7f) + 1.0f) * 2.0f);
        }
        if (!remainder.m_41619_() && !level.f_46443_) {
            ItemEntity entityitem = new ItemEntity(level, player.m_20185_(), player.m_20186_() + 0.5d, player.m_20189_(), remainder);
            entityitem.m_32010_(40);
            entityitem.m_20256_(entityitem.m_20184_().m_82542_(0.0d, 1.0d, 0.0d));
            level.m_7967_(entityitem);
        }
    }
}
