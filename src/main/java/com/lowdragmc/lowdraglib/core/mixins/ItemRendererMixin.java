package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemRenderer.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/ItemRendererMixin.class */
public abstract class ItemRendererMixin {
    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    public void injectRenderItem(ItemStack stack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        IRenderer renderer;
        if ((stack.m_41720_() instanceof IItemRendererProvider) && !IItemRendererProvider.disabled.get().booleanValue() && (renderer = stack.m_41720_().getRenderer(stack)) != null) {
            if (transformType == ItemTransforms.TransformType.GUI && renderer.useBlockLight(stack) != model.m_7547_()) {
                if (renderer.useBlockLight(stack)) {
                    Lighting.m_84931_();
                } else {
                    Lighting.m_84930_();
                }
            }
            renderer.renderItem(stack, transformType, leftHand, matrixStack, buffer, combinedLight, combinedOverlay, model);
            if (transformType == ItemTransforms.TransformType.GUI && (buffer instanceof MultiBufferSource.BufferSource)) {
                MultiBufferSource.BufferSource bufferSource = (MultiBufferSource.BufferSource) buffer;
                bufferSource.m_109911_();
                if (model.m_7547_()) {
                    Lighting.m_84931_();
                } else {
                    Lighting.m_84930_();
                }
            }
            ci.cancel();
        }
    }
}
