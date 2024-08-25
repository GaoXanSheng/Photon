package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Size;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/Configurator.class */
public class Configurator extends WidgetGroup {
    protected ConfigPanel configPanel;
    protected ConfigPanel.Tab tab;
    protected String[] tips;
    protected String name;
    protected int leftWidth;
    protected int rightWidth;
    protected int width;
    @Nullable
    protected LabelWidget nameWidget;

    public ConfigPanel getConfigPanel() {
        return this.configPanel;
    }

    public ConfigPanel.Tab getTab() {
        return this.tab;
    }

    public String getName() {
        return this.name;
    }

    public int getLeftWidth() {
        return this.leftWidth;
    }

    public int getRightWidth() {
        return this.rightWidth;
    }

    public int getWidth() {
        return this.width;
    }

    @Nullable
    public LabelWidget getNameWidget() {
        return this.nameWidget;
    }

    public Configurator(String name) {
        super(0, 0, 200, 15);
        this.tips = new String[0];
        this.width = -1;
        this.name = name;
        setClientSideWidget();
        if (!name.isEmpty()) {
            LabelWidget labelWidget = new LabelWidget(3, 3, name);
            this.nameWidget = labelWidget;
            addWidget(labelWidget);
            this.leftWidth = Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format(name, new Object[0])) + 6;
            return;
        }
        this.leftWidth = 3;
    }

    public Configurator() {
        this("");
    }

    public void setConfigPanel(ConfigPanel configPanel, ConfigPanel.Tab tab) {
        this.configPanel = configPanel;
        this.tab = tab;
    }

    public void computeLayout() {
        if (this.configPanel != null) {
            this.configPanel.computeLayout(this.tab);
        }
    }

    public void setTips(String... tips) {
        this.tips = tips;
        this.rightWidth = tips.length > 0 ? 13 : 0;
    }

    public boolean isInit() {
        return this.width > -1;
    }

    public void computeHeight() {
    }

    public void init(int width) {
        this.width = width;
        setSize(new Size(width, getSize().height));
        if (this.tips.length > 0) {
            addWidget(new ImageWidget(width - 12, 2, 9, 9, Icons.HELP).setHoverTooltips(this.tips));
        }
    }
}
