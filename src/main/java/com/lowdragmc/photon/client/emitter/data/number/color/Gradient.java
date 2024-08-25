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
import com.lowdragmc.lowdraglib.utils.GradientColor;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.gui.editor.GradientsResource;
import com.lowdragmc.photon.gui.editor.configurator.NumberFunctionConfigurator;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/color/Gradient.class */
public class Gradient implements NumberFunction {
    private final GradientColor gradientColor;

    public GradientColor getGradientColor() {
        return this.gradientColor;
    }

    public Gradient() {
        this.gradientColor = new GradientColor();
    }

    public Gradient(int color) {
        this.gradientColor = new GradientColor(color, color);
    }

    public Gradient(NumberFunctionConfig config) {
        this((int) config.defaultValue());
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(RandomSource randomSource, float t) {
        return Integer.valueOf(this.gradientColor.getColor(t));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public Number get(float t, Supplier<Float> lerp) {
        return Integer.valueOf(this.gradientColor.getColor(t));
    }

    @Override // com.lowdragmc.photon.client.emitter.data.number.NumberFunction
    public void createConfigurator(WidgetGroup group, NumberFunctionConfigurator configurator) {
        ColorBorderTexture background = ColorPattern.T_GRAY.borderTexture(1);
        group.addWidget(new ButtonWidget(0, 2, group.getSize().width, 10, new GuiTextureGroup(background, new GradientColorTexture(this.gradientColor)), cd -> {
            if (Editor.INSTANCE != null) {
                Size size = new Size(160, 188);
                Position position = group.getPosition();
                int rightPlace = group.getGui().getScreenWidth() - size.width;
                GradientColorWidget gradientWidget = new GradientColorWidget(5, 0, CompassView.LIST_WIDTH, this.gradientColor);
                gradientWidget.setOnUpdate(g -> {
                    group.updateValue();
                });
                DialogWidget dialog = Editor.INSTANCE.openDialog(new DialogWidget(Math.min(position.x, rightPlace), Math.max(0, position.y - size.height), size.width, size.height));
                dialog.setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
                dialog.setClickClose(true);
                dialog.addWidget(gradientWidget);
            }
        }).setDraggingConsumer(o -> {
            if (o instanceof GradientsResource.Gradients) {
                GradientsResource.Gradients g = (GradientsResource.Gradients) o;
                if (!g.isRandomGradient()) {
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
                this.gradientColor.deserializeNBT(g.gradient0.mo129serializeNBT());
                configurator.updateValue();
                configurator.setColor(ColorPattern.T_GRAY.color);
            }
        }));
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        return this.gradientColor.mo129serializeNBT();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        this.gradientColor.deserializeNBT(nbt);
    }
}
