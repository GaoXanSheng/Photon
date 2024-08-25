package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Configurable(name = "ldlib.gui.editor.group.transform")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/TransformTexture.class */
public abstract class TransformTexture implements IGuiTexture {
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 1.0d)
    protected float xOffset;
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 1.0d)
    protected float yOffset;
    @Configurable
    @NumberRange(range = {0.0d, 3.4028234663852886E38d})
    protected float scale = 1.0f;
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 5.0d)
    protected float rotation;

    @OnlyIn(Dist.CLIENT)
    protected abstract void drawInternal(PoseStack poseStack, int i, int i2, float f, float f2, int i3, int i4);

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public TransformTexture rotate(float degree) {
        this.rotation = degree;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public TransformTexture scale(float scale) {
        this.scale = scale;
        return this;
    }

    public TransformTexture transform(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    protected void preDraw(PoseStack stack, float x, float y, float width, float height) {
        stack.m_85836_();
        stack.m_85837_(this.xOffset, this.yOffset, 0.0d);
        stack.m_85837_(x + (width / 2.0f), y + (height / 2.0f), 0.0d);
        stack.m_85841_(this.scale, this.scale, 1.0f);
        stack.m_85845_(new Quaternion(0.0f, 0.0f, this.rotation, true));
        stack.m_85837_((-x) + ((-width) / 2.0f), (-y) + ((-height) / 2.0f), 0.0d);
    }

    @OnlyIn(Dist.CLIENT)
    protected void postDraw(PoseStack stack, float x, float y, float width, float height) {
        stack.m_85849_();
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public final void draw(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        preDraw(stack, x, y, width, height);
        drawInternal(stack, mouseX, mouseY, x, y, width, height);
        postDraw(stack, x, y, width, height);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public final void drawSubArea(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        preDraw(stack, x, y, width, height);
        drawSubAreaInternal(stack, x, y, width, height, drawnU, drawnV, drawnWidth, drawnHeight);
        postDraw(stack, x, y, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    protected void drawSubAreaInternal(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        drawInternal(stack, 0, 0, x, y, (int) width, (int) height);
    }
}
