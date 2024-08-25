package com.lowdragmc.lowdraglib.side.fluid;

import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/FluidActionResult.class */
public class FluidActionResult {
    public static final FluidActionResult FAILURE = new FluidActionResult(false, ItemStack.f_41583_);
    public final boolean success;
    @Nonnull
    public final ItemStack result;

    public FluidActionResult(@Nonnull ItemStack result) {
        this(true, result);
    }

    private FluidActionResult(boolean success, @Nonnull ItemStack result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    @Nonnull
    public ItemStack getResult() {
        return this.result;
    }
}
