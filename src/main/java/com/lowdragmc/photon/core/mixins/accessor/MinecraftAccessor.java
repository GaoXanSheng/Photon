package com.lowdragmc.photon.core.mixins.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Minecraft.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/core/mixins/accessor/MinecraftAccessor.class */
public interface MinecraftAccessor {
    @Accessor
    static int getFps() {
        return 0;
    }
}
