package com.lowdragmc.lowdraglib.async;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lowdragmc.lowdraglib.LDLib;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/async/AsyncThreadData.class */
public class AsyncThreadData extends SavedData {
    private final ServerLevel serverLevel;
    private final CopyOnWriteArrayList<IAsyncLogic> asyncLogics;
    private ScheduledExecutorService executorService;
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("LDLib Async Thread-%d").setDaemon(true).build();
    private static final ThreadLocal<Boolean> IN_SERVICE = ThreadLocal.withInitial(() -> {
        return false;
    });
    private long periodID;

    public static AsyncThreadData getOrCreate(ServerLevel serverLevel) {
        return (AsyncThreadData) serverLevel.m_8895_().m_164861_(tag -> {
            return new AsyncThreadData(serverLevel, tag);
        }, () -> {
            return new AsyncThreadData(serverLevel);
        }, "ldlib");
    }

    private AsyncThreadData(ServerLevel serverLevel) {
        this.asyncLogics = new CopyOnWriteArrayList<>();
        this.periodID = Long.MIN_VALUE;
        this.serverLevel = serverLevel;
    }

    private AsyncThreadData(ServerLevel serverLevel, CompoundTag compoundTag) {
        this(serverLevel);
    }

    public CompoundTag m_7176_(CompoundTag compoundTag) {
        return compoundTag;
    }

    public void createExecutorService() {
        if (this.executorService == null || this.executorService.isShutdown()) {
            this.executorService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
            this.executorService.scheduleAtFixedRate(this::searchingTask, 0L, 50L, TimeUnit.MILLISECONDS);
        }
    }

    public void addAsyncLogic(IAsyncLogic logic) {
        this.asyncLogics.add(logic);
        createExecutorService();
    }

    public void removeAsyncLogic(IAsyncLogic logic) {
        this.asyncLogics.remove(logic);
        if (this.asyncLogics.isEmpty()) {
            releaseExecutorService();
        }
    }

    private void searchingTask() {
        try {
        } catch (Throwable e) {
            try {
                e.printStackTrace();
                LDLib.LOGGER.error("asyncThreadLogic error: {}", e.getMessage());
                IN_SERVICE.set(false);
            } catch (Throwable th) {
                IN_SERVICE.set(false);
                throw th;
            }
        }
        if (this.serverLevel.m_7654_().m_195518_()) {
            IN_SERVICE.set(false);
            return;
        }
        IN_SERVICE.set(true);
        Iterator<IAsyncLogic> it = this.asyncLogics.iterator();
        while (it.hasNext()) {
            IAsyncLogic logic = it.next();
            logic.asyncTick(this.periodID);
        }
        IN_SERVICE.set(false);
        this.periodID++;
    }

    public static boolean isThreadService() {
        return IN_SERVICE.get().booleanValue();
    }

    public void releaseExecutorService() {
        if (this.executorService != null) {
            this.executorService.shutdownNow();
        }
        this.executorService = null;
    }

    public long getPeriodID() {
        return this.periodID;
    }
}
