package com.lowdragmc.lowdraglib.jei;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.core.mixins.jei.RecipesGuiAccessor;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIJeiHandler;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.library.gui.recipes.RecipeLayout;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/jei/JEIPlugin.class */
public class JEIPlugin implements IModPlugin {
    public static IJeiRuntime jeiRuntime;
    private static final ModularUIJeiHandler modularUIGuiHandler = new ModularUIJeiHandler();

    public JEIPlugin() {
        LDLib.LOGGER.debug("LDLib JEI Plugin created");
    }

    @Nonnull
    public static List<RecipeLayout<?>> getRecipeLayouts(RecipesGui recipesGui) {
        return new ArrayList(((RecipesGuiAccessor) recipesGui).getRecipeLayouts());
    }

    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime2) {
        jeiRuntime = jeiRuntime2;
    }

    public void registerGuiHandlers(@Nonnull IGuiHandlerRegistration registration) {
        if (LDLib.isReiLoaded() || LDLib.isEmiLoaded()) {
            return;
        }
        registration.addGhostIngredientHandler(ModularUIGuiContainer.class, modularUIGuiHandler);
        registration.addGenericGuiContainerHandler(ModularUIGuiContainer.class, modularUIGuiHandler);
    }

    public void registerAdvanced(@Nonnull IAdvancedRegistration registration) {
    }

    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("ldlib", "jei_plugin");
    }
}
