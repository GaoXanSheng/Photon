package com.lowdragmc.lowdraglib.test;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.lowdragmc.lowdraglib.client.renderer.impl.IModelRenderer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/test/TestBlock.class */
public class TestBlock extends Block implements EntityBlock, IBlockRendererProvider {
    public static final TestBlock BLOCK = new TestBlock();
    IRenderer renderer;

    private TestBlock() {
        super(BlockBehaviour.Properties.m_60939_(Material.f_76279_).m_60955_().m_155954_(2.0f));
        this.renderer = new IModelRenderer(LDLib.location("block/cube")) { // from class: com.lowdragmc.lowdraglib.test.TestBlock.1
            @Override // com.lowdragmc.lowdraglib.client.renderer.IRenderer
            public boolean reBakeCustomQuads() {
                return true;
            }
        };
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public BlockEntity m_142194_(BlockPos pPos, BlockState pState) {
        return new TestBlockEntity(pPos, pState);
    }

    public InteractionResult m_6227_(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity m_7702_ = pLevel.m_7702_(pPos);
        if (m_7702_ instanceof TestBlockEntity) {
            TestBlockEntity blockEntity = (TestBlockEntity) m_7702_;
            blockEntity.use(pPlayer);
        }
        return InteractionResult.SUCCESS;
    }

    @Override // com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider
    @Nullable
    public IRenderer getRenderer(BlockState state) {
        return this.renderer;
    }
}
