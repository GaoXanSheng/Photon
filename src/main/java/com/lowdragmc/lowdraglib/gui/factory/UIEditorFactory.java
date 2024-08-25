package com.lowdragmc.lowdraglib.gui.factory;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.ui.UIEditor;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/factory/UIEditorFactory.class */
public class UIEditorFactory extends UIFactory<UIEditorFactory> implements IUIHolder {
    public static final UIEditorFactory INSTANCE = new UIEditorFactory();

    private UIEditorFactory() {
        super(LDLib.location("ui_editor"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public ModularUI createUITemplate(UIEditorFactory holder, Player entityPlayer) {
        return createUI(entityPlayer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public UIEditorFactory readHolderFromSyncData(FriendlyByteBuf syncData) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public void writeHolderToSyncData(FriendlyByteBuf syncData, UIEditorFactory holder) {
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(this, entityPlayer).widget(new UIEditor(LDLib.getLDLibDir()));
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public boolean isInvalid() {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public boolean isRemote() {
        return LDLib.isRemote();
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public void markAsDirty() {
    }
}
