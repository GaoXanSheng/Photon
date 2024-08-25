package com.lowdragmc.lowdraglib.utils.interpolate;

import java.util.function.Consumer;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/interpolate/Interpolator.class */
public class Interpolator {
    private final float from;
    private final float to;
    private final float durationTick;
    private final IEase ease;
    private final Consumer<Number> interpolate;
    private final Consumer<Number> callback;
    private float tick;
    private float startTick;

    public Interpolator(float from, float to, float durationTick, IEase ease, Consumer<Number> interpolate) {
        this(from, to, durationTick, ease, interpolate, null);
    }

    public Interpolator(float from, float to, float durationTick, IEase ease, Consumer<Number> interpolate, Consumer<Number> callback) {
        this.tick = -1.0f;
        this.startTick = 0.0f;
        this.from = from;
        this.to = to;
        this.durationTick = durationTick;
        this.ease = ease;
        this.interpolate = interpolate;
        this.callback = callback;
    }

    public void reset() {
        this.tick = -1.0f;
    }

    public boolean isFinish() {
        return this.tick == this.durationTick;
    }

    public void update(float tickTime) {
        if (this.tick == -2.0f) {
            return;
        }
        if (this.tick == -1.0f) {
            this.startTick = tickTime;
        }
        if (this.tick - this.startTick >= this.durationTick) {
            this.tick = -2.0f;
            if (this.interpolate != null) {
                this.interpolate.accept(Float.valueOf((this.ease.getInterpolation(1.0f) * (this.to - this.from)) + this.from));
            }
            if (this.callback != null) {
                this.callback.accept(Float.valueOf((this.ease.getInterpolation(1.0f) * (this.to - this.from)) + this.from));
                return;
            }
            return;
        }
        this.tick = tickTime;
        if (this.interpolate != null) {
            this.interpolate.accept(Float.valueOf((this.ease.getInterpolation((this.tick - this.startTick) / this.durationTick) * (this.to - this.from)) + this.from));
        }
    }
}
