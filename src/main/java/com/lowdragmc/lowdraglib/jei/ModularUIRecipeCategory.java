package com.lowdragmc.lowdraglib.jei;

import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/jei/ModularUIRecipeCategory.class */
public abstract class ModularUIRecipeCategory<T extends ModularWrapper<?>> implements IRecipeCategory<T> {
    /* JADX WARN: Multi-variable type inference failed */
    public /* bridge */ /* synthetic */ void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, Object obj, IFocusGroup iFocusGroup) {
        setRecipe(iRecipeLayoutBuilder, (IRecipeLayoutBuilder) ((ModularWrapper) obj), iFocusGroup);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, T wrapper, IFocusGroup focuses) {
        List<Widget> flatVisibleWidgetCollection = wrapper.modularUI.getFlatWidgetCollection();
        for (int i = 0; i < flatVisibleWidgetCollection.size(); i++) {
            Widget widget = flatVisibleWidgetCollection.get(i);
            if (widget instanceof IRecipeIngredientSlot) {
                IRecipeIngredientSlot slot = (IRecipeIngredientSlot) widget;
                IRecipeSlotBuilder slotBuilder = builder.addSlot(mapToRole(slot.getIngredientIO()), i, -1);
                Object ingredient = slot.getJEIIngredient();
                if (ingredient != null) {
                    slotBuilder.addIngredientsUnsafe(List.of(ingredient));
                }
            }
        }
    }

    private RecipeIngredientRole mapToRole(IngredientIO ingredientIO) {
        switch (ingredientIO) {
            case INPUT:
                return RecipeIngredientRole.INPUT;
            case OUTPUT:
                return RecipeIngredientRole.OUTPUT;
            case CATALYST:
                return RecipeIngredientRole.CATALYST;
            case RENDER_ONLY:
                return RecipeIngredientRole.RENDER_ONLY;
            default:
                throw new IncompatibleClassChangeError();
        }
    }
}
