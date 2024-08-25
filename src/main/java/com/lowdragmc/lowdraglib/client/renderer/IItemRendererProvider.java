package com.lowdragmc.lowdraglib.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/renderer/IItemRendererProvider.class */
public interface IItemRendererProvider {
    public static final ThreadLocal<Boolean> disabled = ThreadLocal.withInitial(() -> {
        return false;
    });

    @Nullable
    IRenderer getRenderer(ItemStack itemStack);
}
