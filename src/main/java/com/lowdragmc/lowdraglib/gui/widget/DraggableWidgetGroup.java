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

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/DraggableWidgetGroup.class */
public class DraggableWidgetGroup extends WidgetGroup implements DraggableScrollableWidgetGroup.IDraggable {
    protected boolean isSelected;
    protected IGuiTexture selectedTexture;
    protected Consumer<DraggableWidgetGroup> onSelected;
    protected Consumer<DraggableWidgetGroup> onUnSelected;
    protected Consumer<DraggableWidgetGroup> onStartDrag;
    protected Consumer<DraggableWidgetGroup> onDragging;
    protected Consumer<DraggableWidgetGroup> onEndDrag;

    public DraggableWidgetGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public DraggableWidgetGroup(Position position) {
        super(position);
    }

    public DraggableWidgetGroup(Position position, Size size) {
        super(position, size);
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public DraggableWidgetGroup setOnSelected(Consumer<DraggableWidgetGroup> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public DraggableWidgetGroup setOnUnSelected(Consumer<DraggableWidgetGroup> onUnSelected) {
        this.onUnSelected = onUnSelected;
        return this;
    }

    public DraggableWidgetGroup setOnStartDrag(Consumer<DraggableWidgetGroup> onStartDrag) {
        this.onStartDrag = onStartDrag;
        return this;
    }

    public DraggableWidgetGroup setOnDragging(Consumer<DraggableWidgetGroup> onDragging) {
        this.onDragging = onDragging;
        return this;
    }

    public DraggableWidgetGroup setOnEndDrag(Consumer<DraggableWidgetGroup> onEndDrag) {
        this.onEndDrag = onEndDrag;
        return this;
    }

    public DraggableWidgetGroup setSelectedTexture(IGuiTexture selectedTexture) {
        this.selectedTexture = selectedTexture;
        return this;
    }

    public DraggableWidgetGroup setSelectedTexture(int border, int color) {
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

    @Override // com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.IDraggable
    public void startDrag(double mouseX, double mouseY) {
        if (this.onStartDrag != null) {
            this.onStartDrag.accept(this);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.IDraggable
    public boolean dragging(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (this.onDragging != null) {
            this.onDragging.accept(this);
            return true;
        }
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.IDraggable
    public void endDrag(double mouseX, double mouseY) {
        if (this.onEndDrag != null) {
            this.onEndDrag.accept(this);
        }
    }
}
