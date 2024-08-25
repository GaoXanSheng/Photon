package com.lowdragmc.lowdraglib.client.shader.management;

import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.client.shader.management.Shader;
import com.lowdragmc.lowdraglib.client.shader.uniform.IUniformCallback;
import com.lowdragmc.lowdraglib.utils.PositionedRect;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/management/ShaderManager.class */
public class ShaderManager {
    private static final ShaderManager INSTANCE = new ShaderManager();
    private final Reference2ReferenceMap<Shader, ShaderProgram> programs = new Reference2ReferenceOpenHashMap();
    private static TextureTarget TEMP_TARGET;
    private PositionedRect viewPort;

    public static ShaderManager getInstance() {
        return INSTANCE;
    }

    public static boolean allowedShader() {
        return true;
    }

    private ShaderManager() {
    }

    public static TextureTarget getTempTarget() {
        if (TEMP_TARGET == null) {
            TEMP_TARGET = new TextureTarget(1024, 1024, false, Minecraft.f_91002_);
            TEMP_TARGET.m_83936_(9729);
            TEMP_TARGET.m_83931_(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return TEMP_TARGET;
    }

    public void reload() {
        this.programs.forEach(shader, shaderProgram -> {
            shader.deleteShader();
            shaderProgram.delete();
        });
        this.programs.clear();
    }

    public void setViewPort(PositionedRect viewPort) {
        this.viewPort = viewPort;
    }

    public boolean hasViewPort() {
        return this.viewPort != null;
    }

    public void clearViewPort() {
        this.viewPort = null;
    }

    public RenderTarget renderFullImageInFramebuffer(RenderTarget fbo, Shader frag, IUniformCallback consumeCache, Consumer<ShaderProgram> programCreated) {
        if (fbo == null || frag == null || !allowedShader() || frag.shaderType != Shader.ShaderType.FRAGMENT) {
            return fbo;
        }
        fbo.m_83947_(true);
        ShaderProgram program = (ShaderProgram) this.programs.get(frag);
        if (program == null) {
            Reference2ReferenceMap<Shader, ShaderProgram> reference2ReferenceMap = this.programs;
            ShaderProgram shaderProgram = new ShaderProgram();
            program = shaderProgram;
            reference2ReferenceMap.put(frag, shaderProgram);
            program.attach(Shaders.IMAGE_V).attach(frag);
            if (programCreated != null) {
                programCreated.accept(program);
            }
        }
        program.use(cache -> {
            float time;
            Minecraft mc = Minecraft.m_91087_();
            if (mc.f_91074_ != null) {
                time = (mc.f_91074_.f_19797_ + mc.m_91296_()) / 20.0f;
            } else {
                time = ((float) System.currentTimeMillis()) / 1000.0f;
            }
            cache.glUniform1F("iTime", time);
            cache.glUniform2F("iResolution", fbo.f_83915_, fbo.f_83916_);
            if (consumeCache != null) {
                consumeCache.apply(cache);
            }
        });
        Tesselator tessellator = Tesselator.m_85913_();
        BufferBuilder buffer = tessellator.m_85915_();
        buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85814_);
        buffer.m_5483_(-1.0d, 1.0d, 0.0d).m_5752_();
        buffer.m_5483_(-1.0d, -1.0d, 0.0d).m_5752_();
        buffer.m_5483_(1.0d, -1.0d, 0.0d).m_5752_();
        buffer.m_5483_(1.0d, 1.0d, 0.0d).m_5752_();
        buffer.m_231175_();
        BufferUploader.m_231209_(buffer.m_231175_());
        program.release();
        if (this.viewPort != null) {
            RenderSystem.m_69949_(this.viewPort.position.x, this.viewPort.position.y, this.viewPort.size.width, this.viewPort.size.height);
        }
        return fbo;
    }
}
