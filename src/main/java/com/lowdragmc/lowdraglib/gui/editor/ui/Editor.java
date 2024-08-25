package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.gui.editor.ILDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.util.TreeNode;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.MenuWidget;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/Editor.class */
public abstract class Editor extends WidgetGroup implements ILDLRegister {
    @OnlyIn(Dist.CLIENT)
    public static Editor INSTANCE;
    protected final File workSpace;
    protected IProject currentProject;
    protected MenuPanel menuPanel;
    protected TabContainer tabPages;
    protected ConfigPanel configPanel;
    protected ResourcePanel resourcePanel;
    protected WidgetGroup floatView;
    protected ToolPanel toolPanel;
    protected String copyType;
    protected Object copied;

    public File getWorkSpace() {
        return this.workSpace;
    }

    public IProject getCurrentProject() {
        return this.currentProject;
    }

    public MenuPanel getMenuPanel() {
        return this.menuPanel;
    }

    public TabContainer getTabPages() {
        return this.tabPages;
    }

    public ConfigPanel getConfigPanel() {
        return this.configPanel;
    }

    public ResourcePanel getResourcePanel() {
        return this.resourcePanel;
    }

    public WidgetGroup getFloatView() {
        return this.floatView;
    }

    public ToolPanel getToolPanel() {
        return this.toolPanel;
    }

    public String getCopyType() {
        return this.copyType;
    }

    public Object getCopied() {
        return this.copied;
    }

    public Editor(File workSpace) {
        super(0, 0, 10, 10);
        setClientSideWidget();
        this.workSpace = workSpace;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void setGui(ModularUI gui) {
        super.setGui(gui);
        if (isRemote()) {
            if (gui == null) {
                INSTANCE = null;
                return;
            }
            INSTANCE = this;
            getGui().registerCloseListener(() -> {
                INSTANCE = null;
            });
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void onScreenSizeUpdate(int screenWidth, int screenHeight) {
        setSize(new Size(screenWidth, screenHeight));
        super.onScreenSizeUpdate(screenWidth, screenHeight);
        clearAllWidgets();
        initEditorViews();
        loadProject(this.currentProject);
    }

    public void initEditorViews() {
        TabContainer tabContainer = new TabContainer(0, 0, getSize().width, getSize().height);
        this.tabPages = tabContainer;
        addWidget(tabContainer);
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
    }

    public DialogWidget openDialog(DialogWidget dialog) {
        addWidget(dialog);
        Position pos = dialog.getPosition();
        Size size = dialog.getSize();
        if (pos.x + size.width > getGui().getScreenWidth()) {
            dialog.addSelfPosition((pos.x + size.width) - getGui().getScreenWidth(), 0);
        } else if (pos.x < 0) {
            dialog.addSelfPosition(-pos.x, 0);
        }
        if (pos.y + size.height > getGui().getScreenHeight()) {
            dialog.addSelfPosition(0, (pos.y + size.height) - getGui().getScreenHeight());
        } else if (pos.y < 0) {
            dialog.addSelfPosition(0, -pos.y);
        }
        return dialog;
    }

    public <T, C> MenuWidget<T, C> openMenu(double posX, double posY, TreeNode<T, C> menuNode) {
        MenuWidget<T, C> menu = new MenuWidget((int) posX, (int) posY, 14, menuNode).setNodeTexture(MenuWidget.NODE_TEXTURE).setLeafTexture(MenuWidget.LEAF_TEXTURE).setNodeHoverTexture(MenuWidget.NODE_HOVER_TEXTURE);
        waitToAdded(menu.setBackground(MenuWidget.BACKGROUND));
        return menu;
    }

    public void openMenu(double posX, double posY, TreeBuilder.Menu menuBuilder) {
        if (menuBuilder == null) {
            return;
        }
        openMenu(posX, posY, menuBuilder.build()).setCrossLinePredicate(TreeBuilder.Menu::isCrossLine).setKeyIconSupplier(TreeBuilder.Menu::getIcon).setKeyNameSupplier(TreeBuilder.Menu::getName).setOnNodeClicked(TreeBuilder.Menu::handle);
    }

    public void loadProject(IProject project) {
        if (this.currentProject != null) {
            this.currentProject.onClosed(this);
        }
        this.currentProject = project;
        this.tabPages.clearAllWidgets();
        this.toolPanel.clearAllWidgets();
        if (this.currentProject != null) {
            this.currentProject.onLoad(this);
        }
    }

    public void setCopy(String copyType, Object copied) {
        this.copied = copied;
        this.copyType = copyType;
    }

    public void ifCopiedPresent(String copyType, Consumer<Object> consumer) {
        if (Objects.equals(copyType, this.copyType)) {
            consumer.accept(this.copied);
        }
    }
}
