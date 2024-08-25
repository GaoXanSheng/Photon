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
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/PhysicsSetting.class */
public class PhysicsSetting extends ToggleGroup {
    @Configurable(tips = {"photon.emitter.config.physics.hasCollision"})
    protected boolean hasCollision = true;
    @Configurable(tips = {"photon.emitter.config.physics.removedWhenCollided"})
    protected boolean removedWhenCollided = false;
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, max = 1.0f, defaultValue = 0.98f, curveConfig = @CurveConfig(xAxis = "duration", yAxis = "friction"))
    @Configurable(tips = {"photon.emitter.config.physics.friction"})
    protected NumberFunction friction = NumberFunction.constant(Double.valueOf(0.98d));
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "duration", yAxis = "gravity"))
    @Configurable(tips = {"photon.emitter.config.physics.gravity"})
    protected NumberFunction gravity = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, max = 1.0f, defaultValue = 1.0f, curveConfig = @CurveConfig(xAxis = "duration", yAxis = "bounce chance"))
    @Configurable(tips = {"photon.emitter.config.physics.bounceChance"})
    protected NumberFunction bounceChance = NumberFunction.constant(1);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "duration", yAxis = "bounce rate"))
    @Configurable(tips = {"photon.emitter.config.physics.bounceRate"})
    protected NumberFunction bounceRate = NumberFunction.constant(1);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "duration", yAxis = "spread"))
    @Configurable(tips = {"photon.emitter.config.physics.bounceSpreadRate"})
    protected NumberFunction bounceSpreadRate = NumberFunction.constant(0);

    public void setHasCollision(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }

    public boolean isHasCollision() {
        return this.hasCollision;
    }

    public void setRemovedWhenCollided(boolean removedWhenCollided) {
        this.removedWhenCollided = removedWhenCollided;
    }

    public boolean isRemovedWhenCollided() {
        return this.removedWhenCollided;
    }

    public void setFriction(NumberFunction friction) {
        this.friction = friction;
    }

    public NumberFunction getFriction() {
        return this.friction;
    }

    public void setGravity(NumberFunction gravity) {
        this.gravity = gravity;
    }

    public NumberFunction getGravity() {
        return this.gravity;
    }

    public void setBounceChance(NumberFunction bounceChance) {
        this.bounceChance = bounceChance;
    }

    public NumberFunction getBounceChance() {
        return this.bounceChance;
    }

    public void setBounceRate(NumberFunction bounceRate) {
        this.bounceRate = bounceRate;
    }

    public NumberFunction getBounceRate() {
        return this.bounceRate;
    }

    public void setBounceSpreadRate(NumberFunction bounceSpreadRate) {
        this.bounceSpreadRate = bounceSpreadRate;
    }

    public NumberFunction getBounceSpreadRate() {
        return this.bounceSpreadRate;
    }

    public void setupParticlePhysics(LParticle particle) {
        particle.setFriction(this.friction.get(particle.getT(), () -> {
            return Float.valueOf(particle.getMemRandom("friction"));
        }).floatValue());
        particle.setGravity(this.gravity.get(particle.getT(), () -> {
            return Float.valueOf(particle.getMemRandom("gravity"));
        }).floatValue());
        particle.setBounceChance(this.bounceChance.get(particle.getT(), () -> {
            return Float.valueOf(particle.getMemRandom("bounce_chance"));
        }).floatValue());
        particle.setBounceRate(this.bounceRate.get(particle.getT(), () -> {
            return Float.valueOf(particle.getMemRandom("bounce_rate"));
        }).floatValue());
        particle.setBounceSpreadRate(this.bounceSpreadRate.get(particle.getT(), () -> {
            return Float.valueOf(particle.getMemRandom("bounce_spread_rate"));
        }).floatValue());
    }
}
