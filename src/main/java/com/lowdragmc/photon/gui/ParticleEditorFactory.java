package com.lowdragmc.photon.gui;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.photon.Photon;
import com.lowdragmc.photon.gui.editor.ParticleEditor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/ParticleEditorFactory.class */
public class ParticleEditorFactory extends UIFactory<ParticleEditorFactory> implements IUIHolder {
    public static final ParticleEditorFactory INSTANCE = new ParticleEditorFactory();

    private ParticleEditorFactory() {
        super(Photon.id("editor"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public ModularUI createUITemplate(ParticleEditorFactory holder, Player entityPlayer) {
        return createUI(entityPlayer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public ParticleEditorFactory readHolderFromSyncData(FriendlyByteBuf syncData) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public void writeHolderToSyncData(FriendlyByteBuf syncData, ParticleEditorFactory holder) {
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(this, entityPlayer).widget(new ParticleEditor(LDLib.getLDLibDir()));
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
