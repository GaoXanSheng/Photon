package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.TexturesResource;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.UIResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/MainPanel.class */
public class MainPanel extends WidgetGroup {
    public static final String COPY_TYPE = "widgets";
    protected final Editor editor;
    protected final WidgetGroup root;
    private final Set<UIWrapper> selectedUIs;
    protected UIWrapper hoverUI;
    private double lastDeltaX;
    private double lastDeltaY;
    private boolean isDragPosition;
    private boolean isDragSize;

    public Editor getEditor() {
        return this.editor;
    }

    public WidgetGroup getRoot() {
        return this.root;
    }

    public Set<UIWrapper> getSelectedUIs() {
        return this.selectedUIs;
    }

    public UIWrapper getHoverUI() {
        return this.hoverUI;
    }

    public MainPanel(Editor editor, WidgetGroup root) {
        super(0, 0, editor.getSize().width, editor.getSize().height);
        this.selectedUIs = new HashSet();
        this.editor = editor;
        this.root = root;
        addWidget(root);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseMoved(double mouseX, double mouseY) {
        Widget hovered = getHoverElement(mouseX, mouseY);
        if (hovered instanceof IConfigurableWidget) {
            IConfigurableWidget configurableWidget = (IConfigurableWidget) hovered;
            if (hovered != this) {
                if (this.hoverUI == null || !this.hoverUI.is(configurableWidget)) {
                    this.hoverUI = new UIWrapper(this, configurableWidget);
                }
                return super.mouseMoved(mouseX, mouseY);
            }
        }
        this.hoverUI = null;
        return super.mouseMoved(mouseX, mouseY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hoverUI == null) {
            this.selectedUIs.clear();
        } else if (isCtrlDown()) {
            if (this.selectedUIs.contains(this.hoverUI)) {
                this.selectedUIs.remove(this.hoverUI);
            } else {
                this.selectedUIs.add(this.hoverUI);
            }
        } else if (!this.selectedUIs.contains(this.hoverUI)) {
            this.selectedUIs.clear();
            this.selectedUIs.add(this.hoverUI);
        }
        this.lastDeltaX = 0.0d;
        this.lastDeltaY = 0.0d;
        this.isDragPosition = false;
        this.isDragSize = false;
        if (!this.selectedUIs.isEmpty()) {
            if (button == 0 && this.hoverUI != null) {
                this.editor.configPanel.openConfigurator(ConfigPanel.Tab.WIDGET, this.hoverUI);
            }
            if (isAltDown()) {
                if (button == 0) {
                    this.isDragPosition = true;
                    return true;
                } else if (button == 1) {
                    this.isDragSize = true;
                    return true;
                } else {
                    return true;
                }
            } else if (isShiftDown()) {
                UIWrapper[] uiWrappers = (UIWrapper[]) this.selectedUIs.toArray(x$0 -> {
                    return new UIWrapper[x$0];
                });
                getGui().getModularUIGui().setDraggingElement(uiWrappers, new GuiTextureGroup((IGuiTexture[]) this.selectedUIs.stream().map(w -> {
                    return w.toDraggingTexture((int) mouseX, (int) mouseY);
                }).toArray(x$02 -> {
                    return new IGuiTexture[x$02];
                })));
                return true;
            }
        }
        if (button == 1) {
            this.editor.openMenu(mouseX, mouseY, createMenu());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void removeSelected() {
        for (UIWrapper selectedUI : this.selectedUIs) {
            selectedUI.remove();
        }
        this.hoverUI = null;
        this.selectedUIs.clear();
    }

    protected TreeBuilder.Menu createMenu() {
        return TreeBuilder.Menu.start().leaf(Icons.DELETE, "ldlib.gui.editor.menu.remove", this::removeSelected).leaf(Icons.COPY, "ldlib.gui.editor.menu.copy", this::copy).leaf(Icons.CUT, "ldlib.gui.editor.menu.cut", this::cut).leaf(Icons.PASTE, "ldlib.gui.editor.menu.paste", this::paste).crossLine().branch("ldlib.gui.editor.menu.align", menu -> {
            menu.leaf(Icons.ALIGN_H_C, "ldlib.gui.editor.menu.align.hc", this::alignHC).leaf(Icons.ALIGN_H_D, "ldlib.gui.editor.menu.align.hd", this::alignHD).leaf(Icons.ALIGN_H_L, "ldlib.gui.editor.menu.align.hl", this::alignHL).leaf(Icons.ALIGN_H_R, "ldlib.gui.editor.menu.align.hr", this::alignHR).leaf(Icons.ALIGN_V_C, "ldlib.gui.editor.menu.align.vc", this::alignVC).leaf(Icons.ALIGN_V_D, "ldlib.gui.editor.menu.align.vd", this::alignVD).leaf(Icons.ALIGN_V_T, "ldlib.gui.editor.menu.align.vt", this::alignVT).leaf(Icons.ALIGN_V_B, "ldlib.gui.editor.menu.align.vb", this::alignVB);
        });
    }

    private void cut() {
        copy();
        if (!this.selectedUIs.isEmpty()) {
            for (UIWrapper selectedUI : this.selectedUIs) {
                selectedUI.inner().widget().getParent().onWidgetRemoved(selectedUI.inner());
            }
            this.selectedUIs.clear();
        }
    }

    protected void copy() {
        if (this.editor.getResourcePanel().resources != null) {
            UIResourceTexture.setCurrentResource(this.editor.getResourcePanel().resources.resources.get(TexturesResource.RESOURCE_NAME), true);
        }
        List<CompoundTag> list = new ArrayList<>();
        if (!this.selectedUIs.isEmpty()) {
            for (UIWrapper selectedUI : this.selectedUIs) {
                list.add(selectedUI.inner().serializeWrapper());
            }
        }
        UIResourceTexture.clearCurrentResource();
        getEditor().setCopy(COPY_TYPE, list);
    }

    protected void paste() {
        if (this.hoverUI != null) {
            getEditor().ifCopiedPresent(COPY_TYPE, c -> {
                if (this.editor.getResourcePanel().resources != null) {
                    UIResourceTexture.setCurrentResource(this.editor.getResourcePanel().resources.resources.get(TexturesResource.RESOURCE_NAME), true);
                }
                List<CompoundTag> list = (List) c;
                for (CompoundTag tag : list) {
                    IConfigurableWidget widget = IConfigurableWidget.deserializeWrapper(tag);
                    if (widget != null) {
                        IConfigurableWidget patt7138$temp = this.hoverUI.inner();
                        if (patt7138$temp instanceof IConfigurableWidgetGroup) {
                            IConfigurableWidgetGroup group = (IConfigurableWidgetGroup) patt7138$temp;
                            widget.widget().addSelfPosition(5, 5);
                            if (group.canWidgetAccepted(widget)) {
                                group.acceptWidget(widget);
                            }
                        }
                    }
                }
                UIResourceTexture.clearCurrentResource();
            });
        }
    }

    protected void alignVB() {
        if (this.selectedUIs.size() > 0) {
            int max = Integer.MIN_VALUE;
            for (UIWrapper ui : this.selectedUIs) {
                max = Math.max(max, ui.inner().widget().getRect().down);
            }
            for (UIWrapper ui2 : this.selectedUIs) {
                ui2.inner().widget().addSelfPosition(0, max - ui2.inner().widget().getRect().down);
            }
        }
    }

    protected void alignVT() {
        if (this.selectedUIs.size() > 0) {
            int min = Integer.MAX_VALUE;
            for (UIWrapper ui : this.selectedUIs) {
                min = Math.min(min, ui.inner().widget().getRect().up);
            }
            for (UIWrapper ui2 : this.selectedUIs) {
                ui2.inner().widget().addSelfPosition(0, min - ui2.inner().widget().getRect().up);
            }
        }
    }

    protected void alignVD() {
        if (this.selectedUIs.size() > 2) {
            List<Widget> uis = this.selectedUIs.stream().map(ui -> {
                return ui.inner().widget();
            }).sorted(Comparator.comparingInt(w -> {
                return w.getRect().getHeightCenter();
            })).toList();
            int min = uis.get(0).getRect().getHeightCenter();
            int max = uis.get(uis.size() - 1).getRect().getHeightCenter();
            for (int i = 0; i < uis.size(); i++) {
                int centerY = min + (((max - min) * i) / (this.selectedUIs.size() - 1));
                Widget ui2 = uis.get(i);
                ui2.addSelfPosition(0, centerY - ui2.getRect().getHeightCenter());
            }
        }
    }

    protected void alignVC() {
        if (this.selectedUIs.size() > 0) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (UIWrapper ui : this.selectedUIs) {
                min = Math.min(min, ui.inner().widget().getRect().up);
                max = Math.max(max, ui.inner().widget().getRect().down);
            }
            int mid = (min + max) / 2;
            for (UIWrapper ui2 : this.selectedUIs) {
                ui2.inner().widget().addSelfPosition(0, mid - ui2.inner().widget().getRect().getHeightCenter());
            }
        }
    }

    protected void alignHR() {
        if (this.selectedUIs.size() > 0) {
            int max = Integer.MIN_VALUE;
            for (UIWrapper ui : this.selectedUIs) {
                max = Math.max(max, ui.inner().widget().getRect().right);
            }
            for (UIWrapper ui2 : this.selectedUIs) {
                ui2.inner().widget().addSelfPosition(max - ui2.inner().widget().getRect().right, 0);
            }
        }
    }

    protected void alignHL() {
        if (this.selectedUIs.size() > 0) {
            int min = Integer.MAX_VALUE;
            for (UIWrapper ui : this.selectedUIs) {
                min = Math.min(min, ui.inner().widget().getRect().left);
            }
            for (UIWrapper ui2 : this.selectedUIs) {
                ui2.inner().widget().addSelfPosition(min - ui2.inner().widget().getRect().left, 0);
            }
        }
    }

    protected void alignHD() {
        if (this.selectedUIs.size() > 2) {
            List<Widget> uis = this.selectedUIs.stream().map(ui -> {
                return ui.inner().widget();
            }).sorted(Comparator.comparingInt(w -> {
                return w.getRect().getWidthCenter();
            })).toList();
            int min = uis.get(0).getRect().getWidthCenter();
            int max = uis.get(uis.size() - 1).getRect().getWidthCenter();
            for (int i = 0; i < uis.size(); i++) {
                int centerX = min + (((max - min) * i) / (this.selectedUIs.size() - 1));
                Widget ui2 = uis.get(i);
                ui2.addSelfPosition(centerX - ui2.getRect().getWidthCenter(), 0);
            }
        }
    }

    protected void alignHC() {
        if (this.selectedUIs.size() > 0) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (UIWrapper ui : this.selectedUIs) {
                min = Math.min(min, ui.inner().widget().getRect().left);
                max = Math.max(max, ui.inner().widget().getRect().right);
            }
            int mid = (min + max) / 2;
            for (UIWrapper ui2 : this.selectedUIs) {
                ui2.inner().widget().addSelfPosition(mid - ui2.inner().widget().getRect().getWidthCenter(), 0);
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (UIWrapper selectedUI : this.selectedUIs) {
            selectedUI.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
            Position pos = selectedUI.inner().widget().getPosition();
            Size size = selectedUI.inner().widget().getSize();
            minX = Math.min(minX, pos.x);
            minY = Math.min(minY, pos.y);
            maxX = Math.max(maxX, pos.x + size.width);
            maxY = Math.max(maxY, pos.y + size.height);
        }
        if (this.hoverUI != null) {
            this.hoverUI.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        }
        if (!this.selectedUIs.isEmpty() && Widget.isAltDown()) {
            Position pos2 = new Position(minX, minY);
            Size size2 = new Size(maxX - minX, maxY - minY);
            float middleX = pos2.x + ((size2.width - 16) / 2.0f);
            float middleY = pos2.y + ((size2.height - 16) / 2.0f);
            if (this.isDragPosition) {
                Icons.UP.copy().setColor(-1).draw(poseStack, mouseX, mouseY, middleX, (pos2.y - 10) - 16, 16, 16);
                Icons.LEFT.copy().setColor(-1).draw(poseStack, mouseX, mouseY, (pos2.x - 10) - 16, middleY, 16, 16);
            }
            if (this.isDragPosition || this.isDragSize) {
                Icons.DOWN.copy().setColor(-1).draw(poseStack, mouseX, mouseY, middleX, pos2.y + size2.height + 10, 16, 16);
                Icons.RIGHT.copy().setColor(-1).draw(poseStack, mouseX, mouseY, pos2.x + size2.width + 10, middleY, 16, 16);
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        double dx = deltaX + this.lastDeltaX;
        double dy = deltaY + this.lastDeltaY;
        double deltaX2 = (int) dx;
        double deltaY2 = (int) dy;
        this.lastDeltaX = dx - deltaX2;
        this.lastDeltaY = dy - deltaY2;
        if (!this.selectedUIs.isEmpty() && isAltDown()) {
            if (this.isDragPosition) {
                for (UIWrapper selectedUI : this.selectedUIs) {
                    selectedUI.onDragPosition((int) deltaX2, (int) deltaY2);
                }
                return true;
            } else if (this.isDragSize) {
                for (UIWrapper selectedUI2 : this.selectedUIs) {
                    selectedUI2.onDragSize((int) deltaX2, (int) deltaY2);
                }
                return true;
            } else {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX2, deltaY2);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.hoverUI != null && this.hoverUI.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
