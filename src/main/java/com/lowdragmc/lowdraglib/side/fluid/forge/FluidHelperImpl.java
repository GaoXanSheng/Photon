package com.lowdragmc.lowdraglib.side.fluid.forge;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/forge/FluidHelperImpl.class */
public class FluidHelperImpl {
    public static FluidStack toFluidStack(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return new FluidStack(fluidStack.getFluid(), (int) Math.min(fluidStack.getAmount(), 2147483647L), fluidStack.getTag());
    }

    public static com.lowdragmc.lowdraglib.side.fluid.FluidStack toFluidStack(FluidStack fluidStack) {
        return com.lowdragmc.lowdraglib.side.fluid.FluidStack.create(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getTag());
    }

    public static long getBucket() {
        return 1000L;
    }

    public static int getColor(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(toFluidStack(fluidStack));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        ResourceLocation texture = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getStillTexture(toFluidStack(fluidStack));
        if (texture == null) {
            return null;
        }
        return (TextureAtlasSprite) Minecraft.m_91087_().m_91258_(InventoryMenu.f_39692_).apply(texture);
    }

    public static Component getDisplayName(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getDescription(toFluidStack(fluidStack));
    }

    public static int getTemperature(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getTemperature(toFluidStack(fluidStack));
    }

    public static boolean isLighterThanAir(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().isLighterThanAir();
    }

    public static boolean canBePlacedInWorld(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack, BlockAndTintGetter level, BlockPos pos) {
        return fluidStack.getFluid().getFluidType().canBePlacedInLevel(level, pos, toFluidStack(fluidStack));
    }

    public static boolean doesVaporize(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack, Level level, BlockPos pos) {
        return fluidStack.getFluid().getFluidType().isVaporizedOnPlacement(level, pos, toFluidStack(fluidStack));
    }

    public static SoundEvent getEmptySound(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getSound(toFluidStack(fluidStack), SoundActions.BUCKET_EMPTY);
    }

    public static SoundEvent getFillSound(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getSound(toFluidStack(fluidStack), SoundActions.BUCKET_FILL);
    }

    public static Object toRealFluidStack(com.lowdragmc.lowdraglib.side.fluid.FluidStack fluidStack) {
        return toFluidStack(fluidStack);
    }

    public static String getUnit() {
        return "mB";
    }
}
