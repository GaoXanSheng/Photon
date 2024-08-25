package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "switch", group = "widget.container")
@Configurable(name = "ldlib.gui.editor.register.widget.switch", collapse = false)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/SwitchWidget.class */
public class SwitchWidget extends Widget implements IConfigurableWidget {
    @Configurable
    protected IGuiTexture baseTexture;
    @Configurable
    protected IGuiTexture pressedTexture;
    @Configurable
    protected IGuiTexture hoverTexture;
    @Configurable
    protected boolean isPressed;
    protected BiConsumer<ClickData, Boolean> onPressCallback;
    protected Supplier<Boolean> supplier;

    public SwitchWidget() {
        this(0, 0, 40, 20, null);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public void initTemplate() {
        setTexture(new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("off")), new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("on")));
        setHoverBorderTexture(1, -1);
    }

    public SwitchWidget(int xPosition, int yPosition, int width, int height, BiConsumer<ClickData, Boolean> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
    }

    public void setOnPressCallback(BiConsumer<ClickData, Boolean> onPressCallback) {
        this.onPressCallback = onPressCallback;
    }

    public SwitchWidget setTexture(IGuiTexture baseTexture, IGuiTexture pressedTexture) {
        setBaseTexture(baseTexture);
        setPressedTexture(pressedTexture);
        return this;
    }

    public SwitchWidget setBaseTexture(IGuiTexture... baseTexture) {
        this.baseTexture = new GuiTextureGroup(baseTexture);
        return this;
    }

    public SwitchWidget setPressedTexture(IGuiTexture... pressedTexture) {
        this.pressedTexture = new GuiTextureGroup(pressedTexture);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public SwitchWidget setHoverTexture(IGuiTexture... hoverTexture) {
        this.hoverTexture = new GuiTextureGroup(hoverTexture);
        return this;
    }

    public SwitchWidget setHoverBorderTexture(int border, int color) {
        this.hoverTexture = new ColorBorderTexture(border, color);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        if (this.baseTexture != null) {
            this.baseTexture.updateTick();
        }
        if (this.pressedTexture != null) {
            this.pressedTexture.updateTick();
        }
        if (this.hoverTexture != null) {
            this.hoverTexture.updateTick();
        }
        if (this.isClientSideWidget && this.supplier != null) {
            setPressed(this.supplier.get().booleanValue());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.isPressed);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        this.isPressed = buffer.readBoolean();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.isClientSideWidget && this.supplier != null) {
            setPressed(this.supplier.get().booleanValue());
        }
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public SwitchWidget setPressed(boolean isPressed) {
        if (this.isPressed == isPressed) {
            return this;
        }
        this.isPressed = isPressed;
        if (this.gui == null) {
            return this;
        }
        if (isRemote()) {
            writeClientAction(2, buffer -> {
                buffer.writeBoolean(isPressed);
            });
        } else {
            writeUpdateInfo(2, buffer2 -> {
                buffer2.writeBoolean(isPressed);
            });
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Position position = getPosition();
        Size size = getSize();
        if (this.baseTexture != null && !this.isPressed) {
            this.baseTexture.draw(matrixStack, mouseX, mouseY, position.x, position.y, size.width, size.height);
        } else if (this.pressedTexture != null && this.isPressed) {
            this.pressedTexture.draw(matrixStack, mouseX, mouseY, position.x, position.y, size.width, size.height);
        }
        if (isMouseOverElement(mouseX, mouseY) && this.hoverTexture != null) {
            this.hoverTexture.draw(matrixStack, mouseX, mouseY, position.x, position.y, size.width, size.height);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            ClickData clickData = new ClickData();
            this.isPressed = !this.isPressed;
            writeClientAction(1, buffer -> {
                clickData.writeToBuf(clickData);
                clickData.writeBoolean(this.isPressed);
            });
            if (this.onPressCallback != null) {
                this.onPressCallback.accept(clickData, Boolean.valueOf(this.isPressed));
            }
            playButtonClickSound();
            return true;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            if (this.onPressCallback != null) {
                BiConsumer<ClickData, Boolean> biConsumer = this.onPressCallback;
                ClickData readFromBuf = ClickData.readFromBuf(buffer);
                boolean readBoolean = buffer.readBoolean();
                this.isPressed = readBoolean;
                biConsumer.accept(readFromBuf, Boolean.valueOf(readBoolean));
            }
        } else if (id == 2) {
            this.isPressed = buffer.readBoolean();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 2) {
            this.isPressed = buffer.readBoolean();
        } else {
            super.readUpdateInfo(id, buffer);
        }
    }

    public SwitchWidget setSupplier(Supplier<Boolean> supplier) {
        this.supplier = supplier;
        return this;
    }
}
