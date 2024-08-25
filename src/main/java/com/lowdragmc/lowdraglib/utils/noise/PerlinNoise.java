package com.lowdragmc.lowdraglib.utils.noise;

import com.lowdragmc.lowdraglib.gui.compass.CompassView;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import expr.Expr;
import java.util.Random;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/noise/PerlinNoise.class */
public class PerlinNoise {
    private double seed;
    private long default_size;
    private int[] p;

    public PerlinNoise(double seed) {
        this.seed = seed;
        init();
    }

    public PerlinNoise() {
        this.seed = new Random().nextGaussian() * 255.0d;
        init();
    }

    private void init() {
        this.p = new int[512];
        int[] permutation = {151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, Expr.ATAN, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, ConfigPanel.WIDTH, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, Expr.SIN, 229, 122, 60, 211, 133, 230, 220, Expr.COS, 92, 41, 55, 46, 245, 40, 244, Expr.ASIN, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, Expr.NEG, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, Expr.ACOS, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, Expr.LOG, Expr.ROUND, 79, Expr.TAN, 224, 232, 178, 185, Expr.SQRT, Expr.CEIL, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, Expr.FLOOR, 49, 192, 214, 31, 181, 199, Expr.EXP, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, CompassView.LIST_WIDTH, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};
        this.default_size = 35L;
        for (int i = 0; i < 256; i++) {
            int i2 = permutation[i];
            this.p[i] = i2;
            this.p[256 + i] = i2;
        }
    }

    public void setSeed(double seed) {
        this.seed = seed;
    }

    public double getSeed() {
        return this.seed;
    }

    public double noise(double x, double y, double z, int size) {
        double value = 0.0d;
        double initialSize = size;
        while (size >= 1) {
            value += smoothNoise(x / size, y / size, z / size) * size;
            size = (int) (size / 2.0d);
        }
        return value / initialSize;
    }

    public double noise(double x, double y, double z) {
        double value = 0.0d;
        double size = this.default_size;
        while (size >= 1.0d) {
            value += smoothNoise(x / size, y / size, z / size) * size;
            size /= 2.0d;
        }
        return value / size;
    }

    public double noise(double x, double y) {
        double value = 0.0d;
        double size = this.default_size;
        while (size >= 1.0d) {
            value += smoothNoise(x / size, y / size, 0.0d / size) * size;
            size /= 2.0d;
        }
        return value / size;
    }

    public double noise(double x) {
        double value = 0.0d;
        double size = this.default_size;
        while (size >= 1.0d) {
            value += smoothNoise(x / size, 0.0d / size, 0.0d / size) * size;
            size /= 2.0d;
        }
        return value / size;
    }

    public double smoothNoise(double x, double y, double z) {
        double x2 = x + this.seed;
        double y2 = y + this.seed;
        double x3 = x2 + this.seed;
        int X = ((int) Math.floor(x3)) & 255;
        int Y = ((int) Math.floor(y2)) & 255;
        int Z = ((int) Math.floor(z)) & 255;
        double x4 = x3 - Math.floor(x3);
        double y3 = y2 - Math.floor(y2);
        double z2 = z - Math.floor(z);
        double u = fade(x4);
        double v = fade(y3);
        double w = fade(z2);
        int A = this.p[X] + Y;
        int AA = this.p[A] + Z;
        int AB = this.p[A + 1] + Z;
        int B = this.p[X + 1] + Y;
        int BA = this.p[B] + Z;
        int BB = this.p[B + 1] + Z;
        return lerp(w, lerp(v, lerp(u, grad(this.p[AA], x4, y3, z2), grad(this.p[BA], x4 - 1.0d, y3, z2)), lerp(u, grad(this.p[AB], x4, y3 - 1.0d, z2), grad(this.p[BB], x4 - 1.0d, y3 - 1.0d, z2))), lerp(v, lerp(u, grad(this.p[AA + 1], x4, y3, z2 - 1.0d), grad(this.p[BA + 1], x4 - 1.0d, y3, z2 - 1.0d)), lerp(u, grad(this.p[AB + 1], x4, y3 - 1.0d, z2 - 1.0d), grad(this.p[BB + 1], x4 - 1.0d, y3 - 1.0d, z2 - 1.0d))));
    }

    private double fade(double t) {
        return t * t * t * ((t * ((t * 6.0d) - 15.0d)) + 10.0d);
    }

    private double lerp(double t, double a, double b) {
        return a + (t * (b - a));
    }

    private double grad(int hash, double x, double y, double z) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : (h == 12 || h == 14) ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
