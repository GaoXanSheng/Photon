package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.LDLib;
import java.net.URL;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ResourceHelper.class */
public class ResourceHelper {
    public static boolean isResourceExistRaw(ResourceLocation rs) {
        URL url = ResourceHelper.class.getResource(String.format("/assets/%s/%s", rs.m_135827_(), rs.m_135815_()));
        return url != null;
    }

    public static boolean isResourceExist(ResourceLocation rs) {
        if (LDLib.isClient()) {
            return Minecraft.m_91087_().m_91098_().m_213713_(rs).isPresent();
        }
        return false;
    }

    public static boolean isTextureExist(@Nonnull ResourceLocation location) {
        ResourceLocation textureLocation = new ResourceLocation(location.m_135827_(), "textures/%s.png".formatted(new Object[]{location.m_135815_()}));
        return isResourceExist(textureLocation) || isResourceExistRaw(textureLocation);
    }

    public static boolean isModelExist(@Nonnull ResourceLocation location) {
        ResourceLocation modelLocation = new ResourceLocation(location.m_135827_(), "models/%s.json".formatted(new Object[]{location.m_135815_()}));
        return isResourceExist(modelLocation) || isResourceExistRaw(modelLocation);
    }
}
