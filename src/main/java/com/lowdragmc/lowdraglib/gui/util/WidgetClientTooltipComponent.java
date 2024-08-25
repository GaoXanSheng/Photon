package com.lowdragmc.lowdraglib.gui.util;

import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import net.minecraft.MethodsReturnNonnullByDefault;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/6/29
 * @implNote WidgetClientTooltipComponent
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public record WidgetClientTooltipComponent(WidgetTooltipComponent tooltipComponent) implements ClientTooltipComponent {
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