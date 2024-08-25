package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.ShaderTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.Arrays;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/ImageComponent.class */
public class ImageComponent extends TextBoxComponent {
    protected int width = 50;
    protected int height = 50;
    protected float u0 = 0.0f;
    protected float v0 = 0.0f;
    protected float u1 = 1.0f;
    protected float v1 = 1.0f;
    protected IGuiTexture guiTexture = new ColorBorderTexture(-1, -1);

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.TextBoxComponent, com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        IGuiTexture iGuiTexture;
        this.width = XmlUtils.getAsInt(element, "width", this.width);
        this.height = XmlUtils.getAsInt(element, "height", this.height);
        this.u0 = XmlUtils.getAsFloat(element, "u0", this.u0);
        this.v0 = XmlUtils.getAsFloat(element, "v0", this.v0);
        this.u1 = XmlUtils.getAsFloat(element, "u1", this.u1);
        this.v1 = XmlUtils.getAsFloat(element, "v1", this.v1);
        String type = XmlUtils.getAsString(element, "type", "resource");
        String url = XmlUtils.getAsString(element, "url", "");
        boolean z = true;
        switch (type.hashCode()) {
            case -903579675:
                if (type.equals("shader")) {
                    z = true;
                    break;
                }
                break;
            case -341064690:
                if (type.equals("resource")) {
                    z = false;
                    break;
                }
                break;
            case 3242771:
                if (type.equals("item")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                iGuiTexture = new ResourceTexture(url).getSubTexture(this.u0, this.v0, this.u1, this.v1);
                break;
            case true:
                XmlUtils.SizedIngredient item = XmlUtils.getIngredient(element);
                ItemStack[] items = (ItemStack[]) Arrays.stream(item.ingredient().m_43908_()).map(i -> {
                    ItemStack copied = i.m_41777_();
                    copied.m_41764_(item.count());
                    return copied;
                }).toArray(x$0 -> {
                    return new ItemStack[x$0];
                });
                iGuiTexture = new ItemStackTexture(items);
                break;
            case true:
                iGuiTexture = ShaderTexture.createShader(new ResourceLocation(url));
                break;
            default:
                iGuiTexture = IGuiTexture.EMPTY;
                break;
        }
        this.guiTexture = iGuiTexture;
        this.isCenter = true;
        return super.fromXml(element);
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.TextBoxComponent, com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    @OnlyIn(Dist.CLIENT)
    public LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        ImageWidget imageWidget = new ImageWidget(0, 0, this.width, this.height, this.guiTexture);
        if (this.hoverInfo != null) {
            imageWidget.setHoverTooltips(this.hoverInfo);
        }
        currentPage.addStreamWidget(wrapper(imageWidget));
        currentPage.addOffsetSpace(3);
        return super.addWidgets(currentPage);
    }
}
