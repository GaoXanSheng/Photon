package com.lowdragmc.photon.core.mixins.accessor;

import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.shaders.Uniform;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ShaderInstance.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/core/mixins/accessor/ShaderInstanceAccessor.class */
public interface ShaderInstanceAccessor {
    @Accessor
    BlendMode getBlend();

    @Accessor
    List<String> getSamplerNames();

    @Accessor
    Map<String, Uniform> getUniformMap();
}
