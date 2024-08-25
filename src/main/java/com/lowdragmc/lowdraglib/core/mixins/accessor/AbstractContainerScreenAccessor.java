package com.lowdragmc.lowdraglib.core.mixins.accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({AbstractContainerScreen.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/accessor/AbstractContainerScreenAccessor.class */
public interface AbstractContainerScreenAccessor {
    @Accessor
    ItemStack getDraggingItem();

    @Accessor
    int getQuickCraftingRemainder();

    @Accessor("isSplittingStack")
    boolean isSplittingStack();

    @Accessor
    ItemStack getSnapbackItem();

    @Accessor
    void setSnapbackItem(ItemStack itemStack);

    @Accessor
    int getSnapbackStartX();

    @Accessor
    int getSnapbackStartY();

    @Accessor
    Slot getSnapbackEnd();

    @Accessor
    long getSnapbackTime();
}
