package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/WrapperConfigurator.class */
public class WrapperConfigurator extends Configurator {
    public final Widget inner;

    public WrapperConfigurator(String name, Widget widget) {
        super(name);
        this.inner = widget;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void computeHeight() {
        super.computeHeight();
        setSize(new Size(getSize().width, this.inner.getSize().height + 19));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        Size size = this.inner.getSize();
        this.inner.setSelfPosition(new Position((width - size.width) / 2, 17));
        addWidget(this.inner);
    }
}
