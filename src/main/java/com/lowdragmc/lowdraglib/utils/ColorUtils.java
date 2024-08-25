package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.LDLib;
import expr.Expr;
import net.minecraft.util.Mth;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ColorUtils.class */
public class ColorUtils {
    public static int randomColor(int minR, int maxR, int minG, int maxG, int minB, int maxB) {
        return (-16777216) | ((minR + LDLib.random.nextInt((maxR + 1) - minR)) << 16) | ((minG + LDLib.random.nextInt((maxG + 1) - minG)) << 8) | (minB + LDLib.random.nextInt((maxB + 1) - minB));
    }

    public static int randomColor(int minA, int maxA, int minR, int maxR, int minG, int maxG, int minB, int maxB) {
        return ((minR + LDLib.random.nextInt((maxA + 1) - minA)) << 24) | ((minR + LDLib.random.nextInt((maxR + 1) - minR)) << 16) | ((minG + LDLib.random.nextInt((maxG + 1) - minG)) << 8) | (minB + LDLib.random.nextInt((maxB + 1) - minB));
    }

    public static int randomColor(int colorA, int colorB) {
        return randomColor(Math.min(alphaI(colorA), alphaI(colorB)), Math.max(alphaI(colorA), alphaI(colorB)), Math.min(redI(colorA), redI(colorB)), Math.max(redI(colorA), redI(colorB)), Math.min(greenI(colorA), greenI(colorB)), Math.max(greenI(colorA), greenI(colorB)), Math.min(blueI(colorA), blueI(colorB)), Math.max(blueI(colorA), blueI(colorB)));
    }

    public static int randomColor() {
        return randomColor(0, 255, 0, 255, 0, 255);
    }

    public static int averageColor(int... colors) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int color : colors) {
            r += (color >> 16) & 255;
            g += (color >> 8) & 255;
            b += color & 255;
        }
        return ((r / colors.length) << 16) | ((g / colors.length) << 8) | (b / colors.length);
    }

    public static double softLightBlend(double bg, double fg, double alphaBg, double alphaFg) {
        double newColor;
        if (fg <= 0.5d) {
            newColor = (2.0d * bg * fg) + (bg * bg * (1.0d - (2.0d * fg)));
        } else {
            newColor = (Math.sqrt(bg) * ((2.0d * fg) - 1.0d)) + (2.0d * bg * (1.0d - fg));
        }
        return (alphaFg * newColor) + (alphaBg * (1.0d - alphaFg) * newColor);
    }

    public static float alpha(int color) {
        return ((color >> 24) & 255) / 255.0f;
    }

    public static float red(int color) {
        return ((color >> 16) & 255) / 255.0f;
    }

    public static float green(int color) {
        return ((color >> 8) & 255) / 255.0f;
    }

    public static float blue(int color) {
        return (color & 255) / 255.0f;
    }

    public static int alphaI(int color) {
        return (color >> 24) & 255;
    }

    public static int redI(int color) {
        return (color >> 16) & 255;
    }

    public static int greenI(int color) {
        return (color >> 8) & 255;
    }

    public static int blueI(int color) {
        return color & 255;
    }

    public static int color(int alpha, int red, int green, int blue) {
        if (alpha > 255) {
            alpha = 255;
        }
        if (red > 255) {
            red = 255;
        }
        if (green > 255) {
            green = 255;
        }
        if (blue > 255) {
            blue = 255;
        }
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int color(float alpha, float red, float green, float blue) {
        return color((int) (alpha * 255.0f), (int) (red * 255.0f), (int) (green * 255.0f), (int) (blue * 255.0f));
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0f) {
            int i = (int) ((brightness * 255.0f) + 0.5f);
            b = i;
            g = i;
            r = i;
        } else {
            float h = (hue - ((float) Math.floor(hue))) * 6.0f;
            float f = h - ((float) Math.floor(h));
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - (saturation * f));
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) ((brightness * 255.0f) + 0.5f);
                    g = (int) ((t * 255.0f) + 0.5f);
                    b = (int) ((p * 255.0f) + 0.5f);
                    break;
                case 1:
                    r = (int) ((q * 255.0f) + 0.5f);
                    g = (int) ((brightness * 255.0f) + 0.5f);
                    b = (int) ((p * 255.0f) + 0.5f);
                    break;
                case 2:
                    r = (int) ((p * 255.0f) + 0.5f);
                    g = (int) ((brightness * 255.0f) + 0.5f);
                    b = (int) ((t * 255.0f) + 0.5f);
                    break;
                case 3:
                    r = (int) ((p * 255.0f) + 0.5f);
                    g = (int) ((q * 255.0f) + 0.5f);
                    b = (int) ((brightness * 255.0f) + 0.5f);
                    break;
                case 4:
                    r = (int) ((t * 255.0f) + 0.5f);
                    g = (int) ((p * 255.0f) + 0.5f);
                    b = (int) ((brightness * 255.0f) + 0.5f);
                    break;
                case Expr.ATAN2 /* 5 */:
                    r = (int) ((brightness * 255.0f) + 0.5f);
                    g = (int) ((p * 255.0f) + 0.5f);
                    b = (int) ((q * 255.0f) + 0.5f);
                    break;
            }
        }
        return (-16777216) | (r << 16) | (g << 8) | b;
    }

    public static float[] RGBtoHSB(int color) {
        float saturation;
        float hue;
        float hue2;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int cmax = Math.max(r, g);
        if (b > cmax) {
            cmax = b;
        }
        int cmin = Math.min(r, g);
        if (b < cmin) {
            cmin = b;
        }
        float brightness = cmax / 255.0f;
        if (cmax != 0) {
            saturation = (cmax - cmin) / cmax;
        } else {
            saturation = 0.0f;
        }
        if (saturation == 0.0f) {
            hue2 = 0.0f;
        } else {
            float redc = (cmax - r) / (cmax - cmin);
            float greenc = (cmax - g) / (cmax - cmin);
            float bluec = (cmax - b) / (cmax - cmin);
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = (2.0f + redc) - bluec;
            } else {
                hue = (4.0f + greenc) - redc;
            }
            hue2 = hue / 6.0f;
            if (hue2 < 0.0f) {
                hue2 += 1.0f;
            }
        }
        return new float[]{hue2, saturation, brightness};
    }

    public static int blendColor(int color0, int color1, float lerp) {
        return color(Mth.m_14179_(lerp, alpha(color0), alpha(color1)), Mth.m_14179_(lerp, red(color0), red(color1)), Mth.m_14179_(lerp, green(color0), green(color1)), Mth.m_14179_(lerp, blue(color0), blue(color1)));
    }
}
