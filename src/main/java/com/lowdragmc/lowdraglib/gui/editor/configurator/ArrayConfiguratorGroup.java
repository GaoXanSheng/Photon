package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.WidgetTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/ArrayConfiguratorGroup.class */
public class ArrayConfiguratorGroup<T> extends ConfiguratorGroup {
    protected final Supplier<List<T>> source;
    protected final BiFunction<Supplier<T>, Consumer<T>, Configurator> configuratorProvider;
    protected Supplier<T> addDefault;
    protected Consumer<List<T>> onUpdate;
    protected Consumer<T> onAdd;
    protected Consumer<T> onRemove;
    protected BiConsumer<Integer, T> onReorder;
    protected boolean canAdd;
    protected boolean canRemove;
    protected boolean forceUpdate;
    protected boolean addMask;
    protected ArrayConfiguratorGroup<T>.ItemConfigurator removeMask;

    public void setAddDefault(Supplier<T> addDefault) {
        this.addDefault = addDefault;
    }

    public void setOnUpdate(Consumer<List<T>> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setOnAdd(Consumer<T> onAdd) {
        this.onAdd = onAdd;
    }

    public void setOnRemove(Consumer<T> onRemove) {
        this.onRemove = onRemove;
    }

    public void setOnReorder(BiConsumer<Integer, T> onReorder) {
        this.onReorder = onReorder;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public ArrayConfiguratorGroup(String name, boolean isCollapse, Supplier<List<T>> source, BiFunction<Supplier<T>, Consumer<T>, Configurator> configuratorProvider, boolean forceUpdate) {
        super(name, isCollapse);
        this.canAdd = true;
        this.canRemove = true;
        this.configuratorProvider = configuratorProvider;
        this.source = source;
        this.forceUpdate = forceUpdate;
        for (T object : source.get()) {
            addConfigurators(new ItemConfigurator(object, configuratorProvider));
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void updateScreen() {
        super.updateScreen();
        boolean rebuild = false;
        List<T> newSource = this.source.get();
        if (newSource.size() == this.configurators.size()) {
            int i = 0;
            while (true) {
                if (i >= newSource.size()) {
                    break;
                }
                ArrayConfiguratorGroup<T>.ItemConfigurator itemConfigurator = (ItemConfigurator) this.configurators.get(i);
                if (itemConfigurator.object.equals(newSource.get(i))) {
                    i++;
                } else {
                    rebuild = true;
                    break;
                }
            }
        } else {
            rebuild = true;
        }
        if (rebuild) {
            this.configurators.forEach((v1) -> {
                removeWidget(v1);
            });
            this.configurators.clear();
            for (T object : this.source.get()) {
                addConfigurators(new ItemConfigurator(object, this.configuratorProvider));
            }
            computeLayout();
            this.addMask = false;
            this.removeMask = null;
            return;
        }
        if (this.addMask) {
            this.addMask = false;
            if (this.addDefault != null && this.canAdd) {
                T newItem = this.addDefault.get();
                if (this.onAdd != null) {
                    this.onAdd.accept(newItem);
                }
                addConfigurators(new ItemConfigurator(newItem, this.configuratorProvider));
                notifyListUpdate();
                computeLayout();
            }
        }
        if (this.removeMask != null) {
            if (this.canRemove) {
                if (this.onRemove != null) {
                    this.onRemove.accept(this.removeMask.object);
                }
                this.configurators.remove(this.removeMask);
                removeWidget(this.removeMask);
            }
            this.removeMask = null;
            notifyListUpdate();
            computeLayout();
        }
    }

    public void notifyListUpdate() {
        if (this.onUpdate != null) {
            this.onUpdate.accept(this.configurators.stream().map(c -> {
                return ((ItemConfigurator) c).object;
            }).toList());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup
    @Deprecated
    public void addConfigurators(Configurator... configurators) {
        super.addConfigurators(configurators);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        if (this.addDefault != null) {
            addWidget(new ButtonWidget(width - (this.tips.length > 0 ? 24 : 12), 2, 9, 9, Icons.ADD, cd -> {
                this.addMask = true;
            }).setHoverTooltips("ldlib.gui.editor.tips.add_item"));
        }
    }

    public void updateOrder(ArrayConfiguratorGroup<T>.ItemConfigurator src, ArrayConfiguratorGroup<T>.ItemConfigurator dst, boolean before) {
        if (this.configurators.remove(src)) {
            removeWidget(src);
            int index = this.configurators.indexOf(dst);
            if (!before) {
                index++;
            }
            this.configurators.add(index, src);
            addWidget(index, src);
            if (this.onReorder != null) {
                this.onReorder.accept(Integer.valueOf(index), src.object);
            }
        }
        notifyListUpdate();
        computeLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/ArrayConfiguratorGroup$ItemConfigurator.class */
    public class ItemConfigurator extends Configurator {
        T object;
        Configurator inner;

        public ItemConfigurator(T object, BiFunction<Supplier<T>, Consumer<T>, Configurator> provider) {
            this.object = object;
            this.inner = provider.apply(this::getter, this::setter);
            addWidget(this.inner);
            addWidget(new ButtonWidget(2, 2, 9, 9, Icons.REMOVE, cd -> {
                ArrayConfiguratorGroup.this.removeMask = this;
            }).setHoverTooltips("ldlib.gui.editor.tips.remove_item"));
        }

        private void setter(T t) {
            this.object = t;
            ArrayConfiguratorGroup.this.notifyListUpdate();
        }

        private T getter() {
            return this.object;
        }

        @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
        public void setConfigPanel(ConfigPanel configPanel, ConfigPanel.Tab tab) {
            super.setConfigPanel(configPanel, tab);
            this.inner.setConfigPanel(configPanel, tab);
        }

        @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
        public void setSelfPosition(Position selfPosition) {
            super.setSelfPosition(selfPosition);
        }

        @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
        public void computeHeight() {
            this.inner.computeHeight();
            this.inner.setSelfPosition(new Position(13, 0));
            int height = this.inner.getSize().height;
            setSize(new Size(getSize().width, height));
        }

        @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
        public void init(int width) {
            super.init(width);
            this.inner.init((width - 10) - 15);
            ImageWidget imageWidget = new ImageWidget(width - 12, 2, 9, 9, new ColorRectTexture(-1).setRadius(4.5f));
            imageWidget.setHoverTooltips("ldlib.gui.editor.tips.drag_item");
            if (ArrayConfiguratorGroup.this.onReorder != null) {
                setDraggingProvider(() -> {
                    return this;
                }, t, p -> {
                    return new GuiTextureGroup(new WidgetTexture(p.x, p.y, this.inner), new WidgetTexture(p.x, p.y, imageWidget));
                });
                imageWidget.setDraggingProvider(() -> {
                    return this;
                }, t2, p2 -> {
                    return new GuiTextureGroup(new WidgetTexture(p2.x, p2.y, this.inner), new WidgetTexture(p2.x, p2.y, imageWidget));
                });
            }
            addWidget(imageWidget);
        }

        @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
        @OnlyIn(Dist.CLIENT)
        public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
            if (isMouseOverElement(mouseX, mouseY) && ArrayConfiguratorGroup.this.onReorder != null) {
                Object object = getGui().getModularUIGui().getDraggingElement();
                Position pos = getPosition();
                Size size = getSize();
                if (object != null && object.getClass() == getClass() && object != this) {
                    if (mouseY > pos.y + (size.height / 2)) {
                        ColorPattern.T_GREEN.rectTexture().draw(poseStack, 0, 0, pos.x, pos.y + size.height, size.width, 2);
                    } else {
                        ColorPattern.T_GREEN.rectTexture().draw(poseStack, 0, 0, pos.x, pos.y - 1, size.width, 2);
                    }
                }
            }
        }

        @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
        @OnlyIn(Dist.CLIENT)
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (isMouseOverElement(mouseX, mouseY) && ArrayConfiguratorGroup.this.onReorder != null) {
                Object object = getGui().getModularUIGui().getDraggingElement();
                Position pos = getPosition();
                Size size = getSize();
                if (object != null && object.getClass() == getClass() && object != this) {
                    ArrayConfiguratorGroup.this.updateOrder((ItemConfigurator) object, this, mouseY < ((double) (((float) pos.y) + (((float) size.height) / 2.0f))));
                    return true;
                }
            }
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }
}
