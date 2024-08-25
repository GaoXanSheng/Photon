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

@LDLRegister(name = "color_border_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ColorBorderTexture.class */
public class ColorBorderTexture extends TransformTexture {
    @NumberColor
    @Configurable
    public int color;
    @Configurable
    @NumberRange(range = {-100.0d, 100.0d})
    public int border;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusLTInner;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusLBInner;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusRTInner;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusRBInner;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusLTOuter;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusLBOuter;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusRTOuter;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d}, wheel = 1.0d)
    public float radiusRBOuter;

    public void setRadiusLTInner(float radiusLTInner) {
        this.radiusLTInner = radiusLTInner;
    }

    public void setRadiusLBInner(float radiusLBInner) {
        this.radiusLBInner = radiusLBInner;
    }

    public void setRadiusRTInner(float radiusRTInner) {
        this.radiusRTInner = radiusRTInner;
    }

    public void setRadiusRBInner(float radiusRBInner) {
        this.radiusRBInner = radiusRBInner;
    }

    public void setRadiusLTOuter(float radiusLTOuter) {
        this.radiusLTOuter = radiusLTOuter;
    }

    public void setRadiusLBOuter(float radiusLBOuter) {
        this.radiusLBOuter = radiusLBOuter;
    }

    public void setRadiusRTOuter(float radiusRTOuter) {
        this.radiusRTOuter = radiusRTOuter;
    }

    public void setRadiusRBOuter(float radiusRBOuter) {
        this.radiusRBOuter = radiusRBOuter;
    }

    public ColorBorderTexture() {
        this(-2, 1326448095);
    }

    public ColorBorderTexture(int border, int color) {
        this.color = color;
        this.border = border;
    }

    public ColorBorderTexture(int border, Color color) {
        this.color = color.getRGB();
        this.border = border;
    }

    public ColorBorderTexture setBorder(int border) {
        this.border = border;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public ColorBorderTexture setColor(int color) {
        this.color = color;
        return this;
    }

    public ColorBorderTexture setRadius(float radius) {
        this.radiusLBInner = radius;
        this.radiusRTInner = radius;
        this.radiusRBInner = radius;
        this.radiusLTInner = radius;
        this.radiusLBOuter = radius;
        this.radiusRTOuter = radius;
        this.radiusRBOuter = radius;
        this.radiusLTOuter = radius;
        return this;
    }

    public ColorBorderTexture setLeftRadius(float radius) {
        setLeftRadiusInner(radius);
        setLeftRadiusOuter(radius);
        return this;
    }

    public ColorBorderTexture setRightRadius(float radius) {
        setRightRadiusInner(radius);
        setRightRadiusOuter(radius);
        return this;
    }

    public ColorBorderTexture setTopRadius(float radius) {
        setTopRadiusInner(radius);
        setTopRadiusOuter(radius);
        return this;
    }

    public ColorBorderTexture setBottomRadius(float radius) {
        setBottomRadiusInner(radius);
        setBottomRadiusOuter(radius);
        return this;
    }

    public ColorBorderTexture setLeftRadiusInner(float radius) {
        this.radiusLBInner = radius;
        this.radiusLTInner = radius;
        return this;
    }

    public ColorBorderTexture setRightRadiusInner(float radius) {
        this.radiusRTInner = radius;
        this.radiusRBInner = radius;
        return this;
    }

    public ColorBorderTexture setTopRadiusInner(float radius) {
        this.radiusRTInner = radius;
        this.radiusLTInner = radius;
        return this;
    }

    public ColorBorderTexture setBottomRadiusInner(float radius) {
        this.radiusLBInner = radius;
        this.radiusRBInner = radius;
        return this;
    }

    public ColorBorderTexture setLeftRadiusOuter(float radius) {
        this.radiusLBOuter = radius;
        this.radiusLTOuter = radius;
        return this;
    }

    public ColorBorderTexture setRightRadiusOuter(float radius) {
        this.radiusRTOuter = radius;
        this.radiusRBOuter = radius;
        return this;
    }

    public ColorBorderTexture setTopRadiusOuter(float radius) {
        this.radiusRTOuter = radius;
        this.radiusLTOuter = radius;
        return this;
    }

    public ColorBorderTexture setBottomRadiusOuter(float radius) {
        this.radiusLBOuter = radius;
        this.radiusRBOuter = radius;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
        if (this.radiusLTInner > 0.0f || this.radiusLBInner > 0.0f || this.radiusRTInner > 0.0f || this.radiusRBInner > 0.0f || this.radiusLTOuter > 0.0f || this.radiusLBOuter > 0.0f || this.radiusRTOuter > 0.0f || this.radiusRBOuter > 0.0f) {
            float radius = Math.min(width, height) / 2.0f;
            DrawerHelper.drawFrameRoundBox(stack, Rect.ofRelative((int) x, width, (int) y, height), this.border, new Vector4f(Math.min(radius, this.radiusRTInner), Math.min(this.radiusRBInner, radius), Math.min(radius, this.radiusLTInner), Math.min(radius, this.radiusLBInner)), new Vector4f(Math.min(radius, this.radiusRTOuter), Math.min(this.radiusRBOuter, radius), Math.min(radius, this.radiusLTOuter), Math.min(radius, this.radiusLBOuter)), this.color);
            return;
        }
        DrawerHelper.drawBorder(stack, (int) x, (int) y, width, height, this.color, this.border);
    }
}
