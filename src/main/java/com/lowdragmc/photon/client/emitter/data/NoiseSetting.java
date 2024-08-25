package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.lowdraglib.utils.noise.PerlinNoise;
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
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/NoiseSetting.class */
public class NoiseSetting extends ToggleGroup {
    private final ThreadLocal<PerlinNoise> noise = ThreadLocal.withInitial(PerlinNoise::new);
    @Configurable(tips = {"photon.emitter.config.noise.frequency"})
    @NumberRange(range = {1.401298464324817E-45d, 3.4028234663852886E38d})
    protected float frequency = 1.0f;
    @Configurable(tips = {"photon.emitter.config.noise.quality"})
    protected Quality quality = Quality.Noise2D;
    @Configurable(subConfigurable = true, tips = {"photon.emitter.config.noise.remap"})
    protected final Remap remap = new Remap();
    @NumberFunction3Config(common = @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "lifetime", yAxis = "strength")))
    @Configurable(tips = {"photon.emitter.config.noise.position"})
    protected NumberFunction3 position = new NumberFunction3(Double.valueOf(0.1d), Double.valueOf(0.1d), Double.valueOf(0.1d));
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, wheelDur = 10.0f, curveConfig = @CurveConfig(bound = {0.0f, 180.0f}, xAxis = "rotation amount", yAxis = "lifetime"))
    @Configurable(tips = {"photon.emitter.config.noise.rotation"})
    protected NumberFunction rotation = NumberFunction.constant(0);
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, curveConfig = @CurveConfig(bound = {-1.0f, 1.0f}, xAxis = "size amount", yAxis = "lifetime"))
    @Configurable(tips = {"photon.emitter.config.noise.size"})
    protected NumberFunction size = NumberFunction.constant(0);

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/NoiseSetting$Quality.class */
    public enum Quality {
        Noise1D,
        Noise2D,
        Noise3D
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getFrequency() {
        return this.frequency;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Quality getQuality() {
        return this.quality;
    }

    public Remap getRemap() {
        return this.remap;
    }

    public void setPosition(NumberFunction3 position) {
        this.position = position;
    }

    public NumberFunction3 getPosition() {
        return this.position;
    }

    public void setRotation(NumberFunction rotation) {
        this.rotation = rotation;
    }

    public NumberFunction getRotation() {
        return this.rotation;
    }

    public void setSize(NumberFunction size) {
        this.size = size;
    }

    public NumberFunction getSize() {
        return this.size;
    }

    public float getNoise(float t) {
        float noise;
        float input = t * this.frequency;
        switch (this.quality) {
            case Noise1D:
                noise = (float) this.noise.get().noise(input);
                break;
            case Noise2D:
                noise = (float) this.noise.get().noise(input, input);
                break;
            case Noise3D:
                noise = (float) this.noise.get().noise(input, input, input);
                break;
            default:
                throw new IncompatibleClassChangeError();
        }
        float value = noise;
        if (this.remap.isEnable()) {
            value = this.remap.remapCurve.get((value + 1.0f) / 2.0f, () -> {
                return Float.valueOf(0.0f);
            }).floatValue();
        }
        return value;
    }

    public void setupSeed(LParticle particle) {
        this.noise.get().setSeed(particle.getMemRandom("noise-seed", randomSource -> {
            return Float.valueOf((float) randomSource.m_188583_());
        }) * 255.0f);
    }

    public Vector3 getRotation(LParticle particle, float partialTicks) {
        setupSeed(particle);
        float t = particle.getT(partialTicks);
        float degree = this.rotation.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("noise-rotation"));
        }).floatValue();
        if (degree != 0.0f) {
            return new Vector3(degree, 0.0d, 0.0d).multiply((getNoise((t + (10.0f * particle.getMemRandom("noise-rotation-degree"))) * 100.0f) * 6.2831855f) / 360.0f);
        }
        return new Vector3(0.0d, 0.0d, 0.0d);
    }

    public Vector3 getSize(LParticle particle, float partialTicks) {
        setupSeed(particle);
        float t = particle.getT(partialTicks);
        float scale = this.size.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("noise-size"));
        }).floatValue();
        if (scale != 0.0f) {
            return new Vector3(scale, scale, scale).multiply(getNoise((t + (10.0f * particle.getMemRandom("noise-size-scale"))) * 100.0f));
        }
        return new Vector3(0.0d, 0.0d, 0.0d);
    }

    public Vector3 getPosition(LParticle particle, float partialTicks) {
        setupSeed(particle);
        float t = particle.getT(partialTicks);
        Vector3 offset = this.position.get(t, () -> {
            return Float.valueOf(particle.getMemRandom("noise-position"));
        });
        if (offset.x != 0.0d || offset.y != 0.0d || offset.z != 0.0d) {
            offset.multiply(getNoise((t + (10.0f * particle.getMemRandom("noise-position-x"))) * 100.0f), getNoise((t + (10.0f * particle.getMemRandom("noise-position-y"))) * 100.0f), getNoise((t + (10.0f * particle.getMemRandom("noise-position-z"))) * 100.0f));
            return offset;
        }
        return new Vector3(0.0d, 0.0d, 0.0d);
    }

    @Override // com.lowdragmc.photon.client.emitter.data.ToggleGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        super.buildConfigurator(father);
        father.addConfigurator(0, new WrapperConfigurator("Noise preview", new ImageWidget(0, 0, 100, 100, new NoisePreview(LDLib.random.nextGaussian() * 255.0d))));
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/NoiseSetting$Remap.class */
    public static class Remap extends ToggleGroup {
        @NumberFunctionConfig(types = {Curve.class}, defaultValue = 1.0f, curveConfig = @CurveConfig(bound = {-1.0f, 1.0f}, xAxis = "base noise", yAxis = "remap result"))
        @Configurable
        protected NumberFunction remapCurve = new Curve(-2.1474836E9f, 2.1474836E9f, -1.0f, 1.0f, 1.0f, "base noise", "remap result");

        public void setRemapCurve(NumberFunction remapCurve) {
            this.remapCurve = remapCurve;
        }

        public NumberFunction getRemapCurve() {
            return this.remapCurve;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/NoiseSetting$NoisePreview.class */
    private class NoisePreview implements IGuiTexture {
        private final double seed;

        public NoisePreview(double seed) {
            this.seed = seed;
        }

        @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
        @OnlyIn(Dist.CLIENT)
        public void draw(PoseStack pose, int mouseX, int mouseY, float x, float y, int width, int height) {
            float value;
            NoiseSetting.this.noise.get().setSeed(this.seed);
            RenderSystem.m_69478_();
            RenderSystem.m_69453_();
            Matrix4f mat = pose.m_85850_().m_85861_();
            Tesselator tesselator = Tesselator.m_85913_();
            BufferBuilder buffer = tesselator.m_85915_();
            RenderSystem.m_157427_(GameRenderer::m_172811_);
            buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85815_);
            for (int i = 0; i < width; i++) {
                if (NoiseSetting.this.quality == Quality.Noise1D) {
                    float value2 = (((float) NoiseSetting.this.noise.get().noise(i * NoiseSetting.this.frequency)) + 1.0f) / 2.0f;
                    if (NoiseSetting.this.remap.isEnable()) {
                        value2 = (NoiseSetting.this.remap.remapCurve.get(value2, () -> {
                            return Float.valueOf(0.0f);
                        }).floatValue() + 1.0f) / 2.0f;
                    }
                    buffer.m_85982_(mat, x + i + 1.0f, y, 0.0f).m_85950_(value2, value2, value2, 1.0f).m_5752_();
                    buffer.m_85982_(mat, x + i, y, 0.0f).m_85950_(value2, value2, value2, 1.0f).m_5752_();
                    buffer.m_85982_(mat, x + i, y + height, 0.0f).m_85950_(value2, value2, value2, 1.0f).m_5752_();
                    buffer.m_85982_(mat, x + i + 1.0f, y + height, 0.0f).m_85950_(value2, value2, value2, 1.0f).m_5752_();
                } else {
                    for (int j = 0; j < height; j++) {
                        if (NoiseSetting.this.quality == Quality.Noise2D) {
                            value = (((float) NoiseSetting.this.noise.get().noise(i * NoiseSetting.this.frequency, j * NoiseSetting.this.frequency)) + 1.0f) / 2.0f;
                        } else {
                            value = (((float) NoiseSetting.this.noise.get().noise(i * NoiseSetting.this.frequency, j * NoiseSetting.this.frequency, 1.0d)) + 1.0f) / 2.0f;
                        }
                        if (NoiseSetting.this.remap.isEnable()) {
                            value = (NoiseSetting.this.remap.remapCurve.get(value, () -> {
                                return Float.valueOf(0.0f);
                            }).floatValue() + 1.0f) / 2.0f;
                        }
                        buffer.m_85982_(mat, x + i + 1.0f, y + j, 0.0f).m_85950_(value, value, value, 1.0f).m_5752_();
                        buffer.m_85982_(mat, x + i, y + j, 0.0f).m_85950_(value, value, value, 1.0f).m_5752_();
                        buffer.m_85982_(mat, x + i, y + j + 1.0f, 0.0f).m_85950_(value, value, value, 1.0f).m_5752_();
                        buffer.m_85982_(mat, x + i + 1.0f, y + j + 1.0f, 0.0f).m_85950_(value, value, value, 1.0f).m_5752_();
                    }
                }
            }
            tesselator.m_85914_();
        }
    }
}
