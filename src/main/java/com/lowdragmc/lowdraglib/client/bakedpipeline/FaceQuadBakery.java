package com.lowdragmc.lowdraglib.client.bakedpipeline;

import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockMath;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/FaceQuadBakery.class */
public class FaceQuadBakery {
    public static final int VERTEX_INT_SIZE = 8;
    private static final float RESCALE_22_5 = (1.0f / ((float) Math.cos(0.39269909262657166d))) - 1.0f;
    private static final float RESCALE_45 = (1.0f / ((float) Math.cos(0.7853981852531433d))) - 1.0f;
    public static final int VERTEX_COUNT = 4;
    private static final int COLOR_INDEX = 3;
    public static final int UV_INDEX = 4;

    public BakedQuad bakeQuad(Vector3f pPosFrom, Vector3f pPosTo, BlockElementFace pFace, TextureAtlasSprite pSprite, Direction pFacing, ModelState pTransform, @Nullable BlockElementRotation pPartRotation, boolean pShade, int emissivity) {
        BlockFaceUV blockfaceuv = pFace.f_111357_;
        if (pTransform.m_7538_()) {
            blockfaceuv = recomputeUVs(pFace.f_111357_, pFacing, pTransform.m_6189_());
        }
        float[] afloat = new float[blockfaceuv.f_111387_.length];
        System.arraycopy(blockfaceuv.f_111387_, 0, afloat, 0, afloat.length);
        float f = pSprite.m_118417_();
        float f1 = (((blockfaceuv.f_111387_[0] + blockfaceuv.f_111387_[0]) + blockfaceuv.f_111387_[2]) + blockfaceuv.f_111387_[2]) / 4.0f;
        float f2 = (((blockfaceuv.f_111387_[1] + blockfaceuv.f_111387_[1]) + blockfaceuv.f_111387_[3]) + blockfaceuv.f_111387_[3]) / 4.0f;
        blockfaceuv.f_111387_[0] = Mth.m_14179_(f, blockfaceuv.f_111387_[0], f1);
        blockfaceuv.f_111387_[2] = Mth.m_14179_(f, blockfaceuv.f_111387_[2], f1);
        blockfaceuv.f_111387_[1] = Mth.m_14179_(f, blockfaceuv.f_111387_[1], f2);
        blockfaceuv.f_111387_[3] = Mth.m_14179_(f, blockfaceuv.f_111387_[3], f2);
        int[] aint = makeVertices(blockfaceuv, pSprite, pFacing, setupShape(pPosFrom, pPosTo), pTransform.m_6189_(), pPartRotation, pShade);
        Direction direction = calculateFacing(aint);
        System.arraycopy(afloat, 0, blockfaceuv.f_111387_, 0, afloat.length);
        if (pPartRotation == null) {
            recalculateWinding(aint, direction);
        }
        fillNormal(aint, direction);
        BakedQuad quad = new BakedQuad(aint, pFace.f_111355_, direction, pSprite, pShade);
        QuadTransformers.settingEmissivity(emissivity).processInPlace(quad);
        return quad;
    }

    public static BlockFaceUV recomputeUVs(BlockFaceUV pUv, Direction pFacing, Transformation pModelRotation) {
        float f8;
        float f9;
        float f10;
        float f11;
        Matrix4f matrix4f = BlockMath.m_121844_(pModelRotation, pFacing, () -> {
            return "Unable to resolve UVLock for model";
        }).m_121104_();
        float f = pUv.m_111392_(pUv.m_111398_(0));
        float f1 = pUv.m_111396_(pUv.m_111398_(0));
        Vector4f vector4f = new Vector4f(f / 16.0f, f1 / 16.0f, 0.0f, 1.0f);
        vector4f.m_123607_(matrix4f);
        float f2 = 16.0f * vector4f.m_123601_();
        float f3 = 16.0f * vector4f.m_123615_();
        float f4 = pUv.m_111392_(pUv.m_111398_(2));
        float f5 = pUv.m_111396_(pUv.m_111398_(2));
        Vector4f vector4f1 = new Vector4f(f4 / 16.0f, f5 / 16.0f, 0.0f, 1.0f);
        vector4f1.m_123607_(matrix4f);
        float f6 = 16.0f * vector4f1.m_123601_();
        float f7 = 16.0f * vector4f1.m_123615_();
        if (Math.signum(f4 - f) == Math.signum(f6 - f2)) {
            f8 = f2;
            f9 = f6;
        } else {
            f8 = f6;
            f9 = f2;
        }
        if (Math.signum(f5 - f1) == Math.signum(f7 - f3)) {
            f10 = f3;
            f11 = f7;
        } else {
            f10 = f7;
            f11 = f3;
        }
        float f12 = (float) Math.toRadians(pUv.f_111388_);
        Vector3f vector3f = new Vector3f(Mth.m_14089_(f12), Mth.m_14031_(f12), 0.0f);
        Matrix3f matrix3f = new Matrix3f(matrix4f);
        vector3f.m_122249_(matrix3f);
        int i = Math.floorMod((-((int) Math.round(Math.toDegrees(Math.atan2(vector3f.m_122260_(), vector3f.m_122239_())) / 90.0d))) * 90, 360);
        return new BlockFaceUV(new float[]{f8, f10, f9, f11}, i);
    }

    private int[] makeVertices(BlockFaceUV pUvs, TextureAtlasSprite pSprite, Direction pOrientation, float[] pPosDiv16, Transformation pRotation, @Nullable BlockElementRotation pPartRotation, boolean pShade) {
        int[] aint = new int[32];
        for (int i = 0; i < 4; i++) {
            bakeVertex(aint, i, pOrientation, pUvs, pPosDiv16, pSprite, pRotation, pPartRotation, pShade);
        }
        return aint;
    }

    private float[] setupShape(Vector3f pPos1, Vector3f pPos2) {
        float[] afloat = new float[Direction.values().length];
        afloat[FaceInfo.Constants.f_108996_] = pPos1.m_122239_() / 16.0f;
        afloat[FaceInfo.Constants.f_108995_] = pPos1.m_122260_() / 16.0f;
        afloat[FaceInfo.Constants.f_108994_] = pPos1.m_122269_() / 16.0f;
        afloat[FaceInfo.Constants.f_108993_] = pPos2.m_122239_() / 16.0f;
        afloat[FaceInfo.Constants.f_108992_] = pPos2.m_122260_() / 16.0f;
        afloat[FaceInfo.Constants.f_108991_] = pPos2.m_122269_() / 16.0f;
        return afloat;
    }

    private void bakeVertex(int[] pVertexData, int pVertexIndex, Direction pFacing, BlockFaceUV pBlockFaceUV, float[] pPosDiv16, TextureAtlasSprite pSprite, Transformation pRotation, @Nullable BlockElementRotation pPartRotation, boolean pShade) {
        FaceInfo.VertexInfo faceinfo$vertexinfo = FaceInfo.m_108984_(pFacing).m_108982_(pVertexIndex);
        Vector3f vector3f = new Vector3f(pPosDiv16[faceinfo$vertexinfo.f_108998_], pPosDiv16[faceinfo$vertexinfo.f_108999_], pPosDiv16[faceinfo$vertexinfo.f_109000_]);
        applyElementRotation(vector3f, pPartRotation);
        applyModelRotation(vector3f, pRotation);
        fillVertex(pVertexData, pVertexIndex, vector3f, pSprite, pBlockFaceUV);
    }

    private void fillVertex(int[] pVertexData, int pVertexIndex, Vector3f pVector, TextureAtlasSprite pSprite, BlockFaceUV pBlockFaceUV) {
        int i = pVertexIndex * 8;
        pVertexData[i] = Float.floatToRawIntBits(pVector.m_122239_());
        pVertexData[i + 1] = Float.floatToRawIntBits(pVector.m_122260_());
        pVertexData[i + 2] = Float.floatToRawIntBits(pVector.m_122269_());
        pVertexData[i + 3] = -1;
        pVertexData[i + 4] = Float.floatToRawIntBits(pSprite.m_118367_((pBlockFaceUV.m_111392_(pVertexIndex) * 0.999d) + (pBlockFaceUV.m_111392_((pVertexIndex + 2) % 4) * 0.001d)));
        pVertexData[i + 4 + 1] = Float.floatToRawIntBits(pSprite.m_118393_((pBlockFaceUV.m_111396_(pVertexIndex) * 0.999d) + (pBlockFaceUV.m_111396_((pVertexIndex + 2) % 4) * 0.001d)));
    }

    private void applyElementRotation(Vector3f pVec, @Nullable BlockElementRotation pPartRotation) {
        Vector3f vector3f;
        Vector3f vector3f1;
        if (pPartRotation != null) {
            switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction$Axis[pPartRotation.f_111379_.ordinal()]) {
                case 1:
                    vector3f = Vector3f.f_122223_;
                    vector3f1 = new Vector3f(0.0f, 1.0f, 1.0f);
                    break;
                case 2:
                    vector3f = Vector3f.f_122225_;
                    vector3f1 = new Vector3f(1.0f, 0.0f, 1.0f);
                    break;
                case 3:
                    vector3f = Vector3f.f_122227_;
                    vector3f1 = new Vector3f(1.0f, 1.0f, 0.0f);
                    break;
                default:
                    throw new IllegalArgumentException("There are only 3 axes");
            }
            Quaternion quaternion = vector3f.m_122240_(pPartRotation.f_111380_);
            if (pPartRotation.f_111381_) {
                if (Math.abs(pPartRotation.f_111380_) == 22.5f) {
                    vector3f1.m_122261_(RESCALE_22_5);
                } else {
                    vector3f1.m_122261_(RESCALE_45);
                }
                vector3f1.m_122272_(1.0f, 1.0f, 1.0f);
            } else {
                vector3f1.m_122245_(1.0f, 1.0f, 1.0f);
            }
            rotateVertexBy(pVec, pPartRotation.f_111378_.m_122281_(), new Matrix4f(quaternion), vector3f1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuadBakery$1  reason: invalid class name */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/bakedpipeline/FaceQuadBakery$1.class */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$core$Direction$Axis = new int[Direction.Axis.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$core$Direction$Axis[Direction.Axis.X.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction$Axis[Direction.Axis.Y.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction$Axis[Direction.Axis.Z.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public void applyModelRotation(Vector3f pPos, Transformation pTransform) {
        if (pTransform != Transformation.m_121093_()) {
            rotateVertexBy(pPos, new Vector3f(0.5f, 0.5f, 0.5f), pTransform.m_121104_(), new Vector3f(1.0f, 1.0f, 1.0f));
        }
    }

    private void rotateVertexBy(Vector3f pPos, Vector3f pOrigin, Matrix4f pTransform, Vector3f pScale) {
        Vector4f vector4f = new Vector4f(pPos.m_122239_() - pOrigin.m_122239_(), pPos.m_122260_() - pOrigin.m_122260_(), pPos.m_122269_() - pOrigin.m_122269_(), 1.0f);
        vector4f.m_123607_(pTransform);
        vector4f.m_123611_(pScale);
        pPos.m_122245_(vector4f.m_123601_() + pOrigin.m_122239_(), vector4f.m_123615_() + pOrigin.m_122260_(), vector4f.m_123616_() + pOrigin.m_122269_());
    }

    public static Direction calculateFacing(int[] pFaceData) {
        Direction[] values;
        Vector3f vector3f = new Vector3f(Float.intBitsToFloat(pFaceData[0]), Float.intBitsToFloat(pFaceData[1]), Float.intBitsToFloat(pFaceData[2]));
        Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(pFaceData[8]), Float.intBitsToFloat(pFaceData[9]), Float.intBitsToFloat(pFaceData[10]));
        Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(pFaceData[16]), Float.intBitsToFloat(pFaceData[17]), Float.intBitsToFloat(pFaceData[18]));
        Vector3f vector3f3 = vector3f.m_122281_();
        vector3f3.m_122267_(vector3f1);
        Vector3f vector3f4 = vector3f2.m_122281_();
        vector3f4.m_122267_(vector3f1);
        Vector3f vector3f5 = vector3f4.m_122281_();
        vector3f5.m_122279_(vector3f3);
        vector3f5.m_122278_();
        Direction direction = null;
        float f = 0.0f;
        for (Direction direction1 : Direction.values()) {
            Vec3i vec3i = direction1.m_122436_();
            Vector3f vector3f6 = new Vector3f(vec3i.m_123341_(), vec3i.m_123342_(), vec3i.m_123343_());
            float f1 = vector3f5.m_122276_(vector3f6);
            if (f1 >= 0.0f && f1 > f) {
                f = f1;
                direction = direction1;
            }
        }
        return direction == null ? Direction.UP : direction;
    }

    private void recalculateWinding(int[] pVertices, Direction pDirection) {
        int[] aint = new int[pVertices.length];
        System.arraycopy(pVertices, 0, aint, 0, pVertices.length);
        float[] afloat = new float[Direction.values().length];
        afloat[FaceInfo.Constants.f_108996_] = 999.0f;
        afloat[FaceInfo.Constants.f_108995_] = 999.0f;
        afloat[FaceInfo.Constants.f_108994_] = 999.0f;
        afloat[FaceInfo.Constants.f_108993_] = -999.0f;
        afloat[FaceInfo.Constants.f_108992_] = -999.0f;
        afloat[FaceInfo.Constants.f_108991_] = -999.0f;
        for (int i = 0; i < 4; i++) {
            int j = 8 * i;
            float f = Float.intBitsToFloat(aint[j]);
            float f1 = Float.intBitsToFloat(aint[j + 1]);
            float f2 = Float.intBitsToFloat(aint[j + 2]);
            if (f < afloat[FaceInfo.Constants.f_108996_]) {
                afloat[FaceInfo.Constants.f_108996_] = f;
            }
            if (f1 < afloat[FaceInfo.Constants.f_108995_]) {
                afloat[FaceInfo.Constants.f_108995_] = f1;
            }
            if (f2 < afloat[FaceInfo.Constants.f_108994_]) {
                afloat[FaceInfo.Constants.f_108994_] = f2;
            }
            if (f > afloat[FaceInfo.Constants.f_108993_]) {
                afloat[FaceInfo.Constants.f_108993_] = f;
            }
            if (f1 > afloat[FaceInfo.Constants.f_108992_]) {
                afloat[FaceInfo.Constants.f_108992_] = f1;
            }
            if (f2 > afloat[FaceInfo.Constants.f_108991_]) {
                afloat[FaceInfo.Constants.f_108991_] = f2;
            }
        }
        FaceInfo faceinfo = FaceInfo.m_108984_(pDirection);
        for (int i1 = 0; i1 < 4; i1++) {
            int j1 = 8 * i1;
            FaceInfo.VertexInfo faceinfo$vertexinfo = faceinfo.m_108982_(i1);
            float f8 = afloat[faceinfo$vertexinfo.f_108998_];
            float f3 = afloat[faceinfo$vertexinfo.f_108999_];
            float f4 = afloat[faceinfo$vertexinfo.f_109000_];
            pVertices[j1] = Float.floatToRawIntBits(f8);
            pVertices[j1 + 1] = Float.floatToRawIntBits(f3);
            pVertices[j1 + 2] = Float.floatToRawIntBits(f4);
            for (int k = 0; k < 4; k++) {
                int l = 8 * k;
                float f5 = Float.intBitsToFloat(aint[l]);
                float f6 = Float.intBitsToFloat(aint[l + 1]);
                float f7 = Float.intBitsToFloat(aint[l + 2]);
                if (Mth.m_14033_(f8, f5) && Mth.m_14033_(f3, f6) && Mth.m_14033_(f4, f7)) {
                    pVertices[j1 + 4] = aint[l + 4];
                    pVertices[j1 + 4 + 1] = aint[l + 4 + 1];
                }
            }
        }
    }

    public static void fillNormal(int[] faceData, Direction facing) {
        Vector3f v1 = getVertexPos(faceData, 3);
        Vector3f t1 = getVertexPos(faceData, 1);
        Vector3f v2 = getVertexPos(faceData, 2);
        Vector3f t2 = getVertexPos(faceData, 0);
        v1.m_122267_(t1);
        v2.m_122267_(t2);
        v2.m_122279_(v1);
        v2.m_122278_();
        int x = ((byte) Math.round(v2.m_122239_() * 127.0f)) & 255;
        int y = ((byte) Math.round(v2.m_122260_() * 127.0f)) & 255;
        int z = ((byte) Math.round(v2.m_122269_() * 127.0f)) & 255;
        int normal = x | (y << 8) | (z << 16);
        for (int i = 0; i < 4; i++) {
            faceData[(i * 8) + 7] = normal;
        }
    }

    private static Vector3f getVertexPos(int[] data, int vertex) {
        int idx = vertex * 8;
        float x = Float.intBitsToFloat(data[idx]);
        float y = Float.intBitsToFloat(data[idx + 1]);
        float z = Float.intBitsToFloat(data[idx + 2]);
        return new Vector3f(x, y, z);
    }
}
