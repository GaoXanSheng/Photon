package com.lowdragmc.lowdraglib.gui.animation;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.interpolate.Eases;
import com.lowdragmc.lowdraglib.utils.interpolate.IEase;
import com.lowdragmc.lowdraglib.utils.interpolate.Interpolator;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/animation/Animation.class */
public class Animation {
    protected Widget widget;
    protected Interpolator interpolator;
    protected boolean isFinish;
    protected FloatConsumer onUpdate;
    protected Runnable onFinish;
    @Nullable
    protected Size size;
    @Nullable
    protected Position position;
    protected Size initialSize;
    protected Position initialPosition;
    protected float time = 0.0f;
    protected long startTick = -1;
    protected boolean init = false;
    protected long duration = 250;
    protected long delay = 0;
    protected IEase ease = Eases.EaseLinear;

    public Animation duration(long duration) {
        this.duration = duration;
        return this;
    }

    public Animation delay(long delay) {
        this.delay = delay;
        return this;
    }

    public Animation ease(IEase ease) {
        this.ease = ease;
        return this;
    }

    public Animation onUpdate(FloatConsumer onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public Animation onFinish(Runnable onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    public Animation size(@Nullable Size size) {
        this.size = size;
        return this;
    }

    public Animation position(@Nullable Position position) {
        this.position = position;
        return this;
    }

    public Animation setWidget(Widget widget) {
        this.widget = widget;
        return this;
    }

    public Widget getWidget() {
        return this.widget;
    }

    protected void onInterpolatorUpdate(Number number) {
        this.time = number.floatValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getTime() {
        return this.time;
    }

    protected void onInterpolatorFinish(Number number) {
        this.widget.setActive(true);
        this.interpolator = null;
        this.isFinish = true;
        if (this.onFinish != null) {
            this.onFinish.run();
        }
    }

    public boolean isFinish() {
        return this.isFinish;
    }

    public Animation appendOnFinish(Runnable onFinish) {
        if (this.onFinish != null) {
            Runnable last = this.onFinish;
            this.onFinish = () -> {
                last.run();
                onFinish.run();
            };
        } else {
            this.onFinish = onFinish;
        }
        return this;
    }

    public Runnable getOnFinish() {
        return this.onFinish;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getTick() {
        if (!this.init) {
            init();
        }
        return (float) (System.currentTimeMillis() - this.startTick);
    }

    protected void init() {
        this.init = true;
        this.interpolator = new Interpolator(0.0f, 1.0f, (float) this.duration, this.ease, this::onInterpolatorUpdate, this::onInterpolatorFinish);
        this.startTick = System.currentTimeMillis();
        this.widget.setActive(false);
        this.initialSize = this.widget.getSize();
        this.initialPosition = this.widget.getSelfPosition();
    }

    protected void updateWidget(float t) {
        if (this.onUpdate != null) {
            this.onUpdate.accept(t);
        }
        if (this.size != null) {
            this.widget.setSize(new Size((int) ((this.size.width * t) + (this.initialSize.width * (1.0f - t))), (int) ((this.size.height * t) + (this.initialSize.height * (1.0f - t)))));
        }
        if (this.position != null) {
            this.widget.setSelfPosition(new Position((int) ((this.position.x * t) + (this.initialPosition.x * (1.0f - t))), (int) ((this.position.y * t) + (this.initialPosition.y * (1.0f - t)))));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        float tickTime = getTick();
        if (tickTime >= ((float) this.delay) && this.interpolator != null) {
            this.interpolator.update(tickTime);
            updateWidget(getTime());
        }
        this.widget.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        float tickTime = getTick();
        if (tickTime >= ((float) this.delay) && this.interpolator != null) {
            this.interpolator.update(tickTime);
        }
        this.widget.drawInForeground(poseStack, mouseX, mouseY, partialTicks);
    }
}
