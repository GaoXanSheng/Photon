package com.lowdragmc.lowdraglib.gui.util;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.client.shader.management.ShaderProgram;
import com.lowdragmc.lowdraglib.client.shader.uniform.UniformCache;
import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.lowdragmc.lowdraglib.gui.util.forge.DrawerHelperImpl;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.utils.LdUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Rect;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/DrawerHelper.class */
public class DrawerHelper {
    public static ShaderProgram ROUND;
    public static ShaderProgram PANEL_BG;
    public static ShaderProgram ROUND_BOX;
    public static ShaderProgram PROGRESS_ROUND_BOX;
    public static ShaderProgram FRAME_ROUND_BOX;
    public static ShaderProgram ROUND_LINE;

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static void drawTooltip(PoseStack poseStack, int mouseX, int mouseY, List<Component> tooltipTexts, ItemStack tooltipStack, @Nullable TooltipComponent tooltipComponent, Font tooltipFont) {
        DrawerHelperImpl.drawTooltip(poseStack, mouseX, mouseY, tooltipTexts, tooltipStack, tooltipComponent, tooltipFont);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static ClientTooltipComponent getClientTooltipComponent(TooltipComponent component) {
        return DrawerHelperImpl.getClientTooltipComponent(component);
    }

    public static void init() {
        ROUND = (ShaderProgram) LdUtils.make(new ShaderProgram(), program -> {
            program.attach(Shaders.ROUND_F).attach(Shaders.SCREEN_V);
        });
        PANEL_BG = (ShaderProgram) LdUtils.make(new ShaderProgram(), program2 -> {
            program2.attach(Shaders.PANEL_BG_F).attach(Shaders.SCREEN_V);
        });
        ROUND_BOX = (ShaderProgram) LdUtils.make(new ShaderProgram(), program3 -> {
            program3.attach(Shaders.ROUND_BOX_F).attach(Shaders.SCREEN_V);
        });
        PROGRESS_ROUND_BOX = (ShaderProgram) LdUtils.make(new ShaderProgram(), program4 -> {
            program4.attach(Shaders.PROGRESS_ROUND_BOX_F).attach(Shaders.SCREEN_V);
        });
        FRAME_ROUND_BOX = (ShaderProgram) LdUtils.make(new ShaderProgram(), program5 -> {
            program5.attach(Shaders.FRAME_ROUND_BOX_F).attach(Shaders.SCREEN_V);
        });
        ROUND_LINE = (ShaderProgram) LdUtils.make(new ShaderProgram(), program6 -> {
            program6.attach(Shaders.ROUND_LINE_F).attach(Shaders.SCREEN_V);
        });
    }

    public static void drawFluidTexture(PoseStack poseStack, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel, int fluidColor) {
        float uMin = textureSprite.m_118409_();
        float uMax = textureSprite.m_118410_();
        float vMin = textureSprite.m_118411_();
        float vMax = textureSprite.m_118412_();
        float uMax2 = uMax - ((maskRight / 16.0f) * (uMax - uMin));
        float vMax2 = vMax - ((maskTop / 16.0f) * (vMax - vMin));
        BufferBuilder buffer = Tesselator.m_85913_().m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172820_);
        buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85819_);
        Matrix4f mat = poseStack.m_85850_().m_85861_();
        buffer.m_85982_(mat, xCoord, yCoord + 16.0f, zLevel).m_7421_(uMin, vMax2).m_193479_(fluidColor).m_5752_();
        buffer.m_85982_(mat, (xCoord + 16.0f) - maskRight, yCoord + 16.0f, zLevel).m_7421_(uMax2, vMax2).m_193479_(fluidColor).m_5752_();
        buffer.m_85982_(mat, (xCoord + 16.0f) - maskRight, yCoord + maskTop, zLevel).m_7421_(uMax2, vMin).m_193479_(fluidColor).m_5752_();
        buffer.m_85982_(mat, xCoord, yCoord + maskTop, zLevel).m_7421_(uMin, vMin).m_193479_(fluidColor).m_5752_();
        BufferUploader.m_231202_(buffer.m_231175_());
    }

    public static void drawFluidForGui(PoseStack poseStack, FluidStack contents, long tankCapacity, int startX, int startY, int widthT, int heightT) {
        ResourceLocation LOCATION_BLOCKS_TEXTURE = InventoryMenu.f_39692_;
        TextureAtlasSprite fluidStillSprite = FluidHelper.getStillTexture(contents);
        if (fluidStillSprite == null) {
            fluidStillSprite = (TextureAtlasSprite) Minecraft.m_91087_().m_91258_(TextureAtlas.f_118259_).apply(MissingTextureAtlasSprite.m_118071_());
            if (Platform.isDevEnv()) {
                LDLib.LOGGER.error("Missing fluid texture for fluid: " + contents.getDisplayName().getString());
            }
        }
        int fluidColor = FluidHelper.getColor(contents) | (-16777216);
        int scaledAmount = (int) ((contents.getAmount() * heightT) / tankCapacity);
        if (contents.getAmount() > 0 && scaledAmount < 1) {
            scaledAmount = 1;
        }
        if (scaledAmount > heightT || contents.getAmount() == tankCapacity) {
            scaledAmount = heightT;
        }
        RenderSystem.m_69478_();
        RenderSystem.m_157456_(0, LOCATION_BLOCKS_TEXTURE);
        int xTileCount = widthT / 16;
        int xRemainder = widthT - (xTileCount * 16);
        int yTileCount = scaledAmount / 16;
        int yRemainder = scaledAmount - (yTileCount * 16);
        int yStart = startY + heightT;
        int xTile = 0;
        while (xTile <= xTileCount) {
            int yTile = 0;
            while (yTile <= yTileCount) {
                int width = xTile == xTileCount ? xRemainder : 16;
                int height = yTile == yTileCount ? yRemainder : 16;
                int x = startX + (xTile * 16);
                int y = yStart - ((yTile + 1) * 16);
                if (width > 0 && height > 0) {
                    int maskTop = 16 - height;
                    int maskRight = 16 - width;
                    drawFluidTexture(poseStack, x, y, fluidStillSprite, maskTop, maskRight, 0.0f, fluidColor);
                }
                yTile++;
            }
            xTile++;
        }
        RenderSystem.m_69478_();
    }

    public static void drawBorder(PoseStack poseStack, int x, int y, int width, int height, int color, int border) {
        drawSolidRect(poseStack, x - border, y - border, width + (2 * border), border, color);
        drawSolidRect(poseStack, x - border, y + height, width + (2 * border), border, color);
        drawSolidRect(poseStack, x - border, y, border, height, color);
        drawSolidRect(poseStack, x + width, y, border, height, color);
    }

    public static void drawStringSized(PoseStack poseStack, String text, float x, float y, int color, boolean dropShadow, float scale, boolean center) {
        poseStack.m_85836_();
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        double scaledTextWidth = center ? fontRenderer.m_92895_(text) * scale : 0.0d;
        poseStack.m_85837_(x - (scaledTextWidth / 2.0d), y, 0.0d);
        poseStack.m_85841_(scale, scale, scale);
        if (dropShadow) {
            fontRenderer.m_92750_(poseStack, text, 0.0f, 0.0f, color);
        } else {
            fontRenderer.m_92883_(poseStack, text, 0.0f, 0.0f, color);
        }
        poseStack.m_85849_();
    }

    public static void drawStringFixedCorner(PoseStack poseStack, String text, float x, float y, int color, boolean dropShadow, float scale) {
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        float scaledWidth = fontRenderer.m_92895_(text) * scale;
        Objects.requireNonNull(fontRenderer);
        float scaledHeight = 9.0f * scale;
        drawStringSized(poseStack, text, x - scaledWidth, y - scaledHeight, color, dropShadow, scale, false);
    }

    public static void drawText(PoseStack poseStack, String text, float x, float y, float scale, int color) {
        drawText(poseStack, text, x, y, scale, color, false);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawText(PoseStack poseStack, String text, float x, float y, float scale, int color, boolean shadow) {
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        RenderSystem.m_69461_();
        poseStack.m_85836_();
        poseStack.m_85841_(scale, scale, 0.0f);
        float sf = 1.0f / scale;
        if (shadow) {
            fontRenderer.m_92750_(poseStack, text, x * sf, y * sf, color);
        } else {
            fontRenderer.m_92883_(poseStack, text, x * sf, y * sf, color);
        }
        poseStack.m_85849_();
        RenderSystem.m_69478_();
    }

    public static void drawItemStack(PoseStack poseStack, ItemStack itemStack, int x, int y, int color, @Nullable String altTxt) {
        PoseStack posestack = RenderSystem.m_157191_();
        posestack.m_85836_();
        posestack.m_166854_(poseStack.m_85850_().m_85861_());
        posestack.m_85837_(0.0d, 0.0d, 32.0d);
        RenderSystem.m_157182_();
        float a = ((color >> 24) & 255) / 255.0f;
        float r = ((color >> 16) & 255) / 255.0f;
        float g = ((color >> 8) & 255) / 255.0f;
        float b = (color & 255) / 255.0f;
        RenderSystem.m_157429_(r, g, b, a);
        RenderSystem.m_157427_(GameRenderer::m_172817_);
        RenderSystem.m_69482_();
        RenderSystem.m_69458_(true);
        Minecraft mc = Minecraft.m_91087_();
        ItemRenderer itemRenderer = mc.m_91291_();
        itemRenderer.f_115093_ = 200.0f;
        itemRenderer.m_115203_(itemStack, x, y);
        itemRenderer.m_115174_(mc.f_91062_, itemStack, x, y, altTxt);
        itemRenderer.f_115093_ = 0.0f;
        RenderSystem.m_69421_(256, Minecraft.f_91002_);
        RenderSystem.m_69458_(false);
        RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
        posestack.m_85849_();
        RenderSystem.m_157182_();
        RenderSystem.m_69482_();
        RenderSystem.m_69478_();
        RenderSystem.m_69465_();
    }

    public static List<Component> getItemToolTip(ItemStack itemStack) {
        Minecraft mc = Minecraft.m_91087_();
        return itemStack.m_41651_(mc.f_91074_, mc.f_91066_.f_92125_ ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }

    public static void drawSolidRect(PoseStack poseStack, int x, int y, int width, int height, int color) {
        Gui.m_93172_(poseStack, x, y, x + width, y + height, color);
        RenderSystem.m_69478_();
    }

    public static void drawSolidRect(PoseStack poseStack, Rect rect, int color) {
        drawSolidRect(poseStack, rect.left, rect.up, rect.right, rect.down, color);
    }

    public static void drawRectShadow(PoseStack poseStack, int x, int y, int width, int height, int distance) {
        drawGradientRect(poseStack, x + distance, y + height, width - distance, distance, 1325400064, 0, false);
        drawGradientRect(poseStack, x + width, y + distance, distance, height - distance, 1325400064, 0, true);
        RenderSystem.m_69472_();
        RenderSystem.m_69478_();
        RenderSystem.m_69416_(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tesselator tesselator = Tesselator.m_85913_();
        BufferBuilder buffer = tesselator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172811_);
        buffer.m_166779_(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.f_85815_);
        int x2 = x + width;
        int y2 = y + height;
        Matrix4f mat = poseStack.m_85850_().m_85861_();
        buffer.m_85982_(mat, x2, y2, 0.0f).m_85950_(0.0f, 0.0f, 0.0f, 0.30980393f).m_5752_();
        buffer.m_85982_(mat, x2, y2 + distance, 0.0f).m_6122_(0, 0, 0, 0).m_5752_();
        buffer.m_85982_(mat, x2 + distance, y2 + distance, 0.0f).m_6122_(0, 0, 0, 0).m_5752_();
        buffer.m_85982_(mat, x2, y2, 0.0f).m_85950_(0.0f, 0.0f, 0.0f, 0.30980393f).m_5752_();
        buffer.m_85982_(mat, x2 + distance, y2 + distance, 0.0f).m_6122_(0, 0, 0, 0).m_5752_();
        buffer.m_85982_(mat, x2 + distance, y2, 0.0f).m_6122_(0, 0, 0, 0).m_5752_();
        tesselator.m_85914_();
        RenderSystem.m_69493_();
    }

    public static void drawGradientRect(PoseStack poseStack, int x, int y, int width, int height, int startColor, int endColor) {
        drawGradientRect(poseStack, x, y, width, height, startColor, endColor, false);
    }

    public static void drawGradientRect(PoseStack poseStack, float x, float y, float width, float height, int startColor, int endColor, boolean horizontal) {
        float startAlpha = ((startColor >> 24) & 255) / 255.0f;
        float startRed = ((startColor >> 16) & 255) / 255.0f;
        float startGreen = ((startColor >> 8) & 255) / 255.0f;
        float startBlue = (startColor & 255) / 255.0f;
        float endAlpha = ((endColor >> 24) & 255) / 255.0f;
        float endRed = ((endColor >> 16) & 255) / 255.0f;
        float endGreen = ((endColor >> 8) & 255) / 255.0f;
        float endBlue = (endColor & 255) / 255.0f;
        RenderSystem.m_69472_();
        RenderSystem.m_69478_();
        RenderSystem.m_69416_(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Matrix4f mat = poseStack.m_85850_().m_85861_();
        Tesselator tesselator = Tesselator.m_85913_();
        BufferBuilder buffer = tesselator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172811_);
        buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85815_);
        if (horizontal) {
            buffer.m_85982_(mat, x + width, y, 0.0f).m_85950_(endRed, endGreen, endBlue, endAlpha).m_5752_();
            buffer.m_85982_(mat, x, y, 0.0f).m_85950_(startRed, startGreen, startBlue, startAlpha).m_5752_();
            buffer.m_85982_(mat, x, y + height, 0.0f).m_85950_(startRed, startGreen, startBlue, startAlpha).m_5752_();
            buffer.m_85982_(mat, x + width, y + height, 0.0f).m_85950_(endRed, endGreen, endBlue, endAlpha).m_5752_();
            tesselator.m_85914_();
        } else {
            buffer.m_85982_(mat, x + width, y, 0.0f).m_85950_(startRed, startGreen, startBlue, startAlpha).m_5752_();
            buffer.m_85982_(mat, x, y, 0.0f).m_85950_(startRed, startGreen, startBlue, startAlpha).m_5752_();
            buffer.m_85982_(mat, x, y + height, 0.0f).m_85950_(endRed, endGreen, endBlue, endAlpha).m_5752_();
            buffer.m_85982_(mat, x + width, y + height, 0.0f).m_85950_(endRed, endGreen, endBlue, endAlpha).m_5752_();
            tesselator.m_85914_();
        }
        RenderSystem.m_69493_();
    }

    public static void drawLines(PoseStack poseStack, List<Vec2> points, int startColor, int endColor, float width) {
        Tesselator tesselator = Tesselator.m_85913_();
        BufferBuilder bufferbuilder = tesselator.m_85915_();
        RenderSystem.m_69478_();
        RenderSystem.m_69472_();
        RenderSystem.m_69453_();
        RenderSystem.m_157427_(GameRenderer::m_172811_);
        bufferbuilder.m_166779_(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.f_85815_);
        RenderBufferUtils.drawColorLines(poseStack, bufferbuilder, points, startColor, endColor, width);
        tesselator.m_85914_();
        RenderSystem.m_69493_();
        RenderSystem.m_69453_();
    }

    public static void drawTextureRect(PoseStack poseStack, float x, float y, float width, float height) {
        Tesselator tesselator = Tesselator.m_85913_();
        BufferBuilder buffer = tesselator.m_85915_();
        Matrix4f mat = poseStack.m_85850_().m_85861_();
        RenderSystem.m_157427_(GameRenderer::m_172817_);
        buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85817_);
        buffer.m_85982_(mat, x, y + height, 0.0f).m_7421_(0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, x + width, y + height, 0.0f).m_7421_(1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, x + width, y, 0.0f).m_7421_(1.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, x, y, 0.0f).m_7421_(0.0f, 1.0f).m_5752_();
        tesselator.m_85914_();
    }

    public static void updateScreenVshUniform(PoseStack poseStack, UniformCache uniform) {
        Window window = Minecraft.m_91087_().m_91268_();
        uniform.glUniform1F("GuiScale", (float) window.m_85449_());
        uniform.glUniform2F("ScreenSize", window.m_85441_(), window.m_85442_());
        uniform.glUniformMatrix4F("PoseStack", poseStack.m_85850_().m_85861_());
        uniform.glUniformMatrix4F("ProjMat", RenderSystem.m_157192_());
    }

    public static void drawRound(PoseStack poseStack, int color, float radius, Position centerPos) {
        ROUND.use(uniform -> {
            updateScreenVshUniform(poseStack, uniform);
            uniform.fillRGBAColor("Color", color);
            uniform.glUniform1F("StepLength", 1.0f);
            uniform.glUniform1F("Radius", radius);
            uniform.glUniform2F("CenterPos", centerPos.x, centerPos.y);
        });
        RenderSystem.m_69478_();
        uploadScreenPosVertex();
    }

    public static void drawPanelBg(PoseStack poseStack) {
        PANEL_BG.use(uniform -> {
            updateScreenVshUniform(poseStack, uniform);
            uniform.glUniform1F("Density", 5.0f);
            uniform.glUniform1F("SquareSize", 0.1f);
            uniform.glUniform4F("BgColor", 0.078431375f, 0.078431375f, 0.078431375f, 0.95f);
            uniform.glUniform4F("SquareColor", 0.15686275f, 0.15686275f, 0.15686275f, 0.95f);
        });
        RenderSystem.m_69478_();
        uploadScreenPosVertex();
    }

    public static void drawRoundBox(PoseStack poseStack, Rect square, Vector4f radius, int color) {
        ROUND_BOX.use(uniform -> {
            updateScreenVshUniform(poseStack, uniform);
            uniform.glUniform4F("SquareVertex", square.left - 1.0f, square.up - 1.0f, square.right - 1.0f, square.down - 1.0f);
            uniform.glUniform4F("RoundRadius", radius.m_123601_(), radius.m_123615_(), radius.m_123616_(), radius.m_123617_());
            uniform.fillRGBAColor("Color", color);
            uniform.glUniform1F("Blur", 2.0f);
        });
        RenderSystem.m_69478_();
        uploadScreenPosVertex();
    }

    public static void drawProgressRoundBox(PoseStack poseStack, Rect square, Vector4f radius, int color1, int color2, float progress) {
        PROGRESS_ROUND_BOX.use(uniform -> {
            updateScreenVshUniform(poseStack, uniform);
            uniform.glUniform4F("SquareVertex", square.left, square.up, square.right, square.down);
            uniform.glUniform4F("RoundRadius", radius.m_123601_(), radius.m_123615_(), radius.m_123616_(), radius.m_123617_());
            uniform.fillRGBAColor("Color1", color1);
            uniform.fillRGBAColor("Color2", color2);
            uniform.glUniform1F("Blur", 2.0f);
            uniform.glUniform1F("Progress", progress);
        });
        RenderSystem.m_69478_();
        uploadScreenPosVertex();
    }

    public static void drawFrameRoundBox(PoseStack poseStack, Rect square, float thickness, Vector4f radius1, Vector4f radius2, int color) {
        FRAME_ROUND_BOX.use(uniform -> {
            updateScreenVshUniform(poseStack, uniform);
            uniform.glUniform4F("SquareVertex", square.left - 1, square.up - 1, square.right - 1, square.down - 1);
            uniform.glUniform4F("RoundRadius1", radius1.m_123601_(), radius1.m_123615_(), radius1.m_123616_(), radius1.m_123617_());
            uniform.glUniform4F("RoundRadius2", radius2.m_123601_(), radius2.m_123615_(), radius2.m_123616_(), radius2.m_123617_());
            uniform.glUniform1F("Thickness", thickness);
            uniform.fillRGBAColor("Color", color);
            uniform.glUniform1F("Blur", 2.0f);
        });
        RenderSystem.m_69478_();
        uploadScreenPosVertex();
    }

    public static void drawRoundLine(PoseStack poseStack, Position begin, Position end, int width, int color1, int color2) {
        ROUND_LINE.use(uniform -> {
            updateScreenVshUniform(poseStack, uniform);
            uniform.glUniform1F("Width", width);
            uniform.glUniform2F("Point1", begin.x, begin.y);
            uniform.glUniform2F("Point2", end.x, end.y);
            uniform.fillRGBAColor("Color1", color1);
            uniform.fillRGBAColor("Color2", color2);
            uniform.glUniform1F("Blur", 2.0f);
        });
        RenderSystem.m_69478_();
        uploadScreenPosVertex();
    }

    private static void uploadScreenPosVertex() {
        BufferBuilder builder = Tesselator.m_85913_().m_85915_();
        builder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85814_);
        builder.m_5483_(-1.0d, 1.0d, 0.0d).m_5752_();
        builder.m_5483_(-1.0d, -1.0d, 0.0d).m_5752_();
        builder.m_5483_(1.0d, -1.0d, 0.0d).m_5752_();
        builder.m_5483_(1.0d, 1.0d, 0.0d).m_5752_();
        BufferUploader.m_231209_(builder.m_231175_());
    }
}
