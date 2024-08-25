package com.lowdragmc.lowdraglib.forge;

import com.lowdragmc.lowdraglib.async.AsyncThreadData;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "ldlib", bus = Mod.EventBusSubscriber.Bus.FORGE)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/forge/CommonListeners.class */
public class CommonListeners {
    @SubscribeEvent
    public static void onWorldUnLoad(LevelEvent.Unload event) {
        ServerLevel level = event.getLevel();
        if (!level.m_5776_() && (level instanceof ServerLevel)) {
            ServerLevel serverLevel = level;
            AsyncThreadData.getOrCreate(serverLevel).releaseExecutorService();
        }
    }
}
