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
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote VelocityOverLifetimeSetting
 */
@OnlyIn(Dist.CLIENT)
public class VelocityOverLifetimeSetting extends ToggleGroup {

    public enum OrbitalMode {
        AngularVelocity,
        LinearVelocity,
        FixedVelocity
    }

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.velocityOverLifetime.linear")
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 1, curveConfig = @CurveConfig(bound = {-2, 2}, xAxis = "lifetime", yAxis = "additional velocity")))
    protected NumberFunction3 linear = new NumberFunction3(0, 0, 0);

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.velocityOverLifetime.orbitalMode")
    protected OrbitalMode orbitalMode = OrbitalMode.AngularVelocity;

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.velocityOverLifetime.orbital")
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 1, curveConfig = @CurveConfig(bound = {-2, 2}, xAxis = "lifetime", yAxis = "orbital velocity")))
    protected NumberFunction3 orbital = new NumberFunction3(0, 0, 0);

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.velocityOverLifetime.offset")
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 1, curveConfig = @CurveConfig(bound = {-3, 3}, xAxis = "lifetime", yAxis = "orbital offset")))
    protected NumberFunction3 offset = new NumberFunction3(0, 0, 0);

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.velocityOverLifetime.speedModifier")
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, defaultValue = 1f, curveConfig = @CurveConfig(bound = {-1, 1}, xAxis = "lifetime", yAxis = "speed modifier"))
    protected NumberFunction speedModifier = NumberFunction.constant(1);

    public Vector3 getVelocityAddition(LParticle particle, LParticle emitter) {
        var center = emitter.getPos();
        var lifetime = particle.getT();
        var addition = linear.get(lifetime, () -> particle.getMemRandom("vol0")).multiply(0.05);
        var orbitalVec = orbital.get(lifetime, () -> particle.getMemRandom("vol1"));
        if (!orbitalVec.isZero()) {
            if (orbitalMode == OrbitalMode.AngularVelocity) {
                var toPoint = particle.getPos().copy().subtract(center.copy().add(offset.get(lifetime, () -> particle.getMemRandom("vol2"))));
                if (orbitalVec.x != 0) {
                    var radiusVec = toPoint.copy().subtract(toPoint.copy().project(Vector3.X));
                    addition.add(radiusVec.copy().rotate(orbitalVec.x * 0.05f, Vector3.X).subtract(radiusVec));
                }
                if (orbitalVec.y != 0) {
                    var radiusVec = toPoint.copy().subtract(toPoint.copy().project(Vector3.Y));
                    addition.add(radiusVec.copy().rotate(orbitalVec.y * 0.05f, Vector3.Y).subtract(radiusVec));
                }
                if (orbitalVec.z != 0) {
                    var radiusVec = toPoint.copy().subtract(toPoint.copy().project(Vector3.Z));
                    addition.add(radiusVec.copy().rotate(orbitalVec.z * 0.05f, Vector3.Z).subtract(radiusVec));
                }
            } else if (orbitalMode == OrbitalMode.LinearVelocity) {
                var toPoint = particle.getPos().copy().subtract(center.copy().add(offset.get(lifetime, () -> particle.getMemRandom("vol2"))));
                if (orbitalVec.x != 0) {
                    var radiusVec = toPoint.copy().subtract(toPoint.copy().project(Vector3.X));
                    var r = radiusVec.mag();
                    addition.add(radiusVec.copy().rotate(orbitalVec.x * 0.05f / r, Vector3.X).subtract(radiusVec));
                }
                if (orbitalVec.y != 0) {
                    var radiusVec = toPoint.copy().subtract(toPoint.copy().project(Vector3.Y));
                    var r = radiusVec.mag();
                    addition.add(radiusVec.copy().rotate(orbitalVec.y * 0.05f / r, Vector3.Y).subtract(radiusVec));
                }
                if (orbitalVec.z != 0) {
                    var radiusVec = toPoint.copy().subtract(toPoint.copy().project(Vector3.Z));
                    var r = radiusVec.mag();
                    addition.add(radiusVec.copy().rotate(orbitalVec.z * 0.05f / r, Vector3.Z).subtract(radiusVec));
                }
            } else if (orbitalMode == OrbitalMode.FixedVelocity) {
                var toCenter = center.copy().add(offset.get(lifetime, () -> particle.getMemRandom("vol2"))).subtract(particle.getPos());
                if (orbitalVec.x != 0) {
                    addition.add(toCenter.copy().crossProduct(Vector3.X).normalize().multiply(orbitalVec.x * 0.05));
                }
                if (orbitalVec.y != 0) {
                    addition.add(toCenter.copy().crossProduct(Vector3.Y).normalize().multiply(orbitalVec.y * 0.05));
                }
                if (orbitalVec.z != 0) {
                    addition.add(toCenter.copy().crossProduct(Vector3.Z).normalize().multiply(orbitalVec.z * 0.05));
                }
            }
        }
        return addition;
    }

    public float getVelocityMultiplier(LParticle particle) {
        var lifetime = particle.getT();
        return speedModifier.get(lifetime, () -> particle.getMemRandom(this)).floatValue();
    }

}
