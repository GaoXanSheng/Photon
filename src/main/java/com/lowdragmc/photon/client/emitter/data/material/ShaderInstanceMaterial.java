package com.lowdragmc.photon.client.emitter.data.material;

import com.lowdragmc.lowdraglib.client.shader.management.ShaderManager;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.photon.Photon;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/material/ShaderInstanceMaterial.class */
public abstract class ShaderInstanceMaterial implements IMaterial {
    public final ShaderTexture preview = new ShaderTexture();

    public abstract ShaderInstance getShader();

    public void setupUniform() {
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public void begin(boolean isInstancing) {
        if (Photon.isUsingShaderPack() && Editor.INSTANCE == null) {
            ShaderInstance lastShader = RenderSystem.m_157196_();
            ShaderManager.getTempTarget().m_83954_(false);
            ShaderManager.getTempTarget().m_83947_(true);
            int lastID = GL11.glGetInteger(36006);
            Tesselator tessellator = Tesselator.m_85913_();
            BufferBuilder bufferbuilder = tessellator.m_85915_();
            RenderSystem.m_157427_(this::getShader);
            setupUniform();
            RenderSystem.m_157183_();
            Matrix4f mat = new Matrix4f();
            mat.m_27624_();
            RenderSystem.m_157425_(mat);
            PoseStack stack = RenderSystem.m_157191_();
            stack.m_85836_();
            stack.m_166856_();
            RenderSystem.m_157182_();
            LightTexture lightTexture = Minecraft.m_91087_().f_91063_.m_109154_();
            lightTexture.m_109896_();
            bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85813_);
            bufferbuilder.m_5483_(-1.0d, -1.0d, 0.0d).m_7421_(0.0f, 0.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            bufferbuilder.m_5483_(1.0d, -1.0d, 0.0d).m_7421_(0.0f + 1.0f, 0.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            bufferbuilder.m_5483_(1.0d, 1.0d, 0.0d).m_7421_(0.0f + 1.0f, 0.0f + 1.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            bufferbuilder.m_5483_(-1.0d, 1.0d, 0.0d).m_7421_(0.0f, 0.0f + 1.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            tessellator.m_85914_();
            lightTexture.m_109891_();
            RenderSystem.m_157424_();
            stack.m_85849_();
            RenderSystem.m_157182_();
            GlStateManager.m_84486_(36160, lastID);
            if (!ShaderManager.getInstance().hasViewPort()) {
                RenderTarget mainTarget = Minecraft.m_91087_().m_91385_();
                GlStateManager.m_84430_(0, 0, mainTarget.f_83917_, mainTarget.f_83918_);
            }
            RenderSystem.m_157453_(0, ShaderManager.getTempTarget().m_83975_());
            RenderSystem.m_157427_(() -> {
                return lastShader;
            });
            return;
        }
        RenderSystem.m_157427_(this::getShader);
        setupUniform();
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public void end(boolean isInstancing) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.photon.client.emitter.data.material.IMaterial, com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT */
    public final CompoundTag mo129serializeNBT() {
        return super.mo129serializeNBT();
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public IGuiTexture preview() {
        return this.preview;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/material/ShaderInstanceMaterial$ShaderTexture.class */
    public class ShaderTexture implements IGuiTexture {
        public ShaderTexture() {
        }

        @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
        public void draw(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
            Tesselator tessellator = Tesselator.m_85913_();
            BufferBuilder bufferbuilder = tessellator.m_85915_();
            ShaderInstanceMaterial.this.begin(false);
            LightTexture lightTexture = Minecraft.m_91087_().f_91063_.m_109154_();
            lightTexture.m_109896_();
            Matrix4f mat = stack.m_85850_().m_85861_();
            bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85813_);
            bufferbuilder.m_85982_(mat, x, y + height, 0.0f).m_7421_(0.0f, 0.0f + 1.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            bufferbuilder.m_85982_(mat, x + width, y + height, 0.0f).m_7421_(0.0f + 1.0f, 0.0f + 1.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            bufferbuilder.m_85982_(mat, x + width, y, 0.0f).m_7421_(0.0f + 1.0f, 0.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            bufferbuilder.m_85982_(mat, x, y, 0.0f).m_7421_(0.0f, 0.0f).m_193479_(-1).m_85969_(15728880).m_5752_();
            tessellator.m_85914_();
            lightTexture.m_109891_();
        }
    }
}
