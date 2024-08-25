package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.GuiTextureConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.UIResourceTexture;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@LDLRegister(name = "progress", group = "widget.basic")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/ProgressWidget.class */
public class ProgressWidget extends Widget implements IConfigurableWidget {
    public static final DoubleSupplier JEIProgress = () -> {
        return Math.abs(System.currentTimeMillis() % 2000) / 2000.0d;
    };
    public DoubleSupplier progressSupplier;
    private Function<Double, String> dynamicHoverTips;
    @Configurable
    private IGuiTexture progressTexture;
    @Configurable
    private IGuiTexture overlayTexture;
    private double lastProgressValue;

    public ProgressWidget setProgressSupplier(DoubleSupplier progressSupplier) {
        this.progressSupplier = progressSupplier;
        return this;
    }

    public ProgressWidget setDynamicHoverTips(Function<Double, String> dynamicHoverTips) {
        this.dynamicHoverTips = dynamicHoverTips;
        return this;
    }

    public ProgressWidget setProgressTexture(IGuiTexture progressTexture) {
        this.progressTexture = progressTexture;
        return this;
    }

    public ProgressWidget setOverlayTexture(IGuiTexture overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    public ProgressWidget() {
        this(JEIProgress, 0, 0, 40, 40, new ProgressTexture());
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height, ResourceTexture fullImage) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
        this.progressTexture = new ProgressTexture(fullImage.getSubTexture(0.0d, 0.0d, 1.0d, 0.5d), fullImage.getSubTexture(0.0d, 0.5d, 1.0d, 0.5d));
        this.lastProgressValue = -1.0d;
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height, ProgressTexture progressBar) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
        this.progressTexture = progressBar;
        this.lastProgressValue = -1.0d;
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
    }

    public ProgressWidget setProgressTexture(IGuiTexture emptyBarArea, IGuiTexture filledBarArea) {
        this.progressTexture = new ProgressTexture(emptyBarArea, filledBarArea);
        return this;
    }

    public ProgressWidget setFillDirection(ProgressTexture.FillDirection fillDirection) {
        IGuiTexture iGuiTexture = this.progressTexture;
        if (iGuiTexture instanceof ProgressTexture) {
            ProgressTexture progressTexture = (ProgressTexture) iGuiTexture;
            progressTexture.setFillDirection(fillDirection);
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void updateScreen() {
        super.updateScreen();
        if (this.progressTexture != null) {
            this.progressTexture.updateTick();
        }
        if (this.overlayTexture != null) {
            this.overlayTexture.updateTick();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if ((!this.tooltipTexts.isEmpty() || this.dynamicHoverTips != null) && isMouseOverElement(mouseX, mouseY) && getHoverElement(mouseX, mouseY) == this && this.gui != null && this.gui.getModularUIGui() != null) {
            ArrayList<Component> tips = new ArrayList<>(this.tooltipTexts);
            if (this.dynamicHoverTips != null) {
                tips.add(Component.m_237115_(this.dynamicHoverTips.apply(Double.valueOf(this.lastProgressValue))));
            }
            this.gui.getModularUIGui().setHoverTooltip(tips, ItemStack.f_41583_, null, null);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(poseStack, mouseX, mouseY, partialTicks);
        if (this.progressSupplier == JEIProgress || this.isClientSideWidget) {
            this.lastProgressValue = this.progressSupplier.getAsDouble();
        }
        IGuiTexture iGuiTexture = this.progressTexture;
        if (iGuiTexture instanceof ProgressTexture) {
            ProgressTexture texture = (ProgressTexture) iGuiTexture;
            texture.setProgress(this.lastProgressValue);
        }
        Position pos = getPosition();
        Size size = getSize();
        if (this.progressTexture != null) {
            this.progressTexture.draw(poseStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
        if (this.overlayTexture != null) {
            this.overlayTexture.draw(poseStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void initWidget() {
        super.initWidget();
        this.lastProgressValue = this.progressSupplier.getAsDouble();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.writeDouble(this.lastProgressValue);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        this.lastProgressValue = buffer.readDouble();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        double actualValue = this.progressSupplier.getAsDouble();
        if (actualValue - this.lastProgressValue != 0.0d) {
            this.lastProgressValue = actualValue;
            writeUpdateInfo(0, buffer -> {
                buffer.writeDouble(actualValue);
            });
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 0) {
            this.lastProgressValue = buffer.readDouble();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        super.buildConfigurator(father);
        for (Configurator configurator : father.getConfigurators()) {
            if (configurator instanceof GuiTextureConfigurator) {
                GuiTextureConfigurator guiConfigurator = (GuiTextureConfigurator) configurator;
                if (configurator.getName().equals("progressTexture")) {
                    guiConfigurator.setOnUpdate(t -> {
                        if (!(t instanceof ProgressTexture)) {
                            if (!(t instanceof UIResourceTexture)) {
                                return;
                            }
                            UIResourceTexture uiResourceTexture = (UIResourceTexture) t;
                            if (!(uiResourceTexture.getTexture() instanceof ProgressTexture)) {
                                return;
                            }
                        }
                        this.progressTexture = t;
                    });
                    guiConfigurator.setAvailable(t2 -> {
                        if (!(t2 instanceof ProgressTexture)) {
                            if (t2 instanceof UIResourceTexture) {
                                UIResourceTexture uiResourceTexture = (UIResourceTexture) t2;
                                if (uiResourceTexture.getTexture() instanceof ProgressTexture) {
                                }
                            }
                            return false;
                        }
                        return true;
                    });
                    guiConfigurator.setTips("ldlib.gui.editor.tips.progress_texture");
                }
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public boolean canDragIn(Object dragging) {
        if (dragging instanceof IGuiTexture) {
            if (!(dragging instanceof ProgressTexture)) {
                if (dragging instanceof UIResourceTexture) {
                    UIResourceTexture uiResourceTexture = (UIResourceTexture) dragging;
                    if (uiResourceTexture.getTexture() instanceof ProgressTexture) {
                    }
                }
                return false;
            }
            return true;
        }
        return IConfigurableWidget.super.canDragIn(dragging);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public boolean handleDragging(Object dragging) {
        if (dragging instanceof IGuiTexture) {
            if (!(dragging instanceof ProgressTexture)) {
                if (!(dragging instanceof UIResourceTexture)) {
                    return false;
                }
                UIResourceTexture uiResourceTexture = (UIResourceTexture) dragging;
                if (!(uiResourceTexture.getTexture() instanceof ProgressTexture)) {
                    return false;
                }
            }
            this.progressTexture = (IGuiTexture) dragging;
            return true;
        }
        return super.handleDragging(dragging);
    }
}
