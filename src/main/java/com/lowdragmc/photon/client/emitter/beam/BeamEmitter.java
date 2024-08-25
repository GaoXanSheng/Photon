package com.lowdragmc.photon.client.emitter.beam;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegisterClient;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.runtime.ConfiguratorParser;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.emitter.ParticleQueueRenderType;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.lowdragmc.photon.client.emitter.data.material.TextureMaterial;
import com.lowdragmc.photon.client.fx.IEffect;
import com.lowdragmc.photon.client.particle.BeamParticle;
import com.lowdragmc.photon.client.particle.LParticle;
import com.lowdragmc.photon.core.mixins.accessor.BlendModeAccessor;
import com.lowdragmc.photon.core.mixins.accessor.ShaderInstanceAccessor;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector4f;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

@LDLRegisterClient(name = "beam", group = "emitter")
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/beam/BeamEmitter.class */
public class BeamEmitter extends BeamParticle implements IParticleEmitter {
    public static int VERSION = 1;
    @Persisted
    protected String name;
    @Persisted
    protected boolean isSubEmitter;
    @Persisted(subPersisted = true)
    protected final BeamConfig config;
    protected final PhotonParticleRenderType renderType;
    protected final Map<PhotonParticleRenderType, Queue<LParticle>> particles;
    protected boolean visible;
    @Nullable
    protected IEffect effect;

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public void setName(String name) {
        this.name = name;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public String getName() {
        return this.name;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public void setSubEmitter(boolean isSubEmitter) {
        this.isSubEmitter = isSubEmitter;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public boolean isSubEmitter() {
        return this.isSubEmitter;
    }

    public BeamConfig getConfig() {
        return this.config;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public Map<PhotonParticleRenderType, Queue<LParticle>> getParticles() {
        return this.particles;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public boolean isVisible() {
        return this.visible;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    @Nullable
    public IEffect getEffect() {
        return this.effect;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public void setEffect(@Nullable IEffect effect) {
        this.effect = effect;
    }

    public BeamEmitter() {
        this(new BeamConfig());
        this.config.material.setMaterial(new TextureMaterial(new ResourceLocation("photon:textures/particle/laser.png")));
    }

    public BeamEmitter(BeamConfig config) {
        super(null, new Vector3(0.0d, 0.0d, 0.0d), new Vector3(3.0d, 0.0d, 0.0d));
        this.name = "beam emitter";
        this.isSubEmitter = false;
        this.visible = true;
        this.config = config;
        this.renderType = new RenderType(config);
        this.particles = Map.of(this.renderType, new ArrayDeque(1));
        init();
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public IParticleEmitter copy(boolean deep) {
        IParticleEmitter copied = deep ? super.copy() : new BeamEmitter(this.config);
        copied.setName(this.name);
        copied.setSubEmitter(this.isSubEmitter);
        return copied;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAutoPersistedSerializable, com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = super.mo129serializeNBT();
        tag.m_128405_("_version", VERSION);
        return tag;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAutoPersistedSerializable, com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        ConfiguratorParser.createConfigurators(father, new HashMap(), this.config.getClass(), this.config);
    }

    public void init() {
        this.particles.get(this.renderType).clear();
        this.particles.get(this.renderType).add(this);
        this.end = this.config.end;
        this.config.renderer.setupQuaternion(this, this);
        super.setDelay(this.config.startDelay);
        super.setDynamicLight(p, partialTicks -> {
            if (usingBloom()) {
                return 15728880;
            }
            if (this.config.lights.isEnable()) {
                return Integer.valueOf(this.config.lights.getLight(p, partialTicks.floatValue()));
            }
            return Integer.valueOf(p.getLight(partialTicks.floatValue()));
        });
        super.setDynamicColor(p2, partialTicks2 -> {
            int color = this.config.color.get(p2.getT(partialTicks2.floatValue()), () -> {
                return Float.valueOf(p2.getMemRandom("color"));
            }).intValue();
            return new Vector4f(ColorUtils.red(color), ColorUtils.green(color), ColorUtils.blue(color), ColorUtils.alpha(color));
        });
        super.setDynamicWidth(p3, partialTicks3 -> {
            return Float.valueOf(this.config.width.get(p3.getT(partialTicks3.floatValue()), () -> {
                return Float.valueOf(p3.getMemRandom("width"));
            }).floatValue());
        });
        super.setDynamicEmit(p4, partialTicks4 -> {
            return Float.valueOf(this.config.emitRate.get(p4.getT(partialTicks4.floatValue()), () -> {
                return Float.valueOf(p4.getMemRandom("emit"));
            }).floatValue());
        });
    }

    public int m_107273_() {
        return this.config.duration;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void updateOrigin() {
        super.updateOrigin();
        m_107257_(this.config.duration);
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void resetParticle() {
        super.resetParticle();
        init();
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void m_5989_() {
        if (this.effect != null && this.effect.updateEmitter(this)) {
            return;
        }
        if (isDev() && !this.config.end.equals(this.end)) {
            this.end = this.config.end;
        }
        if (this.delay > 0) {
            this.delay--;
            return;
        }
        updateOrigin();
        if (this.f_107224_ >= this.config.duration && !this.config.isLooping()) {
            m_107274_();
        }
        update();
        this.f_107224_++;
        this.t = ((this.f_107224_ % this.config.duration) * 1.0f) / this.config.duration;
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void m_5744_(@NotNull VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        if (ParticleQueueRenderType.INSTANCE.isRenderingQueue()) {
            super.m_5744_(pBuffer, pRenderInfo, pPartialTicks);
        } else if (this.delay <= 0 && isVisible() && PhotonParticleRenderType.checkLayer(this.config.renderer.getLayer()) && PhotonParticleRenderType.checkFrustum(getCullBox(pPartialTicks))) {
            ParticleQueueRenderType.INSTANCE.pipeQueue(this.renderType, this.particles.get(this.renderType), pRenderInfo, pPartialTicks);
        }
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    @Nonnull
    /* renamed from: getRenderType */
    public final PhotonParticleRenderType m_7556_() {
        return ParticleQueueRenderType.INSTANCE;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public boolean emitParticle(LParticle particle) {
        return false;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    @Nonnull
    public AABB getCullBox(float partialTicks) {
        return new AABB(getPos(partialTicks).vec3(), this.end.vec3()).m_82400_(getWidth(partialTicks));
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public boolean usingBloom() {
        return this.config.renderer.isBloomEffect();
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/beam/BeamEmitter$RenderType.class */
    private static class RenderType extends PhotonParticleRenderType {
        protected final BeamConfig config;
        private BlendMode lastBlend = null;

        public RenderType(BeamConfig config) {
            this.config = config;
        }

        @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
        public void prepareStatus() {
            if (this.config.renderer.isBloomEffect()) {
                beginBloom();
            }
            this.config.material.pre();
            this.config.material.getMaterial().begin(false);
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
            bufferBuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85813_);
        }

        @Override // com.lowdragmc.photon.client.emitter.PhotonParticleRenderType
        public void releaseStatus() {
            this.config.material.getMaterial().end(false);
            this.config.material.post();
            if (this.lastBlend != null) {
                this.lastBlend.m_85526_();
                this.lastBlend = null;
            }
            if (this.config.renderer.isBloomEffect()) {
                endBloom();
            }
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
