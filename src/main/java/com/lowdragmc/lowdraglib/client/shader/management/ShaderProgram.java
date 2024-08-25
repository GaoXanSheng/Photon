package com.lowdragmc.lowdraglib.client.shader.management;

import com.lowdragmc.lowdraglib.client.shader.uniform.IUniformCallback;
import com.lowdragmc.lowdraglib.client.shader.uniform.UniformCache;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL20;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/management/ShaderProgram.class */
public class ShaderProgram {
    public final UniformCache uniformCache;
    private boolean unLinked;
    private IUniformCallback globalUniform;
    public final int programId = GL20.glCreateProgram();
    public final Set<Shader> shaders = new ReferenceOpenHashSet();
    public final LinkedHashMap<String, Integer> samplers = new LinkedHashMap<>();

    public ShaderProgram() {
        if (this.programId == 0) {
            throw new IllegalStateException("Unable to create ShaderProgram.");
        }
        this.uniformCache = new UniformCache(this.programId);
    }

    public ShaderProgram attach(Shader loader) {
        if (loader == null) {
            return this;
        }
        if (this.shaders.contains(loader)) {
            throw new IllegalStateException(String.format("Unable to attach Shader as it is already attached:\n%s", loader.source));
        }
        this.shaders.add(loader);
        loader.attachShader(this);
        this.unLinked = true;
        return this;
    }

    public void use(IUniformCallback callback) {
        use();
        callback.apply(this.uniformCache);
    }

    public void setGlobalUniform(IUniformCallback globalUniform) {
        this.globalUniform = globalUniform;
    }

    public void bindTexture(String samplerName, int textureId) {
        if (textureId > 0) {
            this.samplers.put(samplerName, Integer.valueOf(textureId));
        } else {
            this.samplers.remove(samplerName);
        }
    }

    public void bindTexture(String samplerName, ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            bindTexture(samplerName, 0);
            return;
        }
        AbstractTexture abstracttexture = Minecraft.m_91087_().m_91097_().m_118506_(resourceLocation);
        int textureId = abstracttexture.m_117963_();
        bindTexture(samplerName, textureId);
    }

    public void use() {
        if (this.unLinked) {
            this.uniformCache.invalidate();
            GL20.glLinkProgram(this.programId);
            if (GL20.glGetProgrami(this.programId, 35714) == 0) {
                throw new RuntimeException(String.format("ShaderProgram validation has failed!\n%s", GL20.glGetProgramInfoLog(this.programId, GL20.glGetProgrami(this.programId, 35716))));
            }
            this.unLinked = false;
        }
        GL20.glUseProgram(this.programId);
        if (!this.samplers.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, Integer> entry : this.samplers.entrySet()) {
                RenderSystem.m_69388_(33984 + i);
                RenderSystem.m_69493_();
                RenderSystem.m_69396_(entry.getValue().intValue());
                this.uniformCache.glUniform1I(entry.getKey(), i);
                i++;
            }
        }
        if (this.globalUniform != null) {
            this.globalUniform.apply(this.uniformCache);
        }
    }

    public void release() {
        if (!this.samplers.isEmpty()) {
            for (int i = 0; i < this.samplers.size(); i++) {
                RenderSystem.m_69388_(33984 + i);
                RenderSystem.m_69396_(0);
            }
        }
        GL20.glUseProgram(0);
    }

    public void delete() {
        if (this.programId != 0) {
            GL20.glDeleteProgram(this.programId);
        }
    }
}
