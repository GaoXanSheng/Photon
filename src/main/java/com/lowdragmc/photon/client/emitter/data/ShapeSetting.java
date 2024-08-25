package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.SelectorConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3Config;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;
import com.lowdragmc.photon.client.emitter.data.shape.Cone;
import com.lowdragmc.photon.client.emitter.data.shape.IShape;
import com.lowdragmc.photon.client.particle.LParticle;
import com.lowdragmc.photon.integration.PhotonLDLibPlugin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/ShapeSetting.class */
public class ShapeSetting implements IConfigurable {
    @Persisted
    private IShape shape = new Cone();
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = -1000.0f, max = 1000.0f, curveConfig = @CurveConfig(bound = {-3.0f, 3.0f}, xAxis = "duration", yAxis = "position")))
    @Configurable(tips = {"photon.emitter.config.shape.position"})
    private NumberFunction3 position = new NumberFunction3((Number) 0, (Number) 0, (Number) 0);
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 10.0f, min = -3.4028235E38f, max = Float.MAX_VALUE, curveConfig = @CurveConfig(bound = {-180.0f, 180.0f}, xAxis = "duration", yAxis = "rotation")))
    @Configurable(tips = {"photon.emitter.config.shape.rotation"})
    private NumberFunction3 rotation = new NumberFunction3((Number) 0, (Number) 0, (Number) 0);
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, max = 1000.0f, curveConfig = @CurveConfig(bound = {0.0f, 3.0f}, xAxis = "duration", yAxis = "scale")))
    @Configurable(tips = {"photon.emitter.config.shape.scale"})
    private NumberFunction3 scale = new NumberFunction3((Number) 1, (Number) 1, (Number) 1);

    public IShape getShape() {
        return this.shape;
    }

    public void setShape(IShape shape) {
        this.shape = shape;
    }

    public NumberFunction3 getPosition() {
        return this.position;
    }

    public void setPosition(NumberFunction3 position) {
        this.position = position;
    }

    public NumberFunction3 getRotation() {
        return this.rotation;
    }

    public void setRotation(NumberFunction3 rotation) {
        this.rotation = rotation;
    }

    public NumberFunction3 getScale() {
        return this.scale;
    }

    public void setScale(NumberFunction3 scale) {
        this.scale = scale;
    }

    public void setupParticle(LParticle particle, LParticle emitter) {
        float t = emitter.getT();
        this.shape.nextPosVel(particle, emitter, emitter.getPos().add(this.position.get(t, () -> {
            return Float.valueOf(emitter.getMemRandom("shape_position"));
        })), emitter.getRotation(0.0f).add(new Vector3(this.rotation.get(t, () -> {
            return Float.valueOf(emitter.getMemRandom("shape_rotation"));
        })).multiply(0.01745329238474369d)), new Vector3(this.scale.get(t, () -> {
            return Float.valueOf(emitter.getMemRandom("shape_scale"));
        })));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        super.buildConfigurator(father);
        ConfiguratorGroup group = new ConfiguratorGroup("", false);
        SelectorConfigurator<String> selector = new SelectorConfigurator<>("Shape", () -> {
            return this.shape.name();
        }, name -> {
            AnnotationDetector.Wrapper<LDLRegister, ? extends IShape> wrapper = PhotonLDLibPlugin.REGISTER_SHAPES.get(father);
            if (wrapper != null) {
                this.shape = wrapper.creator().get();
                group.removeAllConfigurators();
                this.shape.buildConfigurator(group);
                group.computeLayout();
            }
        }, "Sphere", true, PhotonLDLibPlugin.REGISTER_SHAPES.keySet().stream().toList(), (v0) -> {
            return v0.toString();
        });
        selector.setMax(PhotonLDLibPlugin.REGISTER_SHAPES.size());
        father.addConfigurators(selector);
        group.setCanCollapse(false);
        this.shape.buildConfigurator(group);
        father.addConfigurators(group);
    }
}
