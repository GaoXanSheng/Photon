package com.lowdragmc.lowdraglib.client.bakedpipeline;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/ISubmap.class */
public interface ISubmap {
    float getYOffset();

    float getXOffset();

    float getWidth();

    float getHeight();

    float getInterpolatedU(TextureAtlasSprite textureAtlasSprite, float f);

    float getInterpolatedV(TextureAtlasSprite textureAtlasSprite, float f);

    float[] toArray();

    ISubmap normalize();

    ISubmap relativize();
}
