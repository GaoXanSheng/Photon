package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "group_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/GuiTextureGroup.class */
public class GuiTextureGroup extends TransformTexture {
    @Configurable(collapse = false)
    public IGuiTexture[] textures;

    public GuiTextureGroup() {
        this(ResourceBorderTexture.BORDERED_BACKGROUND, new ResourceTexture());
    }

    public GuiTextureGroup(IGuiTexture... textures) {
        this.textures = textures;
    }

    public GuiTextureGroup setTextures(IGuiTexture[] textures) {
        this.textures = textures;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public GuiTextureGroup setColor(int color) {
        IGuiTexture[] iGuiTextureArr;
        for (IGuiTexture texture : this.textures) {
            texture.setColor(color);
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        IGuiTexture[] iGuiTextureArr;
        for (IGuiTexture texture : this.textures) {
            texture.draw(stack, mouseX, mouseY, x, y, width, height);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public void updateTick() {
        IGuiTexture[] iGuiTextureArr;
        for (IGuiTexture texture : this.textures) {
            texture.updateTick();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawSubAreaInternal(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        IGuiTexture[] iGuiTextureArr;
        for (IGuiTexture texture : this.textures) {
            texture.drawSubArea(stack, x, y, width, height, drawnU, drawnV, drawnWidth, drawnHeight);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void setUIResource(Resource<IGuiTexture> texturesResource) {
        IGuiTexture[] iGuiTextureArr;
        for (IGuiTexture texture : this.textures) {
            texture.setUIResource(texturesResource);
        }
    }
}
