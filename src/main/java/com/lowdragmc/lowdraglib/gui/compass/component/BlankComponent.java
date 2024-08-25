package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/BlankComponent.class */
public class BlankComponent implements ILayoutComponent {
    protected int height = 10;

    @Override // com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        this.height = XmlUtils.getAsInt(element, "height", this.height);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public LayoutPageWidget createWidgets(LayoutPageWidget currentPage) {
        currentPage.addOffsetSpace(this.height);
        return currentPage;
    }
}
