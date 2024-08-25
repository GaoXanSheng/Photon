package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
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
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/SizeOverLifetimeSetting.class */
public class SizeOverLifetimeSetting extends ToggleGroup {
    @NumberFunctionConfig(types = {RandomConstant.class, Curve.class, RandomCurve.class}, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {-1.0f, 1.0f}, xAxis = "lifetime", yAxis = "size scale"))
    @Configurable(tips = {"photon.emitter.config.sizeOverLifetime.scale"})
    protected NumberFunction scale = new RandomConstant(Float.valueOf(0.0f), Float.valueOf(1.0f), true);
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, curveConfig = @CurveConfig(bound = {-1.0f, 1.0f}, xAxis = "lifetime", yAxis = "size")))
    @Configurable(tips = {"photon.emitter.config.sizeOverLifetime.size"})
    protected NumberFunction3 size = new NumberFunction3((Number) 0, (Number) 0, (Number) 0);

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

    public Vector3 getSize(Vector3 startedSize, LParticle particle, float partialTicks) {
        return this.size.get(particle.getT(partialTicks), () -> {
            return Float.valueOf(particle.getMemRandom("sol0"));
        }).add(startedSize).multiply(this.scale.get(particle.getT(partialTicks), () -> {
            return Float.valueOf(particle.getMemRandom("sol1"));
        }).doubleValue());
    }
}
