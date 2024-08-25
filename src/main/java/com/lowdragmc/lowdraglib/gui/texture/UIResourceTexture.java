package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/UIResourceTexture.class */
public class UIResourceTexture implements IGuiTexture {
    private static Resource<IGuiTexture> projectResource;
    private static boolean isProject;
    private Resource<IGuiTexture> resource;
    public final String key;

    public static Resource<IGuiTexture> getProjectResource() {
        return projectResource;
    }

    public static boolean isProject() {
        return isProject;
    }

    public static void setCurrentResource(Resource<IGuiTexture> resource, boolean isProject2) {
        projectResource = resource;
        isProject = isProject2;
    }

    public static void clearCurrentResource() {
        projectResource = null;
        isProject = false;
    }

    public void setResource(Resource<IGuiTexture> resource) {
        this.resource = resource;
    }

    public UIResourceTexture(String key) {
        this.key = key;
    }

    public UIResourceTexture(Resource<IGuiTexture> resource, String key) {
        this.resource = resource;
        this.key = key;
    }

    public IGuiTexture getTexture() {
        return this.resource == null ? IGuiTexture.MISSING_TEXTURE : this.resource.getResourceOrDefault(this.key, IGuiTexture.MISSING_TEXTURE);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public IGuiTexture setColor(int color) {
        return getTexture().setColor(color);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public IGuiTexture rotate(float degree) {
        return getTexture().rotate(degree);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public IGuiTexture scale(float scale) {
        return getTexture().scale(scale);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public IGuiTexture transform(int xOffset, int yOffset) {
        return getTexture().transform(xOffset, yOffset);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public void draw(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        getTexture().draw(stack, mouseX, mouseY, x, y, width, height);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public void updateTick() {
        getTexture().updateTick();
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public void drawSubArea(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        getTexture().drawSubArea(stack, x, y, width, height, drawnU, drawnV, drawnWidth, drawnHeight);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void createPreview(ConfiguratorGroup father) {
        getTexture().createPreview(father);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        getTexture().buildConfigurator(father);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void setUIResource(Resource<IGuiTexture> texturesResource) {
        setResource(texturesResource);
    }
}
