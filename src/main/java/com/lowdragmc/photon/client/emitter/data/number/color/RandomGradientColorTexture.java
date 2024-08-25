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

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/color/RandomGradientColorTexture.class */
public class RandomGradientColorTexture extends TransformTexture {
    public final GradientColor gradientColor0;
    public final GradientColor gradientColor1;

    public RandomGradientColorTexture(GradientColor gradientColor0, GradientColor gradientColor1) {
        this.gradientColor0 = gradientColor0;
        this.gradientColor1 = gradientColor1;
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
        float yh = (height / 2.0f) + posy;
        drawGradient(posy, width, mat, buffer, getXPosition, yh, this.gradientColor0);
        float posy2 = posy + (height / 2.0f);
        float yh2 = (height / 2.0f) + posy2;
        drawGradient(posy2, width, mat, buffer, getXPosition, yh2, this.gradientColor1);
        tesselator.m_85914_();
        RenderSystem.m_69493_();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void drawGradient(float posy, int width, Matrix4f mat, BufferBuilder buffer, Function<Float, Float> getXPosition, float yh, GradientColor gradientColor) {
        for (int i = 0; i < width; i++) {
            Float x = getXPosition.apply(Float.valueOf((i * 1.0f) / width));
            Float xw = getXPosition.apply(Float.valueOf((i + 1.0f) / width));
            int startColor = gradientColor.getColor((i * 1.0f) / width);
            int endColor = gradientColor.getColor((i + 1.0f) / width);
            float startAlpha = ((startColor >> 24) & 255) / 255.0f;
            float startRed = ((startColor >> 16) & 255) / 255.0f;
            float startGreen = ((startColor >> 8) & 255) / 255.0f;
            float startBlue = (startColor & 255) / 255.0f;
            float endAlpha = ((endColor >> 24) & 255) / 255.0f;
            float endRed = ((endColor >> 16) & 255) / 255.0f;
            float endGreen = ((endColor >> 8) & 255) / 255.0f;
            float endBlue = (endColor & 255) / 255.0f;
            buffer.m_85982_(mat, xw.floatValue(), posy, 0.0f).m_85950_(endRed, endGreen, endBlue, endAlpha).m_5752_();
            buffer.m_85982_(mat, x.floatValue(), posy, 0.0f).m_85950_(startRed, startGreen, startBlue, startAlpha).m_5752_();
            buffer.m_85982_(mat, x.floatValue(), yh, 0.0f).m_85950_(startRed, startGreen, startBlue, startAlpha).m_5752_();
            buffer.m_85982_(mat, xw.floatValue(), yh, 0.0f).m_85950_(endRed, endGreen, endBlue, endAlpha).m_5752_();
        }
    }
}
