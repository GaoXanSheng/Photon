package com.lowdragmc.lowdraglib.test;

import com.lowdragmc.lowdraglib.gui.compass.CompassView;
import com.lowdragmc.lowdraglib.gui.factory.BlockEntityUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.test.forge.TestBlockEntityImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/test/TestBlockEntity.class */
public class TestBlockEntity extends BlockEntity implements IUIHolder {
    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static BlockEntityType<?> TYPE() {
        return TestBlockEntityImpl.TYPE();
    }

    public TestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(TYPE(), pWorldPosition, pBlockState);
    }

    public void use(Player player) {
        if (!m_58904_().f_46443_) {
            BlockEntityUIFactory.INSTANCE.openUI(this, (ServerPlayer) player);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(this, entityPlayer).widget(new CompassView("ldlib"));
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public boolean isInvalid() {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public boolean isRemote() {
        return this.f_58857_.f_46443_;
    }

    @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
    public void markAsDirty() {
    }
}
