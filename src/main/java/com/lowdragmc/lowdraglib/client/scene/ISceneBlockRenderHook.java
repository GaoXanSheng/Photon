package com.lowdragmc.lowdraglib.client.scene;

import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/scene/ISceneBlockRenderHook.class */
public interface ISceneBlockRenderHook {
    default void apply(boolean isTESR, RenderType layer) {
    }

    default void applyBESR(Level world, BlockPos pos, BlockEntity blockEntity, PoseStack poseStack, float partialTicks) {
    }

    default void applyVertexConsumerWrapper(Level world, BlockPos pos, BlockState state, WorldSceneRenderer.VertexConsumerWrapper wrapperBuffer, RenderType layer, float partialTicks) {
    }
}
