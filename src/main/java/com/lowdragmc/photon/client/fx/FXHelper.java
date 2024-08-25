package com.lowdragmc.photon.client.fx;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/FXHelper.class */
public class FXHelper {
    private static final Map<ResourceLocation, FX> CACHE = new HashMap();
    public static final String FX_PATH = "fx/";

    public static int clearCache() {
        int count = CACHE.size();
        CACHE.clear();
        return count;
    }

    @Nullable
    public static FX getFX(ResourceLocation fxLocation) {
        return CACHE.computeIfAbsent(fxLocation, location -> {
            ResourceLocation resourceLocation = new ResourceLocation(fxLocation.m_135827_(), "fx/" + fxLocation.m_135815_() + ".fx");
            try {
                InputStream inputStream = Minecraft.m_91087_().m_91098_().m_215595_(resourceLocation);
                CompoundTag tag = NbtIo.m_128939_(inputStream);
                FX fx = new FX(fxLocation, getEmitters(tag), tag);
                if (inputStream != null) {
                    inputStream.close();
                }
                return fx;
            } catch (Exception e) {
                return null;
            }
        });
    }

    public static List<IParticleEmitter> getEmitters(CompoundTag tag) {
        List<IParticleEmitter> emitters = new ArrayList<>();
        Iterator it = tag.m_128437_("emitters", 10).iterator();
        while (it.hasNext()) {
            CompoundTag compoundTag = (Tag) it.next();
            if (compoundTag instanceof CompoundTag) {
                CompoundTag data = compoundTag;
                IParticleEmitter emitter = IParticleEmitter.deserializeWrapper(data);
                if (emitter != null) {
                    emitters.add(emitter);
                }
            }
        }
        return emitters;
    }
}
