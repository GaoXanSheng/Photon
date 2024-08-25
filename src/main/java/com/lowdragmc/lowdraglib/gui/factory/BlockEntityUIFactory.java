package com.lowdragmc.lowdraglib.gui.factory;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/factory/BlockEntityUIFactory.class */
public class BlockEntityUIFactory extends UIFactory<BlockEntity> {
    public static final BlockEntityUIFactory INSTANCE = new BlockEntityUIFactory();

    private BlockEntityUIFactory() {
        super(LDLib.location("block_entity"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public ModularUI createUITemplate(BlockEntity holder, Player entityPlayer) {
        if (holder instanceof IUIHolder) {
            return ((IUIHolder) holder).createUI(entityPlayer);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    @OnlyIn(Dist.CLIENT)
    public BlockEntity readHolderFromSyncData(FriendlyByteBuf syncData) {
        ClientLevel clientLevel = Minecraft.m_91087_().f_91073_;
        if (clientLevel == null) {
            return null;
        }
        return clientLevel.m_7702_(syncData.m_130135_());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public void writeHolderToSyncData(FriendlyByteBuf syncData, BlockEntity holder) {
        syncData.m_130064_(holder.m_58899_());
    }
}
