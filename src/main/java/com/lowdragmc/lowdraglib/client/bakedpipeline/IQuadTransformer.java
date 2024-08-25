package com.lowdragmc.lowdraglib.client.bakedpipeline;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/IQuadTransformer.class */
public interface IQuadTransformer {
    public static final int STRIDE = DefaultVertexFormat.f_85811_.m_86017_();
    public static final int POSITION = findOffset(DefaultVertexFormat.f_85804_);
    public static final int COLOR = findOffset(DefaultVertexFormat.f_85805_);
    public static final int UV0 = findOffset(DefaultVertexFormat.f_85806_);
    public static final int UV1 = findOffset(DefaultVertexFormat.f_85807_);
    public static final int UV2 = findOffset(DefaultVertexFormat.f_85808_);
    public static final int NORMAL = findOffset(DefaultVertexFormat.f_85809_);

    void processInPlace(BakedQuad bakedQuad);

    default void processInPlace(List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            processInPlace(quad);
        }
    }

    default BakedQuad process(BakedQuad quad) {
        BakedQuad copy = copy(quad);
        processInPlace(copy);
        return copy;
    }

    default List<BakedQuad> process(List<BakedQuad> inputs) {
        return inputs.stream().map(IQuadTransformer::copy).peek(this::processInPlace).toList();
    }

    default IQuadTransformer andThen(IQuadTransformer other) {
        return quad -> {
            processInPlace(other);
            other.processInPlace(other);
        };
    }

    @Deprecated(forRemoval = true, since = "1.19")
    static IQuadTransformer empty() {
        return QuadTransformers.empty();
    }

    @Deprecated(forRemoval = true, since = "1.19")
    static IQuadTransformer applyingLightmap(int lightmap) {
        return QuadTransformers.applyingLightmap(lightmap);
    }

    private static BakedQuad copy(BakedQuad quad) {
        int[] vertices = quad.m_111303_();
        return new BakedQuad(Arrays.copyOf(vertices, vertices.length), quad.m_111305_(), quad.m_111306_(), quad.m_173410_(), quad.m_111307_());
    }

    private static int findOffset(VertexFormatElement element) {
        int index = DefaultVertexFormat.f_85811_.m_86023_().indexOf(element);
        if (index < 0) {
            return -1;
        }
        return DefaultVertexFormat.f_85811_.getOffsets().getInt(index) / 4;
    }
}
