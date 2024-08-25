package com.lowdragmc.lowdraglib.client.bakedpipeline;

import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec2;

@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/Quad.class */
public class Quad {
    private static final TextureAtlasSprite BASE = (TextureAtlasSprite) Minecraft.m_91087_().m_91258_(TextureAtlas.f_118259_).apply(MissingTextureAtlasSprite.m_118071_());
    private final Vector3f[] vertPos;
    private final Vec2[] vertUv;
    private UVs uvs;
    private final Builder builder;
    private final int blocklight;
    private final int skylight;

    public String toString() {
        return "Quad(vertPos=" + Arrays.deepToString(this.vertPos) + ", vertUv=" + Arrays.deepToString(this.vertUv) + ")";
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/Quad$Vertex.class */
    public static final class Vertex {
        private final Vector3f pos;
        private final Vec2 uvs;

        public Vertex(Vector3f pos, Vec2 uvs) {
            this.pos = pos;
            this.uvs = uvs;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Vertex) {
                Vertex other = (Vertex) o;
                Object this$pos = getPos();
                Object other$pos = other.getPos();
                if (this$pos == null) {
                    if (other$pos != null) {
                        return false;
                    }
                } else if (!this$pos.equals(other$pos)) {
                    return false;
                }
                Object this$uvs = getUvs();
                Object other$uvs = other.getUvs();
                return this$uvs == null ? other$uvs == null : this$uvs.equals(other$uvs);
            }
            return false;
        }

        public int hashCode() {
            Object $pos = getPos();
            int result = (1 * 59) + ($pos == null ? 43 : $pos.hashCode());
            Object $uvs = getUvs();
            return (result * 59) + ($uvs == null ? 43 : $uvs.hashCode());
        }

        public String toString() {
            return "Quad.Vertex(pos=" + getPos() + ", uvs=" + getUvs() + ")";
        }

        public Vector3f getPos() {
            return this.pos;
        }

        public Vec2 getUvs() {
            return this.uvs;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/Quad$UVs.class */
    public static class UVs {
        private float minU;
        private float minV;
        private float maxU;
        private float maxV;
        private final TextureAtlasSprite sprite;
        private final Vec2[] data;

        public String toString() {
            return "Quad.UVs(minU=" + getMinU() + ", minV=" + getMinV() + ", maxU=" + getMaxU() + ", maxV=" + getMaxV() + ", sprite=" + getSprite() + ", data=" + Arrays.deepToString(this.data) + ")";
        }

        public float getMinU() {
            return this.minU;
        }

        public float getMinV() {
            return this.minV;
        }

        public float getMaxU() {
            return this.maxU;
        }

        public float getMaxV() {
            return this.maxV;
        }

        public TextureAtlasSprite getSprite() {
            return this.sprite;
        }

        private UVs(Vec2... data) {
            this(Quad.BASE, data);
        }

        private UVs(TextureAtlasSprite sprite, Vec2... data) {
            this.data = data;
            this.sprite = sprite;
            float minU = Float.MAX_VALUE;
            float minV = Float.MAX_VALUE;
            float maxU = 0.0f;
            float maxV = 0.0f;
            for (Vec2 v : data) {
                minU = Math.min(minU, v.f_82470_);
                minV = Math.min(minV, v.f_82471_);
                maxU = Math.max(maxU, v.f_82470_);
                maxV = Math.max(maxV, v.f_82471_);
            }
            this.minU = minU;
            this.minV = minV;
            this.maxU = maxU;
            this.maxV = maxV;
        }

        public UVs(float minU, float minV, float maxU, float maxV, TextureAtlasSprite sprite) {
            this.minU = minU;
            this.minV = minV;
            this.maxU = maxU;
            this.maxV = maxV;
            this.sprite = sprite;
            this.data = vectorize();
        }

        public UVs transform(TextureAtlasSprite other, ISubmap submap) {
            UVs normal = normalize();
            ISubmap submap2 = submap.normalize();
            float width = normal.maxU - normal.minU;
            float height = normal.maxV - normal.minV;
            float minU = submap2.getXOffset();
            float minV = submap2.getYOffset();
            float minU2 = minU + (normal.minU * submap2.getWidth());
            float minV2 = minV + (normal.minV * submap2.getHeight());
            float maxU = minU2 + (width * submap2.getWidth());
            float maxV = minV2 + (height * submap2.getHeight());
            Vec2[] vec2Arr = new Vec2[4];
            vec2Arr[0] = new Vec2(this.data[0].f_82470_ == this.minU ? minU2 : maxU, this.data[0].f_82471_ == this.minV ? minV2 : maxV);
            vec2Arr[1] = new Vec2(this.data[1].f_82470_ == this.minU ? minU2 : maxU, this.data[1].f_82471_ == this.minV ? minV2 : maxV);
            vec2Arr[2] = new Vec2(this.data[2].f_82470_ == this.minU ? minU2 : maxU, this.data[2].f_82471_ == this.minV ? minV2 : maxV);
            vec2Arr[3] = new Vec2(this.data[3].f_82470_ == this.minU ? minU2 : maxU, this.data[3].f_82471_ == this.minV ? minV2 : maxV);
            return new UVs(other, vec2Arr).relativize();
        }

        UVs normalizeQuadrant() {
            UVs normal = normalize();
            int quadrant = normal.getQuadrant();
            float minUInterp = (quadrant == 1 || quadrant == 2) ? 0.5f : 0.0f;
            float minVInterp = quadrant < 2 ? 0.5f : 0.0f;
            float maxUInterp = (quadrant == 0 || quadrant == 3) ? 0.5f : 1.0f;
            float maxVInterp = quadrant > 1 ? 0.5f : 1.0f;
            return new UVs(this.sprite, normalize(new Vec2(minUInterp, minVInterp), new Vec2(maxUInterp, maxVInterp), normal.vectorize())).relativize();
        }

        public UVs normalize() {
            Vec2 min = new Vec2(this.sprite.m_118409_(), this.sprite.m_118411_());
            Vec2 max = new Vec2(this.sprite.m_118410_(), this.sprite.m_118412_());
            return new UVs(this.sprite, normalize(min, max, this.data));
        }

        public UVs relativize() {
            return relativize(this.sprite);
        }

        public UVs relativize(TextureAtlasSprite sprite) {
            Vec2 min = new Vec2(sprite.m_118409_(), sprite.m_118411_());
            Vec2 max = new Vec2(sprite.m_118410_(), sprite.m_118412_());
            return new UVs(sprite, lerp(min, max, this.data));
        }

        public Vec2[] vectorize() {
            return this.data == null ? new Vec2[]{new Vec2(this.minU, this.minV), new Vec2(this.minU, this.maxV), new Vec2(this.maxU, this.maxV), new Vec2(this.maxU, this.minV)} : this.data;
        }

        private Vec2[] normalize(Vec2 min, Vec2 max, Vec2... vecs) {
            Vec2[] ret = new Vec2[vecs.length];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = normalize(min, max, vecs[i]);
            }
            return ret;
        }

        private Vec2 normalize(Vec2 min, Vec2 max, Vec2 vec) {
            return new Vec2(Quad.normalize(min.f_82470_, max.f_82470_, vec.f_82470_), Quad.normalize(min.f_82471_, max.f_82471_, vec.f_82471_));
        }

        private Vec2[] lerp(Vec2 min, Vec2 max, Vec2... vecs) {
            Vec2[] ret = new Vec2[vecs.length];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = lerp(min, max, vecs[i]);
            }
            return ret;
        }

        private Vec2 lerp(Vec2 min, Vec2 max, Vec2 vec) {
            return new Vec2(Quad.lerp(min.f_82470_, max.f_82470_, vec.f_82470_), Quad.lerp(min.f_82471_, max.f_82471_, vec.f_82471_));
        }

        public int getQuadrant() {
            if (this.maxU <= 0.5f) {
                if (this.maxV <= 0.5f) {
                    return 3;
                }
                return 0;
            } else if (this.maxV <= 0.5f) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    public UVs getUvs() {
        return this.uvs;
    }

    private Quad(Vector3f[] verts, Vec2[] uvs, Builder builder, TextureAtlasSprite sprite) {
        this(verts, uvs, builder, sprite, 0, 0);
    }

    private Quad(Vector3f[] verts, Vec2[] uvs, Builder builder, TextureAtlasSprite sprite, int blocklight, int skylight) {
        this.vertPos = verts;
        this.vertUv = uvs;
        this.builder = builder;
        this.uvs = new UVs(sprite, uvs);
        this.blocklight = blocklight;
        this.skylight = skylight;
    }

    private Quad(Vector3f[] verts, UVs uvs, Builder builder, int blocklight, int skylight) {
        this(verts, uvs.vectorize(), builder, uvs.getSprite(), blocklight, skylight);
    }

    public Vector3f getVert(int index) {
        return this.vertPos[index % 4].m_122281_();
    }

    public Quad withVert(int index, Vector3f vert) {
        Vector3f[] newverts = new Vector3f[4];
        System.arraycopy(this.vertPos, 0, newverts, 0, newverts.length);
        newverts[index] = vert;
        return new Quad(newverts, getUvs(), this.builder, this.blocklight, this.skylight);
    }

    public Vec2 getUv(int index) {
        return new Vec2(this.vertUv[index % 4].f_82470_, this.vertUv[index % 4].f_82471_);
    }

    public Quad withUv(int index, Vec2 uv) {
        Vec2[] newuvs = new Vec2[4];
        System.arraycopy(getUvs().vectorize(), 0, newuvs, 0, newuvs.length);
        newuvs[index] = uv;
        return new Quad(this.vertPos, new UVs(newuvs), this.builder, this.blocklight, this.skylight);
    }

    public void compute() {
    }

    public Quad[] subdivide(int count) {
        if (count == 1) {
            return new Quad[]{this};
        }
        if (count != 4) {
            throw new UnsupportedOperationException();
        }
        List<Quad> rects = new ArrayList<>();
        Pair<Quad, Quad> firstDivide = divide(false);
        Pair<Quad, Quad> secondDivide = ((Quad) firstDivide.left()).divide(true);
        rects.add((Quad) secondDivide.left());
        if (firstDivide.right() != null) {
            Pair<Quad, Quad> thirdDivide = ((Quad) firstDivide.right()).divide(true);
            rects.add((Quad) thirdDivide.left());
            rects.add((Quad) thirdDivide.right());
        } else {
            rects.add(null);
            rects.add(null);
        }
        rects.add((Quad) secondDivide.right());
        return (Quad[]) rects.toArray(x$0 -> {
            return new Quad[x$0];
        });
    }

    private Pair<Quad, Quad> divide(boolean vertical) {
        float min;
        float max;
        UVs uvs = getUvs().normalize();
        if (vertical) {
            min = uvs.minV;
            max = uvs.maxV;
        } else {
            min = uvs.minU;
            max = uvs.maxU;
        }
        if (min < 0.5d && max > 0.5d) {
            UVs first = new UVs(vertical ? uvs.minU : 0.5f, vertical ? 0.5f : uvs.minV, uvs.maxU, uvs.maxV, uvs.getSprite());
            UVs second = new UVs(uvs.minU, uvs.minV, vertical ? uvs.maxU : 0.5f, vertical ? 0.5f : uvs.maxV, uvs.getSprite());
            int firstIndex = 0;
            int i = 0;
            while (true) {
                if (i >= this.vertUv.length) {
                    break;
                } else if (this.vertUv[i].f_82471_ != getUvs().minV || this.vertUv[i].f_82470_ != getUvs().minU) {
                    i++;
                } else {
                    firstIndex = i;
                    break;
                }
            }
            float f = (0.5f - min) / (max - min);
            Vector3f[] firstQuad = new Vector3f[4];
            Vector3f[] secondQuad = new Vector3f[4];
            for (int i2 = 0; i2 < 4; i2++) {
                int idx = (firstIndex + i2) % 4;
                firstQuad[i2] = this.vertPos[idx].m_122281_();
                secondQuad[i2] = this.vertPos[idx].m_122281_();
            }
            int i22 = vertical ? (char) 1 : (char) 3;
            int j1 = vertical ? (char) 3 : (char) 1;
            firstQuad[0].m_122245_(lerp(firstQuad[0].m_122239_(), firstQuad[i22].m_122239_(), f), lerp(firstQuad[0].m_122260_(), firstQuad[i22].m_122260_(), f), lerp(firstQuad[0].m_122269_(), firstQuad[i22].m_122269_(), f));
            firstQuad[j1].m_122245_(lerp(firstQuad[j1].m_122239_(), firstQuad[2].m_122239_(), f), lerp(firstQuad[j1].m_122260_(), firstQuad[2].m_122260_(), f), lerp(firstQuad[j1].m_122269_(), firstQuad[2].m_122269_(), f));
            secondQuad[i22].m_122245_(lerp(secondQuad[0].m_122239_(), secondQuad[i22].m_122239_(), f), lerp(secondQuad[0].m_122260_(), secondQuad[i22].m_122260_(), f), lerp(secondQuad[0].m_122269_(), secondQuad[i22].m_122269_(), f));
            secondQuad[2].m_122245_(lerp(secondQuad[j1].m_122239_(), secondQuad[2].m_122239_(), f), lerp(secondQuad[j1].m_122260_(), secondQuad[2].m_122260_(), f), lerp(secondQuad[j1].m_122269_(), secondQuad[2].m_122269_(), f));
            Quad q1 = new Quad(firstQuad, first.relativize(), this.builder, this.blocklight, this.skylight);
            Quad q2 = new Quad(secondQuad, second.relativize(), this.builder, this.blocklight, this.skylight);
            return Pair.of(q1, q2);
        }
        return Pair.of(this, (Object) null);
    }

    public static float lerp(float a, float b, float f) {
        return (a * (1.0f - f)) + (b * f);
    }

    public static float normalize(float min, float max, float x) {
        return (x - min) / (max - min);
    }

    public Quad rotate(int amount) {
        Vec2 vec2;
        Vec2[] uvs = new Vec2[4];
        TextureAtlasSprite s = getUvs().getSprite();
        for (int i = 0; i < 4; i++) {
            Vec2 normalized = new Vec2(normalize(s.m_118409_(), s.m_118410_(), this.vertUv[i].f_82470_), normalize(s.m_118411_(), s.m_118412_(), this.vertUv[i].f_82471_));
            switch (amount) {
                case 1:
                    vec2 = new Vec2(normalized.f_82471_, 1.0f - normalized.f_82470_);
                    break;
                case 2:
                    vec2 = new Vec2(1.0f - normalized.f_82470_, 1.0f - normalized.f_82471_);
                    break;
                case 3:
                    vec2 = new Vec2(1.0f - normalized.f_82471_, normalized.f_82470_);
                    break;
                default:
                    vec2 = new Vec2(normalized.f_82470_, normalized.f_82471_);
                    break;
            }
            Vec2 uv = vec2;
            uvs[i] = uv;
        }
        for (int i2 = 0; i2 < uvs.length; i2++) {
            uvs[i2] = new Vec2(lerp(s.m_118409_(), s.m_118410_(), uvs[i2].f_82470_), lerp(s.m_118411_(), s.m_118412_(), uvs[i2].f_82471_));
        }
        return new Quad(this.vertPos, uvs, this.builder, getUvs().getSprite(), this.blocklight, this.skylight);
    }

    public Quad derotate() {
        int start = 0;
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            } else if (this.vertUv[i].f_82470_ > getUvs().minU || this.vertUv[i].f_82471_ > getUvs().minV) {
                i++;
            } else {
                start = i;
                break;
            }
        }
        Vec2[] uvs = new Vec2[4];
        for (int i2 = 0; i2 < 4; i2++) {
            uvs[i2] = this.vertUv[(i2 + start) % 4];
        }
        return new Quad(this.vertPos, uvs, this.builder, getUvs().getSprite(), this.blocklight, this.skylight);
    }

    public Quad setLight(int blocklight, int skylight) {
        return new Quad(this.vertPos, this.uvs, this.builder, Math.max(this.blocklight, blocklight), Math.max(this.skylight, skylight));
    }

    public BakedQuad rebake() {
        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> {
            quad[0] = q;
        });
        builder.setDirection(this.builder.quadOrientation);
        builder.setTintIndex(this.builder.quadTint);
        builder.setShade(this.builder.applyDiffuseLighting);
        builder.setSprite(this.uvs.getSprite());
        VertexFormat format = DefaultVertexFormat.f_85811_;
        for (int v = 0; v < 4; v++) {
            for (int i = 0; i < format.m_86023_().size(); i++) {
                VertexFormatElement ele = (VertexFormatElement) format.m_86023_().get(i);
                switch (AnonymousClass1.$SwitchMap$com$mojang$blaze3d$vertex$VertexFormatElement$Usage[ele.m_86048_().ordinal()]) {
                    case 1:
                        Vector3f p = this.vertPos[v];
                        builder.m_5483_(p.m_122239_(), p.m_122260_(), p.m_122269_());
                        break;
                    case 2:
                        if (ele.m_86049_() == 2) {
                            builder.m_7120_(this.blocklight * 16, this.skylight * 16);
                            break;
                        } else if (ele.m_86049_() == 0) {
                            Vec2 uv = this.vertUv[v];
                            builder.m_7421_(uv.f_82470_, uv.f_82471_);
                            break;
                        }
                    default:
                        builder.misc(ele, this.builder.packedByElement.get(ele)[v]);
                        break;
                }
            }
            builder.m_5752_();
        }
        return quad[0];
    }

    /* renamed from: com.lowdragmc.lowdraglib.client.bakedpipeline.Quad$1  reason: invalid class name */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/Quad$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mojang$blaze3d$vertex$VertexFormatElement$Usage = new int[VertexFormatElement.Usage.values().length];

        static {
            try {
                $SwitchMap$com$mojang$blaze3d$vertex$VertexFormatElement$Usage[VertexFormatElement.Usage.POSITION.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mojang$blaze3d$vertex$VertexFormatElement$Usage[VertexFormatElement.Usage.UV.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public Quad transformUVs(TextureAtlasSprite sprite) {
        return transformUVs(sprite, Submap.FULL_TEXTURE.normalize());
    }

    public Quad transformUVs(TextureAtlasSprite sprite, ISubmap submap) {
        return new Quad(this.vertPos, getUvs().transform(sprite, submap), this.builder, this.blocklight, this.skylight);
    }

    public Quad grow() {
        return new Quad(this.vertPos, getUvs().normalizeQuadrant(), this.builder, this.blocklight, this.skylight);
    }

    public static Quad from(BakedQuad baked) {
        Builder b = new Builder(baked.m_173410_());
        b.copyFrom(baked, 0.0f);
        return b.build();
    }

    public static Quad from(BakedQuad baked, float offset) {
        Builder b = new Builder(baked.m_173410_());
        b.copyFrom(baked, offset);
        return b.build();
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/Quad$Builder.class */
    public static class Builder {
        private final TextureAtlasSprite sprite;
        private Direction quadOrientation;
        private boolean applyDiffuseLighting;
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
        private int quadTint = -1;
        private final float[][] positions = new float[4];
        private final float[][] uvs = new float[4];
        private final int[][] uvs2 = new int[4];
        private final int[][] colors = new int[4];
        private Map<VertexFormatElement, int[][]> packedByElement = new HashMap();

        /* JADX WARN: Type inference failed for: r1v11, types: [int[], int[][]] */
        /* JADX WARN: Type inference failed for: r1v5, types: [float[], float[][]] */
        /* JADX WARN: Type inference failed for: r1v7, types: [float[], float[][]] */
        /* JADX WARN: Type inference failed for: r1v9, types: [int[], int[][]] */
        public Builder(TextureAtlasSprite sprite) {
            this.sprite = sprite;
        }

        public TextureAtlasSprite getSprite() {
            return this.sprite;
        }

        public void setQuadTint(int quadTint) {
            this.quadTint = quadTint;
        }

        public void setQuadOrientation(Direction quadOrientation) {
            this.quadOrientation = quadOrientation;
        }

        public void setApplyDiffuseLighting(boolean applyDiffuseLighting) {
            this.applyDiffuseLighting = applyDiffuseLighting;
        }

        public void copyFrom(BakedQuad baked, float directionOffset) {
            setQuadTint(baked.m_111305_());
            setQuadOrientation(baked.m_111306_());
            setApplyDiffuseLighting(baked.m_111307_());
            int[] vertices = baked.m_111303_();
            for (int i = 0; i < 4; i++) {
                int offset = i * IQuadTransformer.STRIDE;
                float[] fArr = new float[4];
                fArr[0] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.POSITION]);
                fArr[1] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.POSITION + 1]);
                fArr[2] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.POSITION + 2]);
                fArr[3] = 0.0f;
                this.positions[i] = fArr;
                if (this.quadOrientation != null && directionOffset != 0.0f) {
                    float[] fArr2 = this.positions[i];
                    fArr2[0] = fArr2[0] + (directionOffset * this.quadOrientation.m_122429_());
                    float[] fArr3 = this.positions[i];
                    fArr3[1] = fArr3[1] + (directionOffset * this.quadOrientation.m_122430_());
                    float[] fArr4 = this.positions[i];
                    fArr4[2] = fArr4[2] + (directionOffset * this.quadOrientation.m_122431_());
                }
                int packedColor = vertices[offset + IQuadTransformer.COLOR];
                int[] iArr = new int[4];
                iArr[0] = packedColor & 255;
                iArr[1] = (packedColor << 8) & 255;
                iArr[2] = (packedColor << 16) & 255;
                iArr[3] = (packedColor << 24) & 255;
                this.colors[i] = iArr;
                float[] fArr5 = new float[2];
                fArr5[0] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.UV0]);
                fArr5[1] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.UV0 + 1]);
                this.uvs[i] = fArr5;
                int lightMap = vertices[offset + IQuadTransformer.UV2];
                int[] iArr2 = new int[2];
                iArr2[0] = lightMap & 65535;
                iArr2[1] = (lightMap >> 16) & 65535;
                this.uvs2[i] = iArr2;
            }
            for (Map.Entry<VertexFormatElement, Integer> e : this.ELEMENT_OFFSETS.entrySet()) {
                Integer offset2 = e.getValue();
                int[][] data = new int[4][e.getKey().m_86050_() / 4];
                for (int v = 0; v < 4; v++) {
                    for (int i2 = 0; i2 < data[v].length; i2++) {
                        data[v][i2] = vertices[(v * IQuadTransformer.STRIDE) + offset2.intValue() + i2];
                    }
                }
                this.packedByElement.put(e.getKey(), data);
            }
        }

        public Quad build() {
            Vector3f[] verts = new Vector3f[4];
            Vec2[] uvs = new Vec2[4];
            for (int i = 0; i < verts.length; i++) {
                verts[i] = new Vector3f(this.positions[i][0], this.positions[i][1], this.positions[i][2]);
                uvs[i] = new Vec2(this.uvs[i][0], this.uvs[i][1]);
            }
            return new Quad(verts, uvs, this, getSprite(), this.uvs2[0][0] >> 4, this.uvs2[0][1] >> 4);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private <T> T[] fromData(List<float[]> data, int size) {
            T[] tArr = (T[]) (size == 2 ? new Vec2[data.size()] : new Vector3f[data.size()]);
            for (int i = 0; i < data.size(); i++) {
                tArr[i] = size == 2 ? new Vec2(data.get(i)[0], data.get(i)[1]) : new Vector3f(data.get(i)[0], data.get(i)[1], data.get(i)[2]);
            }
            return tArr;
        }

        public void setTexture(@Nullable TextureAtlasSprite texture) {
        }
    }
}
