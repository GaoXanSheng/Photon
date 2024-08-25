package com.lowdragmc.photon.client.emitter.data.material;

import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/material/IMaterial.class */
public interface IMaterial extends IConfigurable, ITagSerializable<CompoundTag> {
    public static final List<Class<? extends IMaterial>> MATERIALS = new ArrayList(List.of(TextureMaterial.class, CustomShaderMaterial.class));

    void begin(boolean z);

    void end(boolean z);

    IGuiTexture preview();

    CompoundTag serializeNBT(CompoundTag compoundTag);

    static IMaterial deserializeWrapper(CompoundTag tag) {
        String type = tag.m_128461_("_type");
        for (Class<? extends IMaterial> clazz : MATERIALS) {
            if (clazz.getSimpleName().equals(type)) {
                try {
                    IMaterial shape = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                    shape.deserializeNBT(tag);
                    return shape;
                } catch (Throwable th) {
                }
            }
        }
        return null;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    default CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.m_128359_("_type", getClass().getSimpleName());
        return serializeNBT(tag);
    }

    default IMaterial copy() {
        return deserializeWrapper(mo129serializeNBT());
    }
}
