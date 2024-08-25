package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.gui.editor.ui.menu.MenuTab;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/MenuPanel.class */
public class MenuPanel extends WidgetGroup {
    public static final int HEIGHT = 16;
    protected final Editor editor;
    protected final Map<String, MenuTab> tabs;

    public Editor getEditor() {
        return this.editor;
    }

    public Map<String, MenuTab> getTabs() {
        return this.tabs;
    }

    public MenuPanel(Editor editor) {
        super(0, 0, editor.getSize().getWidth() - ConfigPanel.WIDTH, 16);
        this.tabs = new LinkedHashMap();
        setClientSideWidget();
        this.editor = editor;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        setBackground(ColorPattern.T_RED.rectTexture());
        addWidget(new ImageWidget(2, 2, 12, 12, new ResourceTexture()));
        if (isRemote()) {
            initTabs();
        }
        super.initWidget();
    }

    protected void initTabs() {
        int x = 20;
        CompoundTag tag = new CompoundTag();
        try {
            tag = NbtIo.m_128953_(new File(this.editor.getWorkSpace(), "settings/menu.cfg"));
            if (tag == null) {
                tag = new CompoundTag();
            }
        } catch (IOException e) {
            LDLib.LOGGER.error(e.getMessage());
        }
        for (AnnotationDetector.Wrapper<LDLRegister, MenuTab> wrapper : AnnotationDetector.REGISTER_MENU_TABS) {
            if (this.editor.name().startsWith(wrapper.annotation().group())) {
                MenuTab tab = wrapper.creator().get();
                this.tabs.put(wrapper.annotation().name(), tab);
                Widget button = tab.createTabWidget();
                button.addSelfPosition(x, 0);
                x += button.getSize().getWidth();
                addWidget(button);
                if (tag.m_128441_(tab.name())) {
                    tab.deserializeNBT(tag.m_128469_(tab.name()));
                }
            }
        }
    }

    public void saveMenuData() {
        CompoundTag tag = new CompoundTag();
        for (MenuTab tab : this.tabs.values()) {
            CompoundTag nbt = tab.serializeNBT();
            if (!nbt.m_128456_()) {
                tag.m_128365_(tab.name(), nbt);
            }
        }
        try {
            NbtIo.m_128955_(tag, new File(this.editor.getWorkSpace(), "settings/menu.cfg"));
        } catch (IOException e) {
            LDLib.LOGGER.error(e.getMessage());
        }
    }
}
