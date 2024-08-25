package com.lowdragmc.photon.core.mixins;

import com.lowdragmc.lowdraglib.client.scene.CameraEntity;
import com.lowdragmc.lowdraglib.client.scene.ISceneBlockRenderHook;
import com.lowdragmc.lowdraglib.client.scene.ParticleManager;
import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldSceneRenderer.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/core/mixins/WorldSceneRendererMixin.class */
public class WorldSceneRendererMixin {
    @Shadow(remap = false)
    protected ParticleManager particleManager;
    @Shadow(remap = false)
    protected CameraEntity cameraEntity;
    @Shadow(remap = false)
    protected Camera camera;

    @Inject(method = {"renderTESR"}, at = {@At("RETURN")}, remap = false)
    private void injectRenderTESR(Collection<BlockPos> poses, PoseStack pose, MultiBufferSource.BufferSource buffers, @Nullable ISceneBlockRenderHook hook, float partialTicks, CallbackInfo ci) {
        if (this.particleManager != null) {
            PhotonParticleRenderType.prepareForParticleRendering(null);
            PoseStack poseStack = new PoseStack();
            poseStack.m_166856_();
            poseStack.m_85837_(this.cameraEntity.m_20185_(), this.cameraEntity.m_20186_(), this.cameraEntity.m_20189_());
            this.particleManager.render(poseStack, this.camera, partialTicks);
        }
    }
}
