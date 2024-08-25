package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/HsbColorWidget.class */
public class HsbColorWidget extends Widget {
    private int gap;
    private int barWidth;
    private float h;
    private float s;
    private float b;
    private float alpha;
    private int argb;
    private boolean isDraggingMain;
    private boolean isDraggingColorSlider;
    private boolean isDraggingAlphaSlider;
    private HSB_MODE mode;
    private IntSupplier colorSupplier;
    private IntConsumer onChanged;
    private boolean showRGB;
    private boolean showAlpha;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/HsbColorWidget$HSB_MODE.class */
    public enum HSB_MODE {
        H("hue"),
        S("saturation"),
        B("brightness");
        
        private final String name;

        HSB_MODE(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    public HsbColorWidget setGap(int gap) {
        this.gap = gap;
        return this;
    }

    public HsbColorWidget setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        return this;
    }

    public HsbColorWidget setColorSupplier(IntSupplier colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public HsbColorWidget setOnChanged(IntConsumer onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    public HsbColorWidget setShowRGB(boolean showRGB) {
        this.showRGB = showRGB;
        return this;
    }

    public HsbColorWidget setShowAlpha(boolean showAlpha) {
        this.showAlpha = showAlpha;
        return this;
    }

    public boolean isShowRGB() {
        return this.showRGB;
    }

    public boolean isShowAlpha() {
        return this.showAlpha;
    }

    public HsbColorWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.h = 204.0f;
        this.s = 0.72f;
        this.b = 0.94f;
        this.alpha = 1.0f;
        this.mode = HSB_MODE.H;
        this.showRGB = true;
        this.showAlpha = true;
        this.gap = 10;
        this.barWidth = 10;
        refreshRGB();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        super.updateScreen();
        if (this.isClientSideWidget && this.colorSupplier != null) {
            setColor(this.colorSupplier.getAsInt());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.colorSupplier != null && !this.isClientSideWidget) {
            int lastColor = this.argb;
            setColor(this.colorSupplier.getAsInt());
            if (lastColor != this.argb) {
                writeUpdateInfo(-1, buffer -> {
                    buffer.m_130130_(this.argb);
                });
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        Matrix4f pose = poseStack.m_85850_().m_85861_();
        int x = getPosition().x;
        int y = getPosition().y;
        int width = getSize().width;
        int height = getSize().height;
        if (this.showRGB) {
            BufferBuilder builder = Tesselator.m_85913_().m_85915_();
            drawHsbContext(pose, builder, x, y, (width - this.barWidth) - this.gap, (height - this.barWidth) - this.gap);
        }
        if (this.showAlpha) {
            DrawerHelper.drawGradientRect(poseStack, x, (y + height) - this.barWidth, (width - this.barWidth) - this.gap, this.barWidth, this.argb & 16777215, this.argb | (-16777216), true);
        }
        DrawerHelper.drawSolidRect(poseStack, (x + width) - this.barWidth, (y + height) - this.barWidth, this.barWidth, this.barWidth, this.argb);
        float color = 0.0f;
        float mainX = 0.0f;
        float mainY = 0.0f;
        switch (this.mode) {
            case H:
                mainX = this.s;
                mainY = 1.0f - this.b;
                color = 1.0f - (this.h / 360.0f);
                break;
            case S:
                mainX = this.h / 360.0f;
                mainY = 1.0f - this.b;
                color = 1.0f - this.s;
                break;
            case B:
                mainX = this.h / 360.0f;
                mainY = 1.0f - this.s;
                color = 1.0f - this.b;
                break;
        }
        if (this.showRGB) {
            DrawerHelper.drawSolidRect(poseStack, ((int) (x + (mainX * ((width - this.barWidth) - this.gap)))) - 1, ((int) (y + (mainY * ((height - this.barWidth) - this.gap)))) - 1, 3, 3, -65536);
            DrawerHelper.drawSolidRect(poseStack, ((x + width) - this.barWidth) - 2, (int) (y + (color * ((height - this.barWidth) - this.gap))), this.barWidth + 4, 1, -65536);
            renderInfo(poseStack, x, y, (width - this.barWidth) - this.gap, (height - this.barWidth) - this.gap);
            DrawerHelper.drawBorder(poseStack, x, y, (width - this.barWidth) - this.gap, (height - this.barWidth) - this.gap, ColorPattern.WHITE.color, 2);
        }
        if (this.showAlpha) {
            DrawerHelper.drawSolidRect(poseStack, (int) (x + (this.alpha * ((width - this.barWidth) - this.gap))), ((y + height) - this.barWidth) - 2, 1, this.barWidth + 4, -65536);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void drawHsbContext(Matrix4f pose, BufferBuilder builder, int x, int y, int width, int height) {
        RenderSystem.m_157427_(Shaders::getHsbShader);
        builder.m_166779_(VertexFormat.Mode.QUADS, Shaders.HSB_VERTEX_FORMAT);
        renderMain(pose, builder, x, y, width, height);
        renderColorSlide(pose, builder, x, y, width, height);
        BufferUploader.m_231202_(builder.m_231175_());
    }

    @OnlyIn(Dist.CLIENT)
    private void renderMain(Matrix4f pose, BufferBuilder builder, int x, int y, int width, int height) {
        float _h = 0.0f;
        float _s = 0.0f;
        float _b = 0.0f;
        switch (this.mode) {
            case H:
                _h = this.h;
                _s = 0.0f;
                _b = 1.0f;
                break;
            case S:
                _h = 0.0f;
                _s = this.s;
                _b = 1.0f;
                break;
            case B:
                _h = 0.0f;
                _s = 1.0f;
                _b = this.b;
                break;
        }
        builder.m_85982_(pose, x, y, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
        switch (this.mode) {
            case H:
                _h = this.h;
                _s = 0.0f;
                _b = 0.0f;
                break;
            case S:
                _h = 0.0f;
                _s = this.s;
                _b = 0.0f;
                break;
            case B:
                _h = 0.0f;
                _s = 0.0f;
                _b = this.b;
                break;
        }
        builder.m_85982_(pose, x, y + height, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
        switch (this.mode) {
            case H:
                _h = this.h;
                _s = 1.0f;
                _b = 0.0f;
                break;
            case S:
                _h = 360.0f;
                _s = this.s;
                _b = 0.0f;
                break;
            case B:
                _h = 360.0f;
                _s = 0.0f;
                _b = this.b;
                break;
        }
        builder.m_85982_(pose, x + width, y + height, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
        switch (this.mode) {
            case H:
                _h = this.h;
                _s = 1.0f;
                _b = 1.0f;
                break;
            case S:
                _h = 360.0f;
                _s = this.s;
                _b = 1.0f;
                break;
            case B:
                _h = 360.0f;
                _s = 1.0f;
                _b = this.b;
                break;
        }
        builder.m_85982_(pose, x + width, y, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
    }

    @OnlyIn(Dist.CLIENT)
    private void renderColorSlide(Matrix4f pose, BufferBuilder builder, int x, int y, int width, int height) {
        float _h = 0.0f;
        float _s = 0.0f;
        float _b = 0.0f;
        int barX = x + width + this.gap;
        switch (this.mode) {
            case H:
                _h = 0.0f;
                _s = 1.0f;
                _b = 1.0f;
                break;
            case S:
                _h = this.h;
                _s = 0.0f;
                _b = this.b;
                break;
            case B:
                _h = this.h;
                _s = this.s;
                _b = 0.0f;
                break;
        }
        builder.m_85982_(pose, barX, y + height, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
        builder.m_85982_(pose, barX + this.barWidth, y + height, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
        switch (this.mode) {
            case H:
                _h = 360.0f;
                _s = 1.0f;
                _b = 1.0f;
                break;
            case S:
                _h = this.h;
                _s = 1.0f;
                _b = this.b;
                break;
            case B:
                _h = this.h;
                _s = this.s;
                _b = 1.0f;
                break;
        }
        builder.m_85982_(pose, barX + this.barWidth, y, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
        builder.m_85982_(pose, barX, y, 0.0f);
        putColor(builder, _h, _s, _b).m_5751_();
        builder.m_5752_();
    }

    @OnlyIn(Dist.CLIENT)
    private void renderInfo(PoseStack poseStack, int x, int y, int width, int height) {
        Font font = Minecraft.m_91087_().f_91062_;
        int y2 = y + 2;
        int strX = x + 10;
        Objects.requireNonNull(font);
        Objects.requireNonNull(font);
        int strGapY = ((int) Math.max(0.0f, (height - (7.0f * 9.0f)) / 6.0f)) + 9;
        DrawerHelper.drawText(poseStack, "h:" + ((int) this.h) + "°", strX, y2, 1.0f, -1, true);
        DrawerHelper.drawText(poseStack, "s:" + ((int) (this.s * 100.0f)) + "%", strX, y2 + strGapY, 1.0f, -1, true);
        DrawerHelper.drawText(poseStack, "b:" + ((int) (this.b * 100.0f)) + "%", strX, y2 + (strGapY * 2), 1.0f, -1, true);
        DrawerHelper.drawText(poseStack, "r:" + ((this.argb >> 16) & 255), strX, y2 + (strGapY * 3), 1.0f, -1, true);
        DrawerHelper.drawText(poseStack, "g:" + ((this.argb >> 8) & 255), strX, y2 + (strGapY * 4), 1.0f, -1, true);
        DrawerHelper.drawText(poseStack, "b:" + (this.argb & 255), strX, y2 + (strGapY * 5), 1.0f, -1, true);
        DrawerHelper.drawText(poseStack, "a:" + ((this.argb >> 24) & 255), strX, y2 + (strGapY * 6), 1.0f, -1, true);
    }

    @OnlyIn(Dist.CLIENT)
    private BufferBuilder putColor(BufferBuilder builder, float h, float s, float b) {
        return putColor(builder, h, s, b, 1.0f);
    }

    @OnlyIn(Dist.CLIENT)
    private BufferBuilder putColor(BufferBuilder builder, float h, float s, float b, float a) {
        builder.m_5832_(0, h);
        builder.m_5832_(4, s);
        builder.m_5832_(8, b);
        builder.m_5832_(12, a);
        return builder;
    }

    public boolean isMouseOverMain(double mouseX, double mouseY) {
        int x = getPosition().x;
        int y = getPosition().y;
        int width = (getSize().width - this.gap) - this.barWidth;
        int height = (getSize().height - this.gap) - this.barWidth;
        return isMouseOver(x, y, width, height, mouseX, mouseY);
    }

    public boolean isMouseOverColorSlider(double mouseX, double mouseY) {
        int x = (getPosition().x + getSize().width) - this.barWidth;
        int y = getPosition().y;
        int height = (getSize().height - this.gap) - this.barWidth;
        return isMouseOver(x, y, this.barWidth, height, mouseX, mouseY);
    }

    public boolean isMouseOverAlphaSlider(double mouseX, double mouseY) {
        int x = getPosition().x;
        int y = (getPosition().y + getSize().width) - this.barWidth;
        int width = (getSize().width - this.gap) - this.barWidth;
        return isMouseOver(x, y, width, this.barWidth, mouseX, mouseY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        HSB_MODE hsb_mode;
        this.isDraggingMain = false;
        this.isDraggingColorSlider = false;
        this.isDraggingAlphaSlider = false;
        if (isMouseOverMain(mouseX, mouseY) && this.showRGB) {
            if (button == 0) {
                this.isDraggingMain = true;
            } else if (button == 1) {
                switch (this.mode) {
                    case H:
                        hsb_mode = HSB_MODE.S;
                        break;
                    case S:
                        hsb_mode = HSB_MODE.B;
                        break;
                    case B:
                        hsb_mode = HSB_MODE.H;
                        break;
                    default:
                        throw new IncompatibleClassChangeError();
                }
                this.mode = hsb_mode;
                return true;
            }
        } else if (isMouseOverColorSlider(mouseX, mouseY) && this.showRGB) {
            if (button == 0) {
                this.isDraggingColorSlider = true;
            }
        } else if (isMouseOverAlphaSlider(mouseX, mouseY) && this.showAlpha && button == 0) {
            this.isDraggingAlphaSlider = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void refreshRGB() {
        this.argb = ColorUtils.HSBtoRGB(this.h / 360.0f, this.s, this.b);
        this.argb = ColorUtils.color(this.alpha, ColorUtils.red(this.argb), ColorUtils.green(this.argb), ColorUtils.blue(this.argb));
        if (this.onChanged != null) {
            this.onChanged.accept(this.argb);
        }
        if (isRemote() && !this.isClientSideWidget) {
            writeClientAction(-1, buffer -> {
                buffer.m_130130_(this.argb);
            });
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == -1) {
            setColor(buffer.m_130242_());
        } else {
            super.handleClientAction(id, buffer);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == -1) {
            setColor(buffer.m_130242_());
        } else {
            super.readUpdateInfo(id, buffer);
        }
    }

    private static float normalizeMouse(double mouse, int pos, int size) {
        if (mouse >= pos + size) {
            return 1.0f;
        }
        if (mouse <= pos) {
            return 0.0f;
        }
        double x = mouse - pos;
        double y = (x % size) / size;
        if (y < 0.0d) {
            x = -x;
            y = -y;
        }
        return (float) ((x / ((double) size)) % 2.0d > 1.0d ? 1.0d - y : y);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int x = getPosition().x;
        int y = getPosition().y;
        int width = getSize().width;
        int height = getSize().height;
        if (this.isDraggingMain) {
            float normalizedX = normalizeMouse(mouseX, x, (width - this.gap) - this.barWidth);
            float normalizedY = normalizeMouse(mouseY, y, (height - this.gap) - this.barWidth);
            switch (this.mode) {
                case H:
                    this.s = normalizedX;
                    this.b = 1.0f - normalizedY;
                    break;
                case S:
                    this.h = normalizedX * 360.0f;
                    this.b = 1.0f - normalizedY;
                    break;
                case B:
                    this.h = normalizedX * 360.0f;
                    this.s = 1.0f - normalizedY;
                    break;
            }
            refreshRGB();
            return true;
        } else if (this.isDraggingColorSlider) {
            float normalizedY2 = normalizeMouse(mouseY, y, (height - this.gap) - this.barWidth);
            switch (this.mode) {
                case H:
                    this.h = (1.0f - normalizedY2) * 360.0f;
                    break;
                case S:
                    this.s = 1.0f - normalizedY2;
                    break;
                case B:
                    this.b = 1.0f - normalizedY2;
                    break;
            }
            refreshRGB();
            return true;
        } else if (this.isDraggingAlphaSlider) {
            this.alpha = normalizeMouse(mouseX, x, (width - this.gap) - this.barWidth);
            refreshRGB();
            return false;
        } else {
            return false;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDraggingMain = false;
        this.isDraggingColorSlider = false;
        this.isDraggingAlphaSlider = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public HsbColorWidget setColor(int argb) {
        if (this.argb == argb) {
            return this;
        }
        this.alpha = ColorUtils.alpha(argb);
        float[] hsb = ColorUtils.RGBtoHSB(argb);
        hsb[0] = hsb[0] * 360.0f;
        this.h = hsb[0];
        this.s = hsb[1];
        this.b = hsb[2];
        refreshRGB();
        return this;
    }
}
