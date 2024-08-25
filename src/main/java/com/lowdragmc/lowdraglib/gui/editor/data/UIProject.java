package com.lowdragmc.lowdraglib.gui.editor.data;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.editor.ui.MainPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.tool.WidgetToolBox;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.TabButton;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.io.File;
import java.io.IOException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

@LDLRegister(name = "ui", group = "editor.ui")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/data/UIProject.class */
public class UIProject implements IProject {
    public Resources resources;
    public WidgetGroup root;

    private UIProject() {
    }

    public UIProject(Resources resources, WidgetGroup root) {
        this.resources = resources;
        this.root = root;
    }

    public UIProject(CompoundTag tag) {
        deserializeNBT(tag);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.IProject
    public UIProject newEmptyProject() {
        return new UIProject(Resources.defaultResource(), (WidgetGroup) new WidgetGroup(30, 30, 200, 200).setBackground(ResourceBorderTexture.BORDERED_BACKGROUND));
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.m_128365_("resources", this.resources.serializeNBT());
        tag.m_128365_("root", IConfigurableWidget.serializeNBT(this.root, this.resources, true));
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.resources = loadResources(tag.m_128469_("resources"));
        this.root = new WidgetGroup();
        IConfigurableWidget.deserializeNBT(this.root, tag.m_128469_("root"), this.resources, true);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.IProject
    public Resources getResources() {
        return this.resources;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.IProject
    public void saveProject(File file) {
        try {
            NbtIo.m_128955_(serializeNBT(), file);
        } catch (IOException e) {
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.IProject
    public IProject loadProject(File file) {
        try {
            CompoundTag tag = NbtIo.m_128953_(file);
            if (tag != null) {
                return new UIProject(tag);
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.IProject
    public void onLoad(Editor editor) {
        super.onLoad(editor);
        editor.getTabPages().addTab(new TabButton(50, 16, 60, 14).setTexture((IGuiTexture) new GuiTextureGroup(ColorPattern.T_GREEN.rectTexture().setBottomRadius(10.0f).transform(0.0f, 0.4f), new TextTexture("Main")), (IGuiTexture) new GuiTextureGroup(ColorPattern.T_RED.rectTexture().setBottomRadius(10.0f).transform(0.0f, 0.4f), new TextTexture("Main"))), new MainPanel(editor, this.root));
        for (WidgetToolBox.Default tab : WidgetToolBox.Default.TABS) {
            editor.getToolPanel().addNewToolBox("ldlib.gui.editor.group." + tab.groupName, tab.icon, tab.createToolBox());
        }
    }
}
