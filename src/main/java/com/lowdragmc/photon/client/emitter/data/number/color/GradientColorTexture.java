package com.lowdragmc.photon.client.emitter.data.number.color;

import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.utils.GradientColor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import java.util.function.Function;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/color/GradientColorTexture.class */
public class GradientColorTexture extends TransformTexture {
    public final GradientColor gradientColor;

    public GradientColorTexture(GradientColor gradientColor) {
        this.gradientColor = gradientColor;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float posx, float posy, int width, int height) {
        RenderSystem.m_69472_();
        RenderSystem.m_69478_();
        RenderSystem.m_69453_();
        Matrix4f mat = stack.m_85850_().m_85861_();
        Tesselator tesselator = Tesselator.m_85913_();
        BufferBuilder buffer = tesselator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172811_);
        buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85815_);
        Function<Float, Float> getXPosition = coordX -> {
            return Float.valueOf(posx + (width * coordX.floatValue()));
        };
        float yh = height + posy;
        RandomGradientColorTexture.drawGradient(posy, width, mat, buffer, getXPosition, yh, this.gradientColor);
        tesselator.m_85914_();
        RenderSystem.m_69493_();
    }
}
