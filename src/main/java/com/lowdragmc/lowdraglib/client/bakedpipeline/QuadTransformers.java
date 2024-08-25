package com.lowdragmc.lowdraglib.client.bakedpipeline;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/QuadTransformers.class */
public class QuadTransformers {
    private static final IQuadTransformer EMPTY = quad -> {
    };
    private static final IQuadTransformer[] EMISSIVE_TRANSFORMERS = (IQuadTransformer[]) Util.m_137469_(new IQuadTransformer[16], array -> {
        Arrays.setAll(array, i -> {
            return applyingLightmap(LightTexture.m_109885_(i, i));
        });
    });

    public static IQuadTransformer empty() {
        return EMPTY;
    }

    public static IQuadTransformer applyingLightmap(int lightmap) {
        return quad -> {
            int[] vertices = quad.m_111303_();
            for (int i = 0; i < 4; i++) {
                vertices[(i * IQuadTransformer.STRIDE) + IQuadTransformer.UV2] = lightmap;
            }
        };
    }

    public static IQuadTransformer settingEmissivity(int emissivity) {
        Preconditions.checkArgument(emissivity >= 0 && emissivity < 16, "Emissivity must be between 0 and 15.");
        return EMISSIVE_TRANSFORMERS[emissivity];
    }

    public static IQuadTransformer settingMaxEmissivity() {
        return EMISSIVE_TRANSFORMERS[15];
    }

    private QuadTransformers() {
    }
}
