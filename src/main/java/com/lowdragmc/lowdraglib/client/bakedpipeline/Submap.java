package com.lowdragmc.lowdraglib.client.bakedpipeline;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/Submap.class */
public class Submap implements ISubmap {
    public static final Submap[] uvs = {new Submap(4.0f, 4.0f, 0.0f, 0.0f), new Submap(4.0f, 4.0f, 4.0f, 0.0f), new Submap(4.0f, 4.0f, 8.0f, 0.0f), new Submap(4.0f, 4.0f, 12.0f, 0.0f), new Submap(4.0f, 4.0f, 0.0f, 4.0f), new Submap(4.0f, 4.0f, 4.0f, 4.0f), new Submap(4.0f, 4.0f, 8.0f, 4.0f), new Submap(4.0f, 4.0f, 12.0f, 4.0f), new Submap(4.0f, 4.0f, 0.0f, 8.0f), new Submap(4.0f, 4.0f, 4.0f, 8.0f), new Submap(4.0f, 4.0f, 8.0f, 8.0f), new Submap(4.0f, 4.0f, 12.0f, 8.0f), new Submap(4.0f, 4.0f, 0.0f, 12.0f), new Submap(4.0f, 4.0f, 4.0f, 12.0f), new Submap(4.0f, 4.0f, 8.0f, 12.0f), new Submap(4.0f, 4.0f, 12.0f, 12.0f), new Submap(8.0f, 8.0f, 0.0f, 0.0f), new Submap(8.0f, 8.0f, 8.0f, 0.0f), new Submap(8.0f, 8.0f, 0.0f, 8.0f), new Submap(8.0f, 8.0f, 8.0f, 8.0f)};
    public static final Submap FULL_TEXTURE = new Submap(16.0f, 16.0f, 0.0f, 0.0f);
    public static final Submap X1 = new Submap(16.0f, 16.0f, 0.0f, 0.0f);
    public static final Submap[][] X2 = {new Submap[]{new Submap(8.0f, 8.0f, 0.0f, 0.0f), new Submap(8.0f, 8.0f, 8.0f, 0.0f)}, new Submap[]{new Submap(8.0f, 8.0f, 0.0f, 8.0f), new Submap(8.0f, 8.0f, 8.0f, 8.0f)}};
    private static final float DIV3 = 5.3333335f;
    public static final Submap[][] X3 = {new Submap[]{new Submap(DIV3, DIV3, 0.0f, 0.0f), new Submap(DIV3, DIV3, DIV3, 0.0f), new Submap(DIV3, DIV3, 10.666667f, 0.0f)}, new Submap[]{new Submap(DIV3, DIV3, 0.0f, DIV3), new Submap(DIV3, DIV3, DIV3, DIV3), new Submap(DIV3, DIV3, 10.666667f, DIV3)}, new Submap[]{new Submap(DIV3, DIV3, 0.0f, 10.666667f), new Submap(DIV3, DIV3, DIV3, 10.666667f), new Submap(DIV3, DIV3, 10.666667f, 10.666667f)}};
    public static final Submap[][] X4 = {new Submap[]{new Submap(4.0f, 4.0f, 0.0f, 0.0f), new Submap(4.0f, 4.0f, 4.0f, 0.0f), new Submap(4.0f, 4.0f, 8.0f, 0.0f), new Submap(4.0f, 4.0f, 12.0f, 0.0f)}, new Submap[]{new Submap(4.0f, 4.0f, 0.0f, 4.0f), new Submap(4.0f, 4.0f, 4.0f, 4.0f), new Submap(4.0f, 4.0f, 8.0f, 4.0f), new Submap(4.0f, 4.0f, 12.0f, 4.0f)}, new Submap[]{new Submap(4.0f, 4.0f, 0.0f, 8.0f), new Submap(4.0f, 4.0f, 4.0f, 8.0f), new Submap(4.0f, 4.0f, 8.0f, 8.0f), new Submap(4.0f, 4.0f, 12.0f, 8.0f)}, new Submap[]{new Submap(4.0f, 4.0f, 0.0f, 12.0f), new Submap(4.0f, 4.0f, 4.0f, 12.0f), new Submap(4.0f, 4.0f, 8.0f, 12.0f), new Submap(4.0f, 4.0f, 12.0f, 12.0f)}};
    public final float width;
    public final float height;
    public final float xOffset;
    public final float yOffset;
    private final SubmapNormalized normalized = new SubmapNormalized(this);

    public Submap(float width, float height, float xOffset, float yOffset) {
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getWidth() {
        return this.width;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getHeight() {
        return this.height;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getXOffset() {
        return this.xOffset;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getYOffset() {
        return this.yOffset;
    }

    public SubmapNormalized getNormalized() {
        return this.normalized;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getInterpolatedU(TextureAtlasSprite sprite, float u) {
        return sprite.m_118367_(getXOffset() + (u / getWidth()));
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float getInterpolatedV(TextureAtlasSprite sprite, float v) {
        return sprite.m_118393_(getYOffset() + (v / getWidth()));
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public float[] toArray() {
        return new float[]{getXOffset(), getYOffset(), getXOffset() + getWidth(), getYOffset() + getHeight()};
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public SubmapNormalized normalize() {
        return this.normalized;
    }

    @Override // com.lowdragmc.lowdraglib.client.bakedpipeline.ISubmap
    public Submap relativize() {
        return this;
    }
}
