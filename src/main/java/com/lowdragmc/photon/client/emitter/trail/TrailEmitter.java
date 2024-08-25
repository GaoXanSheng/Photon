package com.lowdragmc.photon.client.emitter.trail;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegisterClient;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.runtime.ConfiguratorParser;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.emitter.ParticleQueueRenderType;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.lowdragmc.photon.client.emitter.data.material.CustomShaderMaterial;
import com.lowdragmc.photon.client.fx.IEffect;
import com.lowdragmc.photon.client.particle.LParticle;
import com.lowdragmc.photon.client.particle.TrailParticle;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

@LDLRegisterClient(name = "trail", group = "emitter")
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/trail/TrailEmitter.class */
public class TrailEmitter extends TrailParticle implements IParticleEmitter {
    public static int VERSION = 1;
    @Persisted
    protected String name;
    @Persisted
    protected boolean isSubEmitter;
    @Persisted(subPersisted = true)
    protected final TrailConfig config;
    protected final PhotonParticleRenderType renderType;
    protected final Map<PhotonParticleRenderType, Queue<LParticle>> particles;
    protected boolean visible;
    @Nullable
    protected IEffect effect;
    protected LinkedList<AtomicInteger> tailsTime;

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

    public TrailConfig getConfig() {
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

    public TrailEmitter() {
        this(new TrailConfig());
        this.config.material.setMaterial(new CustomShaderMaterial());
    }

    public TrailEmitter(TrailConfig config) {
        super(null, 0.0d, 0.0d, 0.0d);
        this.name = "trail emitter";
        this.isSubEmitter = false;
        this.visible = true;
        this.tailsTime = new LinkedList<>();
        this.config = config;
        this.renderType = new RenderType(config);
        this.particles = Map.of(this.renderType, new ArrayDeque(1));
        init();
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public IParticleEmitter copy(boolean deep) {
        IParticleEmitter copied = deep ? super.copy() : new TrailEmitter(this.config);
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
        int version = tag.m_128441_("_version") ? tag.m_128451_("_version") : 0;
        if (version == 0) {
            this.name = tag.m_128461_("name");
            PersistedParser.deserializeNBT(tag, new HashMap(), this.config.getClass(), this.config);
            return;
        }
        super.deserializeNBT(tag);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        ConfiguratorParser.createConfigurators(father, new HashMap(), this.config.getClass(), this.config);
    }

    public void init() {
        this.particles.get(this.renderType).clear();
        this.particles.get(this.renderType).add(this);
        super.m_107257_(-1);
        this.config.renderer.setupQuaternion(this, this);
        super.setUvMode(this.config.uvMode);
        super.setMinimumVertexDistance(this.config.minVertexDistance);
        super.setOnRemoveTails(t -> {
            Iterator<AtomicInteger> iterT = this.tailsTime.iterator();
            Iterator<Vector3> iter = this.tails.iterator();
            while (iter.hasNext() && iterT.hasNext()) {
                AtomicInteger tailTime = iterT.next();
                iter.next();
                if (tailTime.getAndAdd(1) > this.config.time) {
                    iterT.remove();
                    iter.remove();
                }
            }
            return true;
        });
        super.setDieWhenRemoved(false);
        super.setDynamicTailColor(t2, tail, partialTicks -> {
            int color = this.config.colorOverTrail.get(tail.intValue() / (t2.getTails().size() - 1.0f), () -> {
                return Float.valueOf(t2.getMemRandom("trails-colorOverTrail"));
            }).intValue();
            return new Vector4f(ColorUtils.red(color), ColorUtils.green(color), ColorUtils.blue(color), ColorUtils.alpha(color));
        });
        super.setDynamicTailWidth(t3, tail2, partialTicks2 -> {
            return Float.valueOf(0.2f * this.config.widthOverTrail.get(tail2.intValue() / (t3.getTails().size() - 1.0f), () -> {
                return Float.valueOf(t3.getMemRandom("trails-widthOverTrail"));
            }).floatValue());
        });
        super.setDynamicLight(t4, partialTicks3 -> {
            if (usingBloom()) {
                return 15728880;
            }
            if (this.config.lights.isEnable()) {
                return Integer.valueOf(this.config.lights.getLight(t4, partialTicks3.floatValue()));
            }
            return Integer.valueOf(t4.getLight(partialTicks3.floatValue()));
        });
        super.setDynamicUVs(p, partialTicks4 -> {
            if (this.config.uvAnimation.isEnable()) {
                Vector4f uvs = this.config.uvAnimation.getUVs(p, partialTicks4.floatValue());
                float x = uvs.m_123601_();
                float y = uvs.m_123615_();
                float w = uvs.m_123616_() - uvs.m_123601_();
                float h = uvs.m_123617_() - uvs.m_123615_();
                return new Vector4f(x + (w * p.getU0(partialTicks4.floatValue())), y + (h * p.getV0(partialTicks4.floatValue())), x + (w * p.getU1(partialTicks4.floatValue())), y + (h * p.getV1(partialTicks4.floatValue())));
            }
            return new Vector4f(p.getU0(partialTicks4.floatValue()), p.getV0(partialTicks4.floatValue()), p.getU1(partialTicks4.floatValue()), p.getV1(partialTicks4.floatValue()));
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void update() {
        if (this.effect != null && this.effect.updateEmitter(this)) {
            return;
        }
        if (this.effect == null) {
            this.config.renderer.setupQuaternion(this, this);
            setUvMode(this.config.uvMode);
        }
        super.update();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.photon.client.particle.TrailParticle
    public void addNewTail(Vector3 tail) {
        super.addNewTail(tail);
        this.tailsTime.addLast(new AtomicInteger(0));
    }

    @Override // com.lowdragmc.photon.client.particle.TrailParticle
    @ConfigSetter(field = "minVertexDistance")
    public void setMinimumVertexDistance(float minimumVertexDistance) {
        super.setMinimumVertexDistance(minimumVertexDistance);
        this.config.minVertexDistance = minimumVertexDistance;
    }

    @Override // com.lowdragmc.photon.client.particle.TrailParticle, com.lowdragmc.photon.client.particle.LParticle
    public void resetParticle() {
        super.resetParticle();
        this.tailsTime.clear();
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void m_5744_(@NotNull VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        if (ParticleQueueRenderType.INSTANCE.isRenderingQueue()) {
            super.m_5744_(pBuffer, pRenderInfo, pPartialTicks);
        } else if (this.delay <= 0 && isVisible() && PhotonParticleRenderType.checkLayer(this.config.renderer.getLayer())) {
            if (!this.config.renderer.getCull().isEnable() || PhotonParticleRenderType.checkFrustum(this.config.renderer.getCull().getCullAABB(this, pPartialTicks))) {
                ParticleQueueRenderType.INSTANCE.pipeQueue(this.renderType, this.particles.get(this.renderType), pRenderInfo, pPartialTicks);
            }
        }
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    @Nonnull
    /* renamed from: getRenderType */
    public PhotonParticleRenderType m_7556_() {
        return ParticleQueueRenderType.INSTANCE;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public boolean emitParticle(LParticle particle) {
        return false;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    @Nullable
    public AABB getCullBox(float partialTicks) {
        if (this.config.renderer.getCull().isEnable()) {
            return this.config.renderer.getCull().getCullAABB(this, partialTicks);
        }
        return null;
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public boolean usingBloom() {
        return this.config.renderer.isBloomEffect();
    }

    @Override // com.lowdragmc.photon.client.emitter.IParticleEmitter
    public void remove(boolean force) {
        m_107274_();
        if (force) {
            this.dieWhenRemoved = true;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/trail/TrailEmitter$RenderType.class */
    private static class RenderType extends PhotonParticleRenderType {
        protected final TrailConfig config;
        private BlendMode lastBlend = null;

        public RenderType(TrailConfig config) {
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
            bufferBuilder.m_166779_(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.f_85813_);
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
