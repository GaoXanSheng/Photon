package com.lowdragmc.photon.core.mixins.accessor;

import com.mojang.blaze3d.shaders.BlendMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BlendMode.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/core/mixins/accessor/BlendModeAccessor.class */
public interface BlendModeAccessor {
    @Accessor
    static BlendMode getLastApplied() {
        return null;
    }

    @Accessor
    static void setLastApplied(BlendMode blendMode) {
    }
}
