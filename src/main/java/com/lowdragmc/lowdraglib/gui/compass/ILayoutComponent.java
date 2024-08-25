package com.lowdragmc.lowdraglib.gui.compass;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/ILayoutComponent.class */
public interface ILayoutComponent {
    ILayoutComponent fromXml(Element element);

    @OnlyIn(Dist.CLIENT)
    default LayoutPageWidget createWidgets(LayoutPageWidget currentPage) {
        return currentPage;
    }
}
