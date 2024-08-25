package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "text_field", group = "widget.basic")
@Configurable(name = "ldlib.gui.editor.register.widget.text_field", collapse = false)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/TextFieldWidget.class */
public class TextFieldWidget extends Widget implements IConfigurableWidget {
    @OnlyIn(Dist.CLIENT)
    protected EditBox textField;
    @Configurable
    @NumberRange(range = {0.0d, 2.147483647E9d})
    protected int maxStringLength;
    protected Function<String, String> textValidator;
    protected Supplier<String> textSupplier;
    protected Consumer<String> textResponder;
    @Configurable
    protected String currentString;
    @Configurable
    protected boolean isBordered;
    @NumberColor
    @Configurable
    protected int textColor;
    protected float wheelDur;
    protected NumberFormat numberInstance;
    protected Component hover;
    private boolean isDragging;

    public TextFieldWidget() {
        this(0, 0, 60, 15, null, null);
    }

    public TextFieldWidget(int xPosition, int yPosition, int width, int height, Supplier<String> textSupplier, Consumer<String> textResponder) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.maxStringLength = Integer.MAX_VALUE;
        this.textValidator = s -> {
            return s;
        };
        this.textColor = -1;
        if (isRemote()) {
            Font fontRenderer = Minecraft.m_91087_().f_91062_;
            this.textField = new EditBox(fontRenderer, xPosition, yPosition, width, height, Component.m_237113_("text field"));
            this.textField.m_94182_(true);
            this.isBordered = true;
            this.textField.m_94199_(this.maxStringLength);
            this.textField.m_94151_(this::onTextChanged);
        }
        this.textSupplier = textSupplier;
        this.textResponder = textResponder;
    }

    public TextFieldWidget setTextSupplier(Supplier<String> textSupplier) {
        this.textSupplier = textSupplier;
        return this;
    }

    public TextFieldWidget setTextResponder(Consumer<String> textResponder) {
        this.textResponder = textResponder;
        return this;
    }

    public TextFieldWidget setBackground(IGuiTexture background) {
        super.setBackground(background);
        return this;
    }

    @ConfigSetter(field = "currentString")
    public TextFieldWidget setCurrentString(Object currentString) {
        this.currentString = currentString.toString();
        if (isRemote() && !this.textField.m_94155_().equals(currentString)) {
            this.textField.m_94144_(currentString.toString());
        }
        return this;
    }

    public String getCurrentString() {
        return this.currentString == null ? "" : this.currentString;
    }

    public String getRawCurrentString() {
        if (isRemote()) {
            return this.textField.m_94155_();
        }
        return getCurrentString();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void onFocusChanged(@Nullable Widget lastFocus, Widget focus) {
        if (!isFocus()) {
            this.textField.m_94178_(false);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    protected void onPositionUpdate() {
        int i;
        if (isRemote() && this.textField != null) {
            Position position = getPosition();
            Size size = getSize();
            this.textField.f_93620_ = this.isBordered ? position.x : position.x + 2;
            EditBox editBox = this.textField;
            if (this.isBordered) {
                i = position.y;
            } else {
                int i2 = position.y;
                int i3 = size.height;
                Objects.requireNonNull(Minecraft.m_91087_().f_91062_);
                i = i2 + ((i3 - 9) / 2) + 1;
            }
            editBox.f_93621_ = i;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    protected void onSizeUpdate() {
        int i;
        if (isRemote() && this.textField != null) {
            Position position = getPosition();
            Size size = getSize();
            this.textField.m_93674_(this.isBordered ? size.width : size.width - 2);
            EditBox editBox = this.textField;
            if (this.isBordered) {
                i = position.y;
            } else {
                int i2 = position.y;
                int i3 = getSize().height;
                Objects.requireNonNull(Minecraft.m_91087_().f_91062_);
                i = i2 + ((i3 - 9) / 2) + 1;
            }
            editBox.f_93621_ = i;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        this.textField.m_6305_(matrixStack, mouseX, mouseY, partialTicks);
        RenderSystem.m_69458_(false);
        RenderSystem.m_69478_();
        RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            this.isDragging = true;
        }
        setFocus(isMouseOverElement(mouseX, mouseY));
        return this.textField.m_6375_(mouseX, mouseY, button);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return keyCode != 256 && (this.textField.m_7933_(keyCode, scanCode, modifiers) || isFocus());
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public boolean charTyped(char codePoint, int modifiers) {
        return this.textField.m_5534_(codePoint, modifiers);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void updateScreen() {
        if (isVisible() && isActive() && this.textSupplier != null && this.isClientSideWidget && !this.textSupplier.get().equals(getCurrentString())) {
            setCurrentString(this.textSupplier.get());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.m_130070_(getCurrentString());
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        setCurrentString(buffer.m_130277_());
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.textSupplier != null && !this.textSupplier.get().equals(getCurrentString())) {
            setCurrentString(this.textSupplier.get());
            writeUpdateInfo(1, buffer -> {
                buffer.m_130070_(getCurrentString());
            });
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            setCurrentString(buffer.m_130277_());
        }
    }

    protected void onTextChanged(String newTextString) {
        String lastText = this.currentString;
        String newText = this.textValidator.apply(newTextString);
        if (!newText.equals(lastText)) {
            this.textField.m_94202_(this.textColor);
            setCurrentString(newText);
            if (this.isClientSideWidget && this.textResponder != null) {
                this.textResponder.accept(newText);
            }
            writeClientAction(1, buffer -> {
                buffer.m_130070_(newText);
            });
        } else if (!newTextString.equals(newText)) {
            this.textField.m_94202_(-2162688);
        } else {
            this.textField.m_94202_(this.textColor);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            String lastText = getCurrentString();
            String newText = this.textValidator.apply(buffer.m_130277_());
            String newText2 = newText.substring(0, Math.min(newText.length(), this.maxStringLength));
            if (lastText == null || !lastText.equals(newText2)) {
                setCurrentString(newText2);
                if (this.textResponder != null) {
                    this.textResponder.accept(newText2);
                }
            }
        }
    }

    @ConfigSetter(field = "isBordered")
    public TextFieldWidget setBordered(boolean bordered) {
        this.isBordered = bordered;
        if (isRemote()) {
            this.textField.m_94182_(bordered);
            onPositionUpdate();
            onSizeUpdate();
        }
        return this;
    }

    @ConfigSetter(field = "textColor")
    public TextFieldWidget setTextColor(int textColor) {
        this.textColor = textColor;
        if (isRemote()) {
            this.textField.m_94202_(textColor);
        }
        return this;
    }

    @ConfigSetter(field = "maxStringLength")
    public TextFieldWidget setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
        if (isRemote()) {
            this.textField.m_94199_(maxStringLength);
        }
        return this;
    }

    public TextFieldWidget setValidator(Function<String, String> validator) {
        this.textValidator = validator;
        return this;
    }

    public TextFieldWidget setResourceLocationOnly() {
        setValidator(s -> {
            try {
                String s = s.toLowerCase().replace(' ', '_');
                if (ResourceLocation.m_135830_(s)) {
                    return s;
                }
            } catch (NumberFormatException e) {
            }
            return this.currentString;
        });
        this.hover = Component.m_237115_("ldlib.gui.text_field.resourcelocation");
        return this;
    }

    public TextFieldWidget setNumbersOnly(long minValue, long maxValue) {
        setValidator(s -> {
            if (maxValue != null) {
                try {
                    if (!maxValue.isEmpty()) {
                        long value = Long.parseLong(maxValue);
                        return (minValue > value || value > minValue) ? value < minValue ? minValue : minValue : maxValue;
                    }
                } catch (NumberFormatException e) {
                    return this.currentString;
                }
            }
            return minValue;
        });
        if (minValue == Long.MIN_VALUE && maxValue == Long.MAX_VALUE) {
            this.hover = Component.m_237115_("ldlib.gui.text_field.number.3");
        } else if (minValue == Long.MIN_VALUE) {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.2", new Object[]{Long.valueOf(maxValue)});
        } else if (maxValue == Long.MAX_VALUE) {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.1", new Object[]{Long.valueOf(minValue)});
        } else {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.0", new Object[]{Long.valueOf(minValue), Long.valueOf(maxValue)});
        }
        return setWheelDur(1.0f);
    }

    public TextFieldWidget setNumbersOnly(int minValue, int maxValue) {
        setValidator(s -> {
            if (maxValue != null) {
                try {
                    if (!maxValue.isEmpty()) {
                        int value = Integer.parseInt(maxValue);
                        return (minValue > value || value > minValue) ? value < minValue ? minValue : minValue : maxValue;
                    }
                } catch (NumberFormatException e) {
                    return this.currentString;
                }
            }
            return minValue;
        });
        if (minValue == Integer.MIN_VALUE && maxValue == Integer.MAX_VALUE) {
            this.hover = Component.m_237115_("ldlib.gui.text_field.number.3");
        } else if (minValue == Integer.MIN_VALUE) {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.2", new Object[]{Integer.valueOf(maxValue)});
        } else if (maxValue == Integer.MAX_VALUE) {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.1", new Object[]{Integer.valueOf(minValue)});
        } else {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.0", new Object[]{Integer.valueOf(minValue), Integer.valueOf(maxValue)});
        }
        return setWheelDur(1.0f);
    }

    public TextFieldWidget setNumbersOnly(float minValue, float maxValue) {
        setValidator(s -> {
            if (maxValue != null) {
                try {
                    if (!maxValue.isEmpty()) {
                        float value = Float.parseFloat(maxValue);
                        return (minValue > value || value > minValue) ? value < minValue ? minValue : minValue : maxValue;
                    }
                } catch (NumberFormatException e) {
                    return this.currentString;
                }
            }
            return minValue;
        });
        if (minValue == -3.4028235E38f && maxValue == Float.MAX_VALUE) {
            this.hover = Component.m_237115_("ldlib.gui.text_field.number.3");
        } else if (minValue == -3.4028235E38f) {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.2", new Object[]{Float.valueOf(maxValue)});
        } else if (maxValue == Float.MAX_VALUE) {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.1", new Object[]{Float.valueOf(minValue)});
        } else {
            this.hover = Component.m_237110_("ldlib.gui.text_field.number.0", new Object[]{Float.valueOf(minValue), Float.valueOf(maxValue)});
        }
        return setWheelDur(0.1f);
    }

    public TextFieldWidget setWheelDur(float wheelDur) {
        this.wheelDur = wheelDur;
        this.numberInstance = NumberFormat.getNumberInstance();
        this.numberInstance.setMaximumFractionDigits(4);
        return this;
    }

    public TextFieldWidget setWheelDur(int digits, float wheelDur) {
        this.wheelDur = wheelDur;
        this.numberInstance = NumberFormat.getNumberInstance();
        this.numberInstance.setMaximumFractionDigits(digits);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        if (this.wheelDur > 0.0f && this.numberInstance != null && isMouseOverElement(mouseX, mouseY) && isFocus()) {
            try {
                onTextChanged(this.numberInstance.format(Float.parseFloat(getCurrentString()) + ((wheelDelta > 0.0d ? 1 : -1) * this.wheelDur)));
            } catch (Exception e) {
            }
            setFocus(true);
            return true;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isDragging && this.numberInstance != null && isFocus()) {
            try {
                onTextChanged(this.numberInstance.format(Float.parseFloat(getCurrentString()) + (dragX * this.wheelDur)));
            } catch (Exception e) {
            }
            setFocus(true);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (isMouseOverElement(mouseX, mouseY) && this.gui != null && this.gui.getModularUIGui() != null) {
            List<Component> tips = new ArrayList<>();
            if (this.tooltipTexts != null) {
                tips.addAll(this.tooltipTexts);
            }
            if (this.hover != null) {
                tips.add(this.hover);
            }
            if (this.wheelDur > 0.0f && this.numberInstance != null && isFocus()) {
                tips.add(Component.m_237110_("ldlib.gui.text_field.number.wheel", new Object[]{this.numberInstance.format(this.wheelDur)}));
            }
            if (!tips.isEmpty()) {
                this.gui.getModularUIGui().setHoverTooltip(tips, ItemStack.f_41583_, null, null);
            }
        }
    }
}
