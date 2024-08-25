package com.lowdragmc.photon;

import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import com.lowdragmc.photon.gui.ParticleEditorFactory;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/PhotonCommonProxy.class */
public class PhotonCommonProxy {
    public static void init() {
        PhotonNetworking.init();
        UIFactory.register(ParticleEditorFactory.INSTANCE);
    }
}
