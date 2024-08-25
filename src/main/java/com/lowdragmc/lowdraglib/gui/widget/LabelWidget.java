package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "label", group = "widget.basic")
@Configurable(name = "ldlib.gui.editor.register.widget.label", collapse = false)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/LabelWidget.class */
public class LabelWidget extends Widget implements IConfigurableWidget {
    @Nonnull
    protected Supplier<String> textSupplier;
    @Nullable
    protected Component component;
    @Configurable(name = "ldlib.gui.editor.name.text")
    private String lastTextValue;
    @NumberColor
    @Configurable
    private int color;
    @Configurable
    private boolean dropShadow;

    public void setTextSupplier(@Nonnull Supplier<String> textSupplier) {
        if (textSupplier == null) {
            throw new NullPointerException("textSupplier is marked non-null but is null");
        }
        this.textSupplier = textSupplier;
    }

    public void setComponent(@Nullable Component component) {
        this.component = component;
    }

    public LabelWidget() {
        this(0, 0, "label");
    }

    public LabelWidget(int xPosition, int yPosition, String text) {
        this(xPosition, yPosition, () -> {
            return text;
        });
    }

    public LabelWidget(int xPosition, int yPosition, Component component) {
        super(new Position(xPosition, yPosition), new Size(10, 10));
        this.lastTextValue = "";
        setDropShadow(true);
        setTextColor(-1);
        this.component = component;
        if (isRemote()) {
            this.lastTextValue = component.getString();
            updateSize();
        }
    }

    public LabelWidget(int xPosition, int yPosition, Supplier<String> text) {
        super(new Position(xPosition, yPosition), new Size(10, 10));
        this.lastTextValue = "";
        setDropShadow(true);
        setTextColor(-1);
        this.textSupplier = text;
        if (isRemote()) {
            this.lastTextValue = text.get();
            updateSize();
        }
    }

    @ConfigSetter(field = "lastTextValue")
    public void setText(String text) {
        this.textSupplier = () -> {
            return text;
        };
    }

    public LabelWidget setTextColor(int color) {
        this.color = color;
        if (this.component != null) {
            this.component = this.component.m_6881_().m_130948_(this.component.m_7383_().m_178520_(color));
        }
        return this;
    }

    public LabelWidget setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        if (!this.isClientSideWidget) {
            if (this.component != null) {
                buffer.writeBoolean(true);
                buffer.m_130083_(this.component);
                return;
            }
            buffer.writeBoolean(false);
            this.lastTextValue = this.textSupplier.get();
            buffer.m_130070_(this.lastTextValue);
            return;
        }
        buffer.writeBoolean(false);
        buffer.m_130070_(this.lastTextValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        if (buffer.readBoolean()) {
            this.component = buffer.m_130238_();
            this.lastTextValue = this.component.getString();
            return;
        }
        this.lastTextValue = buffer.m_130277_();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.isClientSideWidget) {
            if (this.component != null) {
                String latest = this.component.getString();
                if (!latest.equals(this.lastTextValue)) {
                    this.lastTextValue = latest;
                    writeUpdateInfo(-2, buffer -> {
                        buffer.m_130083_(this.component);
                    });
                }
            }
            String latest2 = this.textSupplier.get();
            if (!latest2.equals(this.lastTextValue)) {
                this.lastTextValue = latest2;
                writeUpdateInfo(-1, buffer2 -> {
                    buffer2.m_130070_(this.lastTextValue);
                });
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == -1) {
            this.lastTextValue = buffer.m_130277_();
            updateSize();
        } else if (id == -2) {
            this.component = buffer.m_130238_();
            this.lastTextValue = this.component.getString();
            updateSize();
        } else {
            super.readUpdateInfo(id, buffer);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        super.updateScreen();
        if (this.isClientSideWidget) {
            String latest = this.component == null ? this.textSupplier.get() : this.component.getString();
            if (!latest.equals(this.lastTextValue)) {
                this.lastTextValue = latest;
                updateSize();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void updateSize() {
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        int m_92895_ = this.component == null ? fontRenderer.m_92895_(LocalizationUtils.format(this.lastTextValue, new Object[0])) : fontRenderer.m_92852_(this.component);
        Objects.requireNonNull(fontRenderer);
        setSize(new Size(m_92895_, 9));
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        Font fontRenderer = Minecraft.m_91087_().f_91062_;
        Position position = getPosition();
        if (this.component == null) {
            String suppliedText = LocalizationUtils.format(this.lastTextValue, new Object[0]);
            String[] split = suppliedText.split("\n");
            for (int i = 0; i < split.length; i++) {
                int i2 = position.y;
                Objects.requireNonNull(fontRenderer);
                int y = i2 + (i * (9 + 2));
                if (this.dropShadow) {
                    fontRenderer.m_92750_(poseStack, split[i], position.x, y, this.color);
                } else {
                    fontRenderer.m_92883_(poseStack, split[i], position.x, y, this.color);
                }
            }
        } else if (this.dropShadow) {
            fontRenderer.m_92763_(poseStack, this.component, position.x, position.y, this.color);
        } else {
            fontRenderer.m_92889_(poseStack, this.component, position.x, position.y, this.color);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public boolean handleDragging(Object dragging) {
        if (dragging instanceof String) {
            String string = (String) dragging;
            setText(string);
            return true;
        }
        return super.handleDragging(dragging);
    }
}
