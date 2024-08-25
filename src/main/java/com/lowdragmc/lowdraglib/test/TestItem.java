package com.lowdragmc.lowdraglib.test;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/test/TestItem.class */
public class TestItem extends BlockItem implements IItemRendererProvider {
    public static final TestItem ITEM = new TestItem();

    private TestItem() {
        super(TestBlock.BLOCK, new Item.Properties().m_41491_(CreativeModeTab.f_40751_));
    }

    @Override // com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider
    public IRenderer getRenderer(ItemStack stack) {
        return TestBlock.BLOCK.getRenderer(TestBlock.BLOCK.m_49966_());
    }
}
