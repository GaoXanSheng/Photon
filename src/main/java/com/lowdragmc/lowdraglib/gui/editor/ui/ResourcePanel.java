package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.gui.animation.Transform;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.data.Resources;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.TabButton;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.interpolate.Eases;
import com.lowdragmc.lowdraglib.utils.interpolate.IEase;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/ResourcePanel.class */
public class ResourcePanel extends WidgetGroup {
    public static final int HEIGHT = 100;
    protected Editor editor;
    protected ButtonWidget buttonHide;
    protected TabContainer tabContainer;
    @Nullable
    protected Resources resources;
    protected boolean isShow;

    public Editor getEditor() {
        return this.editor;
    }

    @Nullable
    public Resources getResources() {
        return this.resources;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public ResourcePanel(Editor editor) {
        super(0, editor.getSize().height - 100, editor.getSize().getWidth() - ConfigPanel.WIDTH, 100);
        this.isShow = true;
        setClientSideWidget();
        this.editor = editor;
    }

    private void dispose() {
        if (this.resources != null) {
            this.resources.dispose();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void setGui(ModularUI gui) {
        super.setGui(gui);
        if (gui == null) {
            dispose();
        } else {
            getGui().registerCloseListener(this::dispose);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        Size size = getSize();
        setBackground(ColorPattern.BLACK.rectTexture());
        ButtonWidget hoverBorderTexture = new ButtonWidget((getSize().width - 30) / 2, -10, 30, 10, new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.DOWN), cd -> {
            if (isShow()) {
                hide();
            } else {
                show();
            }
        }).setHoverBorderTexture(1, -1);
        this.buttonHide = hoverBorderTexture;
        addWidget(hoverBorderTexture);
        addWidget(new LabelWidget(3, 3, "ldlib.gui.editor.group.resources"));
        TabContainer tabContainer = new TabContainer(0, 15, size.width, size.height - 14);
        this.tabContainer = tabContainer;
        addWidget(tabContainer);
        this.tabContainer.setBackground(ColorPattern.T_GRAY.borderTexture(-1));
        super.initWidget();
    }

    public void hide() {
        if (isShow() && !inAnimate()) {
            this.isShow = !this.isShow;
            animation(new Transform().offset(0, 100).ease((IEase) Eases.EaseQuadOut).duration(500L).onFinish(() -> {
                addSelfPosition(0, 100);
                this.buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.UP);
            }));
        }
    }

    public void show() {
        if (!isShow() && !inAnimate()) {
            this.isShow = !this.isShow;
            animation(new Transform().offset(0, -100).ease((IEase) Eases.EaseQuadOut).duration(500L).onFinish(() -> {
                addSelfPosition(0, -100);
                this.buttonHide.setButtonTexture(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1), Icons.DOWN);
            }));
        }
    }

    public void loadResource(Resources resources, boolean merge) {
        this.tabContainer.clearAllWidgets();
        if (!merge && this.resources != null) {
            this.resources.dispose();
        }
        if (!merge || this.resources == null) {
            this.resources = resources;
            resources.load();
        } else {
            this.resources.merge(resources);
        }
        int offset = Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format("ldlib.gui.editor.group.resources", new Object[0])) + 8;
        for (Resource<?> resource : this.resources.resources.values()) {
            this.tabContainer.addTab(new TabButton(offset, -15, Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format(resource.name(), new Object[0])) + 8, 15).setTexture((IGuiTexture) new TextTexture(resource.name()), (IGuiTexture) new GuiTextureGroup(new TextTexture(resource.name(), ColorPattern.T_GREEN.color), ColorPattern.T_GRAY.rectTexture())), resource.createContainer(this));
            offset += 52;
        }
    }
}
