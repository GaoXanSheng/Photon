package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.CompassManager;
import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/TextBoxComponent.class */
public class TextBoxComponent extends AbstractComponent {
    protected List<Component> components = new ArrayList();
    protected int space = 2;
    protected boolean isCenter;

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        super.fromXml(element);
        this.components.addAll(XmlUtils.getComponents(element, Style.f_131099_));
        this.space = XmlUtils.getAsInt(element, "space", this.space);
        if (element.hasAttribute("isCenter")) {
            this.isCenter = XmlUtils.getAsBoolean(element, "isCenter", true);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    @OnlyIn(Dist.CLIENT)
    public LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        ComponentPanelWidget panel = new ComponentPanelWidget(0, 0, this.components) { // from class: com.lowdragmc.lowdraglib.gui.compass.component.TextBoxComponent.1
            @Override // com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget
            public void updateComponentTextSize() {
                Font fontRenderer = Minecraft.m_91087_().f_91062_;
                int size = this.cacheLines.size();
                Objects.requireNonNull(fontRenderer);
                int totalHeight = size * (9 + this.space);
                if (totalHeight > 0) {
                    totalHeight = (totalHeight - this.space) + 2;
                }
                setSize(new Size(this.maxWidthLimit, totalHeight));
            }
        }.setSpace(this.space).setCenter(this.isCenter).setMaxWidthLimit(width(currentPage)).clickHandler(CompassManager::onComponentClick);
        return currentPage.addStreamWidget(wrapper(panel));
    }
}
