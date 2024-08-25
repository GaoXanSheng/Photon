package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.CompassView;
import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/RecipeComponent.class */
public class RecipeComponent extends AbstractComponent {
    public static final ResourceTexture PROGRESS_BAR_ARROW = new ResourceTexture("ldlib:textures/gui/progress_bar_arrow.png");
    private static final List<RecipeViewCreator> RECIPE_VIEW_CREATORS = new ArrayList();
    @Nullable
    protected Recipe<?> recipe;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/RecipeComponent$RecipeViewCreator.class */
    public interface RecipeViewCreator extends Predicate<Recipe<?>> {
        ItemStack getWorkstation(Recipe<?> recipe);

        WidgetGroup getViewWidget(Recipe<?> recipe);
    }

    public static void registerRecipeViewCreator(RecipeViewCreator recipeViewCreator) {
        RECIPE_VIEW_CREATORS.add(recipeViewCreator);
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        super.fromXml(element);
        if (element.hasAttribute("id")) {
            ResourceLocation recipeID = new ResourceLocation(element.getAttribute("id"));
            for (Recipe<?> recipe : Minecraft.m_91087_().m_91403_().m_105141_().m_44051_()) {
                if (recipe.m_6423_().equals(recipeID)) {
                    this.recipe = recipe;
                    return this;
                }
            }
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    protected LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        if (this.recipe == null) {
            return currentPage;
        }
        Int2ObjectArrayMap int2ObjectArrayMap = new Int2ObjectArrayMap();
        ItemStack output = this.recipe.m_8043_();
        NonNullList<Ingredient> ingredients = this.recipe.m_7527_();
        ShapedRecipe shapedRecipe = this.recipe;
        if (shapedRecipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe2 = shapedRecipe;
            int w = shapedRecipe2.m_44220_();
            int h = shapedRecipe2.m_44221_();
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int2ObjectArrayMap.put(i + (j * w), (Ingredient) ingredients.get(i + (j * w)));
                }
            }
        } else {
            for (int i2 = 0; i2 < ingredients.size(); i2++) {
                int2ObjectArrayMap.put(i2, (Ingredient) ingredients.get(i2));
            }
        }
        WidgetGroup recipeGroup = null;
        ItemStack workstation = ItemStack.f_41583_;
        Iterator<RecipeViewCreator> it = RECIPE_VIEW_CREATORS.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            RecipeViewCreator creator = it.next();
            if (creator.test(this.recipe)) {
                recipeGroup = creator.getViewWidget(this.recipe);
                workstation = creator.getWorkstation(this.recipe);
                break;
            }
        }
        if (recipeGroup == null) {
            if (this.recipe instanceof CraftingRecipe) {
                recipeGroup = createCraftingRecipeWidget(int2ObjectArrayMap, output);
                workstation = new ItemStack(Items.f_41960_);
            } else if (this.recipe instanceof AbstractCookingRecipe) {
                recipeGroup = createSmeltingRecipeWidget(int2ObjectArrayMap, output);
                workstation = new ItemStack(Items.f_41962_);
            } else {
                recipeGroup = createCraftingRecipeWidget(int2ObjectArrayMap, output);
            }
        }
        recipeGroup.addWidget(new ImageWidget(-40, (recipeGroup.getSize().height / 2) - 15, 30, 30, new ItemStackTexture(workstation)));
        return currentPage.addStreamWidget(wrapper(recipeGroup));
    }

    protected WidgetGroup createSmeltingRecipeWidget(Int2ObjectMap<Ingredient> input, ItemStack output) {
        WidgetGroup widgetGroup = new WidgetGroup(0, 0, CompassView.LIST_WIDTH, 30);
        widgetGroup.setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
        CycleItemStackHandler itemStackHandler = new CycleItemStackHandler(List.of(Arrays.stream(((Ingredient) input.getOrDefault(0, Ingredient.f_43901_)).m_43908_()).toList()));
        widgetGroup.addWidget(new SlotWidget((IItemTransfer) itemStackHandler, 0, 20, 6, false, false));
        ItemStackTransfer handler = new ItemStackTransfer();
        handler.setStackInSlot(0, output);
        widgetGroup.addWidget(new ProgressWidget(ProgressWidget.JEIProgress, 65, 5, 20, 20, new ProgressTexture()));
        widgetGroup.addWidget(new SlotWidget((IItemTransfer) handler, 0, 120, 6, false, false));
        return widgetGroup;
    }

    protected WidgetGroup createCraftingRecipeWidget(Int2ObjectMap<Ingredient> input, ItemStack output) {
        WidgetGroup widgetGroup = new WidgetGroup(0, 0, CompassView.LIST_WIDTH, 66);
        widgetGroup.setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                CycleItemStackHandler itemStackHandler = new CycleItemStackHandler(List.of(Arrays.stream(((Ingredient) input.getOrDefault(x + (y * 3), Ingredient.f_43901_)).m_43908_()).toList()));
                widgetGroup.addWidget(new SlotWidget((IItemTransfer) itemStackHandler, 0, (x * 18) + 20, (y * 18) + 6, false, false));
            }
        }
        ItemStackTransfer handler = new ItemStackTransfer();
        handler.setStackInSlot(0, output);
        widgetGroup.addWidget(new ProgressWidget(ProgressWidget.JEIProgress, 87, 23, 20, 20, PROGRESS_BAR_ARROW));
        widgetGroup.addWidget(new SlotWidget((IItemTransfer) handler, 0, 120, 24, false, false));
        return widgetGroup;
    }
}
