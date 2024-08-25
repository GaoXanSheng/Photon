package com.lowdragmc.lowdraglib.gui.util.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/forge/DrawerHelperImpl.class */
public class DrawerHelperImpl {
    public static void drawTooltip(PoseStack poseStack, int mouseX, int mouseY, List<Component> tooltipTexts, ItemStack tooltipStack, TooltipComponent tooltipComponent, Font tooltipFont) {
        Minecraft.m_91087_().f_91080_.renderTooltip(poseStack, tooltipTexts, Optional.ofNullable(tooltipComponent), mouseX, mouseY, tooltipFont, tooltipStack);
    }

    public static ClientTooltipComponent getClientTooltipComponent(TooltipComponent component) {
        return ClientTooltipComponent.m_169950_(component);
    }
}
