package com.lowdragmc.lowdraglib.gui.texture;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "animation_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/AnimationTexture.class */
public class AnimationTexture extends TransformTexture {
    @Configurable(name = "ldlib.gui.editor.name.resource")
    public ResourceLocation imageLocation;
    @Configurable(tips = {"ldlib.gui.editor.tips.cell_size"})
    @NumberRange(range = {1.0d, 2.147483647E9d})
    protected int cellSize;
    @Configurable(tips = {"ldlib.gui.editor.tips.cell_from"})
    @NumberRange(range = {0.0d, 2.147483647E9d})
    protected int from;
    @Configurable(tips = {"ldlib.gui.editor.tips.cell_to"})
    @NumberRange(range = {0.0d, 2.147483647E9d})
    protected int to;
    @Configurable(tips = {"ldlib.gui.editor.tips.cell_animation"})
    @NumberRange(range = {0.0d, 2.147483647E9d})
    protected int animation;
    @NumberColor
    @Configurable
    protected int color;
    protected int currentFrame;
    protected int currentTime;
    private long lastTick;

    public AnimationTexture() {
        this("ldlib:textures/gui/particles.png");
        setCellSize(8).setAnimation(32, 44).setAnimation(1);
    }

    public AnimationTexture(String imageLocation) {
        this.color = -1;
        this.imageLocation = new ResourceLocation(imageLocation);
    }

    public AnimationTexture(ResourceLocation imageLocation) {
        this.color = -1;
        this.imageLocation = imageLocation;
    }

    public AnimationTexture copy() {
        return new AnimationTexture(this.imageLocation).setCellSize(this.cellSize).setAnimation(this.from, this.to).setAnimation(this.animation).setColor(this.color);
    }

    public AnimationTexture setCellSize(int cellSize) {
        this.cellSize = cellSize;
        return this;
    }

    public AnimationTexture setAnimation(int from, int to) {
        this.currentFrame = from;
        this.from = from;
        this.to = to;
        return this;
    }

    public AnimationTexture setAnimation(int animation) {
        this.animation = animation;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public AnimationTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    @OnlyIn(Dist.CLIENT)
    public void updateTick() {
        if (Minecraft.m_91087_().f_91073_ != null) {
            long tick = Minecraft.m_91087_().f_91073_.m_46467_();
            if (tick == this.lastTick) {
                return;
            }
            this.lastTick = tick;
        }
        if (this.currentTime >= this.animation) {
            this.currentTime = 0;
            this.currentFrame++;
        } else {
            this.currentTime++;
        }
        if (this.currentFrame > this.to) {
            this.currentFrame = this.from;
        } else if (this.currentFrame < this.from) {
            this.currentFrame = this.from;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        float cell = 1.0f / this.cellSize;
        int X = this.currentFrame % this.cellSize;
        int Y = this.currentFrame / this.cellSize;
        float imageU = X * cell;
        float imageV = Y * cell;
        Tesselator tessellator = Tesselator.m_85913_();
        BufferBuilder bufferbuilder = tessellator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172820_);
        RenderSystem.m_157456_(0, this.imageLocation);
        Matrix4f matrix4f = stack.m_85850_().m_85861_();
        bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85819_);
        bufferbuilder.m_85982_(matrix4f, x, y + height, 0.0f).m_7421_(imageU, imageV + cell).m_193479_(this.color).m_5752_();
        bufferbuilder.m_85982_(matrix4f, x + width, y + height, 0.0f).m_7421_(imageU + cell, imageV + cell).m_193479_(this.color).m_5752_();
        bufferbuilder.m_85982_(matrix4f, x + width, y, 0.0f).m_7421_(imageU + cell, imageV).m_193479_(this.color).m_5752_();
        bufferbuilder.m_85982_(matrix4f, x, y, 0.0f).m_7421_(imageU, imageV).m_193479_(this.color).m_5752_();
        tessellator.m_85914_();
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void createPreview(ConfiguratorGroup father) {
        super.createPreview(father);
        WidgetGroup widgetGroup = new WidgetGroup(0, 0, 100, 100);
        ImageWidget imageWidget = new ImageWidget(0, 0, 100, 100, new GuiTextureGroup(new ResourceTexture(this.imageLocation.toString()), this::drawGuides)).setBorder(2, ColorPattern.T_WHITE.color);
        widgetGroup.addWidget(imageWidget);
        widgetGroup.addWidget(new ButtonWidget(0, 0, 100, 100, IGuiTexture.EMPTY, cd -> {
            if (Editor.INSTANCE == null) {
                return;
            }
            File path = new File(Editor.INSTANCE.getWorkSpace(), "assets/ldlib/textures");
            DialogWidget.showFileDialog(Editor.INSTANCE, "ldlib.gui.editor.tips.select_image", path, true, DialogWidget.suffixFilter(".png"), r -> {
                if (imageWidget != null && imageWidget.isFile()) {
                    this.imageLocation = getTextureFromFile(path, imageWidget);
                    this.cellSize = 1;
                    this.from = 0;
                    this.to = 0;
                    this.animation = 0;
                    path.setImage(new GuiTextureGroup(new ResourceTexture(this.imageLocation.toString()), this::drawGuides));
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
        float cell = 1.0f / this.cellSize;
        int X = this.from % this.cellSize;
        int Y = this.from / this.cellSize;
        float imageU = X * cell;
        float imageV = Y * cell;
        new ColorBorderTexture(-1, -16711936).draw(stack, 0, 0, x + (width * imageU), y + (height * imageV), (int) (width * cell), (int) (height * cell));
        int X2 = this.to % this.cellSize;
        int Y2 = this.to / this.cellSize;
        float imageU2 = X2 * cell;
        float imageV2 = Y2 * cell;
        new ColorBorderTexture(-1, -65536).draw(stack, 0, 0, x + (width * imageU2), y + (height * imageV2), (int) (width * cell), (int) (height * cell));
    }
}
