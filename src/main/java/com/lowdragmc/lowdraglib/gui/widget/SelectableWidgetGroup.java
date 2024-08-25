package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/SelectableWidgetGroup.class */
public class SelectableWidgetGroup extends WidgetGroup implements DraggableScrollableWidgetGroup.ISelected {
    protected boolean isSelected;
    protected IGuiTexture selectedTexture;
    protected Consumer<SelectableWidgetGroup> onSelected;
    protected Consumer<SelectableWidgetGroup> onUnSelected;
    private Object prefab;

    public void setPrefab(Object prefab) {
        this.prefab = prefab;
    }

    public SelectableWidgetGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public SelectableWidgetGroup(Position position) {
        super(position);
    }

    public SelectableWidgetGroup(Position position, Size size) {
        super(position, size);
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public SelectableWidgetGroup setOnSelected(Consumer<SelectableWidgetGroup> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public SelectableWidgetGroup setOnUnSelected(Consumer<SelectableWidgetGroup> onUnSelected) {
        this.onUnSelected = onUnSelected;
        return this;
    }

    public SelectableWidgetGroup setSelectedTexture(IGuiTexture selectedTexture) {
        this.selectedTexture = selectedTexture;
        return this;
    }

    public SelectableWidgetGroup setSelectedTexture(int border, int color) {
        this.selectedTexture = new ColorBorderTexture(border, color);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        if (this.isSelected && this.selectedTexture != null) {
            this.selectedTexture.draw(matrixStack, mouseX, mouseY, getPosition().x, getPosition().y, getSize().width, getSize().height);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.ISelected
    public boolean allowSelected(double mouseX, double mouseY, int button) {
        return isMouseOverElement(mouseX, mouseY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.ISelected
    public void onSelected() {
        this.isSelected = true;
        if (this.onSelected != null) {
            this.onSelected.accept(this);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.ISelected
    public void onUnSelected() {
        this.isSelected = false;
        if (this.onUnSelected != null) {
            this.onUnSelected.accept(this);
        }
    }

    public <T> T getPrefab() {
        return (T) this.prefab;
    }
}
