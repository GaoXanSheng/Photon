package com.lowdragmc.lowdraglib.client.utils;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/utils/ShaderUtils.class */
public class ShaderUtils {
    private static final boolean DEBUG_LABEL_AVAILABLE = GL.getCapabilities().GL_KHR_debug;

    public static void fastBlit(RenderTarget from, RenderTarget to) {
        RenderSystem.m_187554_();
        GlStateManager.m_84300_(true, true, true, true);
        GlStateManager.m_84507_();
        GlStateManager.m_84298_(false);
        to.m_83947_(true);
        Shaders.getBlitShader().m_173350_("DiffuseSampler", Integer.valueOf(from.m_83975_()));
        Shaders.getBlitShader().m_173363_();
        GlStateManager.m_84525_();
        RenderSystem.m_69453_();
        Tesselator tesselator = RenderSystem.m_69883_();
        BufferBuilder bufferbuilder = tesselator.m_85915_();
        bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85814_);
        bufferbuilder.m_5483_(-1.0d, 1.0d, 0.0d).m_5752_();
        bufferbuilder.m_5483_(-1.0d, -1.0d, 0.0d).m_5752_();
        bufferbuilder.m_5483_(1.0d, -1.0d, 0.0d).m_5752_();
        bufferbuilder.m_5483_(1.0d, 1.0d, 0.0d).m_5752_();
        BufferUploader.m_231209_(bufferbuilder.m_231175_());
        Shaders.getBlitShader().m_173362_();
        GlStateManager.m_84298_(true);
        GlStateManager.m_84300_(true, true, true, true);
        GlStateManager.m_84513_();
    }

    public static void warpGLDebugLabel(String message, Runnable block) {
        if (DEBUG_LABEL_AVAILABLE && Platform.isDevEnv()) {
            GL43.glPushDebugGroup(33354, 0, message);
            block.run();
            GL43.glPopDebugGroup();
            return;
        }
        block.run();
    }
}
