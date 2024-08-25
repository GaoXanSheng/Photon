package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/AbstractComponent.class */
public abstract class AbstractComponent implements ILayoutComponent {
    protected int topMargin = 0;
    protected int bottomMargin = 0;
    protected int leftMargin = 0;
    protected int rightMargin = 0;
    protected String hoverInfo;

    @OnlyIn(Dist.CLIENT)
    protected abstract LayoutPageWidget addWidgets(LayoutPageWidget layoutPageWidget);

    @Override // com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        this.topMargin = XmlUtils.getAsInt(element, "top-margin", this.topMargin);
        this.bottomMargin = XmlUtils.getAsInt(element, "bottom-margin", this.bottomMargin);
        this.leftMargin = XmlUtils.getAsInt(element, "left-margin", this.leftMargin);
        this.rightMargin = XmlUtils.getAsInt(element, "right-margin", this.rightMargin);
        this.hoverInfo = XmlUtils.getAsString(element, "hover-info", null);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    @OnlyIn(Dist.CLIENT)
    public final LayoutPageWidget createWidgets(LayoutPageWidget currentPage) {
        if (this.topMargin > 0) {
            currentPage = currentPage.addOffsetSpace(this.topMargin);
        }
        LayoutPageWidget currentPage2 = addWidgets(currentPage);
        if (this.bottomMargin > 0) {
            currentPage2 = currentPage2.addOffsetSpace(this.bottomMargin);
        }
        return currentPage2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int width(LayoutPageWidget currentPage) {
        return (currentPage.getPageWidth() - this.leftMargin) - this.rightMargin;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Widget wrapper(Widget widget) {
        if (this.leftMargin != 0 || this.rightMargin != 0) {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(widget);
            widget.setSelfPosition(new Position(this.leftMargin, 0));
            group.setSize(new Size(widget.getSize().width + this.leftMargin + this.rightMargin, widget.getSize().height));
            return group;
        }
        return widget;
    }
}
