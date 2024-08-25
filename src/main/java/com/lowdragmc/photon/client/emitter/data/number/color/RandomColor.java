package com.lowdragmc.photon.client.emitter.data.number.color;

import com.lowdragmc.lowdraglib.gui.editor.configurator.ColorConfigurator;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.RandomConstant;
import com.lowdragmc.photon.gui.editor.configurator.NumberFunctionConfigurator;
import java.util.function.Supplier;
import net.minecraft.util.RandomSource;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/color/RandomColor.class */
public class RandomColor extends RandomConstant {
    public RandomColor() {
        this(-16777216, -1);
    }

    public RandomColor(Number a, Number b) {
        super(a, b, false);
    }

    public RandomColor(NumberFunctionConfig config) {
        super(config);
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.RandomConstant, com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(float t, Supplier<Float> lerp) {
        int colorA = getA().intValue();
        int colorB = getB().intValue();
        return Integer.valueOf(ColorUtils.blendColor(colorA, colorB, lerp.get().floatValue()));
    }

    private int randomColor(RandomSource randomSource, int minA, int maxA, int minR, int maxR, int minG, int maxG, int minB, int maxB) {
        return ((minR + randomSource.m_188503_((maxA + 1) - minA)) << 24) | ((minR + randomSource.m_188503_((maxR + 1) - minR)) << 16) | ((minG + randomSource.m_188503_((maxG + 1) - minG)) << 8) | (minB + randomSource.m_188503_((maxB + 1) - minB));
    }

    private int randomColor(RandomSource randomSource, int colorA, int colorB) {
        return randomColor(randomSource, Math.min(ColorUtils.alphaI(colorA), ColorUtils.alphaI(colorB)), Math.max(ColorUtils.alphaI(colorA), ColorUtils.alphaI(colorB)), Math.min(ColorUtils.redI(colorA), ColorUtils.redI(colorB)), Math.max(ColorUtils.redI(colorA), ColorUtils.redI(colorB)), Math.min(ColorUtils.greenI(colorA), ColorUtils.greenI(colorB)), Math.max(ColorUtils.greenI(colorA), ColorUtils.greenI(colorB)), Math.min(ColorUtils.blueI(colorA), ColorUtils.blueI(colorB)), Math.max(ColorUtils.blueI(colorA), ColorUtils.blueI(colorB)));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.RandomConstant, com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public void createConfigurator(WidgetGroup group, NumberFunctionConfigurator configurator) {
        Size size = group.getSize();
        WidgetGroup aGroup = new WidgetGroup(0, 0, size.width / 2, size.height);
        WidgetGroup bGroup = new WidgetGroup(size.width / 2, 0, size.width / 2, size.height);
        group.addWidget(aGroup);
        group.addWidget(bGroup);
        setupNumberConfigurator(size, aGroup, new ColorConfigurator("", () -> {
            return Integer.valueOf(getA().intValue());
        }, number -> {
            setA(configurator);
            configurator.updateValue();
        }, Integer.valueOf(getA().intValue()), true), configurator);
        setupNumberConfigurator(size, bGroup, new ColorConfigurator("", () -> {
            return Integer.valueOf(getB().intValue());
        }, number2 -> {
            setB(configurator);
            configurator.updateValue();
        }, Integer.valueOf(getB().intValue()), true), configurator);
    }

    private void setupNumberConfigurator(Size size, WidgetGroup group, ColorConfigurator widget, NumberFunctionConfigurator configurator) {
        group.addWidget(widget);
        widget.setConfigPanel(configurator.getConfigPanel(), configurator.getTab());
        widget.init(size.width / 2);
    }
}
