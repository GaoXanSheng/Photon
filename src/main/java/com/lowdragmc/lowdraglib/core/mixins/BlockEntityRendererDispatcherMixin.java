package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.client.renderer.ATESRRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockEntityRenderDispatcher.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/BlockEntityRendererDispatcherMixin.class */
public abstract class BlockEntityRendererDispatcherMixin {
    @Inject(method = {"getRenderer"}, at = {@At("RETURN")}, cancellable = true)
    private <T extends BlockEntity> void injectGetRenderer(T pBlockEntity, CallbackInfoReturnable<BlockEntityRenderer<T>> cir) {
        BlockEntityRenderer<T> renderer = (BlockEntityRenderer) cir.getReturnValue();
        if ((renderer instanceof ATESRRendererProvider) && !((ATESRRendererProvider) renderer).hasRenderer(pBlockEntity)) {
            cir.setReturnValue((Object) null);
        }
    }
}
