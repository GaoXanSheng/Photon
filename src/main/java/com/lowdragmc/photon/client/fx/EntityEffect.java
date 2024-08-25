package com.lowdragmc.photon.client.fx;

import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/EntityEffect.class */
public class EntityEffect extends FXEffect {
    public static Map<Entity, List<EntityEffect>> CACHE = new HashMap();
    public final Entity entity;

    public EntityEffect(FX fx, Level level, Entity entity) {
        super(fx, level);
        this.entity = entity;
    }

    @Override // com.lowdragmc.photon.client.fx.IEffect
    public boolean updateEmitter(IParticleEmitter emitter) {
        if (!this.entity.m_6084_()) {
            emitter.remove(this.forcedDeath);
            return this.forcedDeath;
        }
        emitter.updatePos(new Vector3(this.entity.m_20185_() + this.xOffset, this.entity.m_20186_() + this.yOffset, this.entity.m_20189_() + this.zOffset));
        return false;
    }

    @Override // com.lowdragmc.photon.client.fx.IFXEffect
    public void start() {
        if (this.entity.m_6084_()) {
            this.emitters.clear();
            this.emitters.addAll(this.fx.generateEmitters());
            if (this.emitters.isEmpty()) {
                return;
            }
            if (!this.allowMulti) {
                List<EntityEffect> effects = CACHE.computeIfAbsent(this.entity, p -> {
                    return new ArrayList();
                });
                Iterator<EntityEffect> iter = effects.iterator();
                while (iter.hasNext()) {
                    EntityEffect effect = iter.next();
                    boolean removed = false;
                    if (effect.emitters.stream().noneMatch(e -> {
                        return e.self().m_107276_();
                    })) {
                        iter.remove();
                        removed = true;
                    }
                    if (effect.fx.equals(this.fx) && !removed) {
                        return;
                    }
                }
                effects.add(this);
            }
            Vector3 realPos = new Vector3(this.entity.m_20318_(0.0f)).add((float) this.xOffset, (float) this.yOffset, (float) this.zOffset);
            for (IParticleEmitter emitter : this.emitters) {
                if (!emitter.isSubEmitter()) {
                    emitter.reset();
                    emitter.self().setDelay(this.delay);
                    emitter.emmitToLevel(this, this.level, realPos.x, realPos.y, realPos.z, this.xRotation, this.yRotation, this.zRotation);
                }
            }
        }
    }
}
