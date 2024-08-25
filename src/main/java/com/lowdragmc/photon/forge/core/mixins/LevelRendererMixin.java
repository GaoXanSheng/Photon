package com.lowdragmc.photon.forge.core.mixins;

import com.lowdragmc.photon.Photon;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LevelRenderer.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/forge/core/mixins/LevelRendererMixin.class */
public class LevelRendererMixin {
    @Shadow
    private Frustum f_172938_;
    @Shadow
    @Final
    private Minecraft f_109461_;
    @Shadow
    @Final
    private RenderBuffers f_109464_;

    @Inject(method = {"renderLevel"}, at = {@At(value = "CONSTANT", args = {"stringValue=entities"}, shift = At.Shift.BEFORE, by = 1)})
    private void prepareForParticleRendering(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        PhotonParticleRenderType.prepareForParticleRendering(this.f_172938_);
        if (!Photon.isShaderModInstalled()) {
            MultiBufferSource.BufferSource bufferSource = this.f_109464_.m_110104_();
            this.f_109461_.f_91061_.render(poseStack, bufferSource, lightTexture, camera, partialTick, this.f_172938_);
        }
    }
}
