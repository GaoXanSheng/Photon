package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.utils.Size;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/GuiTextureConfigurator.class */
public class GuiTextureConfigurator extends ValueConfigurator<IGuiTexture> {
    protected ImageWidget preview;
    protected Consumer<ClickData> onPressCallback;
    protected Predicate<IGuiTexture> available;

    public void setOnPressCallback(Consumer<ClickData> onPressCallback) {
        this.onPressCallback = onPressCallback;
    }

    public void setAvailable(Predicate<IGuiTexture> available) {
        this.available = available;
    }

    public GuiTextureConfigurator(String name, Supplier<IGuiTexture> supplier, Consumer<IGuiTexture> onUpdate, boolean forceUpdate) {
        super(name, supplier, onUpdate, IGuiTexture.EMPTY, forceUpdate);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(IGuiTexture newValue) {
        if (Objects.equals(newValue, this.value)) {
            return;
        }
        super.onValueUpdate((GuiTextureConfigurator) newValue);
        this.preview.setImage(newValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void computeHeight() {
        super.computeHeight();
        setSize(new Size(getSize().width, 15 + this.preview.getSize().height + 4));
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && Editor.INSTANCE != null && this.preview.isMouseOverElement(mouseX, mouseY)) {
            TreeBuilder.Menu menu = TreeBuilder.Menu.start().leaf(Icons.DELETE, "ldlib.gui.editor.menu.remove", () -> {
                onValueUpdate(IGuiTexture.EMPTY);
                updateValue();
            }).leaf(Icons.COPY, "ldlib.gui.editor.menu.copy", () -> {
                Editor.INSTANCE.setCopy("texture", this.value);
            });
            if ("texture".equals(Editor.INSTANCE.getCopyType())) {
                menu.leaf(Icons.PASTE, "ldlib.gui.editor.menu.paste", () -> {
                    Editor.INSTANCE.ifCopiedPresent("texture", c -> {
                        if (c instanceof IGuiTexture) {
                            IGuiTexture texture = (IGuiTexture) c;
                            onValueUpdate(texture);
                            updateValue();
                        }
                    });
                });
            }
            Editor.INSTANCE.openMenu(mouseX, mouseY, menu);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        int w = Math.min(width - 6, 50);
        int x = (width - w) / 2;
        ImageWidget border = new ImageWidget(x, 17, w, w, (IGuiTexture) this.value).setBorder(2, ColorPattern.T_WHITE.color);
        this.preview = border;
        addWidget(border);
        this.preview.setDraggingConsumer(o -> {
            if (this.available == null) {
                return (o instanceof IGuiTexture) || (o instanceof Integer) || (o instanceof String);
            }
            if (o instanceof IGuiTexture) {
                IGuiTexture texture = (IGuiTexture) o;
                if (this.available.test(texture)) {
                    return true;
                }
            }
            return false;
        }, o2 -> {
            this.preview.setBorder(2, ColorPattern.GREEN.color);
        }, o3 -> {
            this.preview.setBorder(2, ColorPattern.T_WHITE.color);
        }, o4 -> {
            IGuiTexture newTexture = null;
            if (this.available != null && (o4 instanceof IGuiTexture)) {
                IGuiTexture texture = (IGuiTexture) o4;
                if (this.available.test(texture)) {
                    newTexture = texture;
                    if (newTexture != null) {
                        onValueUpdate(newTexture);
                        updateValue();
                    }
                    this.preview.setBorder(2, ColorPattern.T_WHITE.color);
                }
            }
            if (o4 instanceof IGuiTexture) {
                newTexture = (IGuiTexture) o4;
            } else if (o4 instanceof Integer) {
                Integer color = (Integer) o4;
                newTexture = new ColorRectTexture(color.intValue());
            } else if (o4 instanceof String) {
                String string = (String) o4;
                newTexture = new TextTexture(string);
            }
            if (newTexture != null) {
            }
            this.preview.setBorder(2, ColorPattern.T_WHITE.color);
        });
        if (this.onPressCallback != null) {
            addWidget(new ButtonWidget(x, 17, w, w, IGuiTexture.EMPTY, this.onPressCallback));
        }
    }
}
