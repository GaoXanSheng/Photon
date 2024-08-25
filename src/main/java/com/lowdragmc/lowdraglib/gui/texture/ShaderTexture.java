package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.client.shader.management.Shader;
import com.lowdragmc.lowdraglib.client.shader.management.ShaderManager;
import com.lowdragmc.lowdraglib.client.shader.management.ShaderProgram;
import com.lowdragmc.lowdraglib.client.shader.uniform.UniformCache;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "shader_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ShaderTexture.class */
public class ShaderTexture extends TransformTexture {
    private static final Map<ResourceLocation, ShaderTexture> CACHE = new HashMap();
    @Configurable(name = "ldlib.gui.editor.name.resource", tips = {"ldlib.gui.editor.tips.shader_location"})
    public ResourceLocation location;
    @OnlyIn(Dist.CLIENT)
    private ShaderProgram program;
    @OnlyIn(Dist.CLIENT)
    private Shader shader;
    @Configurable(tips = {"ldlib.gui.editor.tips.shader_resolution"})
    @NumberRange(range = {1.0d, 3.0d})
    private float resolution;
    @NumberColor
    @Configurable
    private int color;
    private Consumer<UniformCache> uniformCache;
    private final boolean isRaw;

    private ShaderTexture(boolean isRaw) {
        this.resolution = 2.0f;
        this.color = -1;
        this.isRaw = isRaw;
    }

    public ShaderTexture() {
        this(false);
        Shader shader;
        this.location = new ResourceLocation("ldlib:fbm");
        if (LDLib.isRemote() && ShaderManager.allowedShader() && (shader = Shaders.load(Shader.ShaderType.FRAGMENT, this.location)) != null) {
            this.program = new ShaderProgram();
            this.shader = shader;
            this.program.attach(Shaders.GUI_IMAGE_V);
            this.program.attach(shader);
        }
    }

    public static void clearCache() {
        CACHE.values().forEach((v0) -> {
            v0.dispose();
        });
        CACHE.clear();
    }

    public void dispose() {
        if (this.isRaw && this.shader != null) {
            this.shader.deleteShader();
        }
        if (this.program != null) {
            this.program.delete();
        }
        this.shader = null;
        this.program = null;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public ShaderTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @ConfigSetter(field = "location")
    public void updateShader(ResourceLocation location) {
        if (LDLib.isRemote() && ShaderManager.allowedShader()) {
            this.location = location;
            dispose();
            Shader shader = Shaders.load(Shader.ShaderType.FRAGMENT, location);
            if (shader == null) {
                return;
            }
            this.program = new ShaderProgram();
            this.shader = shader;
            this.program.attach(Shaders.GUI_IMAGE_V);
            this.program.attach(shader);
        }
    }

    public void updateRawShader(String rawShader) {
        if (LDLib.isRemote() && ShaderManager.allowedShader()) {
            dispose();
            this.shader = new Shader(Shader.ShaderType.FRAGMENT, rawShader).compileShader();
            this.program = new ShaderProgram();
            this.program.attach(Shaders.GUI_IMAGE_V);
            this.program.attach(this.shader);
        }
    }

    public String getRawShader() {
        if (LDLib.isRemote() && ShaderManager.allowedShader() && this.shader != null) {
            return this.shader.source;
        }
        return "";
    }

    @OnlyIn(Dist.CLIENT)
    private ShaderTexture(Shader shader, boolean isRaw) {
        this.resolution = 2.0f;
        this.color = -1;
        this.isRaw = isRaw;
        if (shader == null) {
            return;
        }
        this.program = new ShaderProgram();
        this.shader = shader;
        this.program.attach(Shaders.GUI_IMAGE_V);
        this.program.attach(shader);
    }

    public static ShaderTexture createShader(ResourceLocation location) {
        ShaderTexture texture;
        if (CACHE.containsKey(location) && CACHE.get(location).shader != null) {
            return CACHE.get(location);
        }
        if (LDLib.isRemote() && ShaderManager.allowedShader()) {
            Shader shader = Shaders.load(Shader.ShaderType.FRAGMENT, location);
            texture = new ShaderTexture(shader, false);
            CACHE.put(location, texture);
        } else {
            texture = new ShaderTexture(false);
        }
        texture.location = location;
        return texture;
    }

    public static ShaderTexture createRawShader(String rawShader) {
        if (LDLib.isRemote() && ShaderManager.allowedShader()) {
            Shader shader = new Shader(Shader.ShaderType.FRAGMENT, rawShader).compileShader();
            return new ShaderTexture(shader, true);
        }
        return new ShaderTexture(true);
    }

    public ShaderTexture setUniformCache(Consumer<UniformCache> uniformCache) {
        this.uniformCache = uniformCache;
        return this;
    }

    public ShaderTexture setResolution(float resolution) {
        this.resolution = resolution;
        return this;
    }

    public float getResolution() {
        return this.resolution;
    }

    public void bindTexture(String samplerName, int id) {
        if (LDLib.isRemote() && ShaderManager.allowedShader() && this.program != null) {
            this.program.bindTexture(samplerName, id);
        }
    }

    public void bindTexture(String samplerName, ResourceLocation location) {
        if (LDLib.isRemote() && ShaderManager.allowedShader() && this.program != null) {
            this.program.bindTexture(samplerName, location);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (this.program != null) {
            try {
                this.program.use(cache -> {
                    float time;
                    Minecraft mc = Minecraft.m_91087_();
                    if (mc.f_91074_ != null) {
                        time = (mc.f_91074_.f_19797_ + mc.m_91296_()) / 20.0f;
                    } else {
                        time = ((float) System.currentTimeMillis()) / 1000.0f;
                    }
                    float mX = Mth.m_14036_(mouseX - mouseX, 0.0f, x);
                    float mY = Mth.m_14036_(width - mouseY, 0.0f, y);
                    height.glUniformMatrix4F("ModelViewMat", RenderSystem.m_157190_());
                    height.glUniformMatrix4F("ProjMat", RenderSystem.m_157192_());
                    height.glUniform2F("iResolution", x * this.resolution, y * this.resolution);
                    height.glUniform2F("iMouse", mX * this.resolution, mY * this.resolution);
                    height.glUniform1F("iTime", time);
                    if (this.uniformCache != null) {
                        this.uniformCache.accept(height);
                    }
                });
                RenderSystem.m_69478_();
                Tesselator tessellator = Tesselator.m_85913_();
                BufferBuilder buffer = tessellator.m_85915_();
                Matrix4f mat = stack.m_85850_().m_85861_();
                buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85819_);
                buffer.m_85982_(mat, x, y + height, 0.0f).m_7421_(0.0f, 0.0f).m_193479_(this.color).m_5752_();
                buffer.m_85982_(mat, x + width, y + height, 0.0f).m_7421_(1.0f, 0.0f).m_193479_(this.color).m_5752_();
                buffer.m_85982_(mat, x + width, y, 0.0f).m_7421_(1.0f, 1.0f).m_193479_(this.color).m_5752_();
                buffer.m_85982_(mat, x, y, 0.0f).m_7421_(0.0f, 1.0f).m_193479_(this.color).m_5752_();
                BufferUploader.m_231209_(buffer.m_231175_());
                this.program.release();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                dispose();
                return;
            }
        }
        DrawerHelper.drawText(stack, "Error compiling shader", x + 2.0f, y + 2.0f, 1.0f, -65536);
    }
}
