package com.lowdragmc.lowdraglib.syncdata.blockentity;

import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/blockentity/IManagedBlockEntity.class */
public interface IManagedBlockEntity {
    IManagedStorage getRootStorage();

    default BlockEntityType<?> getBlockEntityType() {
        return getSelf().m_58903_();
    }

    default BlockPos getCurrentPos() {
        return getSelf().m_58899_();
    }

    default BlockEntity getSelf() {
        return (BlockEntity) this;
    }

    default IRef[] getNonLazyFields() {
        return getRootStorage().getNonLazyFields();
    }
}
