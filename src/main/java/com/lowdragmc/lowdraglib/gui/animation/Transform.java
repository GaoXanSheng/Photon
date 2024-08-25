package com.lowdragmc.lowdraglib.gui.animation;

import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.interpolate.IEase;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import javax.annotation.Nonnull;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/animation/Transform.class */
public class Transform extends Animation {
    protected int xOffset;
    protected int yOffset;
    protected float scale = 1.0f;
    protected boolean in;

    public Transform scale(float scale) {
        this.scale = scale;
        return this;
    }

    public Transform offset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    public Transform setScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Transform duration(long duration) {
        return (Transform) super.duration(duration);
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Transform delay(long delay) {
        return (Transform) super.delay(delay);
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Transform ease(IEase ease) {
        return (Transform) super.ease(ease);
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Transform onUpdate(FloatConsumer onUpdate) {
        return (Transform) super.onUpdate(onUpdate);
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Animation onFinish(Runnable onFinish) {
        return super.onFinish(onFinish);
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Animation size(@Nullable Size size) {
        return super.size(size);
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    public Animation position(@Nullable Position position) {
        return super.position(position);
    }

    public boolean isIn() {
        return this.in;
    }

    public boolean isOut() {
        return !this.in;
    }

    public Animation setIn() {
        this.in = true;
        return this;
    }

    public Animation setOut() {
        this.in = false;
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public void pre(@NotNull PoseStack poseStack) {
        poseStack.m_85836_();
        Position position = this.widget.getPosition();
        Size size = this.widget.getSize();
        float oX = position.x + (size.width / 2.0f);
        float oY = position.y + (size.height / 2.0f);
        if (isIn()) {
            poseStack.m_85837_(this.xOffset * (1.0f - getTime()), this.yOffset * (1.0f - getTime()), 0.0d);
        } else {
            poseStack.m_85837_(this.xOffset * getTime(), this.yOffset * getTime(), 0.0d);
        }
        poseStack.m_85837_(oX, oY, 0.0d);
        if (isIn()) {
            poseStack.m_85841_(this.scale + ((1.0f - this.scale) * getTime()), this.scale + ((1.0f - this.scale) * getTime()), 1.0f);
        } else {
            poseStack.m_85841_(this.scale + ((1.0f - this.scale) * (1.0f - getTime())), this.scale + ((1.0f - this.scale) * (1.0f - getTime())), 1.0f);
        }
        poseStack.m_85837_(-oX, -oY, 0.0d);
    }

    @OnlyIn(Dist.CLIENT)
    public void post(@NotNull PoseStack poseStack) {
        poseStack.m_85849_();
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        float tickTime = getTick();
        if (tickTime >= ((float) this.delay)) {
            if (this.interpolator != null) {
                this.interpolator.update(tickTime);
            }
            pre(poseStack);
            this.widget.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
            post(poseStack);
        } else if (isOut()) {
            this.widget.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.animation.Animation
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        float tickTime = getTick();
        if (tickTime >= ((float) this.delay)) {
            if (this.interpolator != null) {
                this.interpolator.update(tickTime);
            }
            pre(poseStack);
            this.widget.drawInForeground(poseStack, mouseX, mouseY, partialTicks);
            post(poseStack);
        } else if (isOut()) {
            this.widget.drawInForeground(poseStack, mouseX, mouseY, partialTicks);
        }
    }
}
