package com.lowdragmc.lowdraglib.networking;

import com.lowdragmc.lowdraglib.networking.both.PacketRPCMethodPayload;
import com.lowdragmc.lowdraglib.networking.c2s.CPacketUIClientAction;
import com.lowdragmc.lowdraglib.networking.forge.LDLNetworkingImpl;
import com.lowdragmc.lowdraglib.networking.s2c.SPacketManagedPayload;
import com.lowdragmc.lowdraglib.networking.s2c.SPacketUIOpen;
import com.lowdragmc.lowdraglib.networking.s2c.SPacketUIWidgetUpdate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/LDLNetworking.class */
public class LDLNetworking {
    public static final INetworking NETWORK = createNetworking(new ResourceLocation("ldlib", "networking"), "0.0.1");

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static INetworking createNetworking(ResourceLocation networking, String version) {
        return LDLNetworkingImpl.createNetworking(networking, version);
    }

    public static void init() {
        NETWORK.registerS2C(SPacketUIOpen.class);
        NETWORK.registerS2C(SPacketUIWidgetUpdate.class);
        NETWORK.registerS2C(SPacketManagedPayload.class);
        NETWORK.registerC2S(CPacketUIClientAction.class);
        NETWORK.registerBoth(PacketRPCMethodPayload.class);
    }
}
