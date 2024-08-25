package com.lowdragmc.lowdraglib.client.forge;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.ClientCommands;
import com.lowdragmc.lowdraglib.gui.compass.CompassManager;
import com.lowdragmc.lowdraglib.gui.compass.ItemLookupWidget;
import com.lowdragmc.lowdraglib.gui.util.WidgetClientTooltipComponent;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Either;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * @author KilaBash
 * @date 2022/5/12
 * @implNote EventListener
 */
@Mod.EventBusSubscriber(modid = LDLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ClientEventListener {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        List<LiteralArgumentBuilder<CommandSourceStack>> commands = ClientCommands.createClientCommands();
        commands.forEach(dispatcher::register);
    }

    @SubscribeEvent
    public static void onScreenClosed(ScreenEvent event) {
        if (LDLib.isReiLoaded()) {
            if (event.getScreen() instanceof DisplayScreen && !ModularDisplay.CACHE_OPENED.isEmpty()) {
                synchronized (ModularDisplay.CACHE_OPENED) {
                    ModularDisplay.CACHE_OPENED.forEach(modular -> modular.modularUI.triggerCloseListeners());
                    ModularDisplay.CACHE_OPENED.clear();
                }
            }
        }
        if (LDLib.isEmiLoaded()) {
            if (event.getScreen() instanceof RecipeScreen && !ModularEmiRecipe.CACHE_OPENED.isEmpty()) {
                synchronized (ModularEmiRecipe.CACHE_OPENED) {
                    ModularEmiRecipe.CACHE_OPENED.forEach(modular -> modular.modularUI.triggerCloseListeners());
                    ModularEmiRecipe.CACHE_OPENED.clear();
                }
            }
        }
    }
    @SubscribeEvent
    public static void appendRenderTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack itemStack = event.getItemStack();
        var elements = event.getTooltipElements();
        long id = Minecraft.getInstance().getWindow().getWindow();
        var isCPressed = InputConstants.isKeyDown(id, GLFW.GLFW_KEY_C);

        if (CompassManager.INSTANCE.hasCompass(itemStack.getItem())) {
            if (isCPressed) {
                elements.add(Either.right(new WidgetClientTooltipComponent(new ItemLookupWidget("ldlib.compass.c_press"))));
                CompassManager.INSTANCE.onCPressed(itemStack);
            } else {
                elements.add((Either.left(FormattedText.of(I18n.get("ldlib.compass.c_press"), Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)))));
            }
            return;
        }
        CompassManager.INSTANCE.clearCPressed();
    }
}
