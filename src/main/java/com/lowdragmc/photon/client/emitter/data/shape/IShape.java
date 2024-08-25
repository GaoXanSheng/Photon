package com.lowdragmc.photon.client.emitter.data.shape;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.syncdata.IAutoPersistedSerializable;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.particle.LParticle;
import com.lowdragmc.photon.integration.PhotonLDLibPlugin;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/IShape.class */

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote IShape
 */
public interface IShape extends IConfigurable, IAutoPersistedSerializable {

    static CompoundTag serializeWrapper(IShape shape) {
        return shape.serializeNBT();
    }

    static IShape deserializeWrapper(CompoundTag tag) {
        var type = tag.getString("_type");
        var wrapper = PhotonLDLibPlugin.REGISTER_SHAPES.get(type);
        if (wrapper != null) {
            var shape = wrapper.creator().get();
            shape.deserializeNBT(tag);
            return shape;
        }
        return null;
    }

    void nextPosVel(LParticle particle, LParticle emitter, Vector3 position, Vector3 rotation, Vector3 scale);
}
