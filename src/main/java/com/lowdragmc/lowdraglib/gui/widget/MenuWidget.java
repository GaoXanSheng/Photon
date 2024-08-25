package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.TreeNode;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/MenuWidget.class */
public class MenuWidget<K, T> extends WidgetGroup {
    public static IGuiTexture NODE_TEXTURE = new IGuiTexture() { // from class: com.lowdragmc.lowdraglib.gui.widget.MenuWidget.1
        @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
        @OnlyIn(Dist.CLIENT)
        public void draw(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
            ColorPattern.BLACK.rectTexture().draw(stack, mouseX, mouseY, x, y, width, height);
            Icons.RIGHT.draw(stack, mouseX, mouseY, ((x + width) - height) + 3.0f, y + 3.0f, height - 6, height - 6);
        }
    };
    public static IGuiTexture LEAF_TEXTURE = ColorPattern.BLACK.rectTexture();
    public static IGuiTexture NODE_HOVER_TEXTURE = ColorPattern.T_GRAY.rectTexture();
    public static IGuiTexture BACKGROUND = new GuiTextureGroup(new ColorRectTexture(-12828346), ColorPattern.GRAY.borderTexture(1));
    protected final TreeNode<K, T> root;
    protected final int nodeHeight;
    @Nullable
    protected IGuiTexture nodeTexture;
    @Nullable
    protected IGuiTexture leafTexture;
    @Nullable
    protected IGuiTexture nodeHoverTexture;
    @Nullable
    protected Consumer<TreeNode<K, T>> onNodeClicked;
    @Nullable
    protected Function<K, IGuiTexture> keyIconSupplier;
    @Nullable
    protected Function<K, String> keyNameSupplier;
    @Nullable
    protected Predicate<K> crossLinePredicate;
    protected boolean autoClose;
    protected Map<TreeNode<K, T>, WidgetGroup> children;
    protected MenuWidget<K, T> opened;

    public MenuWidget<K, T> setNodeTexture(@Nullable IGuiTexture nodeTexture) {
        this.nodeTexture = nodeTexture;
        return this;
    }

    public MenuWidget<K, T> setLeafTexture(@Nullable IGuiTexture leafTexture) {
        this.leafTexture = leafTexture;
        return this;
    }

    public MenuWidget<K, T> setNodeHoverTexture(@Nullable IGuiTexture nodeHoverTexture) {
        this.nodeHoverTexture = nodeHoverTexture;
        return this;
    }

    public MenuWidget<K, T> setOnNodeClicked(@Nullable Consumer<TreeNode<K, T>> onNodeClicked) {
        this.onNodeClicked = onNodeClicked;
        return this;
    }

    public MenuWidget<K, T> setKeyIconSupplier(@Nullable Function<K, IGuiTexture> keyIconSupplier) {
        this.keyIconSupplier = keyIconSupplier;
        return this;
    }

    public MenuWidget<K, T> setKeyNameSupplier(@Nullable Function<K, String> keyNameSupplier) {
        this.keyNameSupplier = keyNameSupplier;
        return this;
    }

    public MenuWidget<K, T> setCrossLinePredicate(@Nullable Predicate<K> crossLinePredicate) {
        this.crossLinePredicate = crossLinePredicate;
        return this;
    }

    public MenuWidget<K, T> setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    public MenuWidget(int xPosition, int yPosition, int nodeHeight, TreeNode<K, T> root) {
        super(xPosition, yPosition, 100, nodeHeight);
        this.root = root;
        this.autoClose = true;
        this.nodeHeight = nodeHeight;
        this.children = new LinkedHashMap();
    }

    public void close() {
        if (this.parent != null) {
            this.parent.waitToRemoved(this);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        int maxWidth = getSize().width;
        int maxHeight = 1;
        if (!this.root.isLeaf()) {
            if (isRemote()) {
                for (TreeNode<K, T> child : this.root.getChildren()) {
                    K key = child.getKey();
                    String name = key.toString();
                    if (this.keyNameSupplier != null) {
                        name = this.keyNameSupplier.apply(key);
                    }
                    maxWidth = Math.max(Minecraft.m_91087_().f_91062_.m_92895_(LocalizationUtils.format(name, new Object[0])) + 4 + (2 * this.nodeHeight), maxWidth);
                }
            }
            for (TreeNode<K, T> child2 : this.root.getChildren()) {
                K key2 = child2.getKey();
                if (this.crossLinePredicate != null && this.crossLinePredicate.test(key2)) {
                    maxHeight++;
                } else {
                    String name2 = key2.toString();
                    if (this.keyNameSupplier != null) {
                        name2 = this.keyNameSupplier.apply(key2);
                    }
                    WidgetGroup group = new WidgetGroup(0, maxHeight, maxWidth, this.nodeHeight);
                    this.children.put(child2, group);
                    if (child2.isLeaf()) {
                        group.setBackground((IGuiTexture) Objects.requireNonNullElseGet(this.leafTexture, () -> {
                            return LEAF_TEXTURE;
                        }));
                        group.addWidget(new ButtonWidget(0, 0, maxWidth, this.nodeHeight, null, cd -> {
                            if (this.onNodeClicked != null) {
                                this.onNodeClicked.accept(child);
                            }
                            if (this.autoClose) {
                                WidgetGroup widgetGroup = this;
                                while (true) {
                                    WidgetGroup p = widgetGroup;
                                    if (p != null) {
                                        if (p.parent != null && !(p.parent instanceof MenuWidget)) {
                                            p.parent.waitToRemoved(p);
                                            return;
                                        }
                                        widgetGroup = p.parent;
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }).setHoverTexture((IGuiTexture) Objects.requireNonNullElseGet(this.nodeHoverTexture, () -> {
                            return NODE_HOVER_TEXTURE;
                        })));
                    } else {
                        group.setBackground((IGuiTexture) Objects.requireNonNullElseGet(this.nodeTexture, () -> {
                            return NODE_TEXTURE;
                        }));
                        group.addWidget(new ButtonWidget(0, 0, maxWidth, this.nodeHeight, null).setHoverTexture((IGuiTexture) Objects.requireNonNullElseGet(this.nodeHoverTexture, () -> {
                            return NODE_HOVER_TEXTURE;
                        })));
                    }
                    if (this.keyIconSupplier != null) {
                        group.addWidget(new ImageWidget(2, 1, this.nodeHeight - 2, this.nodeHeight - 2, this.keyIconSupplier.apply(child2.getKey())));
                    }
                    group.addWidget(new ImageWidget(this.nodeHeight + 2, 0, (maxWidth - (2 * this.nodeHeight)) - 4, this.nodeHeight, new TextTexture(name2).setType(TextTexture.TextType.LEFT)));
                    addWidget(group);
                    maxHeight += this.nodeHeight;
                }
            }
        }
        Position pos = getPosition();
        setSize(new Size(maxWidth, maxHeight));
        int rightSpace = getGui().getScreenWidth() - pos.getX();
        int bottomSpace = getGui().getScreenHeight() - pos.getY();
        if (rightSpace < maxWidth) {
            WidgetGroup widgetGroup = this.parent;
            if (widgetGroup instanceof MenuWidget) {
                MenuWidget<?, ?> menuWidget = (MenuWidget) widgetGroup;
                addSelfPosition((-menuWidget.getSize().width) - maxWidth, 0);
            }
            int rightSpace2 = getGui().getScreenWidth() - getPosition().getX();
            if (rightSpace2 < maxWidth) {
                addSelfPosition(-(maxWidth - rightSpace2), 0);
            }
            int leftSpace = getPosition().getX();
            if (leftSpace < 0) {
                addSelfPosition(-leftSpace, 0);
            }
        }
        if (bottomSpace < maxHeight) {
            if (this.parent instanceof MenuWidget) {
                addSelfPosition(0, this.nodeHeight - maxHeight);
            }
            int bottomSpace2 = getGui().getScreenHeight() - getPosition().getY();
            if (bottomSpace2 < maxHeight) {
                addSelfPosition(0, -(maxHeight - bottomSpace2));
            }
            int topSpace = getPosition().getY();
            if (topSpace < 0) {
                addSelfPosition(0, -topSpace);
            }
        }
        super.initWidget();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            if (this.autoClose && !(this.parent instanceof MenuWidget)) {
                close();
                return false;
            }
            return false;
        }
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseMoved(double mouseX, double mouseY) {
        if (super.mouseMoved(mouseX, mouseY)) {
            return true;
        }
        if (this.root.getChildren() != null) {
            int maxHeight = 0;
            for (TreeNode<K, T> node : this.root.getChildren()) {
                if (this.crossLinePredicate != null && this.crossLinePredicate.test(node.getKey())) {
                    maxHeight++;
                } else {
                    WidgetGroup widget = this.children.get(node);
                    if (widget.isMouseOverElement(mouseX, mouseY)) {
                        if (this.opened == null || this.opened.root != node) {
                            if (this.opened != null) {
                                removeWidget(this.opened);
                                this.opened = null;
                            }
                            if (!node.isLeaf()) {
                                this.opened = new MenuWidget(getSize().width, maxHeight, this.nodeHeight, node).setNodeHoverTexture(this.nodeHoverTexture).setNodeTexture(this.nodeTexture).setLeafTexture(this.leafTexture).setOnNodeClicked(this.onNodeClicked).setKeyIconSupplier(this.keyIconSupplier).setKeyNameSupplier(this.keyNameSupplier).setCrossLinePredicate(this.crossLinePredicate);
                                addWidget(this.opened.setBackground(this.backgroundTexture));
                                return true;
                            }
                            return true;
                        }
                        return true;
                    }
                    maxHeight += this.nodeHeight;
                }
            }
            return false;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        Position pos = getPosition();
        Size size = getSize();
        int screenHeight = getGui().getScreenHeight();
        if (screenHeight < size.height && isMouseOverElement(mouseX, mouseY)) {
            int offsetY = Mth.m_14045_(pos.getY() + (wheelDelta > 0.0d ? -this.nodeHeight : this.nodeHeight), 0, screenHeight - size.height);
            addSelfPosition(0, offsetY - pos.getY());
            return true;
        }
        return super.mouseWheelMove(mouseX, mouseY, wheelDelta);
    }
}
