package com.lowdragmc.photon.client.emitter.data.number.curve;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/curve/RandomCurveTexture.class */
public class RandomCurveTexture extends TransformTexture {
    private final ECBCurves curves0;
    private final ECBCurves curves1;
    private int color = ColorPattern.T_GREEN.color;
    private float width = 0.5f;

    public void setWidth(float width) {
        this.width = width;
    }

    public RandomCurveTexture(ECBCurves curves0, ECBCurves curves1) {
        this.curves0 = curves0;
        this.curves1 = curves1;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public RandomCurveTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack poseStack, int mouseX, int mouseY, float x, float y, int width, int height) {
        BufferBuilder bufferBuilder = Tesselator.m_85913_().m_85915_();
        RenderSystem.m_69478_();
        RenderSystem.m_69472_();
        RenderSystem.m_69453_();
        RenderSystem.m_157427_(GameRenderer::m_172811_);
        bufferBuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85815_);
        Matrix4f matrix = poseStack.m_85850_().m_85861_();
        Function<Vec2, Vec2> getPointPosition = coord -> {
            return new Vec2(x + (width * coord.f_82470_), y + (height * (1.0f - coord.f_82471_)));
        };
        for (int i = 0; i < width; i++) {
            float x0 = (i * 1.0f) / width;
            float x1 = ((i + 1) * 1.0f) / width;
            Vec2 p0 = getPointPosition.apply(new Vec2(x0, this.curves0.getCurveY(x0)));
            Vec2 p1 = getPointPosition.apply(new Vec2(x1, this.curves0.getCurveY(x1)));
            Vec2 p2 = getPointPosition.apply(new Vec2(x1, this.curves1.getCurveY(x1)));
            Vec2 p3 = getPointPosition.apply(new Vec2(x0, this.curves1.getCurveY(x0)));
            bufferBuilder.m_85982_(matrix, p0.f_82470_, p0.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p1.f_82470_, p1.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p2.f_82470_, p2.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p3.f_82470_, p3.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p3.f_82470_, p3.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p2.f_82470_, p2.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p1.f_82470_, p1.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
            bufferBuilder.m_85982_(matrix, p0.f_82470_, p0.f_82471_, 0.0f).m_193479_(ColorPattern.T_RED.color).m_5752_();
        }
        BufferUploader.m_231202_(bufferBuilder.m_231175_());
        RenderSystem.m_69493_();
        renderLines(poseStack, this.curves0, x, y, width, height);
        renderLines(poseStack, this.curves1, x, y, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    private void renderLines(PoseStack poseStack, ECBCurves curves, float x, float y, int width, int height) {
        List<Vec2> points = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            float coordX = (i * 1.0f) / width;
            points.add(new Vec2(coordX, curves.getCurveY(coordX)));
        }
        points.add(new Vec2(1.0f, curves.getCurveY(1.0f)));
        DrawerHelper.drawLines(poseStack, points.stream().map(coord -> {
            return new Vec2(x + (width * coord.f_82470_), y + (height * (1.0f - coord.f_82471_)));
        }).toList(), this.color, this.color, this.width);
    }
}
