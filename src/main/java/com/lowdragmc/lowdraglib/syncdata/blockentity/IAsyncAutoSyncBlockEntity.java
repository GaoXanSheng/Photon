package com.lowdragmc.lowdraglib.syncdata.blockentity;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.async.AsyncThreadData;
import com.lowdragmc.lowdraglib.async.IAsyncLogic;
import com.lowdragmc.lowdraglib.networking.LDLNetworking;
import com.lowdragmc.lowdraglib.networking.s2c.SPacketManagedPayload;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/blockentity/IAsyncAutoSyncBlockEntity.class */
public interface IAsyncAutoSyncBlockEntity extends IAutoSyncBlockEntity, IAsyncLogic {
    default boolean useAsyncThread() {
        return true;
    }

    default void onValid() {
        if (useAsyncThread()) {
            ServerLevel m_58904_ = getSelf().m_58904_();
            if (m_58904_ instanceof ServerLevel) {
                ServerLevel serverLevel = m_58904_;
                AsyncThreadData.getOrCreate(serverLevel).addAsyncLogic(this);
            }
        }
    }

    default void onInValid() {
        ServerLevel m_58904_ = getSelf().m_58904_();
        if (m_58904_ instanceof ServerLevel) {
            ServerLevel serverLevel = m_58904_;
            AsyncThreadData.getOrCreate(serverLevel).removeAsyncLogic(this);
        }
    }

    @Override // com.lowdragmc.lowdraglib.async.IAsyncLogic
    default void asyncTick(long periodID) {
        IRef[] nonLazyFields;
        if (Platform.getMinecraftServer() != null && useAsyncThread() && !getSelf().m_58901_()) {
            for (IRef field : getNonLazyFields()) {
                field.update();
            }
            if (getRootStorage().hasDirtySyncFields()) {
                Platform.getMinecraftServer().execute(() -> {
                    SPacketManagedPayload packet = SPacketManagedPayload.of(this, false);
                    LDLNetworking.NETWORK.sendToTrackingChunk(packet, ((Level) Objects.requireNonNull(getSelf().m_58904_())).m_46745_(getCurrentPos()));
                });
            }
        }
    }
}
