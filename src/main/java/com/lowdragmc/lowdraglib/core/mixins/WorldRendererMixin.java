package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LevelRenderer.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/WorldRendererMixin.class */
public abstract class WorldRendererMixin {
    @Inject(method = {"getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I"}, at = {@At("HEAD")}, cancellable = true)
    private static void injectShouldRenderFace(BlockAndTintGetter pLevel, BlockState pState, BlockPos pPos, CallbackInfoReturnable<Integer> cir) {
        if (pState.m_60734_() instanceof IBlockRendererProvider) {
            cir.setReturnValue(Integer.valueOf(pState.m_60734_().getLightMap(pLevel, pState, pPos)));
        }
    }
}
