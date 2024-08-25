package com.lowdragmc.lowdraglib.side.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/forge/ForgeEventHooksImpl.class */
public class ForgeEventHooksImpl {
    public static void postPlayerContainerEvent(Player player, AbstractContainerMenu container) {
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, container));
    }

    @OnlyIn(Dist.CLIENT)
    public static void postBackgroundRenderedEvent(Screen screen, PoseStack poseStack) {
        RenderSystem.m_69458_(true);
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(screen, poseStack));
        RenderSystem.m_69458_(false);
    }

    @OnlyIn(Dist.CLIENT)
    public static void postRenderBackgroundEvent(AbstractContainerScreen<?> guiContainer, PoseStack poseStack, int mouseX, int mouseY) {
        RenderSystem.m_69458_(true);
        MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Background(guiContainer, poseStack, mouseX, mouseY));
        RenderSystem.m_69458_(false);
    }

    @OnlyIn(Dist.CLIENT)
    public static void postRenderForegroundEvent(AbstractContainerScreen<?> guiContainer, PoseStack poseStack, int mouseX, int mouseY) {
        RenderSystem.m_69458_(true);
        MinecraftForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground(guiContainer, poseStack, mouseX, mouseY));
        RenderSystem.m_69458_(false);
    }
}
