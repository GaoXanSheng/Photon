package com.lowdragmc.lowdraglib.networking;

import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/IHandlerContext.class */
public interface IHandlerContext {
    Object getContext();

    boolean isClient();

    @Nullable
    ServerPlayer getPlayer();

    @Nullable
    MinecraftServer getServer();

    Level getLevel();
}
