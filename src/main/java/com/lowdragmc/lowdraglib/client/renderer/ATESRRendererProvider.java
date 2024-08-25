package com.lowdragmc.lowdraglib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/renderer/ATESRRendererProvider.class */
public class ATESRRendererProvider<T extends BlockEntity> implements BlockEntityRenderer<T> {
    public int m_142163_() {
        return super.m_142163_();
    }

    public boolean m_142756_(T pBlockEntity, Vec3 pCameraPos) {
        IRenderer renderer = getRenderer(pBlockEntity);
        if (renderer != null) {
            return renderer.shouldRender(pBlockEntity, pCameraPos);
        }
        return super.m_142756_(pBlockEntity, pCameraPos);
    }

    public void m_6922_(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        IRenderer renderer = getRenderer(pBlockEntity);
        if (renderer != null && !renderer.isRaw()) {
            renderer.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
        }
    }

    @Nullable
    public IRenderer getRenderer(@Nonnull T tileEntity) {
        Level world = tileEntity.m_58904_();
        if (world != null) {
            BlockState state = tileEntity.m_58900_();
            IBlockRendererProvider m_60734_ = state.m_60734_();
            if (m_60734_ instanceof IBlockRendererProvider) {
                IBlockRendererProvider blockRendererProvider = m_60734_;
                return blockRendererProvider.getRenderer(state);
            }
            return null;
        }
        return null;
    }

    public boolean hasRenderer(T tileEntity) {
        IRenderer renderer = getRenderer(tileEntity);
        return renderer != null && renderer.hasTESR(tileEntity);
    }

    public boolean m_5932_(@Nonnull T tileEntity) {
        IRenderer renderer = getRenderer(tileEntity);
        if (renderer != null) {
            return renderer.isGlobalRenderer(tileEntity);
        }
        return false;
    }
}
