package com.lowdragmc.lowdraglib.client.bakedpipeline;

import com.mojang.math.Vector3f;
import expr.Expr;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/FaceQuad.class */
public class FaceQuad {
    public static final AABB BLOCK = new AABB(0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 1.0d);

    public static BakedQuad bakeFace(AABB cube, Direction face, TextureAtlasSprite sprite, ModelState rotation, int tintIndex, int emissivity, boolean cull, boolean shade) {
        return new FaceQuadBakery().bakeQuad(new Vector3f(((float) cube.f_82288_) * 16.0f, ((float) cube.f_82289_) * 16.0f, ((float) cube.f_82290_) * 16.0f), new Vector3f(((float) cube.f_82291_) * 16.0f, ((float) cube.f_82292_) * 16.0f, ((float) cube.f_82293_) * 16.0f), new BlockElementFace(cull ? face : null, tintIndex, "", new BlockFaceUV(new float[]{0.0f, 0.0f, 16.0f, 16.0f}, 0)), sprite, face, rotation, null, shade, emissivity);
    }

    public static BakedQuad bakeFace(Direction face, TextureAtlasSprite sprite, ModelState rotation, int tintIndex, int emissivity, boolean cull, boolean shade) {
        return bakeFace(BLOCK, face, sprite, rotation, tintIndex, emissivity, cull, shade);
    }

    public static BakedQuad bakeFace(Direction face, TextureAtlasSprite sprite, ModelState rotation, int tintIndex, int emissivity) {
        return bakeFace(face, sprite, rotation, tintIndex, emissivity, true, true);
    }

    public static BakedQuad bakeFace(Direction face, TextureAtlasSprite sprite, ModelState rotation, int tintIndex) {
        return bakeFace(face, sprite, rotation, tintIndex, 0);
    }

    public static BakedQuad bakeFace(Direction face, TextureAtlasSprite sprite, ModelState rotation) {
        return bakeFace(face, sprite, rotation, -1);
    }

    public static BakedQuad bakeFace(Direction face, TextureAtlasSprite sprite) {
        return bakeFace(face, sprite, BlockModelRotation.X0_Y0);
    }

    public static Builder builder(Direction face, TextureAtlasSprite sprite) {
        return new Builder(face, sprite);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/FaceQuad$Builder.class */
    public static class Builder {
        Direction face;
        TextureAtlasSprite sprite;
        Vector3f from = Vector3f.f_176763_;
        Vector3f to = new Vector3f(16.0f, 16.0f, 16.0f);
        ModelState rotation = BlockModelRotation.X0_Y0;
        int tintIndex = -1;
        int emissivity = 0;
        boolean cull = true;
        boolean shade = true;
        UVPair uv0 = new UVPair(0.0f, 0.0f);
        UVPair uv1 = new UVPair(16.0f, 16.0f);

        public Builder from(Vector3f from) {
            this.from = from;
            return this;
        }

        public Builder to(Vector3f to) {
            this.to = to;
            return this;
        }

        public Builder face(Direction face) {
            this.face = face;
            return this;
        }

        public Builder sprite(TextureAtlasSprite sprite) {
            this.sprite = sprite;
            return this;
        }

        public Builder rotation(ModelState rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder tintIndex(int tintIndex) {
            this.tintIndex = tintIndex;
            return this;
        }

        public Builder emissivity(int emissivity) {
            this.emissivity = emissivity;
            return this;
        }

        public Builder cull(boolean cull) {
            this.cull = cull;
            return this;
        }

        public Builder shade(boolean shade) {
            this.shade = shade;
            return this;
        }

        public Builder uv0(UVPair uv0) {
            this.uv0 = uv0;
            return this;
        }

        public Builder uv1(UVPair uv1) {
            this.uv1 = uv1;
            return this;
        }

        protected Builder(Direction face, TextureAtlasSprite sprite) {
            this.face = face;
            this.sprite = sprite;
        }

        public Builder cube(AABB cube) {
            this.from = new Vector3f(((float) cube.f_82288_) * 16.0f, ((float) cube.f_82289_) * 16.0f, ((float) cube.f_82290_) * 16.0f);
            this.to = new Vector3f(((float) cube.f_82291_) * 16.0f, ((float) cube.f_82292_) * 16.0f, ((float) cube.f_82293_) * 16.0f);
            return this;
        }

        public Builder uv0(float u0, float v0) {
            this.uv0 = new UVPair(u0, v0);
            return this;
        }

        public Builder uv1(float u1, float v1) {
            this.uv1 = new UVPair(u1, v1);
            return this;
        }

        public Builder cubeUV() {
            switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction[this.face.ordinal()]) {
                case 1:
                    return uv0(this.from.m_122239_(), this.from.m_122269_()).uv1(this.to.m_122239_(), this.to.m_122269_());
                case 2:
                    return uv0(this.from.m_122239_(), this.to.m_122269_()).uv1(this.to.m_122239_(), this.from.m_122269_());
                case 3:
                    return uv0(this.to.m_122239_(), this.to.m_122260_()).uv1(this.from.m_122239_(), this.from.m_122260_());
                case 4:
                    return uv0(this.from.m_122239_(), this.to.m_122260_()).uv1(this.to.m_122239_(), this.from.m_122260_());
                case Expr.ATAN2 /* 5 */:
                    return uv0(this.from.m_122269_(), this.to.m_122260_()).uv1(this.to.m_122269_(), this.from.m_122260_());
                case Expr.MAX /* 6 */:
                    return uv0(this.to.m_122269_(), this.to.m_122260_()).uv1(this.from.m_122269_(), this.from.m_122260_());
                default:
                    throw new IncompatibleClassChangeError();
            }
        }

        public BakedQuad bake() {
            return new FaceQuadBakery().bakeQuad(this.from, this.to, new BlockElementFace(this.cull ? this.face : null, this.tintIndex, "", new BlockFaceUV(new float[]{this.uv0.m_171612_(), this.uv0.m_171613_(), this.uv1.m_171612_(), this.uv1.m_171613_()}, 0)), this.sprite, this.face, this.rotation, null, this.shade, this.emissivity);
        }
    }

    /* renamed from: com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad$1  reason: invalid class name */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/FaceQuad$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$core$Direction = new int[Direction.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.UP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }
}
