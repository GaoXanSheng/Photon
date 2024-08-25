package com.lowdragmc.photon.core.mixins.accessor;

import java.util.Map;
import java.util.Queue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ParticleEngine.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/core/mixins/accessor/ParticleEngineAccessor.class */
public interface ParticleEngineAccessor {
    @Accessor
    Map<ParticleRenderType, Queue<Particle>> getParticles();
}
