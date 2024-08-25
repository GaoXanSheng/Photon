package com.lowdragmc.photon.client.fx;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/IFXEffect.class */
public interface IFXEffect extends IEffect {
    FX getFx();

    void setOffset(double d, double d2, double d3);

    void setRotation(double d, double d2, double d3);

    void setDelay(int i);

    void setForcedDeath(boolean z);

    void setAllowMulti(boolean z);

    void start();
}
