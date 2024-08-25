package com.lowdragmc.lowdraglib.kjs;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.AnimationTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.ShaderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.BlockSelectorWidget;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.DraggableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.PhantomFluidWidget;
import com.lowdragmc.lowdraglib.gui.widget.PhantomSlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.SceneWidget;
import com.lowdragmc.lowdraglib.gui.widget.SelectableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.TabButton;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextBoxWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.TreeListWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.Vector3;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/kjs/LDLibKubeJSPlugin.class */
public class LDLibKubeJSPlugin extends KubeJSPlugin {
    public void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);
        filter.allow("com.lowdragmc.lowdraglib");
    }

    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("ResourceTexture", ResourceTexture.class);
        event.add("FillDirection", ProgressTexture.FillDirection.class);
        event.add("ProgressTexture", ProgressTexture.class);
        event.add("AnimationTexture", AnimationTexture.class);
        event.add("ColorRectTexture", ColorRectTexture.class);
        event.add("ColorRectTexture", ColorRectTexture.class);
        event.add("ItemStackTexture", ItemStackTexture.class);
        event.add("ResourceBorderTexture", ResourceBorderTexture.class);
        event.add("ShaderTexture", ShaderTexture.class);
        event.add("TextTexture", TextTexture.class);
        event.add("GuiTextureGroup", GuiTextureGroup.class);
        event.add("ModularUI", ModularUI.class);
        event.add("BlockSelectorWidget", BlockSelectorWidget.class);
        event.add("ButtonWidget", ButtonWidget.class);
        event.add("DialogWidget", DialogWidget.class);
        event.add("DraggableScrollableWidgetGroup", DraggableScrollableWidgetGroup.class);
        event.add("DraggableWidgetGroup", DraggableWidgetGroup.class);
        event.add("ImageWidget", ImageWidget.class);
        event.add("LabelWidget", LabelWidget.class);
        event.add("PhantomFluidWidget", PhantomFluidWidget.class);
        event.add("PhantomSlotWidget", PhantomSlotWidget.class);
        event.add("SceneWidget", SceneWidget.class);
        event.add("SelectableWidgetGroup", SelectableWidgetGroup.class);
        event.add("SlotWidget", SlotWidget.class);
        event.add("SwitchWidget", SwitchWidget.class);
        event.add("TabButton", TabButton.class);
        event.add("TabContainer", TabContainer.class);
        event.add("TankWidget", TankWidget.class);
        event.add("TextBoxWidget", TextBoxWidget.class);
        event.add("TextFieldWidget", TextFieldWidget.class);
        event.add("TreeListWidget", TreeListWidget.class);
        event.add("WidgetGroup", WidgetGroup.class);
        event.add("ProgressWidget", ProgressWidget.class);
        event.add("Vector3", Vector3.class);
        event.add("GuiSize", Size.class);
        event.add("GuiPos", Position.class);
    }

    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        super.registerTypeWrappers(type, typeWrappers);
        typeWrappers.register(FluidStack.class, ctx, o -> {
            dev.architectury.fluid.FluidStack fluidStack = FluidStackJS.of(o).getFluidStack();
            return FluidStack.create(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getTag());
        });
    }
}
