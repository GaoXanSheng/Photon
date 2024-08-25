package com.lowdragmc.lowdraglib.client.bakedpipeline;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/QuadBakingVertexConsumer.class */
public class QuadBakingVertexConsumer implements VertexConsumer {
    private static final int QUAD_DATA_SIZE = IQuadTransformer.STRIDE * 4;
    private final Consumer<BakedQuad> quadConsumer;
    private int tintIndex;
    private boolean shade;
    private boolean hasAmbientOcclusion;
    private final Map<VertexFormatElement, Integer> ELEMENT_OFFSETS = (Map) Util.m_137469_(new IdentityHashMap(), map -> {
        int i = 0;
        UnmodifiableIterator it = DefaultVertexFormat.f_85811_.m_86023_().iterator();
        while (it.hasNext()) {
            VertexFormatElement element = (VertexFormatElement) it.next();
            int i2 = i;
            i++;
            map.put(element, Integer.valueOf(DefaultVertexFormat.f_85811_.getOffsets().getInt(i2) / 4));
        }
    });
    int vertexIndex = 0;
    private int[] quadData = new int[QUAD_DATA_SIZE];
    private Direction direction = Direction.DOWN;
    private TextureAtlasSprite sprite = (TextureAtlasSprite) Minecraft.m_91087_().m_91258_(TextureAtlas.f_118259_).apply(MissingTextureAtlasSprite.m_118071_());

    public QuadBakingVertexConsumer(Consumer<BakedQuad> quadConsumer) {
        this.quadConsumer = quadConsumer;
    }

    public VertexConsumer m_5483_(double x, double y, double z) {
        int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.POSITION;
        this.quadData[offset] = Float.floatToRawIntBits((float) x);
        this.quadData[offset + 1] = Float.floatToRawIntBits((float) y);
        this.quadData[offset + 2] = Float.floatToRawIntBits((float) z);
        return this;
    }

    public VertexConsumer m_5601_(float x, float y, float z) {
        int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.NORMAL;
        this.quadData[offset] = (((int) (x * 127.0f)) & 255) | ((((int) (y * 127.0f)) & 255) << 8) | ((((int) (z * 127.0f)) & 255) << 16);
        return this;
    }

    public VertexConsumer m_6122_(int red, int green, int blue, int alpha) {
        int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.COLOR;
        this.quadData[offset] = ((alpha & 255) << 24) | ((blue & 255) << 16) | ((green & 255) << 8) | (red & 255);
        return this;
    }

    public VertexConsumer m_7421_(float u, float v) {
        int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.UV0;
        this.quadData[offset] = Float.floatToRawIntBits(u);
        this.quadData[offset + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    public VertexConsumer m_7122_(int u, int v) {
        if (IQuadTransformer.UV1 >= 0) {
            int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.UV1;
            this.quadData[offset] = (u & 65535) | ((v & 65535) << 16);
        }
        return this;
    }

    public VertexConsumer m_7120_(int u, int v) {
        int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.UV2;
        this.quadData[offset] = (u & 65535) | ((v & 65535) << 16);
        return this;
    }

    public VertexConsumer misc(VertexFormatElement element, int... rawData) {
        Integer baseOffset = this.ELEMENT_OFFSETS.get(element);
        if (baseOffset != null) {
            int offset = (this.vertexIndex * IQuadTransformer.STRIDE) + baseOffset.intValue();
            System.arraycopy(rawData, 0, this.quadData, offset, rawData.length);
        }
        return this;
    }

    public void m_5752_() {
        int i = this.vertexIndex + 1;
        this.vertexIndex = i;
        if (i == 4) {
            this.quadConsumer.accept(new BakedQuad(this.quadData, this.tintIndex, this.direction, this.sprite, this.shade));
            this.vertexIndex = 0;
            this.quadData = new int[QUAD_DATA_SIZE];
        }
    }

    public void m_7404_(int defaultR, int defaultG, int defaultB, int defaultA) {
    }

    public void m_141991_() {
    }

    public void setTintIndex(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public void setHasAmbientOcclusion(boolean hasAmbientOcclusion) {
        this.hasAmbientOcclusion = hasAmbientOcclusion;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/QuadBakingVertexConsumer$Buffered.class */
    public static class Buffered extends QuadBakingVertexConsumer {
        private final BakedQuad[] output;

        public Buffered() {
            this(new BakedQuad[1]);
        }

        private Buffered(BakedQuad[] output) {
            super(q -> {
                output[0] = q;
            });
            this.output = output;
        }

        public BakedQuad getQuad() {
            BakedQuad quad = (BakedQuad) Preconditions.checkNotNull(this.output[0], "No quad has been emitted. Vertices in buffer: " + this.vertexIndex);
            this.output[0] = null;
            return quad;
        }
    }
}
