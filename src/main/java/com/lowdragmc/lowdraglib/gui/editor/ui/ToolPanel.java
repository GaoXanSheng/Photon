package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.gui.animation.Transform;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.TabButton;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.interpolate.Eases;
import com.lowdragmc.lowdraglib.utils.interpolate.IEase;
import expr.Expr;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/ToolPanel.class */
public class ToolPanel extends WidgetGroup {
    public static final int WIDTH = 100;
    protected final Editor editor;
    protected final List<Widget> toolBoxes;
    protected ButtonWidget buttonHide;
    protected TabContainer tabContainer;
    protected ImageWidget tabsBackground;
    protected boolean isShow;

    public Editor getEditor() {
        return this.editor;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public ToolPanel(Editor editor) {
        super(-100, 30, 100, Math.max(100, (editor.getSize().getHeight() - 100) - 30));
        this.toolBoxes = new ArrayList();
        setClientSideWidget();
        this.editor = editor;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        Size size = getSize();
        setBackground(ColorPattern.BLACK.rectTexture());
        addWidget(new LabelWidget(3, 3, "ldlib.gui.editor.group.tool_box"));
        ImageWidget imageWidget = new ImageWidget(100, 15, 20, 0, ColorPattern.BLACK.rectTexture().setRightRadius(8.0f));
        this.tabsBackground = imageWidget;
        addWidget(imageWidget);
        TabContainer tabContainer = new TabContainer(0, 15, 100, size.height - 15);
        this.tabContainer = tabContainer;
        addWidget(tabContainer);
        this.tabContainer.setBackground(ColorPattern.T_GRAY.borderTexture(-1));
        ButtonWidget hoverBorderTexture = new ButtonWidget(87, 3, 10, 10, new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.RIGHT), cd -> {
            if (isShow()) {
                hide();
            } else {
                show();
            }
        }).setHoverBorderTexture(1, -1);
        this.buttonHide = hoverBorderTexture;
        addWidget(hoverBorderTexture);
        super.initWidget();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
    public void clearAllWidgets() {
        this.toolBoxes.clear();
        this.tabContainer.clearAllWidgets();
    }

    public void addNewToolBox(String name, ResourceTexture texture, WidgetGroup toolBox) {
        toolBox.setSize(new Size(100, getSize().height - 15));
        this.tabContainer.addTab((TabButton) new TabButton(Expr.CEIL, 4 + (this.toolBoxes.size() * 20), 12, 12) { // from class: com.lowdragmc.lowdraglib.gui.editor.ui.ToolPanel.1
            @Override // com.lowdragmc.lowdraglib.gui.widget.SwitchWidget, com.lowdragmc.lowdraglib.gui.widget.Widget
            @OnlyIn(Dist.CLIENT)
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (isMouseOverElement(mouseX, mouseY)) {
                    ToolPanel.this.show();
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }.setTexture((IGuiTexture) texture, (IGuiTexture) texture.copy().setColor(ColorPattern.T_GREEN.color)).setHoverTooltips(name), toolBox);
        this.toolBoxes.add(toolBox);
        this.tabsBackground.setSize(new Size(20, this.toolBoxes.size() * 20));
    }

    public void hide() {
        if (isShow() && !inAnimate()) {
            this.isShow = !this.isShow;
            animation(new Transform().offset(-100, 0).ease((IEase) Eases.EaseQuadOut).duration(500L).onFinish(() -> {
                addSelfPosition(-100, 0);
                this.buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.RIGHT);
            }));
        }
    }

    public void show() {
        if (!isShow() && !inAnimate()) {
            this.isShow = !this.isShow;
            animation(new Transform().offset(100, 0).ease((IEase) Eases.EaseQuadOut).duration(500L).onFinish(() -> {
                addSelfPosition(100, 0);
                this.buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.LEFT);
            }));
        }
    }
}
