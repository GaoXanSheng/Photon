package com.lowdragmc.lowdraglib.core.mixins.accessor;

import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({VertexFormat.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/accessor/VertexFormatAccessor.class */
public interface VertexFormatAccessor {
    @Accessor
    IntList getOffsets();
}
