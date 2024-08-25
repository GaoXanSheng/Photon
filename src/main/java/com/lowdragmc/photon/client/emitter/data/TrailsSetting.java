package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.lowdragmc.photon.client.emitter.data.material.CustomShaderMaterial;
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
import com.lowdragmc.photon.client.emitter.particle.ParticleConfig;
import com.lowdragmc.photon.client.emitter.particle.ParticleEmitter;
import com.lowdragmc.photon.client.particle.LParticle;
import com.lowdragmc.photon.client.particle.TrailParticle;
import com.lowdragmc.photon.core.mixins.accessor.BlendModeAccessor;
import com.lowdragmc.photon.core.mixins.accessor.ShaderInstanceAccessor;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector4f;
import java.util.LinkedList;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/TrailsSetting.class */
public class TrailsSetting extends ToggleGroup {
    @Configurable(tips = {"photon.emitter.config.trails.ratio"})
    @NumberRange(range = {0.0d, 1.0d})
    protected float ratio = 1.0f;
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, max = 1.0f, defaultValue = 1.0f, curveConfig = @CurveConfig(xAxis = "lifetime", yAxis = "trail length"))
    @Configurable(tips = {"photon.emitter.config.trails.lifetime"})
    protected NumberFunction lifetime = NumberFunction.constant(1);
    @Configurable(tips = {"photon.emitter.config.trails.minimumVertexDistance"})
    @NumberRange(range = {0.0d, 3.4028234663852886E38d})
    protected float minimumVertexDistance = 0.02f;
    @Configurable(tips = {"photon.emitter.config.trails.dieWithParticles"})
    protected boolean dieWithParticles = false;
    @Configurable(tips = {"photon.emitter.config.trails.uvMode"})
    protected TrailParticle.UVMode uvMode = TrailParticle.UVMode.Stretch;
    @Configurable(tips = {"photon.emitter.config.trails.sizeAffectsWidth"})
    protected boolean sizeAffectsWidth = true;
    @Configurable(tips = {"photon.emitter.config.trails.sizeAffectsLifetime"})
    protected boolean sizeAffectsLifetime = false;
    @Configurable(tips = {"photon.emitter.config.trails.inheritParticleColor"})
    protected boolean inheritParticleColor = true;
    @NumberFunctionConfig(types = {Color.class, RandomColor.class, Gradient.class, RandomGradient.class}, defaultValue = -1.0f)
    @Configurable(tips = {"photon.emitter.config.trails.colorOverLifetime"})
    protected NumberFunction colorOverLifetime = new Gradient();
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "trail position", yAxis = "width"))
    @Configurable(tips = {"photon.emitter.config.trails.widthOverTrail"})
    protected NumberFunction widthOverTrail = NumberFunction.constant(Float.valueOf(1.0f));
    @NumberFunctionConfig(types = {Color.class, RandomColor.class, Gradient.class, RandomGradient.class}, defaultValue = -1.0f)
    @Configurable(tips = {"photon.emitter.config.trails.colorOverTrail"})
    protected NumberFunction colorOverTrail = new Gradient();
    @Configurable(name = "Material", subConfigurable = true, tips = {"photon.emitter.config.material"})
    protected final MaterialSetting material = new MaterialSetting();
    protected final PhotonParticleRenderType renderType;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getRatio() {
        return this.ratio;
    }

    public void setLifetime(NumberFunction lifetime) {
        this.lifetime = lifetime;
    }

    public NumberFunction getLifetime() {
        return this.lifetime;
    }

    public void setMinimumVertexDistance(float minimumVertexDistance) {
        this.minimumVertexDistance = minimumVertexDistance;
    }

    public float getMinimumVertexDistance() {
        return this.minimumVertexDistance;
    }

    public void setDieWithParticles(boolean dieWithParticles) {
        this.dieWithParticles = dieWithParticles;
    }

    public boolean isDieWithParticles() {
        return this.dieWithParticles;
    }

    public void setUvMode(TrailParticle.UVMode uvMode) {
        this.uvMode = uvMode;
    }

    public TrailParticle.UVMode getUvMode() {
        return this.uvMode;
    }

    public void setSizeAffectsWidth(boolean sizeAffectsWidth) {
        this.sizeAffectsWidth = sizeAffectsWidth;
    }

    public boolean isSizeAffectsWidth() {
        return this.sizeAffectsWidth;
    }

    public void setSizeAffectsLifetime(boolean sizeAffectsLifetime) {
        this.sizeAffectsLifetime = sizeAffectsLifetime;
    }

    public boolean isSizeAffectsLifetime() {
        return this.sizeAffectsLifetime;
    }

    public void setInheritParticleColor(boolean inheritParticleColor) {
        this.inheritParticleColor = inheritParticleColor;
    }

    public boolean isInheritParticleColor() {
        return this.inheritParticleColor;
    }

    public void setColorOverLifetime(NumberFunction colorOverLifetime) {
        this.colorOverLifetime = colorOverLifetime;
    }

    public NumberFunction getColorOverLifetime() {
        return this.colorOverLifetime;
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

    public TrailsSetting(ParticleConfig config) {
        this.renderType = new RenderType(config);
        this.material.setMaterial(new CustomShaderMaterial());
        this.material.cull = false;
    }

    public void setup(ParticleEmitter emitter, LParticle particle) {
        RandomSource random = emitter.getRandomSource();
        if (random.m_188501_() < this.ratio) {
            Vector3 pos = particle.getPos();
            TrailParticle.Basic trail = new TrailParticle.Basic(emitter.getClientLevel(), pos.x, pos.y, pos.z, this.renderType);
            trail.setDelay(particle.getDelay());
            trail.setLevel(emitter.getLevel());
            trail.m_107257_(particle.m_107273_());
            trail.setWidth((float) particle.getQuadSize(0.0f).min());
            trail.setUvMode(this.uvMode);
            trail.setMinimumVertexDistance(this.minimumVertexDistance);
            trail.setOnUpdate(p -> {
                Vector3 positionO = particle.getPos(0.0f);
                Vector3 position = particle.getPos(1.0f);
                p.setPos(positionO.x, positionO.y, positionO.z, true);
                p.setPos(position.x, position.y, position.z, false);
            });
            trail.setOnRemoveTails(t -> {
                float maxTails = this.lifetime.get(particle.getT(), () -> {
                    return Float.valueOf(particle.getMemRandom("trails-lifetime"));
                }).floatValue() * particle.m_107273_();
                if (this.sizeAffectsLifetime) {
                    maxTails = (float) (maxTails * (particle.getQuadSize(0.0f).min() / particle.getWidth()));
                }
                LinkedList<Vector3> tails = particle.getTails();
                while (tails.size() > maxTails) {
                    tails.removeFirst();
                }
                return true;
            });
            trail.setDieWhenRemoved(this.dieWithParticles);
            trail.setDynamicColor(t2, partialTicks -> {
                float a = 1.0f;
                float r = 1.0f;
                float g = 1.0f;
                float b = 1.0f;
                if (this.inheritParticleColor) {
                    Vector4f color = particle.getColor(partialTicks.floatValue());
                    a = 1.0f * color.m_123617_();
                    r = 1.0f * color.m_123601_();
                    g = 1.0f * color.m_123615_();
                    b = 1.0f * color.m_123616_();
                }
                int color2 = this.colorOverLifetime.get(particle.getT(partialTicks.floatValue()), () -> {
                    return Float.valueOf(particle.getMemRandom("trails-colorOverLifetime"));
                }).intValue();
                return new Vector4f(r * ColorUtils.red(color2), g * ColorUtils.green(color2), b * ColorUtils.blue(color2), a * ColorUtils.alpha(color2));
            });
            trail.setDynamicTailColor(t3, tail, partialTicks2 -> {
                int color = this.colorOverTrail.get(tail.intValue() / (t3.getTails().size() - 1.0f), () -> {
                    return Float.valueOf(t3.getMemRandom("trails-colorOverTrail"));
                }).intValue();
                return new Vector4f(ColorUtils.red(color), ColorUtils.green(color), ColorUtils.blue(color), ColorUtils.alpha(color));
            });
            trail.setDynamicTailWidth(t4, tail2, partialTicks3 -> {
                float width = particle.getWidth();
                if (this.sizeAffectsWidth) {
                    width = (float) particle.getQuadSize(partialTicks3.floatValue()).min();
                }
                return Float.valueOf(width * this.widthOverTrail.get(tail2.intValue() / (particle.getTails().size() - 1.0f), () -> {
                    return Float.valueOf(particle.getMemRandom("trails-widthOverTrail"));
                }).floatValue());
            });
            trail.setDynamicLight(p2, partialTicks4 -> {
                if (emitter.usingBloom()) {
                    return 15728880;
                }
                if (emitter.getConfig().getLights().isEnable()) {
                    return Integer.valueOf(emitter.getConfig().getLights().getLight(p2, partialTicks4.floatValue()));
                }
                return Integer.valueOf(p2.getLight(partialTicks4.floatValue()));
            });
            emitter.emitParticle(trail);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/TrailsSetting$RenderType.class */
    private class RenderType extends PhotonParticleRenderType {
        protected final ParticleConfig config;
        private BlendMode lastBlend = null;

        public RenderType(ParticleConfig config) {
            this.config = config;
        }

        @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
        public void prepareStatus() {
            if (this.config.getRenderer().isBloomEffect()) {
                beginBloom();
            }
            TrailsSetting.this.material.pre();
            TrailsSetting.this.material.getMaterial().begin(false);
            ShaderInstanceAccessor m_157196_ = RenderSystem.m_157196_();
            if (m_157196_ instanceof ShaderInstanceAccessor) {
                ShaderInstanceAccessor shader = m_157196_;
                this.lastBlend = BlendModeAccessor.getLastApplied();
                BlendModeAccessor.setLastApplied(shader.getBlend());
            }
            Minecraft.m_91087_().f_91063_.m_109154_().m_109896_();
        }

        @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
        public void begin(@Nonnull BufferBuilder bufferBuilder) {
            bufferBuilder.m_166779_(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.f_85813_);
        }

        @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
        public void releaseStatus() {
            TrailsSetting.this.material.getMaterial().end(false);
            TrailsSetting.this.material.post();
            if (this.lastBlend != null) {
                this.lastBlend.m_85526_();
                this.lastBlend = null;
            }
            if (this.config.getRenderer().isBloomEffect()) {
                endBloom();
            }
        }

        @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
        public boolean isParallel() {
            return this.config.isParallelRendering();
        }

        public int hashCode() {
            return this.config.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof RenderType) {
                RenderType type = (RenderType) obj;
                return type.config.equals(this.config);
            }
            return super.equals(obj);
        }
    }
}
