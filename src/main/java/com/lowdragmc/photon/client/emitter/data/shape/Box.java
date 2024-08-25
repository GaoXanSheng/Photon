package com.lowdragmc.photon.client.emitter.data.shape;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.particle.LParticle;
import net.minecraft.util.RandomSource;

@LDLRegister(name = "box", group = "shape")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/Box.class */
public class Box implements IShape {
    @Configurable
    private Type emitFrom = Type.Volume;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/Box$Type.class */
    public enum Type {
        Volume,
        Shell,
        Edge
    }

    public Type getEmitFrom() {
        return this.emitFrom;
    }

    public void setEmitFrom(Type emitFrom) {
        this.emitFrom = emitFrom;
    }

    @Override // com.lowdragmc.photon.client.emitter.data.shape.IShape
    public void nextPosVel(LParticle particle, LParticle emitter, Vector3 position, Vector3 rotation, Vector3 scale) {
        RandomSource random = particle.getRandomSource();
        Vector3 scale2 = new Vector3(Math.abs(scale.x), Math.abs(scale.y), Math.abs(scale.z)).multiply(0.5d);
        Vector3 pos = new Vector3(((random.m_188500_() * 2.0d) * scale2.x) - scale2.x, ((random.m_188500_() * 2.0d) * scale2.y) - scale2.y, ((random.m_188500_() * 2.0d) * scale2.z) - scale2.z);
        if (this.emitFrom == Type.Shell) {
            double xy = scale2.x * scale2.y;
            double yz = scale2.y * scale2.z;
            double xz = scale2.x * scale2.z;
            double randomValue = random.m_188500_() * (xy + yz + xz);
            if (randomValue < xy) {
                pos.z = ((double) random.m_188501_()) > 0.5d ? scale2.z : -scale2.z;
            } else if (randomValue < yz + xy) {
                pos.x = ((double) random.m_188501_()) > 0.5d ? scale2.x : -scale2.x;
            } else {
                pos.y = ((double) random.m_188501_()) > 0.5d ? scale2.y : -scale2.y;
            }
        } else if (this.emitFrom == Type.Edge) {
            double randomValue2 = random.m_188500_() * (scale2.x + scale2.y + scale2.z);
            if (randomValue2 < scale2.x) {
                pos.z = ((double) random.m_188501_()) > 0.5d ? scale2.z : -scale2.z;
                pos.y = ((double) random.m_188501_()) > 0.5d ? scale2.y : -scale2.y;
            } else if (randomValue2 < scale2.x + scale2.y) {
                pos.z = ((double) random.m_188501_()) > 0.5d ? scale2.z : -scale2.z;
                pos.x = ((double) random.m_188501_()) > 0.5d ? scale2.x : -scale2.x;
            } else {
                pos.x = ((double) random.m_188501_()) > 0.5d ? scale2.x : -scale2.x;
                pos.y = ((double) random.m_188501_()) > 0.5d ? scale2.y : -scale2.y;
            }
        }
        particle.setPos(pos.copy().rotateYXY(rotation).add(position).add(particle.getPos()), true);
        particle.setSpeed(new Vector3(0.0d, 0.05d, 0.0d).rotateYXY(rotation));
    }
}
