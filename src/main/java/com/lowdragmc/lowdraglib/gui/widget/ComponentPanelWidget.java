package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/ComponentPanelWidget.class */
public class ComponentPanelWidget extends Widget {
    protected int maxWidthLimit;
    @Nullable
    protected Consumer<List<Component>> textSupplier;
    protected BiConsumer<String, ClickData> clickHandler;
    protected List<Component> lastText;
    protected List<FormattedCharSequence> cacheLines;
    protected boolean isCenter;
    protected int space;

    public ComponentPanelWidget textSupplier(@Nullable Consumer<List<Component>> textSupplier) {
        this.textSupplier = textSupplier;
        return this;
    }

    public ComponentPanelWidget clickHandler(BiConsumer<String, ClickData> clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public List<FormattedCharSequence> cacheLines() {
        return this.cacheLines;
    }

    public ComponentPanelWidget(int x, int y, @Nonnull Consumer<List<Component>> textSupplier) {
        super(x, y, 0, 0);
        this.lastText = new ArrayList();
        this.cacheLines = Collections.emptyList();
        this.isCenter = false;
        this.space = 2;
        this.textSupplier = textSupplier;
        this.textSupplier.accept(this.lastText);
    }

    public ComponentPanelWidget(int x, int y, List<Component> text) {
        super(x, y, 0, 0);
        this.lastText = new ArrayList();
        this.cacheLines = Collections.emptyList();
        this.isCenter = false;
        this.space = 2;
        this.lastText.addAll(text);
    }

    public static Component withButton(Component textComponent, String componentData) {
        Style style = textComponent.m_7383_();
        return textComponent.m_6881_().m_130948_(style.m_131142_(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + componentData)).m_131140_(ChatFormatting.YELLOW));
    }

    public static Component withButton(Component textComponent, String componentData, int color) {
        Style style = textComponent.m_7383_();
        return textComponent.m_6881_().m_130948_(style.m_131142_(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + componentData)).m_178520_(color));
    }

    public static Component withHoverTextTranslate(Component textComponent, Component hover) {
        Style style = textComponent.m_7383_();
        return textComponent.m_6881_().m_130948_(style.m_131144_(new HoverEvent(HoverEvent.Action.f_130831_, hover)));
    }

    public ComponentPanelWidget setMaxWidthLimit(int maxWidthLimit) {
        this.maxWidthLimit = maxWidthLimit;
        if (isRemote()) {
            formatDisplayText();
            updateComponentTextSize();
        }
        return this;
    }

    public ComponentPanelWidget setCenter(boolean center) {
        this.isCenter = center;
        if (isRemote()) {
            formatDisplayText();
            updateComponentTextSize();
        }
        return this;
    }

    public ComponentPanelWidget setSpace(int space) {
        this.space = space;
        if (isRemote()) {
            formatDisplayText();
            updateComponentTextSize();
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.m_130130_(this.lastText.size());
        for (Component textComponent : this.lastText) {
            buffer.m_130083_(textComponent);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        readUpdateInfo(1, buffer);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        super.initWidget();
        if (this.textSupplier != null) {
            this.lastText.clear();
            this.textSupplier.accept(this.lastText);
        }
        if (this.isClientSideWidget && isRemote()) {
            formatDisplayText();
            updateComponentTextSize();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void updateScreen() {
        super.updateScreen();
        if (this.isClientSideWidget && this.textSupplier != null) {
            List<Component> textBuffer = new ArrayList<>();
            this.textSupplier.accept(textBuffer);
            if (!this.lastText.equals(textBuffer)) {
                this.lastText = textBuffer;
                formatDisplayText();
                updateComponentTextSize();
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.textSupplier != null) {
            List<Component> textBuffer = new ArrayList<>();
            this.textSupplier.accept(textBuffer);
            if (!this.lastText.equals(textBuffer)) {
                this.lastText = textBuffer;
                writeUpdateInfo(1, buffer -> {
                    buffer.m_130130_(this.lastText.size());
                    for (Component textComponent : this.lastText) {
                        buffer.m_130083_(textComponent);
                    }
                });
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 1) {
            this.lastText.clear();
            int count = buffer.m_130242_();
            for (int i = 0; i < count; i++) {
                this.lastText.add(buffer.m_130238_());
            }
            formatDisplayText();
            updateComponentTextSize();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            String componentData = buffer.m_130277_();
            if (this.clickHandler != null) {
                this.clickHandler.accept(componentData, clickData);
                return;
            }
            return;
        }
        super.handleClientAction(id, buffer);
    }

    @OnlyIn(Dist.CLIENT)
    public void updateComponentTextSize() {
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        int size = this.cacheLines.size();
        Objects.requireNonNull(fontRenderer);
        int totalHeight = size * (9 + this.space);
        if (totalHeight > 0) {
            totalHeight -= this.space;
        }
        if (this.isCenter) {
            setSize(new Size(this.maxWidthLimit, totalHeight));
            return;
        }
        int maxStringWidth = 0;
        for (FormattedCharSequence line : this.cacheLines) {
            maxStringWidth = Math.max(fontRenderer.m_92724_(line), maxStringWidth);
        }
        setSize(new Size(this.maxWidthLimit == 0 ? maxStringWidth : Math.min(this.maxWidthLimit, maxStringWidth), totalHeight));
    }

    @OnlyIn(Dist.CLIENT)
    public void formatDisplayText() {
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        int maxTextWidthResult = this.maxWidthLimit == 0 ? Integer.MAX_VALUE : this.maxWidthLimit;
        this.cacheLines = this.lastText.stream().flatMap(component -> {
            return ComponentRenderUtils.m_94005_(component, maxTextWidthResult, fontRenderer).stream();
        }).toList();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected Style getStyleUnderMouse(double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        Position position = getPosition();
        Size size = getSize();
        Objects.requireNonNull(fontRenderer);
        double selectedLine = (mouseY - position.y) / (9 + this.space);
        if (this.isCenter) {
            if (selectedLine >= 0.0d && selectedLine < this.cacheLines.size()) {
                FormattedCharSequence cacheLine = this.cacheLines.get((int) selectedLine);
                int lineWidth = fontRenderer.m_92724_(cacheLine);
                float offsetX = position.x + ((size.width - lineWidth) / 2.0f);
                if (mouseX >= offsetX) {
                    int mouseOffset = (int) (mouseX - position.x);
                    return fontRenderer.m_92865_().m_92338_(cacheLine, mouseOffset);
                }
                return null;
            }
            return null;
        } else if (mouseX >= position.x && selectedLine >= 0.0d && selectedLine < this.cacheLines.size()) {
            FormattedCharSequence cacheLine2 = this.cacheLines.get((int) selectedLine);
            int mouseOffset2 = (int) (mouseX - position.x);
            return fontRenderer.m_92865_().m_92338_(cacheLine2, mouseOffset2);
        } else {
            return null;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Style style = getStyleUnderMouse(mouseX, mouseY);
        if (style != null && style.m_131182_() != null) {
            ClickEvent clickEvent = style.m_131182_();
            String componentText = clickEvent.m_130623_();
            if (clickEvent.m_130622_() == ClickEvent.Action.OPEN_URL) {
                if (componentText.startsWith("@!")) {
                    String rawText = componentText.substring(2);
                    ClickData clickData = new ClickData();
                    if (this.clickHandler != null) {
                        this.clickHandler.accept(rawText, clickData);
                    }
                    writeClientAction(1, buf -> {
                        clickData.writeToBuf(buf);
                        buf.m_130070_(rawText);
                    });
                } else if (componentText.startsWith("@#")) {
                    Util.m_137581_().m_137646_(componentText.substring(2));
                }
                playButtonClickSound();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Style style = getStyleUnderMouse(mouseX, mouseY);
        if (style != null && style.m_131186_() != null) {
            HoverEvent hoverEvent = style.m_131186_();
            Component hoverTips = (Component) hoverEvent.m_130823_(HoverEvent.Action.f_130831_);
            if (hoverTips != null) {
                this.gui.getModularUIGui().setHoverTooltip(List.of(hoverTips), ItemStack.f_41583_, null, null);
                return;
            }
        }
        super.drawInForeground(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        Position position = getPosition();
        Size size = getSize();
        for (int i = 0; i < this.cacheLines.size(); i++) {
            FormattedCharSequence cacheLine = this.cacheLines.get(i);
            if (this.isCenter) {
                int lineWidth = fontRenderer.m_92724_(cacheLine);
                int i2 = position.y;
                Objects.requireNonNull(fontRenderer);
                fontRenderer.m_92877_(poseStack, cacheLine, position.x + ((size.width - lineWidth) / 2.0f), i2 + (i * (9 + this.space)), -1);
            } else {
                int i3 = position.y;
                Objects.requireNonNull(fontRenderer);
                fontRenderer.m_92877_(poseStack, cacheLine, position.x, i3 + (i * (9 + this.space)), -1);
            }
        }
    }
}
