package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "border_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ResourceBorderTexture.class */
public class ResourceBorderTexture extends ResourceTexture {
    public static final ResourceBorderTexture BORDERED_BACKGROUND = new ResourceBorderTexture("ldlib:textures/gui/background.png", 16, 16, 4, 4);
    public static final ResourceBorderTexture BORDERED_BACKGROUND_INVERSE = new ResourceBorderTexture("ldlib:textures/gui/background_inverse.png", 16, 16, 4, 4);
    public static final ResourceBorderTexture BORDERED_BACKGROUND_BLUE = new ResourceBorderTexture("ldlib:textures/gui/bordered_background_blue.png", 195, 136, 4, 4);
    public static final ResourceBorderTexture BUTTON_COMMON = new ResourceBorderTexture("ldlib:textures/gui/button.png", 32, 32, 2, 2);
    public static final ResourceBorderTexture BAR = new ResourceBorderTexture("ldlib:textures/gui/button_common.png", 180, 20, 1, 1);
    public static final ResourceBorderTexture SELECTED = new ResourceBorderTexture("ldlib:textures/gui/selected.png", 16, 16, 2, 2);
    @Configurable(tips = {"ldlib.gui.editor.tips.corner_size.0", "ldlib.gui.editor.tips.corner_size.1"}, collapse = false)
    public Size boderSize;
    @Configurable(tips = {"ldlib.gui.editor.tips.image_size"}, collapse = false)
    public Size imageSize;

    public ResourceBorderTexture() {
        this("ldlib:textures/gui/bordered_background_blue.png", 195, 136, 4, 4);
    }

    public ResourceBorderTexture(String imageLocation, int imageWidth, int imageHeight, int cornerWidth, int cornerHeight) {
        super(imageLocation);
        this.boderSize = new Size(cornerWidth, cornerHeight);
        this.imageSize = new Size(imageWidth, imageHeight);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.ResourceTexture
    public ResourceTexture copy() {
        return new ResourceBorderTexture(this.imageLocation.toString(), this.imageSize.width, this.imageSize.height, this.boderSize.width, this.boderSize.height);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.ResourceTexture, com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public ResourceBorderTexture setColor(int color) {
        super.setColor(color);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.texture.ResourceTexture, com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    public void drawSubAreaInternal(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        float cornerWidth = (this.boderSize.width * 1.0f) / this.imageSize.width;
        float cornerHeight = (this.boderSize.height * 1.0f) / this.imageSize.height;
        super.drawSubAreaInternal(stack, x, y, this.boderSize.width, this.boderSize.height, 0.0f, 0.0f, cornerWidth, cornerHeight);
        super.drawSubAreaInternal(stack, (x + width) - this.boderSize.width, y, this.boderSize.width, this.boderSize.height, 1.0f - cornerWidth, 0.0f, cornerWidth, cornerHeight);
        super.drawSubAreaInternal(stack, x, (y + height) - this.boderSize.height, this.boderSize.width, this.boderSize.height, 0.0f, 1.0f - cornerHeight, cornerWidth, cornerHeight);
        super.drawSubAreaInternal(stack, (x + width) - this.boderSize.width, (y + height) - this.boderSize.height, this.boderSize.width, this.boderSize.height, 1.0f - cornerWidth, 1.0f - cornerHeight, cornerWidth, cornerHeight);
        super.drawSubAreaInternal(stack, x + this.boderSize.width, y, width - (2 * this.boderSize.width), this.boderSize.height, cornerWidth, 0.0f, 1.0f - (2.0f * cornerWidth), cornerHeight);
        super.drawSubAreaInternal(stack, x + this.boderSize.width, (y + height) - this.boderSize.height, width - (2 * this.boderSize.width), this.boderSize.height, cornerWidth, 1.0f - cornerHeight, 1.0f - (2.0f * cornerWidth), cornerHeight);
        super.drawSubAreaInternal(stack, x, y + this.boderSize.height, this.boderSize.width, height - (2 * this.boderSize.height), 0.0f, cornerHeight, cornerWidth, 1.0f - (2.0f * cornerHeight));
        super.drawSubAreaInternal(stack, (x + width) - this.boderSize.width, y + this.boderSize.height, this.boderSize.width, height - (2 * this.boderSize.height), 1.0f - cornerWidth, cornerHeight, cornerWidth, 1.0f - (2.0f * cornerHeight));
        super.drawSubAreaInternal(stack, x + this.boderSize.width, y + this.boderSize.height, width - (2 * this.boderSize.width), height - (2 * this.boderSize.height), cornerWidth, cornerHeight, 1.0f - (2.0f * cornerWidth), 1.0f - (2.0f * cornerHeight));
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.ResourceTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawGuides(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        new ColorBorderTexture(-1, -65536).draw(stack, 0, 0, x + (width * this.offsetX), y + (height * this.offsetY), (int) (width * this.imageWidth), (int) (height * this.imageHeight));
        float cornerWidth = (this.boderSize.width * 1.0f) / this.imageSize.width;
        float cornerHeight = (this.boderSize.height * 1.0f) / this.imageSize.height;
        new ColorBorderTexture(-1, -16711936).draw(stack, 0, 0, x, y, (int) (width * cornerWidth), (int) (height * cornerHeight));
    }
}
