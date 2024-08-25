package com.lowdragmc.photon.client.postprocessing;

import com.lowdragmc.photon.Photon;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/postprocessing/BloomEffect.class */
public class BloomEffect {
    private static int LAST_WIDTH;
    private static int LAST_HEIGHT;
    private static RenderTarget INPUT;
    private static RenderTarget OUTPUT;
    private static RenderTarget SWAP2A;
    private static RenderTarget SWAP4A;
    private static RenderTarget SWAP8A;
    private static RenderTarget SWAP2B;
    private static RenderTarget SWAP4B;
    private static RenderTarget SWAP8B;
    private static final Minecraft MC = Minecraft.m_91087_();
    private static final ShaderInstance SEPARABLE_BLUR = loadShader("photon:separable_blur");
    private static final ShaderInstance UNREAL_COMPOSITE = loadShader("photon:unreal_composite");

    private static ShaderInstance loadShader(String shaderName) {
        try {
            return new ShaderInstance(Minecraft.m_91087_().m_91098_(), shaderName, DefaultVertexFormat.f_85814_);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static RenderTarget getInput() {
        if (INPUT == null) {
            INPUT = resize(null, MC.m_91268_().m_85441_(), MC.m_91268_().m_85442_(), true);
            hookDepthBuffer(INPUT, MC.m_91385_().m_83980_());
        }
        return INPUT;
    }

    public static RenderTarget getOutput() {
        OUTPUT = resize(OUTPUT, MC.m_91268_().m_85441_(), MC.m_91268_().m_85442_(), false);
        return OUTPUT;
    }

    public static void hookDepthBuffer(RenderTarget fbo, int depthBuffer) {
        GlStateManager.m_84486_(36160, fbo.f_83920_);
        if (!Photon.isStencilEnabled(fbo)) {
            GlStateManager.m_84173_(36160, 36096, 3553, depthBuffer, 0);
        } else if (Photon.useCombinedDepthStencilAttachment()) {
            GlStateManager.m_84173_(36160, 33306, 3553, depthBuffer, 0);
        } else {
            GlStateManager.m_84173_(36160, 36096, 3553, depthBuffer, 0);
            GlStateManager.m_84173_(36160, 36128, 3553, depthBuffer, 0);
        }
    }

    public static void updateScreenSize(int width, int height) {
        if (LAST_WIDTH == width && LAST_HEIGHT == height) {
            return;
        }
        INPUT = resize(null, width, height, true);
        hookDepthBuffer(INPUT, MC.m_91385_().m_83980_());
        OUTPUT = resize(OUTPUT, width, height, false);
        SWAP2A = resize(SWAP2A, width / 2, height / 2, false);
        SWAP4A = resize(SWAP4A, width / 4, height / 4, false);
        SWAP8A = resize(SWAP8A, width / 8, height / 8, false);
        SWAP2B = resize(SWAP2B, width / 2, height / 2, false);
        SWAP4B = resize(SWAP4B, width / 4, height / 4, false);
        SWAP8B = resize(SWAP8B, width / 8, height / 8, false);
        LAST_WIDTH = width;
        LAST_HEIGHT = height;
    }

    private static RenderTarget resize(@Nullable RenderTarget target, int width, int height, boolean useDepth) {
        if (target == null) {
            target = new TextureTarget(width, height, useDepth, Minecraft.f_91002_);
            target.m_83931_(0.0f, 0.0f, 0.0f, 0.0f);
        }
        target.m_83941_(width, height, Minecraft.f_91002_);
        target.m_83936_(9729);
        return target;
    }

    public static void renderBloom(int width, int height, int background, int input, RenderTarget output) {
        updateScreenSize(width, height);
        RenderSystem.m_69444_(true, true, true, true);
        RenderSystem.m_69465_();
        RenderSystem.m_69458_(false);
        RenderSystem.m_69478_();
        RenderSystem.m_69453_();
        SEPARABLE_BLUR.m_173350_("DiffuseSampler", Integer.valueOf(input));
        SEPARABLE_BLUR.m_173356_("BlurDir").m_7971_(1.0f, 0.0f);
        SEPARABLE_BLUR.m_173356_("Radius").m_142617_(3);
        SEPARABLE_BLUR.m_173356_("OutSize").m_7971_(SWAP2A.f_83915_, SWAP2A.f_83916_);
        blitShader(SEPARABLE_BLUR, SWAP2A);
        SEPARABLE_BLUR.m_173350_("DiffuseSampler", SWAP2A);
        SEPARABLE_BLUR.m_173356_("BlurDir").m_7971_(0.0f, 1.0f);
        SEPARABLE_BLUR.m_173356_("Radius").m_142617_(3);
        SEPARABLE_BLUR.m_173356_("OutSize").m_7971_(SWAP2B.f_83915_, SWAP2B.f_83916_);
        blitShader(SEPARABLE_BLUR, SWAP2B);
        SEPARABLE_BLUR.m_173350_("DiffuseSampler", SWAP2B);
        SEPARABLE_BLUR.m_173356_("BlurDir").m_7971_(1.0f, 0.0f);
        SEPARABLE_BLUR.m_173356_("Radius").m_142617_(5);
        SEPARABLE_BLUR.m_173356_("OutSize").m_7971_(SWAP4A.f_83915_, SWAP4A.f_83916_);
        blitShader(SEPARABLE_BLUR, SWAP4A);
        SEPARABLE_BLUR.m_173350_("DiffuseSampler", SWAP4A);
        SEPARABLE_BLUR.m_173356_("BlurDir").m_7971_(0.0f, 1.0f);
        SEPARABLE_BLUR.m_173356_("Radius").m_142617_(5);
        SEPARABLE_BLUR.m_173356_("OutSize").m_7971_(SWAP4B.f_83915_, SWAP4B.f_83916_);
        blitShader(SEPARABLE_BLUR, SWAP4B);
        SEPARABLE_BLUR.m_173350_("DiffuseSampler", SWAP4B);
        SEPARABLE_BLUR.m_173356_("BlurDir").m_7971_(1.0f, 0.0f);
        SEPARABLE_BLUR.m_173356_("Radius").m_142617_(7);
        SEPARABLE_BLUR.m_173356_("OutSize").m_7971_(SWAP8A.f_83915_, SWAP8A.f_83916_);
        blitShader(SEPARABLE_BLUR, SWAP8A);
        SEPARABLE_BLUR.m_173350_("DiffuseSampler", SWAP8A);
        SEPARABLE_BLUR.m_173356_("BlurDir").m_7971_(0.0f, 1.0f);
        SEPARABLE_BLUR.m_173356_("Radius").m_142617_(7);
        SEPARABLE_BLUR.m_173356_("OutSize").m_7971_(SWAP8B.f_83915_, SWAP8B.f_83916_);
        blitShader(SEPARABLE_BLUR, SWAP8B);
        UNREAL_COMPOSITE.m_173350_("DiffuseSampler", Integer.valueOf(background));
        UNREAL_COMPOSITE.m_173350_("HighLight", Integer.valueOf(input));
        UNREAL_COMPOSITE.m_173350_("BlurTexture1", SWAP2B);
        UNREAL_COMPOSITE.m_173350_("BlurTexture2", SWAP4B);
        UNREAL_COMPOSITE.m_173350_("BlurTexture3", SWAP8B);
        UNREAL_COMPOSITE.m_173356_("BloomRadius").m_5985_(1.0f);
        blitShader(UNREAL_COMPOSITE, output);
        RenderSystem.m_69458_(true);
        RenderSystem.m_69444_(true, true, true, true);
        RenderSystem.m_69482_();
    }

    public static void blitShader(ShaderInstance shaderInstance, RenderTarget dist) {
        dist.m_83954_(Minecraft.f_91002_);
        dist.m_83947_(false);
        shaderInstance.m_173363_();
        Tesselator tesselator = RenderSystem.m_69883_();
        BufferBuilder bufferbuilder = tesselator.m_85915_();
        bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85814_);
        bufferbuilder.m_5483_(-1.0d, 1.0d, 0.0d).m_5752_();
        bufferbuilder.m_5483_(-1.0d, -1.0d, 0.0d).m_5752_();
        bufferbuilder.m_5483_(1.0d, -1.0d, 0.0d).m_5752_();
        bufferbuilder.m_5483_(1.0d, 1.0d, 0.0d).m_5752_();
        BufferUploader.m_231209_(bufferbuilder.m_231175_());
        shaderInstance.m_173362_();
    }
}
