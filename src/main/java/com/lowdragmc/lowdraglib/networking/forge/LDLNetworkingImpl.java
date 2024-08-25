package com.lowdragmc.lowdraglib.networking.forge;

import com.lowdragmc.lowdraglib.networking.INetworking;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/forge/LDLNetworkingImpl.class */
public class LDLNetworkingImpl {
    public static INetworking createNetworking(ResourceLocation networking, String version) {
        return new Networking(networking, version);
    }
}
