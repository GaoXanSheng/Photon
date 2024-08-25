package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.HsbColorWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.TabButton;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/ConfigPanel.class */
public class ConfigPanel extends WidgetGroup {
    public static final int WIDTH = 252;
    protected final Editor editor;
    protected final Map<Tab, IConfigurable> focus;
    protected final Map<Tab, DraggableScrollableWidgetGroup> configuratorGroup;
    protected final Map<Tab, List<Configurator>> configurators;
    protected TabContainer tabContainer;
    protected HsbColorWidget palette;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/ConfigPanel$Tab.class */
    public static class Tab {
        public static List<Tab> TABS = new ArrayList();
        public static final Tab WIDGET = registerTab(Icons.WIDGET_SETTING);
        public static final Tab RESOURCE = registerTab(Icons.RESOURCE_SETTING);
        public final ResourceTexture icon;
        public final Consumer<ConfiguratorGroup> configurable;

        private Tab(ResourceTexture icon, Consumer<ConfiguratorGroup> configurable) {
            this.icon = icon;
            this.configurable = configurable;
        }

        public static Tab registerTab(ResourceTexture icon) {
            return registerTab(icon, father -> {
            });
        }

        public static Tab registerTab(ResourceTexture icon, Consumer<ConfiguratorGroup> configurable) {
            Tab tab = new Tab(icon, configurable);
            TABS.add(tab);
            return tab;
        }
    }

    public Editor getEditor() {
        return this.editor;
    }

    public Map<Tab, IConfigurable> getFocus() {
        return this.focus;
    }

    public HsbColorWidget getPalette() {
        return this.palette;
    }

    public ConfigPanel(Editor editor) {
        super(editor.getSize().getWidth() - WIDTH, 0, WIDTH, editor.getSize().height);
        this.focus = new HashMap(Tab.TABS.size());
        this.configuratorGroup = new HashMap(Tab.TABS.size());
        this.configurators = new HashMap(Tab.TABS.size());
        setClientSideWidget();
        this.editor = editor;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        setBackground(ColorPattern.T_BLACK.rectTexture());
        addWidget(new ImageWidget(0, 10, (int) WIDTH, 10, new TextTexture("ldlib.gui.editor.configurator").setWidth(202)));
        addWidget(new ImageWidget(-20, 30, 20, Tab.TABS.size() * 20, ColorPattern.T_BLACK.rectTexture().setLeftRadius(8.0f)));
        TabContainer tabContainer = new TabContainer(0, 0, WIDTH, this.editor.getSize().height);
        this.tabContainer = tabContainer;
        addWidget(tabContainer);
        int y = 34;
        for (Tab tab : Tab.TABS) {
            this.tabContainer.addTab(new TabButton(-16, y, 12, 12).setTexture((IGuiTexture) tab.icon, (IGuiTexture) tab.icon.copy().setColor(ColorPattern.T_GREEN.color)), this.configuratorGroup.computeIfAbsent(tab, key -> {
                return new DraggableScrollableWidgetGroup(0, 25, WIDTH, this.editor.getSize().height - 25).setYScrollBarWidth(2).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1.0f));
            }));
            this.configurators.put(tab, new ArrayList());
            y += 20;
        }
        super.initWidget();
    }

    public void clearAllConfigurators(Tab tab) {
        this.focus.remove(tab);
        this.configuratorGroup.get(tab).clearAllWidgets();
        this.configurators.get(tab).clear();
    }

    public void openConfigurator(Tab tab, IConfigurable configurable) {
        switchTag(tab);
        if (Objects.equals(configurable, this.focus.get(tab))) {
            return;
        }
        clearAllConfigurators(tab);
        this.focus.put(tab, configurable);
        ConfiguratorGroup group = new ConfiguratorGroup("", false);
        tab.configurable.accept(group);
        configurable.buildConfigurator(group);
        for (Configurator configurator : group.getConfigurators()) {
            configurator.setConfigPanel(this, tab);
            configurator.init(250);
            this.configurators.get(tab).add(configurator);
            this.configuratorGroup.get(tab).addWidget(configurator);
        }
        computeLayout(tab);
        this.configuratorGroup.get(tab).setScrollYOffset(0);
    }

    public void switchTag(Tab tab) {
        this.tabContainer.switchTag(this.configuratorGroup.get(tab));
    }

    public void computeLayout(Tab tab) {
        int height = 0;
        for (Configurator configurator : this.configurators.get(tab)) {
            configurator.computeHeight();
            configurator.setSelfPosition(new Position(0, height - this.configuratorGroup.get(tab).getScrollYOffset()));
            height += configurator.getSize().height + 5;
        }
        this.configuratorGroup.get(tab).computeMax();
    }
}
