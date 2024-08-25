package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "selector", group = "widget.basic")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/SelectorWidget.class */
public class SelectorWidget extends WidgetGroup {
    protected List<SelectableWidgetGroup> selectables;
    @Configurable
    protected List<String> candidates;
    @Configurable
    protected String currentValue;
    @Configurable
    @NumberRange(range = {1.0d, 20.0d})
    protected int maxCount;
    @NumberColor
    @Configurable
    protected int fontColor;
    @Configurable
    protected boolean showUp;
    protected boolean isShow;
    protected IGuiTexture popUpTexture;
    private Supplier<String> supplier;
    private Consumer<String> onChanged;
    public final TextTexture textTexture;
    protected final DraggableScrollableWidgetGroup popUp;
    protected final ButtonWidget button;

    public SelectorWidget() {
        this(0, 0, 60, 15, List.of(), -1);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public void initTemplate() {
        setCandidates(new ArrayList(List.of("A", "B", "C", "D", "E", "F", "G")));
        setButtonBackground(ColorPattern.T_GRAY.rectTexture());
        setValue("D");
    }

    public SelectorWidget(int x, int y, int width, int height, List<String> candidates, int fontColor) {
        super(new Position(x, y), new Size(width, height));
        this.maxCount = 5;
        this.fontColor = -1;
        this.popUpTexture = new ColorRectTexture(-1442840576);
        TextTexture type = new TextTexture("", fontColor).setWidth(width).setType(TextTexture.TextType.ROLL);
        this.textTexture = type;
        this.button = new ButtonWidget(0, 0, width, height, type, d -> {
            if (d.isRemote) {
                setShow(!this.isShow);
            }
        });
        this.candidates = candidates;
        this.selectables = new ArrayList();
        addWidget(this.button);
        DraggableScrollableWidgetGroup draggableScrollableWidgetGroup = new DraggableScrollableWidgetGroup(0, height, width, 15);
        this.popUp = draggableScrollableWidgetGroup;
        addWidget(draggableScrollableWidgetGroup);
        this.popUp.setBackground(this.popUpTexture);
        this.popUp.setVisible(false);
        this.popUp.setActive(false);
        this.currentValue = "";
        computeLayout();
    }

    protected void computeLayout() {
        int height = Math.min(this.maxCount, this.candidates.size()) * 15;
        this.popUp.clearAllWidgets();
        this.selectables.clear();
        this.popUp.setSize(new Size(getSize().width, height));
        this.popUp.setSelfPosition(this.showUp ? new Position(0, -height) : new Position(0, getSize().height));
        if (this.candidates.size() > this.maxCount) {
            this.popUp.setYScrollBarWidth(4).setYBarStyle(null, new ColorRectTexture(-1));
        }
        int y = 0;
        int width = this.candidates.size() > this.maxCount ? getSize().width - 4 : getSize().width;
        for (String candidate : this.candidates) {
            SelectableWidgetGroup select = new SelectableWidgetGroup(0, y, width, 15);
            select.addWidget(new ImageWidget(0, 0, width, 15, new TextTexture(candidate, this.fontColor).setWidth(width).setType(TextTexture.TextType.ROLL)));
            select.setSelectedTexture(-1, -1);
            select.setOnSelected(s -> {
                setValue(candidate);
                if (this.onChanged != null) {
                    this.onChanged.accept(candidate);
                }
                writeClientAction(2, buffer -> {
                    buffer.m_130070_(candidate);
                });
                setShow(false);
            });
            this.popUp.addWidget(select);
            this.selectables.add(select);
            y += 15;
        }
        this.popUp.setScrollYOffset(0);
    }

    @ConfigSetter(field = "maxCount")
    public SelectorWidget setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        computeLayout();
        return this;
    }

    @ConfigSetter(field = "showUp")
    public SelectorWidget setIsUp(boolean isUp) {
        this.showUp = isUp;
        computeLayout();
        return this;
    }

    @ConfigSetter(field = "fontColor")
    public SelectorWidget setFontColor(int fontColor) {
        this.fontColor = fontColor;
        computeLayout();
        return this;
    }

    @ConfigSetter(field = "currentValue")
    public SelectorWidget setValue(String value) {
        if (!value.equals(this.currentValue)) {
            this.currentValue = value;
            int index = this.candidates.indexOf(value);
            this.textTexture.updateText(value);
            int i = 0;
            while (i < this.selectables.size()) {
                this.selectables.get(i).isSelected = index == i;
                i++;
            }
        }
        return this;
    }

    @ConfigSetter(field = "candidates")
    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
        computeLayout();
    }

    public SelectorWidget setButtonBackground(IGuiTexture... guiTexture) {
        super.setBackground(guiTexture);
        return this;
    }

    @ConfigSetter(field = "popUpTexture")
    public SelectorWidget setBackground(IGuiTexture background) {
        this.popUpTexture = background;
        this.popUp.setBackground(background);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void setSize(Size size) {
        super.setSize(size);
        this.button.setSize(size);
        computeLayout();
    }

    @OnlyIn(Dist.CLIENT)
    public void setShow(boolean isShow) {
        if (isShow) {
            setFocus(true);
        }
        this.isShow = isShow;
        this.popUp.setVisible(isShow);
        this.popUp.setActive(isShow);
    }

    public String getValue() {
        return this.currentValue;
    }

    public SelectorWidget setOnChanged(Consumer<String> onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    public SelectorWidget setSupplier(Supplier<String> supplier) {
        this.supplier = supplier;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public boolean isMouseOverElement(double mouseX, double mouseY) {
        return super.isMouseOverElement(mouseX, mouseY) || (this.isShow && this.popUp.isMouseOverElement(mouseX, mouseY));
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @Nullable
    public Widget getHoverElement(double mouseX, double mouseY) {
        if (isMouseOverElement(mouseX, mouseY)) {
            return this;
        }
        return null;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void onFocusChanged(@Nullable Widget lastFocus, Widget focus) {
        if (lastFocus != null && !lastFocus.isParent(this) && focus != this) {
            setShow(false);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void updateScreen() {
        ModularUIGuiContainer container;
        super.updateScreen();
        if (this.isClientSideWidget && this.supplier != null) {
            setValue(this.supplier.get());
        }
        if (this.gui != null && (container = this.gui.getModularUIGui()) != null && container.lastFocus != null && container.lastFocus.isParent(this)) {
            setFocus(true);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.isClientSideWidget && this.supplier != null) {
            String last = this.currentValue;
            setValue(this.supplier.get());
            if (!last.equals(this.currentValue)) {
                writeUpdateInfo(3, buffer -> {
                    buffer.m_130070_(this.currentValue);
                });
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        if (this.supplier != null) {
            setValue(this.supplier.get());
        }
        buffer.m_130070_(this.currentValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        setValue(buffer.m_130277_());
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        boolean lastVisible = this.popUp.isVisible();
        this.popUp.setVisible(false);
        super.drawInForeground(matrixStack, mouseX, mouseY, partialTicks);
        this.popUp.setVisible(lastVisible);
        if (this.isShow) {
            matrixStack.m_85837_(0.0d, 0.0d, 200.0d);
            this.popUp.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
            this.popUp.drawInForeground(matrixStack, mouseX, mouseY, partialTicks);
            matrixStack.m_85837_(0.0d, 0.0d, -200.0d);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        boolean lastVisible = this.popUp.isVisible();
        this.popUp.setVisible(false);
        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        this.popUp.setVisible(lastVisible);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            setFocus(false);
            return false;
        }
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 2) {
            setValue(buffer.m_130277_());
            if (this.onChanged != null) {
                this.onChanged.accept(getValue());
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 3) {
            setValue(buffer.m_130277_());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup
    public void addWidgetsConfigurator(ConfiguratorGroup father) {
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidgetGroup
    public boolean canWidgetAccepted(IConfigurableWidget widget) {
        return false;
    }
}
