package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector4f;
import java.awt.Color;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "color_rect_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ColorRectTexture.class */
public class ColorRectTexture extends TransformTexture {
    @NumberColor
    @Configurable
    public int color;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusLT;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusLB;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusRT;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusRB;

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public ColorRectTexture setColor(int color) {
        this.color = color;
        return this;
    }

    public ColorRectTexture setRadiusLT(float radiusLT) {
        this.radiusLT = radiusLT;
        return this;
    }

    public ColorRectTexture setRadiusLB(float radiusLB) {
        this.radiusLB = radiusLB;
        return this;
    }

    public ColorRectTexture setRadiusRT(float radiusRT) {
        this.radiusRT = radiusRT;
        return this;
    }

    public ColorRectTexture setRadiusRB(float radiusRB) {
        this.radiusRB = radiusRB;
        return this;
    }

    public ColorRectTexture() {
        this(1326448095);
    }

    public ColorRectTexture(int color) {
        this.color = color;
    }

    public ColorRectTexture(Color color) {
        this.color = color.getRGB();
    }

    public ColorRectTexture setRadius(float radius) {
        this.radiusLB = radius;
        this.radiusRT = radius;
        this.radiusRB = radius;
        this.radiusLT = radius;
        return this;
    }

    public ColorRectTexture setLeftRadius(float radius) {
        this.radiusLB = radius;
        this.radiusLT = radius;
        return this;
    }

    public ColorRectTexture setRightRadius(float radius) {
        this.radiusRT = radius;
        this.radiusRB = radius;
        return this;
    }

    public ColorRectTexture setTopRadius(float radius) {
        this.radiusRT = radius;
        this.radiusLT = radius;
        return this;
    }

    public ColorRectTexture setBottomRadius(float radius) {
        this.radiusLB = radius;
        this.radiusRB = radius;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
        if (this.radiusLT > 0.0f || this.radiusLB > 0.0f || this.radiusRT > 0.0f || this.radiusRB > 0.0f) {
            float radius = Math.min(width, height) / 2.0f;
            DrawerHelper.drawRoundBox(stack, Rect.ofRelative((int) x, width, (int) y, height), new Vector4f(Math.min(radius, this.radiusRT), Math.min(this.radiusRB, radius), Math.min(radius, this.radiusLT), Math.min(radius, this.radiusLB)), this.color);
            return;
        }
        DrawerHelper.drawSolidRect(stack, (int) x, (int) y, width, height, this.color);
    }
}
