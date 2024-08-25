package com.lowdragmc.lowdraglib.gui.editor.ui.view;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.ILDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/view/FloatViewWidget.class */
public class FloatViewWidget extends WidgetGroup implements ILDLRegister {
    protected final Editor editor;
    protected final boolean isFixedView;
    protected WidgetGroup title;
    protected WidgetGroup content;
    private boolean isDragging;
    private boolean isCollapse;
    private double lastDeltaX;
    private double lastDeltaY;

    public FloatViewWidget(int x, int y, int width, int height, boolean isFixedView) {
        super(x, y, width, height + 15);
        this.editor = Editor.INSTANCE;
        if (this.editor == null) {
            throw new RuntimeException("editor is null while creating a float view %s".formatted(new Object[]{name()}));
        }
        this.isFixedView = isFixedView;
        setClientSideWidget();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        if (!this.isFixedView) {
            WidgetGroup widgetGroup = new WidgetGroup(0, 0, getSize().width, 15);
            this.title = widgetGroup;
            addWidget(widgetGroup);
            this.title.setBackground(new GuiTextureGroup(ColorPattern.T_RED.rectTexture().setTopRadius(5.0f), ColorPattern.GRAY.borderTexture(-1).setTopRadius(5.0f)));
            this.title.addWidget(new ButtonWidget(2, 2, 11, 11, getIcon(), this::collapse).setHoverTexture(getIcon().setColor(ColorPattern.GREEN.color)));
            WidgetGroup widgetGroup2 = new WidgetGroup(0, 15, getSize().width, getSize().height - 15);
            this.content = widgetGroup2;
            addWidget(widgetGroup2);
            this.content.setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture().setBottomRadius(5.0f), ColorPattern.GRAY.borderTexture(-1).setBottomRadius(5.0f)));
        }
        super.initWidget();
    }

    private void collapse(ClickData clickData) {
        this.isCollapse = !this.isCollapse;
        if (this.isCollapse) {
            this.title.setSize(new Size(15, 15));
            this.title.setBackground(new GuiTextureGroup(ColorPattern.T_RED.rectTexture().setRadius(5.0f), ColorPattern.GRAY.borderTexture(-1).setRadius(5.0f)));
            this.content.setVisible(false);
            this.content.setActive(false);
            return;
        }
        this.title.setSize(new Size(getSize().width, 15));
        this.title.setBackground(new GuiTextureGroup(ColorPattern.T_RED.rectTexture().setTopRadius(5.0f), ColorPattern.GRAY.borderTexture(-1).setTopRadius(5.0f)));
        this.content.setVisible(true);
        this.content.setActive(true);
    }

    public IGuiTexture getIcon() {
        return IGuiTexture.EMPTY;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        if (this.title != null && this.title.isMouseOverElement(mouseX, mouseY)) {
            this.lastDeltaX = 0.0d;
            this.lastDeltaY = 0.0d;
            this.isDragging = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isDragging) {
            double dx = dragX + this.lastDeltaX;
            double dy = dragY + this.lastDeltaY;
            double dragX2 = (int) dx;
            double dragY2 = (int) dy;
            this.lastDeltaX = dx - dragX2;
            this.lastDeltaY = dy - dragY2;
            addSelfPosition((int) dragX2, (int) dragY2);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
