package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/WidgetTexture.class */
public class WidgetTexture extends TransformTexture {
    private final Widget widget;
    private int centerX;
    private int centerY;
    private boolean isDragging;
    private boolean fixedCenter;

    public WidgetTexture(Widget widget) {
        this.widget = widget;
        this.centerX = widget.getPosition().x + (widget.getSize().width / 2);
        this.centerY = widget.getPosition().y + (widget.getSize().height / 2);
    }

    public WidgetTexture(int mouseX, int mouseY, Widget widget) {
        this.widget = widget;
        this.centerX = mouseX;
        this.centerY = mouseY;
        this.isDragging = true;
        this.fixedCenter = true;
    }

    public WidgetTexture setDragging(boolean dragging) {
        this.isDragging = dragging;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        int xOffset;
        int yOffset;
        float scale = 1.0f;
        if (!this.fixedCenter) {
            this.centerX = this.widget.getPosition().x + (this.widget.getSize().width / 2);
            this.centerY = this.widget.getPosition().y + (this.widget.getSize().height / 2);
        }
        if (this.isDragging) {
            xOffset = mouseX - this.centerX;
            yOffset = mouseY - this.centerY;
        } else {
            xOffset = (int) ((x + (width / 2)) - this.centerX);
            yOffset = (int) ((y + (height / 2)) - this.centerY);
            float scaleW = (width * 1.0f) / this.widget.getSize().width;
            float scaleH = (height * 1.0f) / this.widget.getSize().height;
            scale = Math.min(scaleW, scaleH);
        }
        float particleTick = Minecraft.m_91087_().m_91296_();
        stack.m_85836_();
        stack.m_85837_(x + (width / 2.0f), y + (height / 2.0f), 0.0d);
        stack.m_85841_(scale, scale, 1.0f);
        stack.m_85837_((-x) + ((-width) / 2.0f), (-y) + ((-height) / 2.0f), 0.0d);
        stack.m_85837_(xOffset, yOffset, 0.0d);
        this.widget.drawInBackground(stack, this.centerX, this.centerY, particleTick);
        this.widget.drawInForeground(stack, this.centerX, this.centerY, particleTick);
        stack.m_85849_();
    }
}
