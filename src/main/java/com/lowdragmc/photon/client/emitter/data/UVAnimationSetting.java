package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.utils.Range;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;
import com.lowdragmc.photon.client.particle.LParticle;
import com.mojang.math.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/UVAnimationSetting.class */
public class UVAnimationSetting extends ToggleGroup {
    @Configurable(tips = {"photon.emitter.config.uvAnimation.tiles"})
    @NumberRange(range = {1.0d, 2.147483647E9d})
    protected Range tiles = new Range(1, 1);
    @Configurable(tips = {"photon.emitter.config.uvAnimation.animation"})
    protected Animation animation = Animation.WholeSheet;
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, curveConfig = @CurveConfig(bound = {0.0f, 4.0f}, xAxis = "lifetime", yAxis = "frame over time"))
    @Configurable(tips = {"photon.emitter.config.uvAnimation.frameOverTime"})
    protected NumberFunction frameOverTime = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class}, min = 0.0f)
    @Configurable(tips = {"photon.emitter.config.uvAnimation.startFrame"})
    protected NumberFunction startFrame = NumberFunction.constant(0);
    @Configurable(tips = {"photon.emitter.config.uvAnimation.cycle"})
    @NumberRange(range = {0.0d, 2.147483647E9d}, wheel = 1.0d)
    protected float cycle = 1.0f;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/UVAnimationSetting$Animation.class */
    public enum Animation {
        WholeSheet,
        SingleRow
    }

    public void setTiles(Range tiles) {
        this.tiles = tiles;
    }

    public Range getTiles() {
        return this.tiles;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public void setFrameOverTime(NumberFunction frameOverTime) {
        this.frameOverTime = frameOverTime;
    }

    public NumberFunction getFrameOverTime() {
        return this.frameOverTime;
    }

    public void setStartFrame(NumberFunction startFrame) {
        this.startFrame = startFrame;
    }

    public NumberFunction getStartFrame() {
        return this.startFrame;
    }

    public void setCycle(float cycle) {
        this.cycle = cycle;
    }

    public float getCycle() {
        return this.cycle;
    }

    public Vector4f getUVs(LParticle particle, float partialTicks) {
        float u0;
        float v0;
        float t = particle.getT(partialTicks);
        float cellU = 1.0f / this.tiles.getA().intValue();
        float cellV = 1.0f / this.tiles.getB().intValue();
        float currentFrame = this.startFrame.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("startFrame"));
        }).floatValue() + (this.cycle * this.frameOverTime.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("frameOverTime"));
        }).floatValue());
        if (this.animation == Animation.WholeSheet) {
            int cellSize = this.tiles.getA().intValue() * this.tiles.getB().intValue();
            int X = (int) (currentFrame % cellSize);
            int Y = (int) (currentFrame / cellSize);
            u0 = X * cellU;
            v0 = Y * cellV;
        } else {
            int X2 = (int) (currentFrame % this.tiles.getA().intValue());
            int Y2 = (int) (particle.getMemRandom("randomRow") * this.tiles.getB().intValue());
            u0 = X2 * cellU;
            v0 = Y2 * cellV;
        }
        float u1 = u0 + cellU;
        float v1 = v0 + cellV;
        return new Vector4f(u0, v0, u1, v1);
    }
}
