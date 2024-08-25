package com.lowdragmc.lowdraglib.core.mixins;

import java.util.Map;
import java.util.Queue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ParticleEngine.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/ParticleEngineMixin.class */
public abstract class ParticleEngineMixin {
    @Shadow
    @Final
    private Map<ParticleRenderType, Queue<Particle>> f_107289_;

    @Inject(method = {"tick"}, at = {@At("TAIL")})
    public void injectTick(CallbackInfo ci) {
        this.f_107289_.entrySet().removeIf(entry -> {
            return ((Queue) entry.getValue()).isEmpty();
        });
    }
}
