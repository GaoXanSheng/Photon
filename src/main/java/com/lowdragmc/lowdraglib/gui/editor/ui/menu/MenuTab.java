package com.lowdragmc.lowdraglib.gui.editor.ui.menu;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.ILDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/menu/MenuTab.class */
public abstract class MenuTab implements ILDLRegister {
    private static final Map<String, List<BiConsumer<MenuTab, TreeBuilder.Menu>>> HOOKS = new LinkedHashMap();
    protected Editor editor = Editor.INSTANCE;

    protected abstract TreeBuilder.Menu createMenu();

    /* JADX INFO: Access modifiers changed from: protected */
    public MenuTab() {
        if (this.editor == null) {
            throw new RuntimeException("editor is null while creating a menu tab %s".formatted(new Object[]{name()}));
        }
    }

    public static void registerMenuHook(String menuName, BiConsumer<MenuTab, TreeBuilder.Menu> consumer) {
        HOOKS.computeIfAbsent(menuName, n -> {
            return new ArrayList();
        }).add(consumer);
    }

    public TreeBuilder.Menu appendMenu(TreeBuilder.Menu menu) {
        for (BiConsumer<MenuTab, TreeBuilder.Menu> hook : HOOKS.getOrDefault(name(), Collections.emptyList())) {
            hook.accept(this, menu);
        }
        return menu;
    }

    @OnlyIn(Dist.CLIENT)
    public Widget createTabWidget() {
        int width = Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format(getTranslateKey(), new Object[0]));
        ButtonWidget button = new ButtonWidget(0, 0, width + 6, 16, new TextTexture(getTranslateKey()), null).setHoverTexture(ColorPattern.T_WHITE.rectTexture(), new TextTexture(getTranslateKey()));
        button.setOnPressCallback(cd -> {
            Position pos = button.getPosition();
            TreeBuilder.Menu view = createMenu();
            IProject currentProject = this.editor.getCurrentProject();
            if (currentProject != null) {
                currentProject.attachMenu(this.editor, name(), view);
            }
            this.editor.openMenu(pos.x, pos.y + 14, appendMenu(view));
        });
        return button.setClientSideWidget();
    }

    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public void deserializeNBT(CompoundTag nbt) {
    }
}
