package com.lowdragmc.photon.forge.core.mixins;

import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.lowdragmc.photon.client.fx.BlockEffect;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ParticleEngine.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/forge/core/mixins/ParticleEngineMixin.class */
public abstract class ParticleEngineMixin {
    @Inject(method = {"render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"}, at = {@At("RETURN")}, remap = false)
    private void injectRenderReturn(PoseStack pMatrixStack, MultiBufferSource.BufferSource pBuffer, LightTexture pLightTexture, Camera pActiveRenderInfo, float pPartialTicks, Frustum clippingHelper, CallbackInfo ci) {
        PhotonParticleRenderType.finishRender();
    }

    @Inject(method = {"setLevel"}, at = {@At("RETURN")})
    private void injectSetLevel(ClientLevel level, CallbackInfo ci) {
        EntityEffect.CACHE.clear();
        BlockEffect.CACHE.clear();
    }
}
