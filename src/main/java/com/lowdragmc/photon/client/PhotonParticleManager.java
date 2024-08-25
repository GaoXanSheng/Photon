package com.lowdragmc.photon.client;

import com.lowdragmc.lowdraglib.client.scene.ParticleManager;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import net.minecraft.client.Camera;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/PhotonParticleManager.class */
public class PhotonParticleManager extends ParticleManager {
    private final long[] lastCPUTimes = new long[20];
    private int tickIndex = 0;

    @Override // com.lowdragmc.lowdraglib.client.scene.ParticleManager
    public void render(PoseStack pMatrixStack, Camera pActiveRenderInfo, float pPartialTicks) {
        super.render(pMatrixStack, pActiveRenderInfo, pPartialTicks);
        PhotonParticleRenderType.finishRender();
    }

    @Override // com.lowdragmc.lowdraglib.client.scene.ParticleManager
    public void tick() {
        long startTime = System.nanoTime();
        super.tick();
        this.lastCPUTimes[this.tickIndex] = System.nanoTime() - startTime;
        this.tickIndex = (this.tickIndex + 1) % this.lastCPUTimes.length;
    }

    public long getCPUTime() {
        return ((long) Arrays.stream(this.lastCPUTimes).average().orElse(0.0d)) / 1000;
    }
}
