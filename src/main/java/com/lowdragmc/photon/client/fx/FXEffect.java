package com.lowdragmc.photon.client.fx;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/FXEffect.class */
public abstract class FXEffect implements IFXEffect {
    public final FX fx;
    public final Level level;
    protected double xOffset;
    protected double yOffset;
    protected double zOffset;
    protected double xRotation;
    protected double yRotation;
    protected double zRotation;
    protected int delay;
    protected boolean forcedDeath;
    protected boolean allowMulti;
    protected final List<IParticleEmitter> emitters = new ArrayList();
    protected final Map<String, IParticleEmitter> cache = new HashMap();

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public FX getFx() {
        return this.fx;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public void setZOffset(double zOffset) {
        this.zOffset = zOffset;
    }

    public void setXRotation(double xRotation) {
        this.xRotation = xRotation;
    }

    public void setYRotation(double yRotation) {
        this.yRotation = yRotation;
    }

    public void setZRotation(double zRotation) {
        this.zRotation = zRotation;
    }

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public void setForcedDeath(boolean forcedDeath) {
        this.forcedDeath = forcedDeath;
    }

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public void setAllowMulti(boolean allowMulti) {
        this.allowMulti = allowMulti;
    }

    @Override // com.lowdragmc.photon.client.fx.IEffect
    public List<IParticleEmitter> getEmitters() {
        return this.emitters;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FXEffect(FX fx, Level level) {
        this.fx = fx;
        this.level = level;
    }

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public void setOffset(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public void setRotation(double x, double y, double z) {
        this.xRotation = x;
        this.yRotation = y;
        this.zRotation = z;
    }

    @Override // com.lowdragmc.photon.client.fx.IEffect
    @Nullable
    public IParticleEmitter getEmitterByName(String name) {
        return this.cache.computeIfAbsent(name, s -> {
            return super.getEmitterByName(name);
        });
    }
}
