package com.lowdragmc.photon.client.emitter.data.number.curve;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.configurator.NumberConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.gui.editor.CurvesResource;
import com.lowdragmc.photon.gui.editor.configurator.NumberFunctionConfigurator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/curve/Curve.class */
public class Curve implements NumberFunction {
    private float min;
    private float max;
    private float defaultValue;
    private ECBCurves curves;
    private String xAxis;
    private String yAxis;
    protected boolean lockControlPoint;
    private float lower;
    private float upper;

    public void setMin(float min) {
        this.min = min;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public void setCurves(ECBCurves curves) {
        this.curves = curves;
    }

    public ECBCurves getCurves() {
        return this.curves;
    }

    public void setXAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public void setYAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getXAxis() {
        return this.xAxis;
    }

    public String getYAxis() {
        return this.yAxis;
    }

    public void setLockControlPoint(boolean lockControlPoint) {
        this.lockControlPoint = lockControlPoint;
    }

    public boolean isLockControlPoint() {
        return this.lockControlPoint;
    }

    public void setLower(float lower) {
        this.lower = lower;
    }

    public void setUpper(float upper) {
        this.upper = upper;
    }

    public float getLower() {
        return this.lower;
    }

    public float getUpper() {
        return this.upper;
    }

    public Curve() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, "", "");
    }

    public Curve(float min, float max, float lower, float upper, float defaultValue, String xAxis, String yAxis) {
        this.lockControlPoint = true;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.lower = lower;
        this.upper = upper;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        float y = upper == lower ? 0.5f : (defaultValue - lower) / (upper - lower);
        this.curves = new ECBCurves(0.0f, y, 0.1f, y, 0.9f, y, 1.0f, y);
    }

    public Curve(NumberFunctionConfig config) {
        this(config.min(), config.max(), config.curveConfig().bound().length > 0 ? Math.max(config.min(), config.curveConfig().bound()[0]) : config.min(), config.curveConfig().bound().length > 1 ? Math.min(config.max(), config.curveConfig().bound()[1]) : config.max(), config.defaultValue(), config.curveConfig().xAxis(), config.curveConfig().yAxis());
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(RandomSource randomSource, float t) {
        return Float.valueOf(this.lower + ((this.upper - this.lower) * this.curves.getCurveY(t)));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(float t, Supplier<Float> lerp) {
        return Float.valueOf(this.lower + ((this.upper - this.lower) * this.curves.getCurveY(t)));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public void createConfigurator(WidgetGroup group, NumberFunctionConfigurator configurator) {
        ColorRectTexture background = ColorPattern.T_GRAY.rectTexture().setRadius(5.0f);
        group.addWidget(new ButtonWidget(0, 2, group.getSize().width, 10, new GuiTextureGroup(background, new CurveTexture(this.curves)), cd -> {
            if (Editor.INSTANCE != null) {
                Size size = new Size(360, 100);
                Position position = group.getPosition();
                int rightPlace = group.getGui().getScreenWidth() - size.width;
                DialogWidget dialog = Editor.INSTANCE.openDialog(new DialogWidget(Math.min(position.x, rightPlace), Math.max(0, position.y - size.height), size.width, size.height));
                dialog.setClickClose(true);
                dialog.addWidget(new ConfiguratorWidget(0, 0, size.width, size.height, curves -> {
                    group.updateValue();
                }));
            }
        }).setDraggingConsumer(o -> {
            if (o instanceof CurvesResource.Curves) {
                CurvesResource.Curves c = (CurvesResource.Curves) o;
                if (!c.isRandomCurve()) {
                    return true;
                }
            }
            return false;
        }, o2 -> {
            background.setColor(ColorPattern.GREEN.color);
        }, o3 -> {
            background.setColor(ColorPattern.T_GRAY.color);
        }, o4 -> {
            if (background instanceof CurvesResource.Curves) {
                CurvesResource.Curves c = (CurvesResource.Curves) background;
                this.curves.deserializeNBT(c.curves0.mo129serializeNBT());
                configurator.updateValue();
                configurator.setColor(ColorPattern.T_GRAY.color);
            }
        }));
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.m_128350_("min", this.min);
        tag.m_128350_("max", this.max);
        tag.m_128350_("defaultValue", this.defaultValue);
        tag.m_128350_("lower", this.lower);
        tag.m_128350_("upper", this.upper);
        tag.m_128365_("curves", this.curves.mo129serializeNBT());
        tag.m_128359_("xAxis", this.xAxis);
        tag.m_128359_("yAxis", this.yAxis);
        tag.m_128379_("lockControlPoint", this.lockControlPoint);
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag tag) {
        this.min = tag.m_128457_("min");
        this.max = tag.m_128457_("max");
        this.defaultValue = tag.m_128457_("defaultValue");
        this.lower = tag.m_128457_("lower");
        this.upper = tag.m_128457_("upper");
        this.curves.deserializeNBT(tag.m_128437_("curves", 9));
        this.xAxis = tag.m_128461_("xAxis");
        this.yAxis = tag.m_128461_("yAxis");
        this.lockControlPoint = tag.m_128471_("lockControlPoint");
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/curve/Curve$ConfiguratorWidget.class */
    public class ConfiguratorWidget extends WidgetGroup {
        public ConfiguratorWidget(int x, int y, int width, int height, Consumer<ECBCurves> onUpdate) {
            super(x, y, width, height);
            NumberConfigurator upper = new NumberConfigurator("", () -> {
                return Float.valueOf(Curve.this.upper);
            }, value -> {
                Curve.this.upper = value.floatValue();
            }, Float.valueOf(Curve.this.defaultValue), true);
            NumberConfigurator lower = new NumberConfigurator("", () -> {
                return Float.valueOf(Curve.this.lower);
            }, value2 -> {
                Curve.this.lower = value2.floatValue();
            }, Float.valueOf(Curve.this.defaultValue), true);
            upper.setRange(Float.valueOf(Curve.this.min), Float.valueOf(Curve.this.max));
            lower.setRange(Float.valueOf(Curve.this.min), Float.valueOf(Curve.this.max));
            upper.init(60);
            lower.init(60);
            upper.addSelfPosition(0, 1);
            lower.addSelfPosition(0, height - 15);
            CurveLineWidget curveLine = new CurveLineWidget(60, 3, width - 63, height - 7, Curve.this.curves);
            curveLine.setOnUpdate(onUpdate);
            curveLine.setLockControlPoint(Curve.this.lockControlPoint);
            curveLine.setGridSize(new Size(6, 2));
            curveLine.setHoverTips(coord -> {
                return Component.m_237113_(String.valueOf(Curve.this.lower + (coord.f_82471_ * (Curve.this.upper - Curve.this.lower))));
            });
            curveLine.setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
            if (!Curve.this.xAxis.isBlank()) {
                addWidget(new ImageWidget(60, height, width - 63, 10, new TextTexture(Curve.this.xAxis)));
            }
            if (!Curve.this.yAxis.isBlank()) {
                addWidget(new ImageWidget(12, (height / 2) - 5, 80, 10, new TextTexture(Curve.this.yAxis).rotate(-90.0f)));
            }
            addWidget(curveLine);
            addWidget(upper);
            addWidget(lower);
        }
    }
}
