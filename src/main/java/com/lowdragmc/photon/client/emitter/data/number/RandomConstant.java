package com.lowdragmc.photon.client.emitter.data.number;

import com.lowdragmc.lowdraglib.gui.editor.configurator.NumberConfigurator;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.gui.editor.configurator.NumberFunctionConfigurator;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/RandomConstant.class */
public class RandomConstant implements NumberFunction {
    private Number a;
    private Number b;
    private boolean isDecimals;

    public void setA(Number a) {
        this.a = a;
    }

    public void setB(Number b) {
        this.b = b;
    }

    public Number getA() {
        return this.a;
    }

    public Number getB() {
        return this.b;
    }

    public void setDecimals(boolean isDecimals) {
        this.isDecimals = isDecimals;
    }

    public boolean isDecimals() {
        return this.isDecimals;
    }

    public RandomConstant() {
        this.a = 0;
        this.b = 0;
    }

    public RandomConstant(Number a, Number b, boolean isDecimals) {
        this.a = a;
        this.b = b;
        this.isDecimals = isDecimals;
    }

    public RandomConstant(NumberFunctionConfig config) {
        this(Float.valueOf(config.defaultValue()), Float.valueOf(config.defaultValue()), config.isDecimals());
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(float t, Supplier<Float> lerp) {
        float min = Math.min(this.a.floatValue(), this.b.floatValue());
        float max = Math.max(this.a.floatValue(), this.b.floatValue());
        return min == max ? Float.valueOf(max) : this.isDecimals ? Float.valueOf(min + (lerp.get().floatValue() * (max - min))) : Integer.valueOf((int) (min + (lerp.get().floatValue() * ((max + 1.0f) - min))));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public void createConfigurator(WidgetGroup group, NumberFunctionConfigurator configurator) {
        int width;
        WidgetGroup aGroup;
        WidgetGroup bGroup;
        Size size = group.getSize();
        if (size.width > 60) {
            width = size.width / 2;
            aGroup = new WidgetGroup(0, 0, width, size.height);
            bGroup = new WidgetGroup(width, 0, width, size.height);
        } else {
            width = size.width;
            aGroup = new WidgetGroup(0, 0, width, size.height);
            bGroup = new WidgetGroup(0, 15, width, size.height);
            group.setSize(new Size(size.width, size.height + 15));
        }
        group.addWidget(aGroup);
        group.addWidget(bGroup);
        setupNumberConfigurator(configurator, width, aGroup, new NumberConfigurator("", () -> {
            return Float.valueOf(this.isDecimals ? this.a.floatValue() : this.a.intValue());
        }, number -> {
            setA(configurator);
            configurator.updateValue();
        }, this.a, true));
        setupNumberConfigurator(configurator, width, bGroup, new NumberConfigurator("", () -> {
            return Float.valueOf(this.isDecimals ? this.b.floatValue() : this.b.intValue());
        }, number2 -> {
            setB(configurator);
            configurator.updateValue();
        }, this.b, true));
    }

    private void setupNumberConfigurator(NumberFunctionConfigurator configurator, int width, WidgetGroup group, NumberConfigurator widget) {
        group.addWidget(widget.setRange(Float.valueOf(configurator.getConfig().min()), Float.valueOf(configurator.getConfig().max())).setWheel(Float.valueOf(configurator.getConfig().isDecimals() ? configurator.getConfig().wheelDur() : Math.max(1, (int) configurator.getConfig().wheelDur()))));
        widget.setConfigPanel(configurator.getConfigPanel(), configurator.getTab());
        widget.init(width);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.m_128379_("isDecimals", this.isDecimals);
        if ((this.a instanceof Float) || (this.a instanceof Double)) {
            tag.m_128350_("a", this.a.floatValue());
        } else {
            tag.m_128405_("a", this.a.intValue());
        }
        if ((this.b instanceof Float) || (this.b instanceof Double)) {
            tag.m_128350_("b", this.b.floatValue());
        } else {
            tag.m_128405_("b", this.b.intValue());
        }
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        this.isDecimals = nbt.m_128471_("isDecimals");
        if (nbt.m_128425_("a", 3)) {
            this.a = Integer.valueOf(nbt.m_128451_("a"));
        } else {
            this.a = Float.valueOf(nbt.m_128457_("a"));
        }
        if (nbt.m_128425_("b", 3)) {
            this.b = Integer.valueOf(nbt.m_128451_("b"));
        } else {
            this.b = Float.valueOf(nbt.m_128457_("b"));
        }
    }
}
