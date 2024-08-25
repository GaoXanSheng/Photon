package com.lowdragmc.photon.gui.editor;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.ui.view.FloatViewWidget;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.core.mixins.accessor.MinecraftAccessor;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;

@LDLRegister(name = "particle_info", group = "editor.particle")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/ParticleInfoView.class */
public class ParticleInfoView extends FloatViewWidget {
    public ParticleInfoView() {
        super(100, 100, 200, 135, false);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ui.view.FloatViewWidget
    public IGuiTexture getIcon() {
        return Icons.INFORMATION.copy();
    }

    public ParticleEditor getEditor() {
        return (ParticleEditor) this.editor;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ui.view.FloatViewWidget, com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        super.initWidget();
        this.content.setBackground(new GuiTextureGroup(ColorPattern.T_BLACK.rectTexture().setBottomRadius(5.0f), ColorPattern.GRAY.borderTexture(-1).setBottomRadius(5.0f)));
        addButton("photon.gui.editor.particle_info.restart", () -> {
            getEditor().restartScene();
        });
        addInformation("photon.gui.editor.particle_info.particles", () -> {
            IParticleEmitter selected;
            EmittersList list = getEditor().getEmittersList();
            if (list != null && (selected = list.getSelected()) != null) {
                return String.valueOf(selected.getParticleAmount());
            }
            return "0";
        });
        addInformation("photon.gui.editor.particle_info.time", () -> {
            IParticleEmitter selected;
            EmittersList list = getEditor().getEmittersList();
            if (list != null && (selected = list.getSelected()) != null) {
                return "%.2f (s)".formatted(new Object[]{Float.valueOf(selected.self().getAge() / 20.0f)});
            }
            return "0 / 0";
        });
        this.content.addWidget(new ProgressWidget(() -> {
            IParticleEmitter selected;
            EmittersList list = getEditor().getEmittersList();
            if (list != null && (selected = list.getSelected()) != null) {
                return selected.self().getT(Minecraft.m_91087_().m_91296_());
            }
            return 0.0d;
        }, 3, (this.content.widgets.size() * 15) + 3, 194, 10, new ProgressTexture(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f).setRadius(5.0f), ColorPattern.GREEN.rectTexture().setRadius(5.0f).setRadius(5.0f))));
        addInformation("FPS", () -> {
            return MinecraftAccessor.getFps() + " fps";
        });
        addInformation("photon.gui.editor.particle_info.cpu_time", () -> {
            return "%d us".formatted(new Object[]{Long.valueOf(getEditor().getParticleScene().getParticleManager().getCPUTime())});
        });
        WidgetGroup group = addToggle("photon.gui.editor.particle_info.draggable", () -> {
            return getEditor().isDraggable();
        }, draggable -> {
            getEditor().setDraggable(draggable);
        });
        int textWidth = Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format("photon.gui.editor.particle_info.draggable", new Object[0])) + 6;
        group.addWidget(new ButtonWidget(textWidth + (((194 - textWidth) - 70) / 2), 0, 70, 10, new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f).setRadius(5.0f), new TextTexture("photon.gui.editor.particle_info.reset_pos").setWidth(194)), cd -> {
            IParticleEmitter selected;
            EmittersList list = getEditor().getEmittersList();
            if (list != null && (selected = list.getSelected()) != null) {
                selected.self().setPos(new Vector3(0.5d, 3.0d, 0.5d), true);
            }
        }));
        addToggle("photon.gui.editor.particle_info.drag_all", () -> {
            return getEditor().isDragAll();
        }, draggable2 -> {
            getEditor().setDragAll(draggable2);
        });
        addToggle("photon.gui.editor.particle_info.cull_box", () -> {
            return getEditor().isRenderCullBox();
        }, cull -> {
            getEditor().setRenderCullBox(cull);
        });
    }

    protected void addButton(String title, Runnable onClick) {
        int offsetY = this.content.widgets.size() * 15;
        this.content.addWidget(new ButtonWidget(3, offsetY + 3, 194, 10, new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f).setRadius(5.0f), new TextTexture(title).setWidth(194)), cd -> {
            onClick.run();
        }));
    }

    protected WidgetGroup addToggle(String title, BooleanSupplier supplier, BooleanConsumer onClick) {
        int offsetY = this.content.widgets.size() * 15;
        WidgetGroup infoGroup = new WidgetGroup(3, offsetY + 3, 194, 10);
        infoGroup.addWidget(new LabelWidget(0, 0, title));
        int textWidth = Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format(title, new Object[0])) + 6;
        SwitchWidget switchWidget = new SwitchWidget(textWidth, -1, 10, 10, cd, pressed -> {
            onClick.accept(pressed.booleanValue());
        });
        Objects.requireNonNull(supplier);
        infoGroup.addWidget(switchWidget.setSupplier(this::getAsBoolean).setPressed(supplier.getAsBoolean()).setTexture(new ColorBorderTexture(-1, -1).setRadius(5.0f), new GuiTextureGroup(new ColorBorderTexture(-1, -1).setRadius(5.0f), new ColorRectTexture(-1).setRadius(5.0f).scale(0.5f))));
        this.content.addWidget(infoGroup);
        return infoGroup;
    }

    protected WidgetGroup addInformation(String title, Supplier<String> info) {
        int offsetY = this.content.widgets.size() * 15;
        WidgetGroup infoGroup = new WidgetGroup(3, offsetY + 3, 194, 10);
        infoGroup.addWidget(new LabelWidget(0, 0, title));
        int textWidth = Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format(title, new Object[0])) + 6;
        infoGroup.addWidget(new ImageWidget(textWidth, 0, 194 - textWidth, 10, new TextTexture().setWidth(194 - textWidth).setSupplier(info)));
        this.content.addWidget(infoGroup);
        return infoGroup;
    }
}
