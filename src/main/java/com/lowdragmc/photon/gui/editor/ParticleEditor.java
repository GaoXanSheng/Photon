package com.lowdragmc.photon.gui.editor;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.editor.ui.MenuPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.ToolPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.menu.MenuTab;
import com.lowdragmc.lowdraglib.gui.editor.ui.menu.ViewMenu;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.fx.EditorEffect;
import com.lowdragmc.photon.client.fx.IEffect;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "editor.particle", group = "editor")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/ParticleEditor.class */
public class ParticleEditor extends Editor {
    @OnlyIn(Dist.CLIENT)
    protected ParticleScene particleScene;
    @OnlyIn(Dist.CLIENT)
    protected IEffect effect;
    @Nullable
    protected EmittersList emittersList;
    private boolean draggable;
    private boolean dragAll;
    private boolean renderCullBox;

    @Nullable
    public EmittersList getEmittersList() {
        return this.emittersList;
    }

    public boolean isDraggable() {
        return this.draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isDragAll() {
        return this.dragAll;
    }

    public void setDragAll(boolean dragAll) {
        this.dragAll = dragAll;
    }

    public boolean isRenderCullBox() {
        return this.renderCullBox;
    }

    public void setRenderCullBox(boolean renderCullBox) {
        this.renderCullBox = renderCullBox;
    }

    public ParticleEditor(File workSpace) {
        super(workSpace);
        this.draggable = false;
        this.dragAll = false;
        this.renderCullBox = false;
    }

    @OnlyIn(Dist.CLIENT)
    public ParticleScene getParticleScene() {
        return this.particleScene;
    }

    @OnlyIn(Dist.CLIENT)
    public IEffect getEditorFX() {
        return this.effect;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ui.Editor
    @OnlyIn(Dist.CLIENT)
    public void initEditorViews() {
        ParticleScene particleScene = new ParticleScene(this);
        this.particleScene = particleScene;
        addWidget(particleScene);
        ToolPanel toolPanel = new ToolPanel(this);
        this.toolPanel = toolPanel;
        addWidget(toolPanel);
        ConfigPanel configPanel = new ConfigPanel(this);
        this.configPanel = configPanel;
        addWidget(configPanel);
        ResourcePanel resourcePanel = new ResourcePanel(this);
        this.resourcePanel = resourcePanel;
        addWidget(resourcePanel);
        MenuPanel menuPanel = new MenuPanel(this);
        this.menuPanel = menuPanel;
        addWidget(menuPanel);
        WidgetGroup widgetGroup = new WidgetGroup(0, 0, getSize().width, getSize().height);
        this.floatView = widgetGroup;
        addWidget(widgetGroup);
        MenuTab menuTab = this.menuPanel.getTabs().get("view");
        if (menuTab instanceof ViewMenu) {
            ViewMenu viewMenu = (ViewMenu) menuTab;
            viewMenu.openView(new ParticleInfoView());
        }
        this.effect = new EditorEffect(this);
        this.particleScene.resetScene();
    }

    public void restartScene() {
        IProject iProject = this.currentProject;
        if (iProject instanceof ParticleProject) {
            ParticleProject particleProject = (ParticleProject) iProject;
            this.particleScene.getParticleManager().clearAllParticles();
            for (IParticleEmitter emitter : particleProject.getEmitters()) {
                Vector3 pos = emitter.self().getPos();
                Vector3 rotation = emitter.self().getRotation(0.0f);
                emitter.reset();
                emitter.emmitToLevel(getEditorFX(), this.particleScene.level, pos.x, pos.y, pos.z, rotation.x, rotation.y, rotation.z);
            }
        }
    }

    public void openEmitterConfigurator(IParticleEmitter emitter) {
        getConfigPanel().openConfigurator(ConfigPanel.Tab.WIDGET, emitter);
    }

    public void clearEmitterConfigurator() {
        getConfigPanel().clearAllConfigurators(ConfigPanel.Tab.WIDGET);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ui.Editor
    public void loadProject(IProject project) {
        if (this.currentProject != null) {
            this.currentProject.onClosed(this);
        }
        clearEmitterConfigurator();
        this.toolPanel.clearAllWidgets();
        if (project instanceof ParticleProject) {
            ParticleProject particleProject = (ParticleProject) project;
            this.currentProject = particleProject;
            this.currentProject.onLoad(this);
            this.particleScene.getParticleManager().clearAllParticles();
            ToolPanel toolPanel = this.toolPanel;
            ResourceTexture resourceTexture = Icons.WIDGET_CUSTOM;
            EmittersList emittersList = new EmittersList(this, particleProject);
            this.emittersList = emittersList;
            toolPanel.addNewToolBox("ldlib.gui.editor.group.particles", resourceTexture, emittersList);
        }
    }
}
