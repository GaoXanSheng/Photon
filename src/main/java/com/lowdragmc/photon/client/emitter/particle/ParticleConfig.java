package com.lowdragmc.photon.client.emitter.particle;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.photon.client.emitter.data.ColorBySpeedSetting;
import com.lowdragmc.photon.client.emitter.data.ColorOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.EmissionSetting;
import com.lowdragmc.photon.client.emitter.data.ForceOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.InheritVelocitySetting;
import com.lowdragmc.photon.client.emitter.data.LifetimeByEmitterSpeedSetting;
import com.lowdragmc.photon.client.emitter.data.LightOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.MaterialSetting;
import com.lowdragmc.photon.client.emitter.data.NoiseSetting;
import com.lowdragmc.photon.client.emitter.data.PhysicsSetting;
import com.lowdragmc.photon.client.emitter.data.RendererSetting;
import com.lowdragmc.photon.client.emitter.data.RotationBySpeedSetting;
import com.lowdragmc.photon.client.emitter.data.RotationOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.ShapeSetting;
import com.lowdragmc.photon.client.emitter.data.SizeBySpeedSetting;
import com.lowdragmc.photon.client.emitter.data.SizeOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.SubEmittersSetting;
import com.lowdragmc.photon.client.emitter.data.TrailsSetting;
import com.lowdragmc.photon.client.emitter.data.UVAnimationSetting;
import com.lowdragmc.photon.client.emitter.data.VelocityOverLifetimeSetting;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction3Config;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.color.Color;
import com.lowdragmc.photon.client.emitter.data.number.color.Gradient;
import com.lowdragmc.photon.client.emitter.data.number.color.RandomColor;
import com.lowdragmc.photon.client.emitter.data.number.color.RandomGradient;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/particle/ParticleConfig.class */
public class ParticleConfig {
    @Configurable(tips = {"photon.emitter.config.duration"})
    @NumberRange(range = {1.0d, 2.147483647E9d})
    protected int duration = 100;
    @Configurable(tips = {"photon.emitter.config.looping"})
    protected boolean looping = true;
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, isDecimals = false, min = 0.0f, curveConfig = @CurveConfig(bound = {0.0f, 100.0f}, xAxis = "duration", yAxis = "delay"))
    @Configurable(tips = {"photon.emitter.config.startDelay"})
    protected NumberFunction startDelay = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, isDecimals = false, min = 0.0f, defaultValue = 100.0f, curveConfig = @CurveConfig(bound = {0.0f, 200.0f}, xAxis = "duration", yAxis = "life time"))
    @Configurable(tips = {"photon.emitter.config.startLifetime"})
    protected NumberFunction startLifetime = NumberFunction.constant(100);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {-2.0f, 2.0f}, xAxis = "duration", yAxis = "speed"))
    @Configurable(tips = {"photon.emitter.config.startSpeed"})
    protected NumberFunction startSpeed = NumberFunction.constant(1);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, defaultValue = 0.1f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "duration", yAxis = "size"))
    @Configurable(tips = {"photon.emitter.config.startSize"})
    protected NumberFunction startSize = NumberFunction.constant(Float.valueOf(0.1f));
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 10.0f, curveConfig = @CurveConfig(bound = {0.0f, 360.0f}, xAxis = "duration", yAxis = "rotation")))
    @Configurable(tips = {"photon.emitter.config.startRotation"})
    protected NumberFunction3 startRotation = new NumberFunction3((Number) 0, (Number) 0, (Number) 0);
    @NumberFunctionConfig(types = {Color.class, RandomColor.class, Gradient.class, RandomGradient.class}, defaultValue = -1.0f)
    @Configurable(tips = {"photon.emitter.config.startColor"})
    protected NumberFunction startColor = NumberFunction.color(-1);
    @Configurable(tips = {"photon.emitter.config.maxParticles"})
    @NumberRange(range = {0.0d, 100000.0d}, wheel = 100.0d)
    protected int maxParticles = 2000;
    @Configurable(tips = {"photon.emitter.config.parallelUpdate.0", "photon.emitter.config.parallelUpdate.1"})
    protected boolean parallelUpdate = false;
    @Configurable(tips = {"photon.emitter.config.parallelRendering.0", "photon.emitter.config.parallelRendering.1"})
    protected boolean parallelRendering = false;
    @Configurable(name = "Emission", subConfigurable = true, tips = {"photon.emitter.config.emission"})
    protected EmissionSetting emission = new EmissionSetting();
    @Configurable(name = "Shape", subConfigurable = true, tips = {"photon.emitter.config.shape"})
    protected ShapeSetting shape = new ShapeSetting();
    @Configurable(name = "Material", subConfigurable = true, tips = {"photon.emitter.config.material"})
    protected MaterialSetting material = new MaterialSetting();
    @Configurable(name = "Renderer", subConfigurable = true, tips = {"photon.emitter.config.renderer"})
    protected RendererSetting renderer = new RendererSetting();
    @Configurable(name = "Physics", subConfigurable = true, tips = {"photon.emitter.config.physics"})
    protected PhysicsSetting physics = new PhysicsSetting();
    @Configurable(name = "Fixed Light", subConfigurable = true, tips = {"photon.emitter.config.lights"})
    protected LightOverLifetimeSetting lights = new LightOverLifetimeSetting();
    @Configurable(name = "Velocity over Lifetime", subConfigurable = true, tips = {"photon.emitter.config.velocityOverLifetime"})
    protected final VelocityOverLifetimeSetting velocityOverLifetime = new VelocityOverLifetimeSetting();
    @Configurable(name = "Inherit Velocity", subConfigurable = true, tips = {"photon.emitter.config.inheritVelocity"})
    protected final InheritVelocitySetting inheritVelocity = new InheritVelocitySetting();
    @Configurable(name = "Lifetime by Emitter Speed", subConfigurable = true, tips = {"photon.emitter.config.lifetimeByEmitterSpeed"})
    protected final LifetimeByEmitterSpeedSetting lifetimeByEmitterSpeed = new LifetimeByEmitterSpeedSetting();
    @Configurable(name = "Force over Lifetime", subConfigurable = true, tips = {"photon.emitter.config.forceOverLifetime"})
    protected final ForceOverLifetimeSetting forceOverLifetime = new ForceOverLifetimeSetting();
    @Configurable(name = "Color over Lifetime", subConfigurable = true, tips = {"photon.emitter.config.colorOverLifetime"})
    protected final ColorOverLifetimeSetting colorOverLifetime = new ColorOverLifetimeSetting();
    @Configurable(name = "Color by Speed", subConfigurable = true, tips = {"photon.emitter.config.colorBySpeed"})
    protected final ColorBySpeedSetting colorBySpeed = new ColorBySpeedSetting();
    @Configurable(name = "Size over Lifetime", subConfigurable = true, tips = {"photon.emitter.config.sizeOverLifetime"})
    protected final SizeOverLifetimeSetting sizeOverLifetime = new SizeOverLifetimeSetting();
    @Configurable(name = "Size by Speed", subConfigurable = true, tips = {"photon.emitter.config.sizeBySpeed"})
    protected final SizeBySpeedSetting sizeBySpeed = new SizeBySpeedSetting();
    @Configurable(name = "Rotation over Lifetime", subConfigurable = true, tips = {"photon.emitter.config.rotationOverLifetime"})
    protected final RotationOverLifetimeSetting rotationOverLifetime = new RotationOverLifetimeSetting();
    @Configurable(name = "Rotation by Speed", subConfigurable = true, tips = {"photon.emitter.config.rotationBySpeed"})
    protected final RotationBySpeedSetting rotationBySpeed = new RotationBySpeedSetting();
    @Configurable(name = "Noise", subConfigurable = true, tips = {"photon.emitter.config.noise"})
    protected final NoiseSetting noise = new NoiseSetting();
    @Configurable(name = "UV Animation", subConfigurable = true, tips = {"photon.emitter.config.uvAnimation"})
    protected final UVAnimationSetting uvAnimation = new UVAnimationSetting();
    @Configurable(name = "Trails", subConfigurable = true, tips = {"photon.emitter.config.trails"})
    protected final TrailsSetting trails = new TrailsSetting(this);
    @Configurable(name = "Sub Emitters", subConfigurable = true, tips = {"photon.emitter.config.sub_emitters"})
    protected final SubEmittersSetting subEmitters = new SubEmittersSetting();

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

    public void setStartDelay(NumberFunction startDelay) {
        this.startDelay = startDelay;
    }

    public NumberFunction getStartDelay() {
        return this.startDelay;
    }

    public void setStartLifetime(NumberFunction startLifetime) {
        this.startLifetime = startLifetime;
    }

    public NumberFunction getStartLifetime() {
        return this.startLifetime;
    }

    public void setStartSpeed(NumberFunction startSpeed) {
        this.startSpeed = startSpeed;
    }

    public NumberFunction getStartSpeed() {
        return this.startSpeed;
    }

    public void setStartSize(NumberFunction startSize) {
        this.startSize = startSize;
    }

    public NumberFunction getStartSize() {
        return this.startSize;
    }

    public void setStartRotation(NumberFunction3 startRotation) {
        this.startRotation = startRotation;
    }

    public NumberFunction3 getStartRotation() {
        return this.startRotation;
    }

    public void setStartColor(NumberFunction startColor) {
        this.startColor = startColor;
    }

    public NumberFunction getStartColor() {
        return this.startColor;
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }

    public int getMaxParticles() {
        return this.maxParticles;
    }

    public void setParallelUpdate(boolean parallelUpdate) {
        this.parallelUpdate = parallelUpdate;
    }

    public boolean isParallelUpdate() {
        return this.parallelUpdate;
    }

    public void setParallelRendering(boolean parallelRendering) {
        this.parallelRendering = parallelRendering;
    }

    public boolean isParallelRendering() {
        return this.parallelRendering;
    }

    public EmissionSetting getEmission() {
        return this.emission;
    }

    public ShapeSetting getShape() {
        return this.shape;
    }

    public MaterialSetting getMaterial() {
        return this.material;
    }

    public RendererSetting getRenderer() {
        return this.renderer;
    }

    public PhysicsSetting getPhysics() {
        return this.physics;
    }

    public LightOverLifetimeSetting getLights() {
        return this.lights;
    }

    public VelocityOverLifetimeSetting getVelocityOverLifetime() {
        return this.velocityOverLifetime;
    }

    public InheritVelocitySetting getInheritVelocity() {
        return this.inheritVelocity;
    }

    public LifetimeByEmitterSpeedSetting getLifetimeByEmitterSpeed() {
        return this.lifetimeByEmitterSpeed;
    }

    public ForceOverLifetimeSetting getForceOverLifetime() {
        return this.forceOverLifetime;
    }

    public ColorOverLifetimeSetting getColorOverLifetime() {
        return this.colorOverLifetime;
    }

    public ColorBySpeedSetting getColorBySpeed() {
        return this.colorBySpeed;
    }

    public SizeOverLifetimeSetting getSizeOverLifetime() {
        return this.sizeOverLifetime;
    }

    public SizeBySpeedSetting getSizeBySpeed() {
        return this.sizeBySpeed;
    }

    public RotationOverLifetimeSetting getRotationOverLifetime() {
        return this.rotationOverLifetime;
    }

    public RotationBySpeedSetting getRotationBySpeed() {
        return this.rotationBySpeed;
    }

    public NoiseSetting getNoise() {
        return this.noise;
    }

    public UVAnimationSetting getUvAnimation() {
        return this.uvAnimation;
    }

    public TrailsSetting getTrails() {
        return this.trails;
    }

    public SubEmittersSetting getSubEmitters() {
        return this.subEmitters;
    }
}
