package com.lowdragmc.lowdraglib.gui.ingredient;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/ingredient/IRecipeIngredientSlot.class */
public interface IRecipeIngredientSlot extends IIngredientSlot {
    @Nullable
    Object getJEIIngredient();

    default Widget self() {
        return (Widget) this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.ingredient.IIngredientSlot
    @Nullable
    default Object getIngredientOverMouse(double mouseX, double mouseY) {
        if (self().isMouseOverElement(mouseX, mouseY)) {
            return getJEIIngredient();
        }
        return null;
    }

    default IngredientIO getIngredientIO() {
        return IngredientIO.RENDER_ONLY;
    }

    default void addTooltipCallback(Consumer<List<Component>> callback) {
    }

    default void clearTooltipCallback() {
    }
}
