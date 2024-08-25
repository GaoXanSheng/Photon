package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ItemStackTexture.class */
public class ItemStackTexture extends TransformTexture {
    public final ItemStack[] itemStack;
    private int index = 0;
    private int ticks = 0;
    private int color = -1;
    private long lastTick;

    public ItemStackTexture(ItemStack... itemStacks) {
        this.itemStack = itemStacks;
    }

    public ItemStackTexture(Item... items) {
        this.itemStack = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            this.itemStack[i] = new ItemStack(items[i]);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public ItemStackTexture setColor(int color) {
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
        if (this.itemStack.length > 1) {
            int i = this.ticks + 1;
            this.ticks = i;
            if (i % 20 == 0) {
                int i2 = this.index + 1;
                this.index = i2;
                if (i2 == this.itemStack.length) {
                    this.index = 0;
                }
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack mStack, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (this.itemStack.length == 0) {
            return;
        }
        mStack.m_85836_();
        mStack.m_85841_(width / 16.0f, height / 16.0f, 1.0f);
        mStack.m_85837_((x * 16.0f) / width, (y * 16.0f) / height, -200.0d);
        DrawerHelper.drawItemStack(mStack, this.itemStack[this.index], 0, 0, this.color, null);
        mStack.m_85849_();
    }
}
