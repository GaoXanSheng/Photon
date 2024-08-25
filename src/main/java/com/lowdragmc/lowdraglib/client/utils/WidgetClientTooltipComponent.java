package com.lowdragmc.lowdraglib.client.utils;

import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/6/29
 * @implNote WidgetClientTooltipComponent
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public record WidgetClientTooltipComponent(
        com.lowdragmc.lowdraglib.gui.util.WidgetClientTooltipComponent tooltipComponent) implements ClientTooltipComponent {
    @Override
    public int getHeight() {
        return tooltipComponent.widget().getSize().height;
    }

    @Override
    public int getWidth(Font font) {
        return tooltipComponent.widget().getSize().width;
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        var modularWrapper = new ModularWrapper<>(tooltipComponent.widget());
        modularWrapper.setRecipeWidget(mouseX, mouseY);
        modularWrapper.draw(poseStack, 0, 0, Minecraft.getInstance().getFrameTime());
    }
}
