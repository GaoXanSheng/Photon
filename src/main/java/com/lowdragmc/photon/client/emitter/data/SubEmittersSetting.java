package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.accessors.TypesAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.runtime.ConfiguratorParser;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;
import com.lowdragmc.photon.client.fx.IEffect;
import com.lowdragmc.photon.client.particle.LParticle;
import com.mojang.math.Vector4f;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/SubEmittersSetting.class */
public class SubEmittersSetting extends ToggleGroup implements IConfigurable, ITagSerializable<CompoundTag> {
    @Configurable(persisted = false)
    protected List<Emitter> emitters = new ArrayList();

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/SubEmittersSetting$Event.class */
    public enum Event {
        Birth,
        Death,
        Collision,
        FirstCollision,
        Tick
    }

    public void setEmitters(List<Emitter> emitters) {
        this.emitters = emitters;
    }

    public List<Emitter> getEmitters() {
        return this.emitters;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        PersistedParser.serializeNBT(tag, getClass(), this);
        ListTag list = new ListTag();
        for (Emitter emitter : this.emitters) {
            CompoundTag element = new CompoundTag();
            PersistedParser.serializeNBT(element, Emitter.class, emitter);
            list.add(element);
        }
        tag.m_128365_("emitters", list);
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag tag) {
        PersistedParser.deserializeNBT(tag, new HashMap(), getClass(), this);
        this.emitters.clear();
        ListTag list = tag.m_128437_("emitters", 10);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CompoundTag compoundTag = (Tag) it.next();
            if (compoundTag instanceof CompoundTag) {
                CompoundTag nbt = compoundTag;
                Emitter emitter = new Emitter();
                try {
                    PersistedParser.deserializeNBT(nbt, new HashMap(), Emitter.class, emitter);
                    this.emitters.add(emitter);
                } catch (Exception e) {
                }
            }
        }
    }

    public void triggerEvent(IParticleEmitter emitter, LParticle father, Event event) {
        IParticleEmitter subParticle;
        if (emitter.getEffect() != null) {
            for (Emitter candidate : this.emitters) {
                if (candidate.event == event && (subParticle = candidate.spawnEmitter(father, emitter.getEffect())) != null) {
                    subParticle.setEffect(emitter.getEffect());
                    emitter.emitParticle(subParticle.self());
                }
            }
        }
    }

    @ConfigAccessor
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/SubEmittersSetting$EmitterAccessor.class */
    public static class EmitterAccessor extends TypesAccessor<Emitter> {
        @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
        public /* bridge */ /* synthetic */ Object defaultValue(Field field, Class cls) {
            return defaultValue(field, (Class<?>) cls);
        }

        public EmitterAccessor() {
            super(Emitter.class);
        }

        @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
        public Emitter defaultValue(Field field, Class<?> type) {
            return new Emitter();
        }

        @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
        public Configurator create(String name, Supplier<Emitter> supplier, Consumer<Emitter> consumer, boolean forceUpdate, Field field) {
            ConfiguratorGroup group = new ConfiguratorGroup("emitter", true);
            Emitter emitter = supplier.get();
            Emitter emitter2 = emitter == null ? new Emitter() : emitter;
            ConfiguratorParser.createConfigurators(group, new HashMap(), Emitter.class, emitter2);
            group.setDraggingConsumer(o -> {
                return o instanceof IParticleEmitter;
            }, o2 -> {
                group.setBackground(ColorPattern.T_GREEN.rectTexture());
            }, o3 -> {
                group.setBackground(IGuiTexture.EMPTY);
            }, o4 -> {
                if (o4 instanceof IParticleEmitter) {
                    IParticleEmitter particleEmitter = (IParticleEmitter) o4;
                    emitter2.emitter = particleEmitter.getName();
                    consumer.accept(emitter2);
                }
                group.setBackground(IGuiTexture.EMPTY);
            });
            return group;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/SubEmittersSetting$Emitter.class */
    public static class Emitter {
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.name"})
        protected String emitter = "";
        protected IParticleEmitter cache = null;
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.event"})
        protected Event event = Event.Birth;
        @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0.0f, max = 1.0f, curveConfig = @CurveConfig(bound = {0.0f, 1.0f}, xAxis = "probability", yAxis = "lifetime"))
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.emit_probability"})
        protected NumberFunction emitProbability = NumberFunction.constant(0);
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.tick_interval"})
        @NumberRange(range = {1.0d, 2.147483647E9d})
        protected int tickInterval = 1;
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.inherit_color"})
        protected boolean inheritColor = false;
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.inherit_size"})
        protected boolean inheritSize = false;
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.inherit_rotation"})
        protected boolean inheritRotation = false;
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.inherit_lifetime"})
        protected boolean inheritLifetime = false;
        @Configurable(tips = {"photon.emitter.config.sub_emitters.emitter.inherit_duration"})
        protected boolean inheritDuration = false;

        @Nullable
        public IParticleEmitter spawnEmitter(LParticle father, @Nonnull IEffect effect) {
            if (this.cache == null) {
                this.cache = effect.getEmitterByName(this.emitter);
            }
            if (this.cache != null && father.getAge() % this.tickInterval == 0 && father.getRandomSource().m_188501_() < this.emitProbability.get(father.getT(0.0f), () -> {
                return Float.valueOf(father.getMemRandom("sub_emitter_probability"));
            }).floatValue()) {
                IParticleEmitter copied = this.cache.copy();
                copied.reset();
                copied.updatePos(father.getPos());
                if (this.inheritLifetime) {
                    copied.self().setAge(father.getAge());
                }
                if (this.inheritDuration) {
                    copied.self().m_107257_(father.m_107273_());
                }
                if (this.inheritColor) {
                    Vector4f color = father.getColor(0.0f);
                    copied.self().setARGBColor(ColorUtils.color(color.m_123617_(), color.m_123601_(), color.m_123615_(), color.m_123616_()));
                }
                if (this.inheritSize) {
                    copied.self().setQuadSize(father.getQuadSize(0.0f));
                }
                if (this.inheritRotation) {
                    copied.self().setRotation(father.getRotation(0.0f));
                }
                return copied;
            }
            return null;
        }
    }
}
