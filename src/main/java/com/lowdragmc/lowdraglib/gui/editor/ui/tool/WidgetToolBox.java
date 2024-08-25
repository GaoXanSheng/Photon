package com.lowdragmc.lowdraglib.gui.editor.ui.tool;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.WidgetTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SelectableWidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/tool/WidgetToolBox.class */
public class WidgetToolBox extends DraggableScrollableWidgetGroup {

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/tool/WidgetToolBox$IWidgetPanelDragging.class */
    public interface IWidgetPanelDragging extends Supplier<IConfigurableWidget> {
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/tool/WidgetToolBox$Default.class */
    public static class Default {
        public static List<Default> TABS = new ArrayList();
        public static final Default BASIC = registerTab("widget.basic", Icons.WIDGET_BASIC);
        public static final Default GROUP = registerTab("widget.group", Icons.WIDGET_GROUP);
        public static final Default CONTAINER = registerTab("widget.container", Icons.WIDGET_CONTAINER);
        public static final Default CUSTOM = registerTab("widget.custom", Icons.WIDGET_CUSTOM);
        public final String groupName;
        public final ResourceTexture icon;

        private Default(String groupName, ResourceTexture icon) {
            this.groupName = groupName;
            this.icon = icon;
            TABS.add(this);
        }

        public WidgetToolBox createToolBox() {
            return new WidgetToolBox(this.groupName);
        }

        public static Default registerTab(String groupName, ResourceTexture icon) {
            return new Default(groupName, icon);
        }
    }

    public WidgetToolBox(String groupName) {
        super(0, 0, 100, 100);
        int yOffset = 3;
        setYScrollBarWidth(4).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(2.0f).transform(-0.5f, 0.0f));
        for (AnnotationDetector.Wrapper<LDLRegister, IConfigurableWidget> wrapper : AnnotationDetector.REGISTER_WIDGETS) {
            String group = wrapper.annotation().group().isEmpty() ? "widget.basic" : wrapper.annotation().group();
            if (group.equals(groupName)) {
                IConfigurableWidget widget = wrapper.creator().get();
                widget.initTemplate();
                widget.widget().setSelfPosition(new Position(0, 0));
                SelectableWidgetGroup selectableWidgetGroup = new SelectableWidgetGroup(0, yOffset, 98, 64);
                selectableWidgetGroup.addWidget(new ImageWidget(26, 17, 45, 30, new WidgetTexture(widget.widget())));
                selectableWidgetGroup.addWidget(new LabelWidget(3, 3, widget.getTranslateKey()));
                selectableWidgetGroup.setSelectedTexture(ColorPattern.T_GRAY.rectTexture());
                selectableWidgetGroup.setDraggingProvider(() -> {
                    IConfigurableWidget configurableWidget = (IConfigurableWidget) wrapper.creator().get();
                    configurableWidget.initTemplate();
                    return () -> {
                        return configurableWidget;
                    };
                }, w, p -> {
                    return new WidgetTexture(w.get().widget()).setDragging(true);
                });
                addWidget(selectableWidgetGroup);
                yOffset += 67;
            }
        }
    }
}
