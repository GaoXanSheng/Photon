package com.lowdragmc.photon.client.emitter.trail;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.photon.client.emitter.data.LightOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.MaterialSetting;
import com.lowdragmc.photon.client.emitter.data.RendererSetting;
import com.lowdragmc.photon.client.emitter.data.UVAnimationSetting;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.color.Color;
import com.lowdragmc.photon.client.emitter.data.number.color.Gradient;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.particle.TrailParticle;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/trail/TrailConfig.class */
public class TrailConfig {
    @Configurable(tips = {"photon.emitter.trail.config.time"})
    @NumberRange(range = {0.0d, 2.147483647E9d})
    protected int time = 20;
    @Configurable(tips = {"photon.emitter.trail.config.minVertexDistance"})
    @NumberRange(range = {0.0d, 3.4028234663852886E38d})
    protected float minVertexDistance = 0.05f;
    @Configurable(tips = {"photon.emitter.config.parallelRendering.0", "photon.emitter.config.parallelRendering.1"})
    protected boolean parallelRendering = false;
    @Configurable(tips = {"photon.emitter.trail.config.uvMode"})
    protected TrailParticle.UVMode uvMode = TrailParticle.UVMode.Stretch;
    @NumberFunctionConfig(types = {Constant.class, Curve.class}, min = 0.0f, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "trail position", yAxis = "width"))
    @Configurable(tips = {"photon.emitter.trail.config.widthOverTrail"})
    protected NumberFunction widthOverTrail = NumberFunction.constant(Float.valueOf(2.0f));
    @NumberFunctionConfig(types = {Color.class, Gradient.class}, defaultValue = -1.0f)
    @Configurable(tips = {"photon.emitter.trail.config.colorOverTrail"})
    protected NumberFunction colorOverTrail = new Gradient();
    @Configurable(name = "Material", subConfigurable = true, tips = {"photon.emitter.config.material"})
    protected final MaterialSetting material = new MaterialSetting();
    @Configurable(name = "Renderer", subConfigurable = true, tips = {"photon.emitter.config.renderer"})
    protected final RendererSetting renderer = new RendererSetting();
    @Configurable(name = "Fixed Light", subConfigurable = true, tips = {"photon.emitter.config.lights"})
    protected final LightOverLifetimeSetting lights = new LightOverLifetimeSetting();
    @Configurable(name = "UV Animation", subConfigurable = true, tips = {"photon.emitter.config.uvAnimation"})
    protected final UVAnimationSetting uvAnimation = new UVAnimationSetting();

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public float getMinVertexDistance() {
        return this.minVertexDistance;
    }

    public void setParallelRendering(boolean parallelRendering) {
        this.parallelRendering = parallelRendering;
    }

    public boolean isParallelRendering() {
        return this.parallelRendering;
    }

    public void setUvMode(TrailParticle.UVMode uvMode) {
        this.uvMode = uvMode;
    }

    public TrailParticle.UVMode getUvMode() {
        return this.uvMode;
    }

    public void setWidthOverTrail(NumberFunction widthOverTrail) {
        this.widthOverTrail = widthOverTrail;
    }

    public NumberFunction getWidthOverTrail() {
        return this.widthOverTrail;
    }

    public void setColorOverTrail(NumberFunction colorOverTrail) {
        this.colorOverTrail = colorOverTrail;
    }

    public NumberFunction getColorOverTrail() {
        return this.colorOverTrail;
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

    public UVAnimationSetting getUvAnimation() {
        return this.uvAnimation;
    }
}
