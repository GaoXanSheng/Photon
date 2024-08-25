package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.utils.Vector3;
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
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/RotationOverLifetimeSetting.class */
public class RotationOverLifetimeSetting extends ToggleGroup {
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 10.0f, curveConfig = @CurveConfig(bound = {0.0f, 360.0f}, xAxis = "lifetime", yAxis = "roll"))
    @Configurable(tips = {"photon.emitter.config.rotation.roll"})
    protected NumberFunction roll = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 10.0f, curveConfig = @CurveConfig(bound = {0.0f, 360.0f}, xAxis = "lifetime", yAxis = "pitch"))
    @Configurable(tips = {"photon.emitter.config.rotation.pitch"})
    protected NumberFunction pitch = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 10.0f, curveConfig = @CurveConfig(bound = {0.0f, 360.0f}, xAxis = "lifetime", yAxis = "yaw"))
    @Configurable(tips = {"photon.emitter.config.rotation.yaw"})
    protected NumberFunction yaw = NumberFunction.constant(0);

    public void setRoll(NumberFunction roll) {
        this.roll = roll;
    }

    public NumberFunction getRoll() {
        return this.roll;
    }

    public void setPitch(NumberFunction pitch) {
        this.pitch = pitch;
    }

    public NumberFunction getPitch() {
        return this.pitch;
    }

    public void setYaw(NumberFunction yaw) {
        this.yaw = yaw;
    }

    public NumberFunction getYaw() {
        return this.yaw;
    }

    public Vector3 getRotation(LParticle particle, float partialTicks) {
        float t = particle.getT(partialTicks);
        return new Vector3(this.roll.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("rol0"));
        }).doubleValue(), this.pitch.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("rol1"));
        }).doubleValue(), this.yaw.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("rol2"));
        }).doubleValue()).multiply(0.01745329238474369d);
    }
}
