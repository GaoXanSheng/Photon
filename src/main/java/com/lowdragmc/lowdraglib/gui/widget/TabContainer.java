package com.lowdragmc.lowdraglib.gui.widget;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ArrayConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.texture.WidgetTexture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

@LDLRegister(name = "tab_group", group = "widget.group")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/TabContainer.class */
public class TabContainer extends WidgetGroup {
    public static final ResourceTexture TABS_LEFT = new ResourceTexture("ldlib:textures/gui/tabs_left.png");
    public final BiMap<TabButton, WidgetGroup> tabs;
    public final WidgetGroup buttonGroup;
    public final WidgetGroup containerGroup;
    public WidgetGroup focus;
    public BiConsumer<WidgetGroup, WidgetGroup> onChanged;

    public TabContainer() {
        this(0, 0, 40, 60);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public void initTemplate() {
        setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
        addTab(new TabButton(-28, 0, 32, 28).setTexture((IGuiTexture) new GuiTextureGroup(TABS_LEFT.getSubTexture(0.0f, 0.0f, 0.5f, 0.33333334f), new TextTexture("A")), (IGuiTexture) new GuiTextureGroup(TABS_LEFT.getSubTexture(0.5f, 0.0f, 0.5f, 0.33333334f), new TextTexture("A"))), new WidgetGroup(0, 0, 0, 0));
        addTab(new TabButton(-28, 28, 32, 28).setTexture((IGuiTexture) new GuiTextureGroup(TABS_LEFT.getSubTexture(0.0f, 0.33333334f, 0.5f, 0.33333334f), new TextTexture("B")), (IGuiTexture) new GuiTextureGroup(TABS_LEFT.getSubTexture(0.5f, 0.33333334f, 0.5f, 0.33333334f), new TextTexture("B"))), new WidgetGroup(0, 0, 0, 0));
    }

    public TabContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.tabs = HashBiMap.create();
        this.buttonGroup = new WidgetGroup(0, 0, 0, 0);
        this.containerGroup = new WidgetGroup(0, 0, 0, 0);
        addWidget(this.containerGroup);
        addWidget(this.buttonGroup);
    }

    public TabContainer setOnChanged(BiConsumer<WidgetGroup, WidgetGroup> onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    public void switchTag(WidgetGroup tabWidget) {
        if (this.focus == tabWidget) {
            return;
        }
        if (this.focus != null) {
            ((TabButton) this.tabs.inverse().get(this.focus)).setPressed(false);
            this.focus.setVisible(false);
            this.focus.setActive(false);
        }
        if (this.onChanged != null) {
            this.onChanged.accept(this.focus, tabWidget);
        }
        this.focus = tabWidget;
        Optional.ofNullable((TabButton) this.tabs.inverse().get(tabWidget)).ifPresent(tab -> {
            tab.setPressed(true);
            tabWidget.setActive(true);
            tabWidget.setVisible(true);
        });
    }

    public void addTab(TabButton tabButton, WidgetGroup tabWidget) {
        tabButton.setContainer(this);
        this.tabs.put(tabButton, tabWidget);
        this.containerGroup.addWidget(tabWidget);
        this.buttonGroup.addWidget(tabButton);
        if (this.focus == null) {
            this.focus = tabWidget;
        }
        tabButton.setPressed(this.focus == tabWidget);
        tabWidget.setVisible(this.focus == tabWidget);
        tabWidget.setActive(this.focus == tabWidget);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
    public void clearAllWidgets() {
        this.tabs.clear();
        this.buttonGroup.clearAllWidgets();
        this.containerGroup.clearAllWidgets();
        this.focus = null;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @Nullable
    public Widget getHoverElement(double mouseX, double mouseY) {
        Widget hovered = super.getHoverElement(mouseX, mouseY);
        if (hovered instanceof WidgetGroup) {
            WidgetGroup group = (WidgetGroup) hovered;
            if (this.tabs.containsValue(group)) {
                return this;
            }
        }
        return hovered;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidgetGroup
    public void acceptWidget(IConfigurableWidget widget) {
        if (this.focus != null) {
            this.focus.addWidget(widget.widget());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
    protected void addWidgetsConfigurator(ConfiguratorGroup father) {
        ArrayConfiguratorGroup<TabButton> tabsGroup = new ArrayConfiguratorGroup<>("tabs", false, () -> {
            return new ArrayList(this.tabs.keySet());
        }, getter, setter -> {
            TabButton tab = (TabButton) getter.get();
            return new WrapperConfigurator(tab.id, new ImageWidget(0, 0, 50, 50, new WidgetTexture(tab)));
        }, true);
        tabsGroup.setAddDefault(() -> {
            return new TabButton(0, 0, 32, 28).setTexture((IGuiTexture) new GuiTextureGroup(TABS_LEFT.getSubTexture(0.0f, 0.33333334f, 0.5f, 0.33333334f), new TextTexture("N")), (IGuiTexture) new GuiTextureGroup(TABS_LEFT.getSubTexture(0.5f, 0.33333334f, 0.5f, 0.33333334f), new TextTexture("N")));
        });
        tabsGroup.setOnAdd(tab -> {
            addTab(tab, new WidgetGroup(0, 0, getSize().width, getSize().height));
        });
        tabsGroup.setOnRemove(tab2 -> {
            this.buttonGroup.removeWidget(tab2);
            this.containerGroup.removeWidget((Widget) this.tabs.get(tab2));
            if (this.focus == this.tabs.remove(tab2)) {
                this.focus = null;
            }
        });
        ArrayConfiguratorGroup<Widget> childrenGroup = new ArrayConfiguratorGroup<>("children", true, () -> {
            return this.focus == null ? new ArrayList() : this.focus.widgets;
        }, getter2, setter2 -> {
            Widget child = (Widget) getter2.get();
            return new WrapperConfigurator(child.id, new ImageWidget(0, 0, 50, 50, new WidgetTexture(child)));
        }, true);
        childrenGroup.setCanAdd(false);
        childrenGroup.setOnRemove(child -> {
            if (this.focus != null) {
                this.focus.removeWidget(child);
            }
        });
        childrenGroup.setOnReorder(index, widget -> {
            if (this.focus != null) {
                this.focus.removeWidget(widget);
                this.focus.addWidget(index.intValue(), widget);
            }
        });
        father.addConfigurators(tabsGroup, childrenGroup);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public CompoundTag serializeInnerNBT() {
        CompoundTag tag = new CompoundTag();
        PersistedParser.serializeNBT(tag, getClass(), this);
        ListTag tabs = new ListTag();
        for (Map.Entry<TabButton, WidgetGroup> entry : this.tabs.entrySet()) {
            TabButton button = entry.getKey();
            WidgetGroup group = entry.getValue();
            CompoundTag tab = new CompoundTag();
            tab.m_128365_("button", button.serializeInnerNBT());
            tab.m_128365_("group", group.serializeWrapper());
            tabs.add(tab);
        }
        tag.m_128365_("tabs", tabs);
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public void deserializeInnerNBT(CompoundTag nbt) {
        clearAllWidgets();
        PersistedParser.deserializeNBT(nbt, new HashMap(), getClass(), this);
        ListTag tabs = nbt.m_128437_("tabs", 10);
        Iterator it = tabs.iterator();
        while (it.hasNext()) {
            CompoundTag compoundTag = (Tag) it.next();
            if (compoundTag instanceof CompoundTag) {
                CompoundTag tab = compoundTag;
                TabButton button = new TabButton();
                button.deserializeInnerNBT(tab.m_128469_("button"));
                IConfigurableWidget widget = IConfigurableWidget.deserializeWrapper(tab.m_128469_("group"));
                if (widget != null) {
                    Widget widget2 = widget.widget();
                    if (widget2 instanceof WidgetGroup) {
                        WidgetGroup group = (WidgetGroup) widget2;
                        addTab(button, group);
                    }
                }
            }
        }
    }
}
