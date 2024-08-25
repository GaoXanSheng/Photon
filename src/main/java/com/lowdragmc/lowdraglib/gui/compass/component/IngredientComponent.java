package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/IngredientComponent.class */
public class IngredientComponent extends AbstractComponent {
    List<Object> ingredients = new ArrayList();

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        super.fromXml(element);
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (e.getNodeName().equals("item")) {
                    XmlUtils.SizedIngredient ingredient = XmlUtils.getIngredient(e);
                    this.ingredients.add(ingredient);
                }
                if (e.getNodeName().equals("fluid")) {
                    FluidStack fluidStack = XmlUtils.getFluidStack(e);
                    this.ingredients.add(fluidStack);
                }
            }
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    protected LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        if (this.ingredients.isEmpty()) {
            return currentPage;
        }
        WidgetGroup group = new WidgetGroup(0, 0, this.ingredients.size() * 20, 20);
        int x = 1;
        for (Object ingredient : this.ingredients) {
            if (ingredient instanceof XmlUtils.SizedIngredient) {
                XmlUtils.SizedIngredient item = (XmlUtils.SizedIngredient) ingredient;
                List<ItemStack> items = Arrays.stream(item.ingredient().m_43908_()).map(i -> {
                    ItemStack copied = i.m_41777_();
                    copied.m_41764_(item.count());
                    return copied;
                }).toList();
                CycleItemStackHandler itemStackHandler = new CycleItemStackHandler(List.of(items));
                group.addWidget(new SlotWidget((IItemTransfer) itemStackHandler, 0, x, 1, false, false).setBackground(new ResourceTexture("ldlib:textures/gui/slot.png")));
            } else if (ingredient instanceof FluidStack) {
                FluidStack fluidStack = (FluidStack) ingredient;
                group.addWidget(new TankWidget(new FluidStorage(fluidStack), x, 1, false, false).setBackground(new ResourceTexture("ldlib:textures/gui/fluid_slot.png")));
            }
            x += 20;
        }
        return currentPage.addStreamWidget(wrapper(group));
    }
}
