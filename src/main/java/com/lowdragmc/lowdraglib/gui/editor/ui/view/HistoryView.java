package com.lowdragmc.lowdraglib.gui.editor.ui.view;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/view/HistoryView.class */
public class HistoryView extends FloatViewWidget {
    public HistoryView() {
        super(100, 100, 120, 120, false);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ui.view.FloatViewWidget, com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        super.initWidget();
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ui.view.FloatViewWidget
    public IGuiTexture getIcon() {
        return Icons.HISTORY.copy();
    }
}
