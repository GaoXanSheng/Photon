package com.lowdragmc.photon.client.emitter;

import com.lowdragmc.lowdraglib.client.scene.ParticleManager;
import com.lowdragmc.lowdraglib.gui.editor.ILDLRegisterClient;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegisterClient;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.syncdata.IAutoPersistedSerializable;
import com.lowdragmc.lowdraglib.utils.DummyWorld;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.fx.EditorEffect;
import com.lowdragmc.photon.client.fx.IEffect;
import com.lowdragmc.photon.client.particle.LParticle;
import com.lowdragmc.photon.integration.PhotonLDLibPlugin;
import java.util.Map;
import java.util.Queue;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


/**
 * @author KilaBash
 * @date 2023/6/2
 * @implNote IParticleEmitter
 */
@OnlyIn(Dist.CLIENT)
public interface IParticleEmitter extends IConfigurable, ILDLRegisterClient, IAutoPersistedSerializable {

    default LParticle self() {
        return (LParticle) this;
    }

    /**
     * reset runtime data
     */
    default void reset() {
        self().resetParticle();
    }

    /**
     * get amount of existing particle which emitted from it.
     */
    default int getParticleAmount() {
        var sum = 0;
        for (var entry : getParticles().entrySet()) {
            if (entry.getKey() == ParticleQueueRenderType.INSTANCE) {
                for (var particle : entry.getValue()) {
                    sum += ((IParticleEmitter) particle).getParticleAmount();
                }
            }
            sum += entry.getValue().size();
        }
        return sum;
    }

    /**
     * emit to a given level.
     */
    @Deprecated
    default void emmitToLevel(Level level, double x, double y, double z) {
        emmitToLevel(null, level, x, y, z, 0, 0, 0);
    }

    default void emmitToLevel(@Nullable IEffect effect, Level level, double x, double y, double z, double xR, double yR, double zR) {
        setEffect(effect);
        self().setPos(x, y, z, true);
        self().setRotation(new Vector3(xR, yR, zR));
        self().setLevel(level);
        self().prepareForEmitting(null);
        if (level instanceof DummyWorld dummyWorld) {
            ParticleManager particleManager = dummyWorld.getParticleManager();
            if (particleManager != null) {
                particleManager.addParticle(self());
            }
        } else {
            Minecraft.getInstance().particleEngine.add(self());
        }
    }

    default void updatePos(Vector3 newPos) {
        self().setPos(newPos, true);
    }

    /**
     * emitter type
     */
    default String getEmitterType() {
        return name();
    }

    @Nullable
    static IParticleEmitter deserializeWrapper(CompoundTag tag) {
        var wrapper = PhotonLDLibPlugin.REGISTER_EMITTERS.get(tag.getString("_type"));
        if (wrapper != null) {
            var emitter = wrapper.creator().get();
            emitter.deserializeNBT(tag);
            return emitter;
        }
        return null;
    }

    default IParticleEmitter copy() {
        return copy(false);
    }

    default IParticleEmitter copy(boolean deep) {
        return deserializeWrapper(serializeNBT());
    }

    /**
     * force - remove without waiting.
     */
    default void remove(boolean force) {
        self().remove();
    }

    /**
     * emitter name unique for one project
     */
    String getName();

    void setName(String name);

    /**
     * is it a sub emitter which not be emitted by default.
     */
    boolean isSubEmitter();

    void setSubEmitter(boolean isSubEmitter);

    /**
     * particles emitted from this emitter.
     * <br>
     * you should not modify it, just read data.
     */
    Map<PhotonParticleRenderType, Queue<LParticle>> getParticles();

    /**
     * emit particle from this emitter
     */
    boolean emitParticle(LParticle particle);

    /**
     * should render particle
     */
    boolean isVisible();

    /**
     * set particle visible
     */
    void setVisible(boolean visible);

    /**
     * get the box of cull.
     * <br>
     * return null - culling disabled.
     */
    @Nullable
    AABB getCullBox(float partialTicks);

    /**
     * use bloom effect
     */
    boolean usingBloom();

    void setEffect(IEffect effect);

    IEffect getEffect();

    default boolean isDev() {
        return getEffect() instanceof EditorEffect;
    }
}
