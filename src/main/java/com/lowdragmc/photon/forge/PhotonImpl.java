package com.lowdragmc.photon.forge;

import com.lowdragmc.photon.Photon;
import com.lowdragmc.photon.client.forge.ClientProxyImpl;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Photon.MOD_ID)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/forge/PhotonImpl.class */
public class PhotonImpl {
    public PhotonImpl() {
        Photon.init();
        DistExecutor.unsafeRunForDist(() -> {
            return ClientProxyImpl::new;
        }, () -> {
            return CommonProxyImpl::new;
        });
    }

    public static boolean isStencilEnabled(RenderTarget target) {
        return target.isStencilEnabled();
    }

    public static boolean useCombinedDepthStencilAttachment() {
        return ((Boolean) ForgeConfig.CLIENT.useCombinedDepthStencilAttachment.get()).booleanValue();
    }
}
