package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "button", group = "widget.basic")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/ButtonWidget.class */
public class ButtonWidget extends Widget implements IConfigurableWidget {
    protected Consumer<ClickData> onPressCallback;

    public ButtonWidget() {
        this(0, 0, 40, 20, new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("Button")), null);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public void initTemplate() {
        setHoverBorderTexture(1, -1);
    }

    public ButtonWidget(int xPosition, int yPosition, int width, int height, IGuiTexture buttonTexture, Consumer<ClickData> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
        setBackground(buttonTexture);
    }

    public ButtonWidget(int xPosition, int yPosition, int width, int height, Consumer<ClickData> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
    }

    public ButtonWidget setOnPressCallback(Consumer<ClickData> onPressCallback) {
        this.onPressCallback = onPressCallback;
        return this;
    }

    public ButtonWidget setButtonTexture(IGuiTexture... buttonTexture) {
        super.setBackground(buttonTexture);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public ButtonWidget setHoverTexture(IGuiTexture... hoverTexture) {
        super.setHoverTexture(hoverTexture);
        return this;
    }

    public ButtonWidget setHoverBorderTexture(int border, int color) {
        super.setHoverTexture(new ColorBorderTexture(border, color));
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            ClickData clickData = new ClickData();
            Objects.requireNonNull(clickData);
            writeClientAction(1, this::writeToBuf);
            if (this.onPressCallback != null) {
                this.onPressCallback.accept(clickData);
            }
            playButtonClickSound();
            return true;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            if (this.onPressCallback != null) {
                this.onPressCallback.accept(clickData);
            }
        }
    }
}
