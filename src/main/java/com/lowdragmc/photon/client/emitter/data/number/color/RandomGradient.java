package com.lowdragmc.photon.client.emitter.data.number.color;

import com.lowdragmc.lowdraglib.gui.compass.CompassView;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.GradientColorWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.lowdraglib.utils.GradientColor;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.gui.editor.GradientsResource;
import com.lowdragmc.photon.gui.editor.configurator.NumberFunctionConfigurator;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/color/RandomGradient.class */
public class RandomGradient implements NumberFunction {
    private final GradientColor gradientColor0;
    private final GradientColor gradientColor1;

    public GradientColor getGradientColor0() {
        return this.gradientColor0;
    }

    public GradientColor getGradientColor1() {
        return this.gradientColor1;
    }

    public RandomGradient() {
        this.gradientColor0 = new GradientColor();
        this.gradientColor1 = new GradientColor();
    }

    public RandomGradient(int color) {
        this.gradientColor0 = new GradientColor(color, color);
        this.gradientColor1 = new GradientColor(color, color);
    }

    public RandomGradient(NumberFunctionConfig config) {
        this((int) config.defaultValue());
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(float t, Supplier<Float> lerp) {
        int color0 = this.gradientColor0.getColor(t);
        int color1 = this.gradientColor1.getColor(t);
        return Integer.valueOf(ColorUtils.blendColor(color0, color1, lerp.get().floatValue()));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public void createConfigurator(WidgetGroup group, NumberFunctionConfigurator configurator) {
        ColorBorderTexture background = ColorPattern.T_GRAY.borderTexture(1);
        group.addWidget(new ButtonWidget(0, 2, group.getSize().width, 10, new GuiTextureGroup(background, new RandomGradientColorTexture(this.gradientColor0, this.gradientColor1)), cd -> {
            if (Editor.INSTANCE != null) {
                Size size = new Size(315, 188);
                Position position = group.getPosition();
                int rightPlace = group.getGui().getScreenWidth() - size.width;
                GradientColorWidget gradientWidget0 = new GradientColorWidget(5, 0, CompassView.LIST_WIDTH, this.gradientColor0);
                gradientWidget0.setOnUpdate(g -> {
                    group.updateValue();
                });
                GradientColorWidget gradientWidget1 = new GradientColorWidget(160, 0, CompassView.LIST_WIDTH, this.gradientColor1);
                gradientWidget1.setOnUpdate(g2 -> {
                    group.updateValue();
                });
                DialogWidget dialog = Editor.INSTANCE.openDialog(new DialogWidget(Math.min(position.x, rightPlace), Math.max(0, position.y - size.height), size.width, size.height));
                dialog.setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
                dialog.setClickClose(true);
                dialog.addWidget(gradientWidget0);
                dialog.addWidget(gradientWidget1);
            }
        }).setDraggingConsumer(o -> {
            if (o instanceof GradientsResource.Gradients) {
                GradientsResource.Gradients g = (GradientsResource.Gradients) o;
                if (g.isRandomGradient()) {
                    return true;
                }
            }
            return false;
        }, o2 -> {
            background.setColor(ColorPattern.GREEN.color);
        }, o3 -> {
            background.setColor(ColorPattern.T_GRAY.color);
        }, o4 -> {
            if (background instanceof GradientsResource.Gradients) {
                GradientsResource.Gradients g = (GradientsResource.Gradients) background;
                if (g.gradient1 != null) {
                    this.gradientColor0.deserializeNBT(g.gradient0.mo129serializeNBT());
                    this.gradientColor1.deserializeNBT(g.gradient1.mo129serializeNBT());
                    configurator.updateValue();
                    configurator.setColor(ColorPattern.T_GRAY.color);
                }
            }
        }));
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.m_128365_("a", this.gradientColor0.mo129serializeNBT());
        tag.m_128365_("b", this.gradientColor1.mo129serializeNBT());
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag tag) {
        this.gradientColor0.deserializeNBT(tag.m_128469_("a"));
        this.gradientColor1.deserializeNBT(tag.m_128469_("b"));
    }
}
