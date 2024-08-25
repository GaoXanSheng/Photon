package com.lowdragmc.lowdraglib.gui.compass.component.animation;

import com.lowdragmc.lowdraglib.gui.compass.CompassManager;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.ShaderTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/animation/InformationAction.class */
public class InformationAction extends Action {
    private final IGuiTexture guiTexture;
    private final List<Component> text;

    public InformationAction(IGuiTexture guiTexture, List<Component> text) {
        this.text = new ArrayList();
        this.guiTexture = guiTexture;
        this.text.addAll(text);
    }

    public InformationAction(Element element) {
        super(element);
        IGuiTexture iGuiTexture;
        this.text = new ArrayList();
        float u0 = XmlUtils.getAsFloat(element, "u0", 0.0f);
        float v0 = XmlUtils.getAsFloat(element, "v0", 0.0f);
        float u1 = XmlUtils.getAsFloat(element, "u1", 1.0f);
        float v1 = XmlUtils.getAsFloat(element, "v1", 1.0f);
        String type = XmlUtils.getAsString(element, "type", "");
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
                iGuiTexture = new ResourceTexture(url).getSubTexture(u0, v0, u1, v1);
                break;
            case true:
                iGuiTexture = new ItemStackTexture((Item) Registry.f_122827_.m_7745_(new ResourceLocation(url)));
                break;
            case true:
                iGuiTexture = ShaderTexture.createShader(new ResourceLocation(url));
                break;
            default:
                iGuiTexture = IGuiTexture.EMPTY;
                break;
        }
        this.guiTexture = iGuiTexture;
        this.text.addAll(XmlUtils.getComponents(element, Style.f_131099_));
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.animation.Action
    public int getDuration() {
        return 20;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.animation.Action
    public void performAction(AnimationFrame frame, CompassScene scene, boolean anima) {
        int size = Math.min(scene.getHeaderGroup().getSize().width, scene.getHeaderGroup().getSize().height);
        if (this.text.isEmpty() && this.guiTexture == IGuiTexture.EMPTY) {
            return;
        }
        if (this.text.isEmpty()) {
            scene.addInformation(new ImageWidget(0, 0, size, size, this.guiTexture), anima);
        } else if (this.guiTexture == IGuiTexture.EMPTY) {
            ComponentPanelWidget componentPanelWidget = new ComponentPanelWidget(0, 0, this.text).clickHandler(CompassManager::onComponentClick);
            componentPanelWidget.setBackground(TooltipBGTexture.INSTANCE);
            int maxWidth = 0;
            while (maxWidth < scene.getHeaderGroup().getSize().width && (componentPanelWidget.getSize().height == 0 || componentPanelWidget.getSize().height > size)) {
                maxWidth += 50;
                componentPanelWidget.setMaxWidthLimit(maxWidth);
            }
            scene.addInformation(componentPanelWidget, anima);
        } else {
            ComponentPanelWidget componentPanelWidget2 = new ComponentPanelWidget(0, 0, this.text).clickHandler(CompassManager::onComponentClick);
            componentPanelWidget2.setBackground(TooltipBGTexture.INSTANCE);
            int maxWidth2 = 0;
            while (maxWidth2 < scene.getHeaderGroup().getSize().width && (componentPanelWidget2.getSize().height == 0 || componentPanelWidget2.getSize().height > size)) {
                maxWidth2 += 50;
                componentPanelWidget2.setMaxWidthLimit(maxWidth2);
            }
            WidgetGroup container = new WidgetGroup(0, 0, size + maxWidth2, size);
            container.addWidget(new ImageWidget(2, 2, size - 4, size - 4, this.guiTexture));
            componentPanelWidget2.addSelfPosition(size, (size - componentPanelWidget2.getSize().height) / 2);
            container.addWidget(componentPanelWidget2);
            scene.addInformation(container, anima);
        }
    }
}
