package com.lowdragmc.lowdraglib.gui.util;

import javax.annotation.ParametersAreNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

/**
 * @author KilaBash
 * @date 2023/6/29
 * @implNote WidgetTooltipComponent
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record WidgetTooltipComponent(Widget widget) implements TooltipComponent {
}

