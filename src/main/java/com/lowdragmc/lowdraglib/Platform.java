package com.lowdragmc.lowdraglib;

import com.lowdragmc.lowdraglib.forge.PlatformImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/Platform.class */
public class Platform {
    @ApiStatus.Internal
    public static RegistryAccess FROZEN_REGISTRY_ACCESS;

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static String platformName() {
        return PlatformImpl.platformName();
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean isForge() {
        return PlatformImpl.isForge();
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean isDevEnv() {
        return PlatformImpl.isDevEnv();
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean isDatagen() {
        return PlatformImpl.isDatagen();
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        return PlatformImpl.isModLoaded(modId);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static boolean isClient() {
        return PlatformImpl.isClient();
    }

    @ExpectPlatform.Transformed
    @Nullable
    @ExpectPlatform
    public static MinecraftServer getMinecraftServer() {
        return PlatformImpl.getMinecraftServer();
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static Path getGamePath() {
        return PlatformImpl.getGamePath();
    }

    @Nullable
    public static RegistryAccess getFrozenRegistry() {
        return FROZEN_REGISTRY_ACCESS;
    }
}
