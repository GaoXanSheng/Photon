package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "resource_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ResourceTexture.class */
public class ResourceTexture extends TransformTexture {
    @Configurable(name = "ldlib.gui.editor.name.resource")
    public ResourceLocation imageLocation;
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 0.02d)
    public float offsetX;
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 0.02d)
    public float offsetY;
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 0.02d)
    public float imageWidth;
    @Configurable
    @NumberRange(range = {-3.4028234663852886E38d, 3.4028234663852886E38d}, wheel = 0.02d)
    public float imageHeight;
    @NumberColor
    @Configurable
    protected int color;

    public ResourceTexture() {
        this.imageLocation = new ResourceLocation("ldlib:textures/gui/icon.png");
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.imageWidth = 1.0f;
        this.imageHeight = 1.0f;
        this.color = -1;
    }

    public ResourceTexture(ResourceLocation imageLocation, float offsetX, float offsetY, float width, float height) {
        this.imageLocation = new ResourceLocation("ldlib:textures/gui/icon.png");
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.imageWidth = 1.0f;
        this.imageHeight = 1.0f;
        this.color = -1;
        this.imageLocation = imageLocation;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public ResourceTexture(String imageLocation) {
        this(new ResourceLocation(imageLocation), 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public ResourceTexture getSubTexture(float offsetX, float offsetY, float width, float height) {
        return new ResourceTexture(this.imageLocation, this.offsetX + (this.imageWidth * offsetX), this.offsetY + (this.imageHeight * offsetY), this.imageWidth * width, this.imageHeight * height);
    }

    public ResourceTexture getSubTexture(double offsetX, double offsetY, double width, double height) {
        return new ResourceTexture(this.imageLocation, this.offsetX + ((float) (this.imageWidth * offsetX)), this.offsetY + ((float) (this.imageHeight * offsetY)), this.imageWidth * ((float) width), this.imageHeight * ((float) height));
    }

    public ResourceTexture copy() {
        return getSubTexture(0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public ResourceTexture setColor(int color) {
        this.color = color;
        return this;
    }

    public static ResourceTexture fromSpirit(ResourceLocation texture) {
        if (LDLib.isClient()) {
            TextureAtlasSprite sprite = (TextureAtlasSprite) Minecraft.m_91087_().m_91258_(TextureAtlas.f_118259_).apply(texture);
            return new ResourceTexture(TextureAtlas.f_118259_, sprite.m_118409_(), sprite.m_118411_(), sprite.m_118410_() - sprite.m_118409_(), sprite.m_118412_() - sprite.m_118411_());
        }
        return new ResourceTexture("");
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        drawSubArea(stack, x, y, width, height, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    public void drawSubAreaInternal(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        float imageU = this.offsetX + (this.imageWidth * drawnU);
        float imageV = this.offsetY + (this.imageHeight * drawnV);
        float imageWidth = this.imageWidth * drawnWidth;
        float imageHeight = this.imageHeight * drawnHeight;
        Tesselator tessellator = Tesselator.m_85913_();
        BufferBuilder bufferbuilder = tessellator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172820_);
        RenderSystem.m_157456_(0, this.imageLocation);
        Matrix4f matrix4f = stack.m_85850_().m_85861_();
        bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85819_);
        bufferbuilder.m_85982_(matrix4f, x, y + height, 0.0f).m_7421_(imageU, imageV + imageHeight).m_193479_(this.color).m_5752_();
        bufferbuilder.m_85982_(matrix4f, x + width, y + height, 0.0f).m_7421_(imageU + imageWidth, imageV + imageHeight).m_193479_(this.color).m_5752_();
        bufferbuilder.m_85982_(matrix4f, x + width, y, 0.0f).m_7421_(imageU + imageWidth, imageV).m_193479_(this.color).m_5752_();
        bufferbuilder.m_85982_(matrix4f, x, y, 0.0f).m_7421_(imageU, imageV).m_193479_(this.color).m_5752_();
        tessellator.m_85914_();
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void createPreview(ConfiguratorGroup father) {
        super.createPreview(father);
        WidgetGroup widgetGroup = new WidgetGroup(0, 0, 100, 100);
        widgetGroup.addWidget(new ImageWidget(0, 0, 100, 100, () -> {
            return new GuiTextureGroup(new ResourceTexture(this.imageLocation.toString()), this::drawGuides);
        }).setBorder(2, ColorPattern.T_WHITE.color));
        widgetGroup.addWidget(new ButtonWidget(0, 0, 100, 100, IGuiTexture.EMPTY, cd -> {
            if (Editor.INSTANCE == null) {
                return;
            }
            File path = new File(Editor.INSTANCE.getWorkSpace(), "assets/ldlib/textures");
            DialogWidget.showFileDialog(Editor.INSTANCE, "ldlib.gui.editor.tips.select_image", path, true, DialogWidget.suffixFilter(".png"), r -> {
                if (path != null && path.isFile()) {
                    this.imageLocation = getTextureFromFile(path, path);
                    this.offsetX = 0.0f;
                    this.offsetY = 0.0f;
                    this.imageWidth = 1.0f;
                    this.imageHeight = 1.0f;
                }
            });
        }));
        WrapperConfigurator base = new WrapperConfigurator("ldlib.gui.editor.group.base_image", widgetGroup);
        base.setTips("ldlib.gui.editor.tips.click_select_image");
        father.addConfigurators(base);
    }

    private ResourceLocation getTextureFromFile(File path, File r) {
        return new ResourceLocation("ldlib:" + r.getPath().replace(path.getPath(), "textures").replace('\\', '/'));
    }

    @OnlyIn(Dist.CLIENT)
    protected void drawGuides(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        new ColorBorderTexture(-1, -65536).draw(stack, 0, 0, x + (width * this.offsetX), y + (height * this.offsetY), (int) (width * this.imageWidth), (int) (height * this.imageHeight));
    }
}
