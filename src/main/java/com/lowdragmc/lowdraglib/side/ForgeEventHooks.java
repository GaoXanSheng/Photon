package com.lowdragmc.lowdraglib.side;

import com.lowdragmc.lowdraglib.side.forge.ForgeEventHooksImpl;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/ForgeEventHooks.class */
public class ForgeEventHooks {
    @ExpectPlatform.Transformed
    @ExpectPlatform
    @PlatformOnly({"forge"})
    public static void postPlayerContainerEvent(Player player, AbstractContainerMenu container) {
        ForgeEventHooksImpl.postPlayerContainerEvent(player, container);
    }

    @ExpectPlatform.Transformed
    @PlatformOnly({"forge"})
    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    public static void postBackgroundRenderedEvent(Screen screen, PoseStack poseStack) {
        ForgeEventHooksImpl.postBackgroundRenderedEvent(screen, poseStack);
    }

    @ExpectPlatform.Transformed
    @PlatformOnly({"forge"})
    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    public static void postRenderBackgroundEvent(AbstractContainerScreen<?> guiContainer, PoseStack poseStack, int mouseX, int mouseY) {
        ForgeEventHooksImpl.postRenderBackgroundEvent(guiContainer, poseStack, mouseX, mouseY);
    }

    @ExpectPlatform.Transformed
    @PlatformOnly({"forge"})
    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    public static void postRenderForegroundEvent(AbstractContainerScreen<?> guiContainer, PoseStack poseStack, int mouseX, int mouseY) {
        ForgeEventHooksImpl.postRenderForegroundEvent(guiContainer, poseStack, mouseX, mouseY);
    }
}
