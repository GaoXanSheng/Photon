package com.lowdragmc.lowdraglib.forge;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.forge.ClientProxyImpl;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod("ldlib")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/forge/LDLibForge.class */
public class LDLibForge {
    public LDLibForge() {
        LDLib.init();
        DistExecutor.unsafeRunForDist(() -> {
            return ClientProxyImpl::new;
        }, () -> {
            return CommonProxyImpl::new;
        });
    }
}
