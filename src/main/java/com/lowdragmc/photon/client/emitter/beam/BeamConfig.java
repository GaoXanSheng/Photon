package com.lowdragmc.photon.client.emitter.beam;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.data.LightOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.MaterialSetting;
import com.lowdragmc.photon.client.emitter.data.RendererSetting;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.color.Color;
import com.lowdragmc.photon.client.emitter.data.number.color.Gradient;
import com.lowdragmc.photon.client.emitter.data.number.color.RandomColor;
import com.lowdragmc.photon.client.emitter.data.number.color.RandomGradient;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/beam/BeamConfig.class */
public class BeamConfig {
    @Configurable(tips = {"photon.emitter.config.duration"})
    @NumberRange(range = {1.0d, 2.147483647E9d})
    protected int duration = 100;
    @Configurable(tips = {"photon.emitter.config.looping"})
    protected boolean looping = true;
    @Configurable(tips = {"photon.emitter.config.startDelay"})
    @NumberRange(range = {0.0d, 2.147483647E9d})
    protected int startDelay = 0;
    @Configurable(tips = {"photon.emitter.beam.config.end"})
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d})
    protected Vector3 end = new Vector3(3.0d, 0.0d, 0.0d);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "duration", yAxis = "width"))
    @Configurable(tips = {"photon.emitter.beam.config.width"})
    protected NumberFunction width = NumberFunction.constant(Double.valueOf(0.2d));
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "duration", yAxis = "width"))
    @Configurable(tips = {"photon.emitter.beam.config.emitRate"})
    protected NumberFunction emitRate = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Color.class, RandomColor.class, Gradient.class, RandomGradient.class}, defaultValue = -1.0f)
    @Configurable(tips = {"photon.emitter.beam.config.color"})
    protected NumberFunction color = new Color();
    @Configurable(name = "Material", subConfigurable = true, tips = {"photon.emitter.config.material"})
    protected final MaterialSetting material = new MaterialSetting();
    @Configurable(name = "Renderer", subConfigurable = true, tips = {"photon.emitter.config.renderer"})
    protected final RendererSetting renderer = new RendererSetting();
    @Configurable(name = "Fixed Light", subConfigurable = true, tips = {"photon.emitter.config.lights"})
    protected final LightOverLifetimeSetting lights = new LightOverLifetimeSetting();

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean isLooping() {
        return this.looping;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public int getStartDelay() {
        return this.startDelay;
    }

    public Vector3 getEnd() {
        return this.end;
    }

    public void setWidth(NumberFunction width) {
        this.width = width;
    }

    public NumberFunction getWidth() {
        return this.width;
    }

    public void setEmitRate(NumberFunction emitRate) {
        this.emitRate = emitRate;
    }

    public NumberFunction getEmitRate() {
        return this.emitRate;
    }

    public void setColor(NumberFunction color) {
        this.color = color;
    }

    public NumberFunction getColor() {
        return this.color;
    }

    public MaterialSetting getMaterial() {
        return this.material;
    }

    public RendererSetting getRenderer() {
        return this.renderer;
    }

    public LightOverLifetimeSetting getLights() {
        return this.lights;
    }
}
