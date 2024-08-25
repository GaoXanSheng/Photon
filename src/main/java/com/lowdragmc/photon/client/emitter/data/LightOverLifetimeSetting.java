package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;
import com.lowdragmc.photon.client.particle.LParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/LightOverLifetimeSetting.class */
public class LightOverLifetimeSetting extends ToggleGroup {
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, isDecimals = false, defaultValue = 15.0f, min = 0.0f, max = 15.0f, wheelDur = 1.0f, curveConfig = @CurveConfig(xAxis = "lifetime", yAxis = "speed modifier"))
    @Configurable(tips = {"photon.emitter.config.lights.skyLight"})
    protected NumberFunction skyLight = NumberFunction.constant(15);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, isDecimals = false, defaultValue = 15.0f, min = 0.0f, max = 15.0f, wheelDur = 1.0f, curveConfig = @CurveConfig(xAxis = "lifetime", yAxis = "speed modifier"))
    @Configurable(tips = {"photon.emitter.config.lights.blockLight"})
    protected NumberFunction blockLight = NumberFunction.constant(15);

    public void setSkyLight(NumberFunction skyLight) {
        this.skyLight = skyLight;
    }

    public NumberFunction getSkyLight() {
        return this.skyLight;
    }

    public void setBlockLight(NumberFunction blockLight) {
        this.blockLight = blockLight;
    }

    public NumberFunction getBlockLight() {
        return this.blockLight;
    }

    public LightOverLifetimeSetting() {
        this.enable = true;
    }

    public int getLight(LParticle particle, float partialTicks) {
        int sky = this.skyLight.get(particle.getT(partialTicks), () -> {
            return Float.valueOf(particle.getMemRandom("sky-light"));
        }).intValue();
        int block = this.blockLight.get(particle.getT(partialTicks), () -> {
            return Float.valueOf(particle.getMemRandom("block-light"));
        }).intValue();
        return (sky << 20) | (block << 4);
    }
}
