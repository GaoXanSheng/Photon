package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.HsbColorWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.utils.Position;
import expr.Expr;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/ColorConfigurator.class */
public class ColorConfigurator extends ValueConfigurator<Number> {
    protected ImageWidget image;
    protected boolean colorBackground;

    public void setColorBackground(boolean colorBackground) {
        this.colorBackground = colorBackground;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ColorConfigurator(String name, Supplier<Number> supplier, Consumer<Number> onUpdate, @Nonnull Number defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        if (this.value == 0) {
            this.value = defaultValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(Number newValue) {
        if (newValue == null) {
            newValue = (Number) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((ColorConfigurator) newValue);
        this.image.setImage(getCommonColor());
    }

    private IGuiTexture getCommonColor() {
        return new ColorRectTexture(((Number) this.value).intValue()).setRadius(5.0f).setRadius(5.0f);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && Editor.INSTANCE != null && this.image.isMouseOverElement(mouseX, mouseY)) {
            TreeBuilder.Menu menu = TreeBuilder.Menu.start().leaf(Icons.COPY, "ldlib.gui.editor.menu.copy", () -> {
                Editor.INSTANCE.setCopy("number", this.value);
            });
            if ("number".equals(Editor.INSTANCE.getCopyType())) {
                menu.leaf(Icons.PASTE, "ldlib.gui.editor.menu.paste", () -> {
                    Editor.INSTANCE.ifCopiedPresent("number", c -> {
                        if (c instanceof Number) {
                            Number number = (Number) c;
                            onValueUpdate((Number) Integer.valueOf(number.intValue()));
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
        ImageWidget imageWidget = new ImageWidget(this.leftWidth, 2, ((width - this.leftWidth) - 3) - this.rightWidth, 10, getCommonColor());
        this.image = imageWidget;
        addWidget(imageWidget);
        this.image.setDraggingConsumer(o -> {
            return o instanceof Number;
        }, o2 -> {
            this.image.setImage(ColorPattern.GREEN.rectTexture().setRadius(5.0f));
        }, o3 -> {
            this.image.setImage(getCommonColor());
        }, o4 -> {
            if (o4 instanceof Number) {
                Number number = (Number) o4;
                onValueUpdate((Number) Integer.valueOf(number.intValue()));
                updateValue();
            }
        });
        addWidget(new ButtonWidget(this.leftWidth, 2, ((width - this.leftWidth) - 3) - this.rightWidth, 10, null, cd -> {
            if (Editor.INSTANCE != null) {
                Position position = this.image.getPosition();
                int rightPlace = getGui().getScreenWidth() - Expr.ROUND;
                DialogWidget dialog = Editor.INSTANCE.openDialog(new DialogWidget(Math.min(position.x, rightPlace), position.y - Expr.ROUND, Expr.ROUND, Expr.ROUND));
                dialog.setClickClose(true);
                dialog.addWidget(new HsbColorWidget(5, 5, 100, 100).setOnChanged(newColor -> {
                    this.value = Integer.valueOf(newColor);
                    updateValue();
                    this.image.setImage(getCommonColor());
                }).setColorSupplier(() -> {
                    return ((Number) this.value).intValue();
                }).setColor(((Number) this.value).intValue())).setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
            }
        }));
    }
}
