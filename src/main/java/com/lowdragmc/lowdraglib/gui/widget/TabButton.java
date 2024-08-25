package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/TabButton.class */
public class TabButton extends SwitchWidget {
    protected TabContainer container;

    public TabButton() {
        this(0, 0, 20, 20);
    }

    public TabButton(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height, null);
        setOnPressCallback((v1, v2) -> {
            onPressed(v1, v2);
        });
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
    public TabButton setTexture(IGuiTexture baseTexture, IGuiTexture pressedTexture) {
        super.setTexture(baseTexture, pressedTexture);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
    public TabButton setBaseTexture(IGuiTexture... baseTexture) {
        return (TabButton) super.setBaseTexture(baseTexture);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
    public TabButton setPressedTexture(IGuiTexture... pressedTexture) {
        return (TabButton) super.setPressedTexture(pressedTexture);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.SwitchWidget, com.lowdragmc.lowdraglib.gui.widget.Widget
    public TabButton setHoverTexture(IGuiTexture... hoverTexture) {
        return (TabButton) super.setHoverTexture(hoverTexture);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.SwitchWidget
    public TabButton setHoverBorderTexture(int border, int color) {
        return (TabButton) super.setHoverBorderTexture(border, color);
    }

    public void setContainer(TabContainer container) {
        this.container = container;
    }

    public void onPressed(ClickData clickData, boolean isPressed) {
        this.isPressed = true;
        if (this.container != null) {
            this.container.switchTag((WidgetGroup) this.container.tabs.get(this));
        }
    }
}
