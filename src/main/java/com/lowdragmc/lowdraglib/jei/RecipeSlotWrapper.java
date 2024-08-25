package com.lowdragmc.lowdraglib.jei;

import com.lowdragmc.lowdraglib.core.mixins.jei.RecipeSlotAccessor;
import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.ingredients.TypedIngredient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/jei/RecipeSlotWrapper.class */
public class RecipeSlotWrapper extends RecipeSlot {
    private final Widget widget;
    private final RecipeSlot wrapperSlot;
    private ImmutableRect2i area;

    public RecipeSlotWrapper(Widget widget, RecipeSlot wrapperSlot, int xPos, int yPos) {
        super(((RecipeSlotAccessor) wrapperSlot).getIngredientManager(), wrapperSlot.getRole(), 0, 0, 0);
        this.widget = widget;
        this.wrapperSlot = wrapperSlot;
        this.area = new ImmutableRect2i(xPos, yPos, widget.getSize().width, widget.getSize().height);
        ((RecipeSlotAccessor) this).setArea(this.area);
        ((RecipeSlotAccessor) wrapperSlot).setArea(this.area);
        if (widget instanceof IRecipeIngredientSlot) {
            IRecipeIngredientSlot slot = (IRecipeIngredientSlot) widget;
            slot.clearTooltipCallback();
        }
    }

    public Stream<ITypedIngredient<?>> getAllIngredients() {
        return this.wrapperSlot.getAllIngredients();
    }

    public boolean isEmpty() {
        return this.wrapperSlot.isEmpty();
    }

    public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType) {
        return this.wrapperSlot.getIngredients(ingredientType);
    }

    public Optional<ITypedIngredient<?>> getDisplayedIngredient() {
        return getDisplayIngredient();
    }

    public <T> Optional<T> getDisplayedIngredient(IIngredientType<T> ingredientType) {
        return this.wrapperSlot.getDisplayedIngredient(ingredientType);
    }

    public Optional<String> getSlotName() {
        return this.wrapperSlot.getSlotName();
    }

    public RecipeIngredientRole getRole() {
        return this.wrapperSlot.getRole();
    }

    public void drawHighlight(PoseStack poseStack, int color) {
        int x = this.area.getX();
        int y = this.area.getY();
        int width = this.area.getWidth();
        int height = this.area.getHeight();
        RenderSystem.m_69465_();
        GuiComponent.m_93172_(poseStack, x, y, x + width, y + height, color);
        RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setBackground(IDrawable background) {
        this.wrapperSlot.setBackground(background);
    }

    public void setOverlay(IDrawable overlay) {
        this.wrapperSlot.setOverlay(overlay);
    }

    public void set(List<Optional<ITypedIngredient<?>>> ingredients, Set<Integer> focusMatches, IIngredientVisibility ingredientVisibility) {
        this.wrapperSlot.set(ingredients, focusMatches, ingredientVisibility);
    }

    public List<Component> getTooltip() {
        return this.wrapperSlot.getTooltip();
    }

    public void addTooltipCallback(IRecipeSlotTooltipCallback tooltipCallback) {
        this.wrapperSlot.addTooltipCallback(tooltipCallback);
        if (this.widget instanceof IRecipeIngredientSlot) {
            ((IRecipeIngredientSlot) this.widget).addTooltipCallback(tooltips -> {
                ArrayList arrayList = new ArrayList();
                tooltipCallback.onTooltip(this, arrayList);
                tooltipCallback.addAll(arrayList);
            });
        }
    }

    public <T> void addRenderOverride(IIngredientType<T> ingredientType, IIngredientRenderer<T> ingredientRenderer) {
        this.wrapperSlot.addRenderOverride(ingredientType, ingredientRenderer);
    }

    public void draw(PoseStack poseStack) {
        this.wrapperSlot.draw(poseStack);
    }

    public void drawHoverOverlays(PoseStack poseStack) {
        this.wrapperSlot.drawHoverOverlays(poseStack);
    }

    public Rect2i getRect() {
        return this.area.toMutable();
    }

    public void setSlotName(String slotName) {
        this.wrapperSlot.setSlotName(slotName);
    }

    public void onPositionUpdate(RecipeLayoutWrapper<?> layoutWrapper) {
        int posY = this.widget.getPosition().y - layoutWrapper.getWrapper().getTop();
        int height = this.widget.getSize().height;
        if (posY < 0) {
            height += posY;
            posY = 0;
        }
        this.area = new ImmutableRect2i(this.widget.getPosition().x - layoutWrapper.getWrapper().getLeft(), posY, this.widget.getSize().width, height);
        ((RecipeSlotAccessor) this).setArea(this.area);
        this.wrapperSlot.setArea(this.area);
    }

    private Optional<ITypedIngredient<?>> getDisplayIngredient() {
        Widget widget = this.widget;
        if (widget instanceof IRecipeIngredientSlot) {
            IRecipeIngredientSlot slot = (IRecipeIngredientSlot) widget;
            if (slot.getJEIIngredient() != null) {
                return TypedIngredient.createAndFilterInvalid(((RecipeSlotAccessor) this).getIngredientManager(), slot.getJEIIngredient());
            }
        }
        return Optional.empty();
    }
}
