package com.lowdragmc.lowdraglib.side.fluid;

import com.lowdragmc.lowdraglib.side.fluid.forge.FluidHelperImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/FluidHelper.class */
public class FluidHelper {
    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static long getBucket() {
        return FluidHelperImpl.getBucket();
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static int getColor(FluidStack fluidStack) {
        return FluidHelperImpl.getColor(fluidStack);
    }

    @ExpectPlatform.Transformed
    @Nullable
    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    public static TextureAtlasSprite getStillTexture(FluidStack fluidStack) {
        return FluidHelperImpl.getStillTexture(fluidStack);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static Component getDisplayName(FluidStack fluidStack) {
        return FluidHelperImpl.getDisplayName(fluidStack);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static int getTemperature(FluidStack fluidStack) {
        return FluidHelperImpl.getTemperature(fluidStack);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean isLighterThanAir(FluidStack fluidStack) {
        return FluidHelperImpl.isLighterThanAir(fluidStack);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean canBePlacedInWorld(FluidStack fluidStack, BlockAndTintGetter level, BlockPos pos) {
        return FluidHelperImpl.canBePlacedInWorld(fluidStack, level, pos);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean doesVaporize(FluidStack fluidStack, Level level, BlockPos pos) {
        return FluidHelperImpl.doesVaporize(fluidStack, level, pos);
    }

    @ExpectPlatform.Transformed
    @Nullable
    @ExpectPlatform
    public static SoundEvent getEmptySound(FluidStack fluidStack) {
        return FluidHelperImpl.getEmptySound(fluidStack);
    }

    @ExpectPlatform.Transformed
    @Nullable
    @ExpectPlatform
    public static SoundEvent getFillSound(FluidStack fluidStack) {
        return FluidHelperImpl.getFillSound(fluidStack);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static Object toRealFluidStack(FluidStack fluidStack) {
        return FluidHelperImpl.toRealFluidStack(fluidStack);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static String getUnit() {
        return FluidHelperImpl.getUnit();
    }
}
