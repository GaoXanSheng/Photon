package com.lowdragmc.lowdraglib.gui.compass;

import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/LayoutPageWidget.class */
public class LayoutPageWidget extends DraggableScrollableWidgetGroup {
    protected int pageWidth;
    protected int offset;

    public int getPageWidth() {
        return this.pageWidth;
    }

    public LayoutPageWidget(int width, int height) {
        super(4, 4, width - 8, height - 4);
        this.offset = 0;
        setClientSideWidget();
        this.pageWidth = width - 8;
    }

    public LayoutPageWidget addOffsetSpace(int offset) {
        return addStreamWidget(new Widget(0, 0, this.pageWidth, offset));
    }

    public LayoutPageWidget addStreamWidget(Widget widget) {
        widget.setSelfPosition(new Position((this.pageWidth - widget.getSize().width) / 2, this.offset));
        addWidget(widget);
        this.offset += widget.getSize().height;
        return this;
    }
}
