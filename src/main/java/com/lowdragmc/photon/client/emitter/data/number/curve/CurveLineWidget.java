package com.lowdragmc.photon.client.emitter.data.number.curve;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.MenuWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.utils.curve.ExplicitCubicBezierCurve2;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/curve/CurveLineWidget.class */
public class CurveLineWidget extends WidgetGroup {
    public final ECBCurves curves;
    protected boolean lockControlPoint;
    protected int selectedPoint;
    protected boolean renderGrid;
    protected Size gridSize;
    protected Consumer<ECBCurves> onUpdate;
    protected Function<Vec2, Component> hoverTips;
    private boolean isDraggingPoint;
    private boolean isDraggingLeftControlPoint;
    private boolean isDraggingRightControlPoint;
    private long lastClickTick;

    public void setLockControlPoint(boolean lockControlPoint) {
        this.lockControlPoint = lockControlPoint;
    }

    public boolean isLockControlPoint() {
        return this.lockControlPoint;
    }

    public void setSelectedPoint(int selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public int getSelectedPoint() {
        return this.selectedPoint;
    }

    public void setRenderGrid(boolean renderGrid) {
        this.renderGrid = renderGrid;
    }

    public void setGridSize(Size gridSize) {
        this.gridSize = gridSize;
    }

    public void setOnUpdate(Consumer<ECBCurves> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setHoverTips(Function<Vec2, Component> hoverTips) {
        this.hoverTips = hoverTips;
    }

    public CurveLineWidget(int x, int y, int width, int height, ECBCurves curves) {
        super(x, y, width, height);
        this.lockControlPoint = true;
        this.selectedPoint = -1;
        this.renderGrid = true;
        this.gridSize = new Size(2, 2);
        this.curves = curves;
    }

    public Vec2 getPointPosition(Vec2 coord) {
        Size size = getSize();
        Position position = getPosition();
        return new Vec2(position.x + (size.width * coord.f_82470_), position.y + (size.height * (1.0f - coord.f_82471_)));
    }

    public Vec2 getPointCoordinate(Vec2 pos) {
        Size size = getSize();
        Position position = getPosition();
        return new Vec2((pos.f_82470_ - position.x) / size.width, 1.0f - ((pos.f_82471_ - position.y) / size.height));
    }

    public Vec2 getPointCoordinate(int index) {
        if (index < this.curves.size()) {
            return this.curves.get(index).p0;
        }
        if (index > 0) {
            return this.curves.get(index - 1).p1;
        }
        return null;
    }

    public void setPointCoordinate(int index, Vec2 coord) {
        if (index < this.curves.size()) {
            Vec2 offset = new Vec2(coord.f_82470_ - this.curves.get(index).p0.f_82470_, coord.f_82471_ - this.curves.get(index).p0.f_82471_);
            this.curves.get(index).p0 = coord;
            this.curves.get(index).c0 = this.curves.get(index).c0.m_165910_(offset);
        }
        if (index > 0) {
            Vec2 offset2 = new Vec2(coord.f_82470_ - this.curves.get(index - 1).p1.f_82470_, coord.f_82471_ - this.curves.get(index - 1).p1.f_82471_);
            this.curves.get(index - 1).p1 = coord;
            this.curves.get(index - 1).c1 = this.curves.get(index - 1).c1.m_165910_(offset2);
        }
    }

    public void setLeftControlPointCoordinate(int index, Vec2 coord) {
        if (index > 0) {
            this.curves.get(index - 1).c1 = coord;
        }
    }

    public void setRightControlPointCoordinate(int index, Vec2 coord) {
        if (index < this.curves.size()) {
            this.curves.get(index).c0 = coord;
        }
    }

    protected void notifyChanged() {
        if (this.onUpdate != null) {
            this.onUpdate.accept(this.curves);
        }
    }

    protected void openMenu(int mouseX, int mouseY) {
        TreeBuilder.Menu menu = TreeBuilder.Menu.start().leaf(this.lockControlPoint ? Icons.CHECK : IGuiTexture.EMPTY, "Lock Controll Points", () -> {
            this.lockControlPoint = !this.lockControlPoint;
        }).branch("Grid", m -> {
            m.leaf("2×2", () -> {
                setGridSize(new Size(2, 2));
            }).leaf("6×2", () -> {
                setGridSize(new Size(6, 2));
            }).leaf("6×4", () -> {
                setGridSize(new Size(6, 4));
            });
        });
        if (this.selectedPoint != -1 && this.curves.size() > 1) {
            menu.leaf("Remove", () -> {
                if (this.selectedPoint == 0) {
                    this.curves.remove(0);
                } else if (this.selectedPoint > 0 && this.selectedPoint < this.curves.size()) {
                    this.curves.get(this.selectedPoint - 1).p1 = this.curves.get(this.selectedPoint).p1;
                    this.curves.get(this.selectedPoint - 1).c1 = this.curves.get(this.selectedPoint).c0;
                    this.curves.remove(this.selectedPoint);
                } else if (this.selectedPoint >= this.curves.size()) {
                    this.curves.remove(this.curves.size() - 1);
                }
                this.selectedPoint = -1;
                notifyChanged();
            });
            MenuWidget<Tuple<IGuiTexture, String>, Runnable> widget = new MenuWidget(mouseX - getPosition().x, mouseY - getPosition().y, 14, menu.build()).setNodeTexture(new IGuiTexture() { // from class: com.lowdragmc.photon.client.emitter.data.number.curve.CurveLineWidget.1
                @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
                @OnlyIn(Dist.CLIENT)
                public void draw(PoseStack stack, int mouseX2, int mouseY2, float x, float y, int width, int height) {
                    ColorPattern.BLACK.rectTexture().draw(stack, mouseX2, mouseY2, x, y, width, height);
                    Icons.RIGHT.draw(stack, mouseX2, mouseY2, ((x + width) - height) + 3.0f, y + 3.0f, height - 6, height - 6);
                }
            }).setLeafTexture(ColorPattern.BLACK.rectTexture()).setNodeHoverTexture(ColorPattern.T_GRAY.rectTexture()).setCrossLinePredicate(TreeBuilder.Menu::isCrossLine).setKeyIconSupplier(TreeBuilder.Menu::getIcon).setKeyNameSupplier(TreeBuilder.Menu::getName).setOnNodeClicked(TreeBuilder.Menu::handle);
            waitToAdded(widget.setBackground(new ColorRectTexture(-12828346), ColorPattern.GRAY.borderTexture(1)));
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (isMouseOver(getPosition().x - 2, getPosition().y - 2, getSize().width + 4, getSize().height + 4, mouseX, mouseY)) {
            if (button == 1) {
                openMenu((int) mouseX, (int) mouseY);
                return false;
            }
            long clickTick = this.gui.getTickCount();
            for (int i = 0; i < this.curves.size(); i++) {
                ExplicitCubicBezierCurve2 curve = this.curves.get(i);
                if (i == 0) {
                    Vec2 pos = getPointPosition(curve.p0);
                    if (isMouseOver((int) (pos.f_82470_ - 2.0f), (int) (pos.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                        this.selectedPoint = 0;
                        this.isDraggingPoint = true;
                        return true;
                    }
                }
                Vec2 pos2 = getPointPosition(curve.p1);
                if (isMouseOver((int) (pos2.f_82470_ - 2.0f), (int) (pos2.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                    this.selectedPoint = i + 1;
                    this.isDraggingPoint = true;
                    return true;
                }
            }
            if (this.selectedPoint >= 0) {
                if (this.selectedPoint > 0) {
                    Vec2 pos3 = getPointPosition(this.curves.get(this.selectedPoint - 1).c1);
                    if (isMouseOver((int) (pos3.f_82470_ - 2.0f), (int) (pos3.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                        this.isDraggingLeftControlPoint = true;
                        return true;
                    }
                }
                if (this.selectedPoint < this.curves.size()) {
                    Vec2 pos4 = getPointPosition(this.curves.get(this.selectedPoint).c0);
                    if (isMouseOver((int) (pos4.f_82470_ - 2.0f), (int) (pos4.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                        this.isDraggingRightControlPoint = true;
                        return true;
                    }
                }
            }
            if (clickTick - this.lastClickTick < 12 && isMouseOverElement(mouseX, mouseY)) {
                this.lastClickTick = 0L;
                float x = (float) ((mouseX - getPosition().x) / getSize().width);
                float y = this.curves.get(0).p0.f_82471_;
                boolean found = x < this.curves.get(0).p0.f_82470_;
                int index = 0;
                if (!found) {
                    Iterator<ExplicitCubicBezierCurve2> it = this.curves.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ExplicitCubicBezierCurve2 curve2 = it.next();
                        index++;
                        if (x >= curve2.p0.f_82470_ && x <= curve2.p1.f_82470_) {
                            y = curve2.getPoint((x - curve2.p0.f_82470_) / (curve2.p1.f_82470_ - curve2.p0.f_82470_)).f_82471_;
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    index++;
                    y = this.curves.get(this.curves.size() - 1).p1.f_82471_;
                }
                Vec2 position = getPointPosition(new Vec2(x, y));
                if (isMouseOver((int) (position.f_82470_ - 2.0f), (int) (position.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                    if (index == 0) {
                        Vec2 right = this.curves.get(0).p0;
                        Vec2 rightCP = this.curves.get(index).c0;
                        this.curves.add(0, new ExplicitCubicBezierCurve2(new Vec2(x, y), new Vec2(x + 0.1f, y), new Vec2(right.f_82470_ + (right.f_82470_ - rightCP.f_82470_), right.f_82471_ + (right.f_82471_ - rightCP.f_82471_)), right));
                    } else if (index > this.curves.size()) {
                        Vec2 left = this.curves.get(this.curves.size() - 1).p1;
                        Vec2 leftCP = this.curves.get(this.curves.size() - 1).c1;
                        this.curves.add(new ExplicitCubicBezierCurve2(left, new Vec2(left.f_82470_ + (left.f_82470_ - leftCP.f_82470_), left.f_82471_ + (left.f_82471_ - leftCP.f_82471_)), new Vec2(x - 0.1f, y), new Vec2(x, y)));
                    } else {
                        ExplicitCubicBezierCurve2 curve3 = this.curves.get(index - 1);
                        this.curves.add(index, new ExplicitCubicBezierCurve2(new Vec2(x, y), new Vec2(x + 0.1f, y), curve3.c1, curve3.p1));
                        curve3.c1 = new Vec2(x - 0.1f, y);
                        curve3.p1 = new Vec2(x, y);
                    }
                    notifyChanged();
                    return false;
                }
                return false;
            }
            this.lastClickTick = clickTick;
            return false;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDraggingPoint = false;
        this.isDraggingLeftControlPoint = false;
        this.isDraggingRightControlPoint = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        Size size = getSize();
        Position position = getPosition();
        if (this.selectedPoint >= 0) {
            if (this.isDraggingPoint) {
                float minX = position.x;
                float maxX = position.x + size.width;
                if (this.selectedPoint > 0) {
                    minX = Math.max(minX, getPointPosition(getPointCoordinate(this.selectedPoint - 1)).f_82470_);
                }
                if (this.selectedPoint < this.curves.size()) {
                    maxX = Math.min(maxX, getPointPosition(getPointCoordinate(this.selectedPoint + 1)).f_82470_);
                }
                setPointCoordinate(this.selectedPoint, getPointCoordinate(new Vec2((float) Mth.m_14008_(mouseX, minX, maxX), (float) Mth.m_14008_(mouseY, position.y, position.y + size.height))));
                notifyChanged();
            }
            Vec2 point = getPointCoordinate(this.selectedPoint);
            Vec2 pointPos = getPointPosition(point);
            if (this.isDraggingLeftControlPoint) {
                Vec2 coord = getPointCoordinate(new Vec2((float) Mth.m_14008_(mouseX, position.x, pointPos.f_82470_), (float) mouseY));
                setLeftControlPointCoordinate(this.selectedPoint, coord);
                if (this.lockControlPoint) {
                    setRightControlPointCoordinate(this.selectedPoint, new Vec2(point.f_82470_ + (point.f_82470_ - coord.f_82470_), point.f_82471_ + (point.f_82471_ - coord.f_82471_)));
                }
                notifyChanged();
            }
            if (this.isDraggingRightControlPoint) {
                Vec2 coord2 = getPointCoordinate(new Vec2((float) Mth.m_14008_(mouseX, pointPos.f_82470_, position.x + size.width), (float) mouseY));
                setRightControlPointCoordinate(this.selectedPoint, coord2);
                if (this.lockControlPoint) {
                    setLeftControlPointCoordinate(this.selectedPoint, new Vec2(point.f_82470_ + (point.f_82470_ - coord2.f_82470_), point.f_82471_ + (point.f_82471_ - coord2.f_82471_)));
                }
                notifyChanged();
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.backgroundTexture != null) {
            Position pos = getPosition();
            Size size = getSize();
            this.backgroundTexture.draw(poseStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
        if (this.hoverTexture != null && isMouseOverElement(mouseX, mouseY)) {
            Position pos2 = getPosition();
            Size size2 = getSize();
            this.hoverTexture.draw(poseStack, mouseX, mouseY, pos2.x, pos2.y, size2.width, size2.height);
        }
        if (this.renderGrid) {
            Position pos3 = getPosition();
            Size size3 = getSize();
            for (int i = 0; i < this.gridSize.width; i++) {
                DrawerHelper.drawSolidRect(poseStack, pos3.x + ((i * getSize().width) / this.gridSize.width), pos3.y, 1, size3.height, ColorPattern.T_GRAY.color);
            }
            for (int i2 = 0; i2 < this.gridSize.height; i2++) {
                DrawerHelper.drawSolidRect(poseStack, pos3.x, pos3.y + ((i2 * getSize().height) / this.gridSize.height), size3.width, 1, ColorPattern.T_GRAY.color);
            }
        }
        List<Vec2> points = (List) this.curves.stream().flatMap(curve -> {
            return curve.getPoints(100).stream().map(this::getPointPosition).toList().stream();
        }).collect(Collectors.toList());
        DrawerHelper.drawLines(poseStack, points, -1, -1, 0.5f);
        Collections.reverse(points);
        DrawerHelper.drawLines(poseStack, points, -1, -1, 0.5f);
        if (this.curves.get(0).p0.f_82470_ > 0.0f) {
            DrawerHelper.drawLines(poseStack, List.of(getPointPosition(new Vec2(0.0f, this.curves.get(0).p0.f_82471_)), getPointPosition(this.curves.get(0).p0)), ColorPattern.T_RED.color, ColorPattern.T_RED.color, 0.3f);
        }
        if (this.curves.get(this.curves.size() - 1).p1.f_82470_ < 1.0f) {
            DrawerHelper.drawLines(poseStack, List.of(getPointPosition(new Vec2(1.0f, this.curves.get(this.curves.size() - 1).p1.f_82471_)), getPointPosition(this.curves.get(this.curves.size() - 1).p1)), ColorPattern.T_RED.color, ColorPattern.T_RED.color, 0.3f);
        }
        if (this.selectedPoint >= 0) {
            if (this.selectedPoint > 0) {
                ExplicitCubicBezierCurve2 curve2 = this.curves.get(this.selectedPoint - 1);
                DrawerHelper.drawLines(poseStack, List.of(getPointPosition(curve2.c1), getPointPosition(curve2.p1)), ColorPattern.T_GREEN.color, ColorPattern.T_GREEN.color, 0.3f);
                renderControlPoint(curve2.c1, poseStack);
            }
            if (this.selectedPoint < this.curves.size()) {
                ExplicitCubicBezierCurve2 curve3 = this.curves.get(this.selectedPoint);
                DrawerHelper.drawLines(poseStack, List.of(getPointPosition(curve3.c0), getPointPosition(curve3.p0)), ColorPattern.T_GREEN.color, ColorPattern.T_GREEN.color, 0.3f);
                renderControlPoint(curve3.c0, poseStack);
            }
        }
        for (int i3 = 0; i3 < this.curves.size(); i3++) {
            ExplicitCubicBezierCurve2 curve4 = this.curves.get(i3);
            if (i3 == 0) {
                renderPoint(curve4.p0, this.selectedPoint == 0, poseStack, mouseX, mouseY);
            }
            renderPoint(curve4.p1, this.selectedPoint == i3 + 1, poseStack, mouseX, mouseY);
        }
        for (Widget widget : this.widgets) {
            if (widget.isVisible()) {
                RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.m_69478_();
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInBackground(poseStack, mouseX, mouseY, partialTicks);
                } else {
                    widget.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
                }
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(poseStack, mouseX, mouseY, partialTicks);
        if (this.gui != null && this.gui.getModularUIGui() != null && this.hoverTips != null) {
            for (int i = 0; i < this.curves.size(); i++) {
                ExplicitCubicBezierCurve2 curve = this.curves.get(i);
                if (i == 0) {
                    Vec2 position = getPointPosition(curve.p0);
                    if (isMouseOver((int) (position.f_82470_ - 2.0f), (int) (position.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                        this.gui.getModularUIGui().setHoverTooltip(List.of(this.hoverTips.apply(curve.p0)), ItemStack.f_41583_, null, null);
                        return;
                    }
                }
                Vec2 position2 = getPointPosition(curve.p1);
                if (isMouseOver((int) (position2.f_82470_ - 2.0f), (int) (position2.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
                    this.gui.getModularUIGui().setHoverTooltip(List.of(this.hoverTips.apply(curve.p0)), ItemStack.f_41583_, null, null);
                    return;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderPoint(Vec2 point, boolean isSelected, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
        Vec2 position = getPointPosition(point);
        if (isSelected) {
            ColorPattern.RED.rectTexture().setRadius(2.0f).draw(poseStack, mouseX, mouseY, position.f_82470_ - 2.0f, position.f_82471_ - 2.0f, 4, 4);
            return;
        }
        ColorPattern.GRAY.rectTexture().setRadius(2.0f).draw(poseStack, mouseX, mouseY, position.f_82470_ - 2.0f, position.f_82471_ - 2.0f, 4, 4);
        if (isMouseOver((int) (position.f_82470_ - 2.0f), (int) (position.f_82471_ - 2.0f), 4, 4, mouseX, mouseY)) {
            ColorPattern.WHITE.borderTexture(1).setRadius(2.0f).draw(poseStack, mouseX, mouseY, position.f_82470_ - 2.0f, position.f_82471_ - 2.0f, 4, 4);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderControlPoint(Vec2 point, @NotNull PoseStack poseStack) {
        Vec2 position = getPointPosition(point);
        ColorPattern.GREEN.rectTexture().setRadius(1.0f).draw(poseStack, 0, 0, position.f_82470_ - 1.0f, position.f_82471_ - 1.0f, 2, 2);
    }
}
