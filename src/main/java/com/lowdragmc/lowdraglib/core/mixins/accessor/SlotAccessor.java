package com.lowdragmc.lowdraglib.core.mixins.accessor;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Slot.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/accessor/SlotAccessor.class */
public interface SlotAccessor {
    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    @Accessor("x")
    @Mutable
    void setX(int i);

    @Accessor("y")
    @Mutable
    void setY(int i);
}
