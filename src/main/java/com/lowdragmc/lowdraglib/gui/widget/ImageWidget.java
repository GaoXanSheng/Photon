package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "image", group = "widget.basic")
@Configurable(name = "ldlib.gui.editor.register.widget.image", collapse = false)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/ImageWidget.class */
public class ImageWidget extends Widget implements IConfigurableWidget {
    @Configurable(name = "ldlib.gui.editor.name.border")
    @NumberRange(range = {-100.0d, 100.0d})
    private int border;
    @NumberColor
    @Configurable(name = "ldlib.gui.editor.name.border_color")
    private int borderColor;
    private Supplier<IGuiTexture> textureSupplier;

    public ImageWidget() {
        this(0, 0, 50, 50, new ResourceTexture());
    }

    public ImageWidget(int xPosition, int yPosition, int width, int height, IGuiTexture area) {
        super(xPosition, yPosition, width, height);
        this.borderColor = -1;
        setImage(area);
    }

    public ImageWidget(int xPosition, int yPosition, int width, int height, Supplier<IGuiTexture> textureSupplier) {
        super(xPosition, yPosition, width, height);
        this.borderColor = -1;
        setImage(textureSupplier);
    }

    public ImageWidget setImage(IGuiTexture area) {
        setBackground(area);
        return this;
    }

    public ImageWidget setImage(Supplier<IGuiTexture> textureSupplier) {
        this.textureSupplier = textureSupplier;
        if (textureSupplier != null) {
            setBackground(textureSupplier.get());
        }
        return this;
    }

    public IGuiTexture getImage() {
        return this.backgroundTexture;
    }

    public ImageWidget setBorder(int border, int color) {
        this.border = border;
        this.borderColor = color;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        super.updateScreen();
        if (this.textureSupplier != null) {
            setBackground(this.textureSupplier.get());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        Position position = getPosition();
        Size size = getSize();
        if (this.border > 0) {
            DrawerHelper.drawBorder(matrixStack, position.x, position.y, size.width, size.height, this.borderColor, this.border);
        }
    }
}
