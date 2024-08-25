package com.lowdragmc.lowdraglib.client.bakedpipeline;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/SubmapNormalized.class */
public class SubmapNormalized implements ISubmap {
    private static final float FACTOR = 16.0f;
    private final Submap parent;

    public SubmapNormalized(Submap submap) {
        this.parent = submap;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getXOffset() {
        return this.parent.getXOffset() / FACTOR;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getYOffset() {
        return this.parent.getYOffset() / FACTOR;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getWidth() {
        return this.parent.getWidth() / FACTOR;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getHeight() {
        return this.parent.getHeight() / FACTOR;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public Submap relativize() {
        return this.parent;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public SubmapNormalized normalize() {
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getInterpolatedU(TextureAtlasSprite sprite, float u) {
        return this.parent.getInterpolatedU(sprite, u);
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getInterpolatedV(TextureAtlasSprite sprite, float v) {
        return this.parent.getInterpolatedV(sprite, v);
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float[] toArray() {
        return this.parent.toArray();
    }
}
