package com.lowdragmc.lowdraglib.forge.core.mixins;

import java.util.Map;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ParticleEngine.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/forge/core/mixins/ParticleEnginAccessor.class */
public interface ParticleEnginAccessor {
    @Accessor("f_107293_")
    Map<ResourceLocation, ParticleProvider<?>> getProviders();
}
