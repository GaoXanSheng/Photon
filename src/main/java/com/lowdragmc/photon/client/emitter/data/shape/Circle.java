package com.lowdragmc.photon.client.emitter.data.shape;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.particle.LParticle;
import net.minecraft.util.RandomSource;

@LDLRegister(name = "circle", group = "shape")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/Circle.class */
public class Circle implements IShape {
    @Configurable
    @NumberRange(range = {0.0d, 1000.0d})
    private float radius = 0.5f;
    @Configurable
    @NumberRange(range = {0.0d, 1.0d})
    private float radiusThickness = 1.0f;
    @Configurable
    @NumberRange(range = {0.0d, 360.0d}, wheel = 10.0d)
    private float arc = 360.0f;

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadiusThickness() {
        return this.radiusThickness;
    }

    public void setRadiusThickness(float radiusThickness) {
        this.radiusThickness = radiusThickness;
    }

    public float getArc() {
        return this.arc;
    }

    public void setArc(float arc) {
        this.arc = arc;
    }

    @Override // com.lowdragmc.photon.client.emitter.data.shape.IShape
    public void nextPosVel(LParticle particle, LParticle emitter, Vector3 position, Vector3 rotation, Vector3 scale) {
        RandomSource random = particle.getRandomSource();
        float outer = this.radius;
        float inner = (1.0f - this.radiusThickness) * this.radius;
        float origin = inner * inner;
        float bound = outer * outer;
        double r = outer == inner ? outer : Math.sqrt(origin + (random.m_188500_() * (bound - origin)));
        double theta = ((this.arc * 6.2831855f) * random.m_188500_()) / 360.0d;
        Vector3 pos = new Vector3(r * Math.cos(theta), 0.0d, r * Math.sin(theta)).multiply(scale);
        particle.setPos(pos.copy().rotateYXY(rotation).add(position).add(particle.getPos()), true);
        particle.setSpeed(pos.copy().normalize().multiply(0.05d).rotateYXY(rotation));
    }
}
