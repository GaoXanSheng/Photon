package com.lowdragmc.lowdraglib.gui.widget;

import com.google.common.collect.Lists;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.ingredient.IGhostIngredientTarget;
import com.lowdragmc.lowdraglib.gui.ingredient.Target;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidStorage;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import dev.emi.emi.api.stack.FluidEmiStack;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "phantom_fluid_slot", group = "widget.container")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/PhantomFluidWidget.class */
public class PhantomFluidWidget extends TankWidget implements IGhostIngredientTarget, IConfigurableWidget {
    private Consumer<FluidStack> fluidStackUpdater;

    public PhantomFluidWidget() {
        this.allowClickFilled = false;
        this.allowClickDrained = false;
    }

    public PhantomFluidWidget(IFluidStorage fluidTank, int x, int y) {
        super(fluidTank, x, y, false, false);
    }

    public PhantomFluidWidget(@Nullable IFluidStorage fluidTank, int x, int y, int width, int height) {
        super(fluidTank, x, y, width, height, false, false);
    }

    public PhantomFluidWidget setIFluidStackUpdater(Consumer<FluidStack> fluidStackUpdater) {
        this.fluidStackUpdater = fluidStackUpdater;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.TankWidget
    @ConfigSetter(field = "allowClickFilled")
    public PhantomFluidWidget setAllowClickFilled(boolean v) {
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.TankWidget
    @ConfigSetter(field = "allowClickDrained")
    public PhantomFluidWidget setAllowClickDrained(boolean v) {
        return this;
    }

    public static FluidStack drainFrom(Object ingredient) {
        if (ingredient instanceof Ingredient) {
            Ingredient ingred = (Ingredient) ingredient;
            ItemStack[] items = ingred.m_43908_();
            if (items.length > 0) {
                ingredient = items[0];
            }
        }
        if (ingredient instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) ingredient;
            IFluidTransfer handler = FluidTransferHelper.getFluidTransfer(new ItemStackTransfer(itemStack), 0);
            if (handler != null) {
                return handler.drain(Long.MAX_VALUE, true);
            }
            return null;
        }
        return null;
    }

    @Override // com.lowdragmc.lowdraglib.gui.ingredient.IGhostIngredientTarget
    @OnlyIn(Dist.CLIENT)
    public List<Target> getPhantomTargets(Object ingredient) {
        if (LDLib.isReiLoaded() && (ingredient instanceof dev.architectury.fluid.FluidStack)) {
            dev.architectury.fluid.FluidStack fluidStack = (dev.architectury.fluid.FluidStack) ingredient;
            ingredient = FluidStack.create(fluidStack.getFluid(), this.fluidTank.getCapacity(), fluidStack.getTag());
        }
        if (LDLib.isEmiLoaded() && (ingredient instanceof FluidEmiStack)) {
            FluidEmiStack fluidEmiStack = (FluidEmiStack) ingredient;
            Fluid fluid = (Fluid) fluidEmiStack.getKeyOfType(Fluid.class);
            ingredient = fluid == null ? FluidStack.empty() : FluidStack.create(fluid, this.fluidTank.getCapacity(), fluidEmiStack.getNbt());
        }
        if (!(ingredient instanceof FluidStack) && drainFrom(ingredient) == null) {
            return Collections.emptyList();
        }
        final Rect2i rectangle = toRectangleBox();
        return Lists.newArrayList(new Target[]{new Target() { // from class: com.lowdragmc.lowdraglib.gui.widget.PhantomFluidWidget.1
            @Override // com.lowdragmc.lowdraglib.gui.ingredient.Target
            @Nonnull
            public Rect2i getArea() {
                return rectangle;
            }

            @Override // com.lowdragmc.lowdraglib.gui.ingredient.Target, java.util.function.Consumer
            public void accept(@Nonnull Object ingredient2) {
                FluidStack ingredientStack;
                if (PhantomFluidWidget.this.fluidTank == null) {
                    return;
                }
                if (LDLib.isReiLoaded() && (ingredient2 instanceof dev.architectury.fluid.FluidStack)) {
                    dev.architectury.fluid.FluidStack fluidStack2 = (dev.architectury.fluid.FluidStack) ingredient2;
                    ingredient2 = FluidStack.create(fluidStack2.getFluid(), PhantomFluidWidget.this.fluidTank.getCapacity(), fluidStack2.getTag());
                }
                if (LDLib.isEmiLoaded() && (ingredient2 instanceof FluidEmiStack)) {
                    FluidEmiStack fluidEmiStack2 = (FluidEmiStack) ingredient2;
                    Fluid fluid2 = (Fluid) fluidEmiStack2.getKeyOfType(Fluid.class);
                    ingredient2 = fluid2 == null ? FluidStack.empty() : FluidStack.create(fluid2, PhantomFluidWidget.this.fluidTank.getCapacity(), fluidEmiStack2.getNbt());
                }
                if (ingredient2 instanceof FluidStack) {
                    ingredientStack = (FluidStack) ingredient2;
                } else {
                    ingredientStack = PhantomFluidWidget.drainFrom(ingredient2);
                }
                if (ingredientStack != null) {
                    CompoundTag tagCompound = ingredientStack.saveToTag(new CompoundTag());
                    PhantomFluidWidget.this.writeClientAction(2, buffer -> {
                        buffer.m_130079_(tagCompound);
                    });
                }
                if (PhantomFluidWidget.this.isClientSideWidget) {
                    PhantomFluidWidget.this.fluidTank.drain(PhantomFluidWidget.this.fluidTank.getCapacity(), false);
                    if (ingredientStack != null) {
                        PhantomFluidWidget.this.fluidTank.fill(ingredientStack.copy(), false);
                    }
                    if (PhantomFluidWidget.this.fluidStackUpdater != null) {
                        PhantomFluidWidget.this.fluidStackUpdater.accept(ingredientStack);
                    }
                }
            }
        }});
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.TankWidget, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == 1) {
            handlePhantomClick();
        } else if (id == 2) {
            FluidStack fluidStack = FluidStack.loadFromTag(buffer.m_130260_());
            if (this.fluidTank == null) {
                return;
            }
            this.fluidTank.drain(this.fluidTank.getCapacity(), false);
            if (fluidStack != null) {
                this.fluidTank.fill(fluidStack.copy(), false);
            }
            if (this.fluidStackUpdater != null) {
                this.fluidStackUpdater.accept(fluidStack);
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.TankWidget, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            if (this.isClientSideWidget) {
                handlePhantomClick();
                return true;
            }
            writeClientAction(1, buffer -> {
            });
            return true;
        }
        return false;
    }

    private void handlePhantomClick() {
        if (this.fluidTank == null) {
            return;
        }
        ItemStack itemStack = this.gui.getModularUIContainer().m_142621_().m_41777_();
        if (!itemStack.m_41619_()) {
            itemStack.m_41764_(1);
            IFluidTransfer handler = FluidTransferHelper.getFluidTransfer(this.gui.entityPlayer, this.gui.getModularUIContainer());
            if (handler != null) {
                FluidStack resultFluid = handler.drain(2147483647L, true);
                this.fluidTank.drain(this.fluidTank.getCapacity(), false);
                this.fluidTank.fill(resultFluid.copy(), false);
                if (this.fluidStackUpdater != null) {
                    this.fluidStackUpdater.accept(resultFluid);
                    return;
                }
                return;
            }
            return;
        }
        this.fluidTank.drain(this.fluidTank.getCapacity(), false);
        if (this.fluidStackUpdater != null) {
            this.fluidStackUpdater.accept(null);
        }
    }
}
