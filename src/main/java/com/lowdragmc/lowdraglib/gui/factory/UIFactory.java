package com.lowdragmc.lowdraglib.gui.factory;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.core.mixins.accessor.ServerPlayerAccessor;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.networking.LDLNetworking;
import com.lowdragmc.lowdraglib.networking.s2c.SPacketUIOpen;
import com.lowdragmc.lowdraglib.side.ForgeEventHooks;
import io.netty.buffer.Unpooled;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/factory/UIFactory.class */
public abstract class UIFactory<T> {
    public final ResourceLocation uiFactoryId;
    public static final Map<ResourceLocation, UIFactory<?>> FACTORIES = new HashMap();

    protected abstract ModularUI createUITemplate(T t, Player player);

    @OnlyIn(Dist.CLIENT)
    protected abstract T readHolderFromSyncData(FriendlyByteBuf friendlyByteBuf);

    protected abstract void writeHolderToSyncData(FriendlyByteBuf friendlyByteBuf, T t);

    public UIFactory(ResourceLocation uiFactoryId) {
        this.uiFactoryId = uiFactoryId;
    }

    public static void register(UIFactory<?> factory) {
        FACTORIES.put(factory.uiFactoryId, factory);
    }

    public final boolean openUI(T holder, ServerPlayer player) {
        ModularUI uiTemplate = createUITemplate(holder, player);
        if (uiTemplate == null) {
            return false;
        }
        uiTemplate.initWidgets();
        if (player.f_36096_ != player.f_36095_) {
            player.m_6915_();
        }
        ((ServerPlayerAccessor) player).callNextContainerCounter();
        int currentWindowId = ((ServerPlayerAccessor) player).getContainerCounter();
        FriendlyByteBuf serializedHolder = new FriendlyByteBuf(Unpooled.buffer());
        writeHolderToSyncData(serializedHolder, holder);
        ModularUIContainer container = new ModularUIContainer(uiTemplate, currentWindowId);
        uiTemplate.mainGroup.writeInitialData(serializedHolder);
        LDLNetworking.NETWORK.sendToPlayer(new SPacketUIOpen(this.uiFactoryId, serializedHolder, currentWindowId), player);
        ((ServerPlayerAccessor) player).callInitMenu(container);
        player.f_36096_ = container;
        if (Platform.isForge()) {
            ForgeEventHooks.postPlayerContainerEvent(player, container);
            return true;
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public final void initClientUI(FriendlyByteBuf serializedHolder, int windowId) {
        T holder = readHolderFromSyncData(serializedHolder);
        Minecraft minecraft = Minecraft.m_91087_();
        LocalPlayer entityPlayer = minecraft.f_91074_;
        ModularUI uiTemplate = createUITemplate(holder, entityPlayer);
        if (uiTemplate == null) {
            return;
        }
        uiTemplate.initWidgets();
        ModularUIGuiContainer ModularUIGuiContainer = new ModularUIGuiContainer(uiTemplate, windowId);
        uiTemplate.mainGroup.readInitialData(serializedHolder);
        minecraft.m_91152_(ModularUIGuiContainer);
        minecraft.f_91074_.f_36096_ = ModularUIGuiContainer.m_6262_();
    }
}
