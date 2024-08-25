package com.lowdragmc.photon.client.emitter.data.shape;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.particle.LParticle;

@LDLRegister(name = "dot", group = "shape")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/Dot.class */
public class Dot implements IShape {
    @Override // com.lowdragmc.photon.client.emitter.data.shape.IShape
    public void nextPosVel(LParticle particle, LParticle emitter, Vector3 position, Vector3 rotation, Vector3 scale) {
        particle.setPos(position.add(particle.getPos()), true);
        particle.setSpeed(new Vector3(0.0d, 0.0d, 0.0d));
    }
}
