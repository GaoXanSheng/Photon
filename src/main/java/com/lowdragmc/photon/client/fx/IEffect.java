package com.lowdragmc.photon.client.fx;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/IEffect.class */
public interface IEffect {
    List<IParticleEmitter> getEmitters();

    boolean updateEmitter(IParticleEmitter iParticleEmitter);

    @Nullable
    default IParticleEmitter getEmitterByName(String name) {
        for (IParticleEmitter emitter : getEmitters()) {
            if (emitter.getName().equals(name)) {
                return emitter;
            }
        }
        return null;
    }
}
