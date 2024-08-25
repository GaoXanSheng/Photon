package com.lowdragmc.lowdraglib.core.mixins.accessor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({AbstractContainerMenu.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/accessor/AbstractContainerMenuAccessor.class */
public interface AbstractContainerMenuAccessor {
    @Accessor
    NonNullList<ItemStack> getLastSlots();

    @Accessor
    NonNullList<ItemStack> getRemoteSlots();
}
