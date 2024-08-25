package com.lowdragmc.lowdraglib.gui.compass;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/CompassSelectorWidget.class */
public class CompassSelectorWidget extends WidgetGroup {
    private final List<CompassNode> nodes;

    public CompassSelectorWidget(List<CompassNode> nodes) {
        super(0, 0, 210, 100);
        setClientSideWidget();
        this.nodes = nodes;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        super.initWidget();
        setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_GRAY.borderTexture(1)));
        DraggableScrollableWidgetGroup listGroup = new DraggableScrollableWidgetGroup(4, 4, 202, 92).setYScrollBarWidth(2).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1.0f)).setBackground(ColorPattern.T_GRAY.rectTexture().setRadius(1.0f));
        addWidget(listGroup);
        Map<ResourceLocation, List<CompassNode>> map = (Map) this.nodes.stream().collect(Collectors.groupingBy(n -> {
            return n.getSection().getSectionName();
        }));
        int y = 3;
        for (Map.Entry<ResourceLocation, List<CompassNode>> entry : map.entrySet()) {
            listGroup.addWidget(new LabelWidget(2, y, entry.getKey().m_214296_("compass.section")));
            y += 12;
            int x = 0;
            for (CompassNode node : entry.getValue()) {
                IGuiTexture background = node.getBackground();
                IGuiTexture hoverBackground = node.getHoverBackground();
                IGuiTexture buttonTexture = node.getButtonTexture();
                ICompassUIConfig config = CompassManager.INSTANCE.getUIConfig(node.getSection().getSectionName().m_135827_());
                if (background == null) {
                    background = config.getNodeBackground();
                }
                if (hoverBackground == null) {
                    hoverBackground = config.getNodeHoverBackground();
                }
                listGroup.addWidget(new ButtonWidget(x + 2, y + 2, 20, 20, new GuiTextureGroup(background, new GuiTextureGroup(buttonTexture).scale(0.8f)), cd -> {
                    CompassManager.INSTANCE.openCompass(node);
                }).setHoverTexture(new GuiTextureGroup(hoverBackground, new GuiTextureGroup(buttonTexture).scale(0.8f))).setHoverTooltips(node.getNodeName().m_214296_("compass.node")));
                x += 25;
                if (x > 175) {
                    x = 0;
                    y += 25;
                }
            }
            if (x != 0) {
                y += 25;
            }
        }
    }
}
