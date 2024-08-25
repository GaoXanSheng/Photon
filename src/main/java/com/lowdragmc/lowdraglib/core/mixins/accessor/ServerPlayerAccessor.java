package com.lowdragmc.lowdraglib.core.mixins.accessor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ServerPlayer.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/accessor/ServerPlayerAccessor.class */
public interface ServerPlayerAccessor {
    @Invoker
    void callNextContainerCounter();

    @Invoker
    void callInitMenu(AbstractContainerMenu abstractContainerMenu);

    @Accessor
    int getContainerCounter();
}
