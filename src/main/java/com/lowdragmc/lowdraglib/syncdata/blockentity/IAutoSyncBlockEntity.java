package com.lowdragmc.lowdraglib.syncdata.blockentity;

import com.lowdragmc.lowdraglib.networking.LDLNetworking;
import com.lowdragmc.lowdraglib.networking.s2c.SPacketManagedPayload;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/blockentity/IAutoSyncBlockEntity.class */
public interface IAutoSyncBlockEntity extends IManagedBlockEntity {
    default void syncNow(boolean force) {
        IRef[] nonLazyFields;
        Level level = (Level) Objects.requireNonNull(getSelf().m_58904_());
        if (level.f_46443_) {
            throw new IllegalStateException("Cannot sync from client");
        }
        for (IRef field : getNonLazyFields()) {
            field.update();
        }
        SPacketManagedPayload packet = SPacketManagedPayload.of(this, force);
        LDLNetworking.NETWORK.sendToTrackingChunk(packet, level.m_46745_(getCurrentPos()));
    }

    default void defaultServerTick() {
        IRef[] nonLazyFields;
        for (IRef field : getNonLazyFields()) {
            field.update();
        }
        if (getRootStorage().hasDirtySyncFields()) {
            SPacketManagedPayload packet = SPacketManagedPayload.of(this, false);
            LDLNetworking.NETWORK.sendToTrackingChunk(packet, ((Level) Objects.requireNonNull(getSelf().m_58904_())).m_46745_(getCurrentPos()));
        }
    }

    default void writeCustomSyncData(CompoundTag tag) {
    }

    default void readCustomSyncData(CompoundTag tag) {
    }

    default String getSyncTag() {
        return "sync";
    }
}
