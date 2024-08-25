package com.lowdragmc.lowdraglib.client.scene;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/scene/ISceneEntityRenderHook.class */
public interface ISceneEntityRenderHook {
    default void applyEntity(Level world, Entity entity, PoseStack poseStack, float partialTicks) {
    }
}
