package com.lowdragmc.photon.forge.core.mixins.no_iris;

import com.google.common.collect.ImmutableList;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.lowdragmc.photon.client.emitter.data.RendererSetting;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ParticleEngine.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/forge/core/mixins/no_iris/ParticleEngineMixin.class */
public abstract class ParticleEngineMixin {
    private static final List<ParticleRenderType> OPAQUE_PARTICLE_RENDER_TYPES = ImmutableList.of(ParticleRenderType.f_107429_, ParticleRenderType.f_107430_, ParticleRenderType.f_107432_, ParticleRenderType.f_107433_, ParticleRenderType.f_107434_);
    @Shadow
    @Final
    private Map<ParticleRenderType, Queue<Particle>> f_107289_;

    @Redirect(method = {"render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/ParticleEngine;particles:Ljava/util/Map;"))
    private Map<ParticleRenderType, Queue<Particle>> iris$selectParticlesToRender(ParticleEngine instance) {
        Map<ParticleRenderType, Queue<Particle>> toRender = new HashMap<>((Map<? extends ParticleRenderType, ? extends Queue<Particle>>) this.f_107289_);
        if (PhotonParticleRenderType.getLAYER() == RendererSetting.Layer.Opaque) {
            toRender.remove(ParticleRenderType.f_107431_);
        } else {
            for (ParticleRenderType type : OPAQUE_PARTICLE_RENDER_TYPES) {
                toRender.remove(type);
            }
        }
        return toRender;
    }
}
