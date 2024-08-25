package com.lowdragmc.lowdraglib.jei;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/jei/ModularWrapper.class */
public class ModularWrapper<T extends Widget> extends ModularUIGuiContainer {
    protected T widget;
    protected boolean shouldRenderTooltips;
    private int lastTick;
    private int left;
    private int top;

    public void setShouldRenderTooltips(boolean shouldRenderTooltips) {
        this.shouldRenderTooltips = shouldRenderTooltips;
    }

    public ModularWrapper(T widget) {
        super(new ModularUI(widget.getSize().width, widget.getSize().height, IUIHolder.EMPTY, Minecraft.m_91087_().f_91074_).widget(widget), -1);
        this.shouldRenderTooltips = false;
        this.modularUI.initWidgets();
        this.f_96541_ = Minecraft.m_91087_();
        this.f_96542_ = this.f_96541_.m_91291_();
        this.f_96547_ = this.f_96541_.f_91062_;
        this.widget = widget;
    }

    public T getWidget() {
        return this.widget;
    }

    public int getLeft() {
        return this.left;
    }

    public int getTop() {
        return this.top;
    }

    public String getUid() {
        return null;
    }

    public void setRecipeLayout(int left, int top) {
        this.modularUI.initWidgets();
        this.left = left;
        this.top = top;
        this.f_96543_ = this.f_96541_.m_91268_().m_85445_();
        this.f_96544_ = this.f_96541_.m_91268_().m_85446_();
        this.modularUI.updateScreenSize(this.f_96543_, this.f_96544_);
        Position displayOffset = new Position(this.modularUI.getGuiLeft(), top);
        this.modularUI.mainGroup.setParentPosition(displayOffset);
    }

    public void setRecipeWidget(int left, int top) {
        this.modularUI.initWidgets();
        this.left = 0;
        this.top = 0;
        this.f_96543_ = this.f_96541_.m_91268_().m_85445_();
        this.f_96544_ = this.f_96541_.m_91268_().m_85446_();
        this.modularUI.updateScreenSize(this.f_96543_, this.f_96544_);
        Position displayOffset = new Position(left, top);
        this.modularUI.mainGroup.setParentPosition(displayOffset);
    }

    public void setEmiRecipeWidget(int left, int top) {
        this.modularUI.initWidgets();
        this.left = left;
        this.top = top;
        this.f_96543_ = this.f_96541_.m_91268_().m_85445_();
        this.f_96544_ = this.f_96541_.m_91268_().m_85446_();
        this.modularUI.updateScreenSize(this.f_96543_, this.f_96544_);
        Position displayOffset = new Position(left, top);
        this.modularUI.mainGroup.setParentPosition(displayOffset);
    }

    public void draw(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.f_96541_.f_91074_.f_19797_ != this.lastTick) {
            updateScreen();
            this.lastTick = this.f_96541_.f_91074_.f_19797_;
        }
        matrixStack.m_85836_();
        matrixStack.m_85837_(-this.left, -this.top, 0.0d);
        m_6305_(matrixStack, mouseX + this.left, mouseY + this.top, partialTicks);
        matrixStack.m_85849_();
    }

    public void updateScreen() {
        this.modularUI.mainGroup.updateScreen();
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer
    public void m_6305_(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.f_97734_ = null;
        RenderSystem.m_69465_();
        RenderSystem.m_69458_(false);
        this.tooltipTexts = null;
        this.tooltipFont = null;
        this.tooltipStack = ItemStack.f_41583_;
        this.tooltipComponent = null;
        this.modularUI.mainGroup.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        this.modularUI.mainGroup.drawInForeground(matrixStack, mouseX, mouseY, partialTicks);
        if (this.shouldRenderTooltips && this.tooltipTexts != null && !this.tooltipTexts.isEmpty()) {
            matrixStack.m_85836_();
            matrixStack.m_85837_(0.0d, 0.0d, 240.0d);
            m_169388_(matrixStack, this.tooltipTexts, Optional.ofNullable(this.tooltipComponent), mouseX, mouseY);
            matrixStack.m_85849_();
        }
        RenderSystem.m_69458_(true);
        RenderSystem.m_69482_();
        RenderSystem.m_69421_(256, Minecraft.f_91002_);
    }
}
