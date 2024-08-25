package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidStorage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/FluidStackSelectorWidget.class */
public class FluidStackSelectorWidget extends WidgetGroup {
    private Consumer<FluidStack> onIFluidStackUpdate;
    private final IFluidStorage handler;
    private final TextFieldWidget fluidField;
    private FluidStack fluid;

    public FluidStackSelectorWidget(int x, int y, int width) {
        super(x, y, width, 20);
        this.fluid = FluidStack.empty();
        setClientSideWidget();
        this.fluidField = (TextFieldWidget) new TextFieldWidget(22, 0, width - 46, 20, null, s -> {
            if (s != null && !s.isEmpty()) {
                Fluid fluid = (Fluid) Registry.f_122822_.m_7745_(new ResourceLocation(s));
                if (fluid == null) {
                    fluid = FluidStack.empty().getFluid();
                }
                if (!this.fluid.isFluidEqual(FluidStack.create(fluid, 1000L))) {
                    this.fluid = FluidStack.create(fluid, 1000L);
                    onUpdate();
                }
            }
        }).setResourceLocationOnly().setHoverTooltips("ldlib.gui.tips.fluid_selector");
        FluidStorage fluidStorage = new FluidStorage(1000L);
        this.handler = fluidStorage;
        addWidget(new PhantomFluidWidget(fluidStorage, 1, 1).setIFluidStackUpdater(fluidStack -> {
            setIFluidStack(fluidStack);
            onUpdate();
        }).setBackground(new ColorBorderTexture(1, -1)));
        addWidget(this.fluidField);
        addWidget(new ButtonWidget(width - 21, 0, 20, 20, null, cd -> {
            if (this.fluid.isEmpty()) {
                return;
            }
            DialogWidget onClosed = new DialogWidget(getGui().mainGroup, this.isClientSideWidget).setOnClosed(this::onUpdate);
            TextFieldWidget nbtField = new TextFieldWidget(10, 10, getGui().mainGroup.getSize().width - 50, 20, null, s2 -> {
                try {
                    this.fluid.setTag(TagParser.m_129359_(s2));
                    onUpdate();
                } catch (CommandSyntaxException e) {
                }
            });
            onClosed.addWidget(nbtField);
            if (this.fluid.hasTag()) {
                nbtField.setCurrentString(this.fluid.getTag().toString());
            }
        }).setButtonTexture(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("NBT", -1).setDropShadow(true)).setHoverBorderTexture(1, -1).setHoverTooltips("ldlib.gui.tips.fluid_tag"));
    }

    public FluidStack getIFluidStack() {
        return this.fluid;
    }

    public FluidStackSelectorWidget setIFluidStack(FluidStack fluidStack) {
        this.fluid = ((FluidStack) Objects.requireNonNullElse(fluidStack, FluidStack.empty())).copy();
        if (this.fluid != FluidStack.empty()) {
            this.fluid.setAmount(1000L);
        }
        this.handler.setFluid(this.fluid);
        this.fluidField.setCurrentString(Registry.f_122822_.m_7981_(this.fluid.getFluid()));
        return this;
    }

    public FluidStackSelectorWidget setOnIFluidStackUpdate(Consumer<FluidStack> onIFluidStackUpdate) {
        this.onIFluidStackUpdate = onIFluidStackUpdate;
        return this;
    }

    private void onUpdate() {
        this.handler.setFluid(this.fluid);
        if (this.onIFluidStackUpdate != null) {
            this.onIFluidStackUpdate.accept(this.fluid);
        }
    }
}
