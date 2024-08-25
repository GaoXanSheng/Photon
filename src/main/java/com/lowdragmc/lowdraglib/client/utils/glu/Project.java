package com.lowdragmc.lowdraglib.client.utils.glu;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/utils/glu/Project.class */
public class Project extends Util {
    private static final float[] IDENTITY_MATRIX = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer finalMatrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer tempMatrix = BufferUtils.createFloatBuffer(16);
    private static final float[] in = new float[4];
    private static final float[] out = new float[4];
    private static final float[] forward = new float[3];
    private static final float[] side = new float[3];
    private static final float[] up = new float[3];

    private static void __gluMakeIdentityf(FloatBuffer m) {
        int oldPos = m.position();
        m.put(IDENTITY_MATRIX);
        m.position(oldPos);
    }

    private static void __gluMultMatrixVecf(FloatBuffer m, float[] in2, float[] out2) {
        for (int i = 0; i < 4; i++) {
            out2[i] = (in2[0] * m.get(m.position() + 0 + i)) + (in2[1] * m.get(m.position() + 4 + i)) + (in2[2] * m.get(m.position() + 8 + i)) + (in2[3] * m.get(m.position() + 12 + i));
        }
    }

    private static boolean __gluInvertMatrixf(FloatBuffer src, FloatBuffer inverse) {
        FloatBuffer temp = tempMatrix;
        for (int i = 0; i < 16; i++) {
            temp.put(i, src.get(i + src.position()));
        }
        __gluMakeIdentityf(inverse);
        for (int i2 = 0; i2 < 4; i2++) {
            int swap = i2;
            for (int j = i2 + 1; j < 4; j++) {
                if (Math.abs(temp.get((j * 4) + i2)) > Math.abs(temp.get((i2 * 4) + i2))) {
                    swap = j;
                }
            }
            if (swap != i2) {
                for (int k = 0; k < 4; k++) {
                    float t = temp.get((i2 * 4) + k);
                    temp.put((i2 * 4) + k, temp.get((swap * 4) + k));
                    temp.put((swap * 4) + k, t);
                    float t2 = inverse.get((i2 * 4) + k);
                    inverse.put((i2 * 4) + k, inverse.get((swap * 4) + k));
                    inverse.put((swap * 4) + k, t2);
                }
            }
            if (temp.get((i2 * 4) + i2) == 0.0f) {
                return false;
            }
            float t3 = temp.get((i2 * 4) + i2);
            for (int k2 = 0; k2 < 4; k2++) {
                temp.put((i2 * 4) + k2, temp.get((i2 * 4) + k2) / t3);
                inverse.put((i2 * 4) + k2, inverse.get((i2 * 4) + k2) / t3);
            }
            for (int j2 = 0; j2 < 4; j2++) {
                if (j2 != i2) {
                    float t4 = temp.get((j2 * 4) + i2);
                    for (int k3 = 0; k3 < 4; k3++) {
                        temp.put((j2 * 4) + k3, temp.get((j2 * 4) + k3) - (temp.get((i2 * 4) + k3) * t4));
                        inverse.put((j2 * 4) + k3, inverse.get((j2 * 4) + k3) - (inverse.get((i2 * 4) + k3) * t4));
                    }
                }
            }
        }
        return true;
    }

    private static void __gluMultMatricesf(FloatBuffer a, FloatBuffer b, FloatBuffer r) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                r.put(r.position() + (i * 4) + j, (a.get(a.position() + (i * 4) + 0) * b.get(b.position() + 0 + j)) + (a.get(a.position() + (i * 4) + 1) * b.get(b.position() + 4 + j)) + (a.get(a.position() + (i * 4) + 2) * b.get(b.position() + 8 + j)) + (a.get(a.position() + (i * 4) + 3) * b.get(b.position() + 12 + j)));
            }
        }
    }

    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        float radians = ((fovy / 2.0f) * 3.1415927f) / 180.0f;
        float deltaZ = zFar - zNear;
        float sine = (float) Math.sin(radians);
        if (deltaZ == 0.0f || sine == 0.0f || aspect == 0.0f) {
            return;
        }
        float cotangent = ((float) Math.cos(radians)) / sine;
        __gluMakeIdentityf(matrix);
        matrix.put(0, cotangent / aspect);
        matrix.put(5, cotangent);
        matrix.put(10, (-(zFar + zNear)) / deltaZ);
        matrix.put(11, -1.0f);
        matrix.put(14, (((-2.0f) * zNear) * zFar) / deltaZ);
        matrix.put(15, 0.0f);
        GL11.glMultMatrixf(matrix);
    }

    public static void gluLookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz) {
        float[] forward2 = forward;
        float[] side2 = side;
        float[] up2 = up;
        forward2[0] = centerx - eyex;
        forward2[1] = centery - eyey;
        forward2[2] = centerz - eyez;
        up2[0] = upx;
        up2[1] = upy;
        up2[2] = upz;
        normalize(forward2);
        cross(forward2, up2, side2);
        normalize(side2);
        cross(side2, forward2, up2);
        __gluMakeIdentityf(matrix);
        matrix.put(0, side2[0]);
        matrix.put(4, side2[1]);
        matrix.put(8, side2[2]);
        matrix.put(1, up2[0]);
        matrix.put(5, up2[1]);
        matrix.put(9, up2[2]);
        matrix.put(2, -forward2[0]);
        matrix.put(6, -forward2[1]);
        matrix.put(10, -forward2[2]);
        GL11.glMultMatrixf(matrix);
        GL11.glTranslatef(-eyex, -eyey, -eyez);
    }

    public static void gluLookAt(PoseStack poseStack, float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz) {
        float[] forward2 = forward;
        float[] side2 = side;
        float[] up2 = up;
        forward2[0] = centerx - eyex;
        forward2[1] = centery - eyey;
        forward2[2] = centerz - eyez;
        up2[0] = upx;
        up2[1] = upy;
        up2[2] = upz;
        normalize(forward2);
        cross(forward2, up2, side2);
        normalize(side2);
        cross(side2, forward2, up2);
        __gluMakeIdentityf(matrix);
        matrix.put(0, side2[0]);
        matrix.put(4, side2[1]);
        matrix.put(8, side2[2]);
        matrix.put(1, up2[0]);
        matrix.put(5, up2[1]);
        matrix.put(9, up2[2]);
        matrix.put(2, -forward2[0]);
        matrix.put(6, -forward2[1]);
        matrix.put(10, -forward2[2]);
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.m_162212_(matrix);
        poseStack.m_166854_(matrix4f);
        poseStack.m_85837_(-eyex, -eyey, -eyez);
    }

    public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        float[] in2 = in;
        float[] out2 = out;
        in2[0] = objx;
        in2[1] = objy;
        in2[2] = objz;
        in2[3] = 1.0f;
        __gluMultMatrixVecf(modelMatrix, in2, out2);
        __gluMultMatrixVecf(projMatrix, out2, in2);
        if (in2[3] == 0.0d) {
            return false;
        }
        in2[3] = (1.0f / in2[3]) * 0.5f;
        in2[0] = (in2[0] * in2[3]) + 0.5f;
        in2[1] = (in2[1] * in2[3]) + 0.5f;
        in2[2] = (in2[2] * in2[3]) + 0.5f;
        win_pos.put(0, (in2[0] * viewport.get(viewport.position() + 2)) + viewport.get(viewport.position() + 0));
        win_pos.put(1, (in2[1] * viewport.get(viewport.position() + 3)) + viewport.get(viewport.position() + 1));
        win_pos.put(2, in2[2]);
        return true;
    }

    public static boolean gluUnProject(float winx, float winy, float winz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer obj_pos) {
        float[] in2 = in;
        float[] out2 = out;
        __gluMultMatricesf(modelMatrix, projMatrix, finalMatrix);
        if (!__gluInvertMatrixf(finalMatrix, finalMatrix)) {
            return false;
        }
        in2[0] = winx;
        in2[1] = winy;
        in2[2] = winz;
        in2[3] = 1.0f;
        in2[0] = (in2[0] - viewport.get(viewport.position() + 0)) / viewport.get(viewport.position() + 2);
        in2[1] = (in2[1] - viewport.get(viewport.position() + 1)) / viewport.get(viewport.position() + 3);
        in2[0] = (in2[0] * 2.0f) - 1.0f;
        in2[1] = (in2[1] * 2.0f) - 1.0f;
        in2[2] = (in2[2] * 2.0f) - 1.0f;
        __gluMultMatrixVecf(finalMatrix, in2, out2);
        if (out2[3] == 0.0d) {
            return false;
        }
        out2[3] = 1.0f / out2[3];
        obj_pos.put(obj_pos.position() + 0, out2[0] * out2[3]);
        obj_pos.put(obj_pos.position() + 1, out2[1] * out2[3]);
        obj_pos.put(obj_pos.position() + 2, out2[2] * out2[3]);
        return true;
    }

    public static void gluPickMatrix(float x, float y, float deltaX, float deltaY, IntBuffer viewport) {
        if (deltaX <= 0.0f || deltaY <= 0.0f) {
            return;
        }
        GL11.glTranslatef((viewport.get(viewport.position() + 2) - (2.0f * (x - viewport.get(viewport.position() + 0)))) / deltaX, (viewport.get(viewport.position() + 3) - (2.0f * (y - viewport.get(viewport.position() + 1)))) / deltaY, 0.0f);
        GL11.glScalef(viewport.get(viewport.position() + 2) / deltaX, viewport.get(viewport.position() + 3) / deltaY, 1.0f);
    }
}
