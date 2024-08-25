package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.utils.Range;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3Config;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;
import com.lowdragmc.photon.client.particle.LParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/SizeBySpeedSetting.class */
public class SizeBySpeedSetting extends ToggleGroup {
    @NumberFunctionConfig(types = {RandomConstant.class, Curve.class, RandomCurve.class}, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {-1.0f, 1.0f}, xAxis = "speed", yAxis = "size scale"))
    @Configurable(tips = {"photon.emitter.config.sizeBySpeed.scale"})
    protected NumberFunction scale = new RandomConstant(Float.valueOf(0.0f), Float.valueOf(1.0f), true);
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, curveConfig = @CurveConfig(bound = {-1.0f, 1.0f}, xAxis = "speed", yAxis = "size")))
    @Configurable(tips = {"photon.emitter.config.sizeBySpeed.size"})
    protected NumberFunction3 size = new NumberFunction3((Number) 0, (Number) 0, (Number) 0);
    @Configurable(tips = {"photon.emitter.config.sizeBySpeed.speedRange"})
    @NumberRange(range = {0.0d, 1000.0d})
    protected Range speedRange = new Range(Float.valueOf(0.0f), Float.valueOf(1.0f));

    public void setScale(NumberFunction scale) {
        this.scale = scale;
    }

    public NumberFunction getScale() {
        return this.scale;
    }

    public void setSize(NumberFunction3 size) {
        this.size = size;
    }

    public NumberFunction3 getSize() {
        return this.size;
    }

    public void setSpeedRange(Range speedRange) {
        this.speedRange = speedRange;
    }

    public Range getSpeedRange() {
        return this.speedRange;
    }

    public Vector3 getSize(Vector3 startedSize, LParticle particle) {
        double value = particle.getVelocity().mag() * 20.0d;
        float t = (float) ((value - this.speedRange.getA().floatValue()) / (this.speedRange.getB().floatValue() - this.speedRange.getA().floatValue()));
        return this.size.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("sbs0"));
        }).add(startedSize).multiply(this.scale.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("sbs1"));
        }).doubleValue());
    }
}
