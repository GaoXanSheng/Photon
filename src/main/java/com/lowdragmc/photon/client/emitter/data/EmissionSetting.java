package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.accessors.TypesAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigAccessor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.runtime.ConfiguratorParser;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.photon.client.emitter.data.number.Constant;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.client.emitter.data.number.curve.Curve;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurve;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/6/1
 * @implNote EmissionSetting
 */
@OnlyIn(Dist.CLIENT)
public class EmissionSetting implements IConfigurable, ITagSerializable<CompoundTag> {

    public enum Mode {
        Exacting,
        Random
    }

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.emission.emissionRate")
    @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0, defaultValue = 0.5f, curveConfig = @CurveConfig(bound = {0, 5}, xAxis = "duration", yAxis = "emission rate"))
    protected NumberFunction emissionRate = NumberFunction.constant(0.5f);

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.emission.emissionMode")
    protected Mode emissionMode = Mode.Exacting;

    @Setter
    @Getter
    @Configurable(tips = "photon.emitter.config.emission.bursts", persisted = false)
    protected List<Burst> bursts = new ArrayList<>();

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        PersistedParser.serializeNBT(tag, getClass(), this);
        var list = new ListTag();
        for (var burst : bursts) {
            var element = new CompoundTag();
            PersistedParser.serializeNBT(element, Burst.class, burst);
            list.add(element);
        }
        tag.put("bursts", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        PersistedParser.deserializeNBT(tag, new HashMap<>(), getClass(), this);
        bursts.clear();
        var list = tag.getList("bursts", Tag.TAG_COMPOUND);
        for (var element : list) {
            if (element instanceof CompoundTag nbt) {
                var burst = new Burst();
                PersistedParser.deserializeNBT(nbt, new HashMap<>(), Burst.class, burst);
                bursts.add(burst);
            }
        }
    }

    public int getEmissionCount(int emitterAge, float t, RandomSource randomSource) {
        var result = emissionRate.get(randomSource, t);
        var number = result.intValue();
        var decimals = result.floatValue() - result.intValue();
        if (emissionMode == Mode.Exacting) {
            if (decimals > 0 && emitterAge % ((int) (1 / decimals)) == 0) {
                number += 1;
            }
        } else {
            if (randomSource.nextFloat() < decimals) {
                number += 1;
            }
        }
        for (var bust : bursts) {
            var realAge = emitterAge - bust.time;
            if (realAge >= 0) {
                var count = bust.count.get(randomSource, t).intValue();
                if (realAge % bust.interval == 0) {
                    if (bust.cycles == 0) {
                        if (randomSource.nextFloat() < bust.probability) {
                            number += count;
                        }
                    } else if (realAge / bust.interval < bust.cycles) {
                        if (randomSource.nextFloat() < bust.probability) {
                            number += count;
                        }
                    }
                }
            }
        }
        return number;
    }

    @ConfigAccessor
    public static class BurstAccessor extends TypesAccessor<Burst> {

        public BurstAccessor() {
            super(Burst.class);
        }

        @Override
        public Burst defaultValue(Field field, Class<?> type) {
            return new Burst();
        }

        @Override
        public Configurator create(String name, Supplier<Burst> supplier, Consumer<Burst> consumer, boolean forceUpdate, Field field) {
            var group = new ConfiguratorGroup("burst", true);
            var burst = supplier.get();
            burst = burst == null ? new Burst() : burst;
            ConfiguratorParser.createConfigurators(group, new HashMap<>(), Burst.class, burst);
            return group;
        }
    }

    public static class Burst {
        @Configurable(tips = "photon.emitter.config.emission.bursts.time")
        @NumberRange(range = {0, Integer.MAX_VALUE}, wheel = 1)
        public int time = 0;
        @Setter
        @Getter
        @Configurable(tips = "photon.emitter.config.emission.bursts.count")
        @NumberFunctionConfig(types = {Constant.class, RandomConstant.class, Curve.class, RandomCurve.class}, min = 0, defaultValue = 50, curveConfig = @CurveConfig(bound = {0, 50}, xAxis = "duration", yAxis = "emit count"))
        protected NumberFunction count = NumberFunction.constant(50);
        @Configurable(tips = "photon.emitter.config.emission.bursts.cycles")
        @NumberRange(range = {0, Integer.MAX_VALUE})
        public int cycles = 1;
        @Configurable(tips = "photon.emitter.config.emission.bursts.interval")
        @NumberRange(range = {1, Integer.MAX_VALUE}, wheel = 1)
        public int interval = 1;
        @Configurable(tips = "photon.emitter.config.emission.bursts.probability")
        @NumberRange(range = {0, 1})
        public float probability = 1;
    }
}
