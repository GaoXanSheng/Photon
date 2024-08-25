package com.lowdragmc.lowdraglib.gui.factory;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/factory/HeldItemUIFactory.class */
public class HeldItemUIFactory extends UIFactory<HeldItemHolder> {
    public static final HeldItemUIFactory INSTANCE = new HeldItemUIFactory();

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/factory/HeldItemUIFactory$IHeldItemUIHolder.class */
    public interface IHeldItemUIHolder {
        ModularUI createUI(Player player, HeldItemHolder heldItemHolder);
    }

    public HeldItemUIFactory() {
        super(LDLib.location("held_item"));
    }

    public final boolean openUI(ServerPlayer player, InteractionHand hand) {
        return openUI((HeldItemUIFactory) new HeldItemHolder(player, hand), player);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public ModularUI createUITemplate(HeldItemHolder holder, Player entityPlayer) {
        return holder.createUI(entityPlayer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    @OnlyIn(Dist.CLIENT)
    public HeldItemHolder readHolderFromSyncData(FriendlyByteBuf syncData) {
        LocalPlayer localPlayer = Minecraft.m_91087_().f_91074_;
        if (localPlayer == null) {
            return null;
        }
        return new HeldItemHolder(localPlayer, syncData.m_130066_(InteractionHand.class));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.factory.UIFactory
    public void writeHolderToSyncData(FriendlyByteBuf syncData, HeldItemHolder holder) {
        syncData.m_130068_(holder.hand);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/factory/HeldItemUIFactory$HeldItemHolder.class */
    public static class HeldItemHolder implements IUIHolder {
        Player player;
        InteractionHand hand;
        ItemStack held;

        public HeldItemHolder(Player player, InteractionHand hand) {
            this.player = player;
            this.hand = hand;
            this.held = player.m_21120_(hand);
        }

        @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
        public ModularUI createUI(Player entityPlayer) {
            IHeldItemUIHolder m_41720_ = this.held.m_41720_();
            if (m_41720_ instanceof IHeldItemUIHolder) {
                IHeldItemUIHolder itemUIHolder = m_41720_;
                return itemUIHolder.createUI(entityPlayer, this);
            }
            return null;
        }

        @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
        public boolean isInvalid() {
            return !ItemStack.m_150942_(this.player.m_21120_(this.hand), this.held);
        }

        @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
        public boolean isRemote() {
            return this.player.f_19853_.f_46443_;
        }

        @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
        public void markAsDirty() {
        }

        public Player getPlayer() {
            return this.player;
        }

        public InteractionHand getHand() {
            return this.hand;
        }

        public ItemStack getHeld() {
            return this.held;
        }
    }
}
