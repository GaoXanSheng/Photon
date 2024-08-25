package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/ConfiguratorGroup.class */
public class ConfiguratorGroup extends Configurator {
    protected boolean canCollapse;
    protected boolean isCollapse;
    protected List<Configurator> configurators;

    public void setCanCollapse(boolean canCollapse) {
        this.canCollapse = canCollapse;
    }

    public boolean isCollapse() {
        return this.isCollapse;
    }

    public List<Configurator> getConfigurators() {
        return this.configurators;
    }

    public ConfiguratorGroup(String name) {
        this(name, true);
    }

    public ConfiguratorGroup(String name, boolean isCollapse) {
        super(name);
        this.canCollapse = true;
        this.configurators = new ArrayList();
        this.isCollapse = isCollapse;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void setConfigPanel(ConfigPanel configPanel, ConfigPanel.Tab tab) {
        super.setConfigPanel(configPanel, tab);
        for (Configurator configurator : this.configurators) {
            configurator.setConfigPanel(configPanel, tab);
        }
    }

    protected void clickName(ClickData clickData) {
        if (this.canCollapse) {
            setCollapse(!this.isCollapse);
        }
    }

    public void setCollapse(boolean collapse) {
        this.isCollapse = collapse;
        for (Configurator configurator : this.configurators) {
            configurator.setActive(!this.isCollapse);
            configurator.setVisible(!this.isCollapse);
        }
        computeLayout();
    }

    public void addConfigurator(int index, Configurator configurator) {
        configurator.setConfigPanel(this.configPanel, this.tab);
        configurator.setActive(!this.isCollapse);
        configurator.setVisible(!this.isCollapse);
        this.configurators.add(index, configurator);
        addWidget(index, configurator);
        if (isInit()) {
            configurator.init(Math.max(0, this.width - 5));
            computeLayout();
        }
    }

    public void removeConfigurator(Configurator configurator) {
        if (this.configurators.remove(configurator)) {
            removeWidget(configurator);
        }
    }

    public void removeAllConfigurators() {
        for (Configurator configurator : this.configurators) {
            removeWidget(configurator);
        }
        this.configurators.clear();
    }

    public void addConfigurators(Configurator... configurators) {
        for (Configurator configurator : configurators) {
            configurator.setConfigPanel(this.configPanel, this.tab);
            configurator.setActive(!this.isCollapse);
            configurator.setVisible(!this.isCollapse);
            this.configurators.add(configurator);
            addWidget(configurator);
        }
        if (isInit()) {
            for (Configurator configurator2 : configurators) {
                configurator2.init(Math.max(0, this.width - 5));
            }
            computeLayout();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void computeHeight() {
        int height = 15;
        if (!this.isCollapse) {
            for (Configurator configurator : this.configurators) {
                configurator.computeHeight();
                configurator.setSelfPosition(new Position(5, height));
                height += configurator.getSize().height;
            }
        }
        setSize(new Size(getSize().width, height));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        addWidget(new ButtonWidget(0, 0, this.leftWidth + 9, 15, IGuiTexture.EMPTY, this::clickName));
        for (Configurator configurator : this.configurators) {
            configurator.init(Math.max(0, width - 5));
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Position pos = getPosition();
        Size size = getSize();
        if (this.isCollapse) {
            Icons.RIGHT.setColor(-1).draw(poseStack, mouseX, mouseY, pos.x + this.leftWidth, pos.y + 3, 9, 9);
        } else {
            Icons.DOWN.setColor(-1).draw(poseStack, mouseX, mouseY, pos.x + this.leftWidth, pos.y + 3, 9, 9);
            if (this.configurators.size() > 0) {
                DrawerHelper.drawSolidRect(poseStack, pos.x + 2, pos.y + 17, 1, size.height - 19, ColorPattern.T_WHITE.color);
            }
        }
        super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
    }
}
