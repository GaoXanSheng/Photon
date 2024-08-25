package com.lowdragmc.lowdraglib.client.utils;

import com.lowdragmc.lowdraglib.utils.Vector3;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import java.util.List;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/utils/RenderBufferUtils.class */
public class RenderBufferUtils {
    public static void renderCubeFrame(PoseStack matrixStack, BufferBuilder buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a) {
        Matrix4f mat = matrixStack.m_85850_().m_85861_();
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5601_(1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5601_(0.0f, 1.0f, 0.0f).m_5752_();
    }

    public static void renderCubeFace(PoseStack matrixStack, BufferBuilder buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float a, boolean shade) {
        Matrix4f mat = matrixStack.m_85850_().m_85861_();
        float r = red;
        float g = green;
        float b = blue;
        if (shade) {
            r = (float) (r * 0.6d);
            g = (float) (g * 0.6d);
            b = (float) (b * 0.6d);
        }
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        if (shade) {
            r = red * 0.5f;
            g = green * 0.5f;
            b = blue * 0.5f;
        }
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        if (shade) {
            r = red;
            g = green;
            b = blue;
        }
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        if (shade) {
            r = red * 0.8f;
            g = green * 0.8f;
            b = blue * 0.8f;
        }
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
    }

    public static void renderCubeFace(PoseStack matrixStack, VertexConsumer buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int color, int combinedLight, TextureAtlasSprite textureSprite) {
        Matrix4f mat = matrixStack.m_85850_().m_85861_();
        Matrix3f normal = matrixStack.m_85850_().m_85864_();
        float uMin = textureSprite.m_118409_();
        float uMax = textureSprite.m_118410_();
        float vMin = textureSprite.m_118411_();
        float vMax = textureSprite.m_118412_();
        buffer.m_85982_(mat, minX, minY, minZ).m_193479_(color).m_7421_(uMin, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, -1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_193479_(color).m_7421_(uMax, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, -1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_193479_(color).m_7421_(uMax, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, -1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_193479_(color).m_7421_(uMin, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, -1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_193479_(color).m_7421_(uMin, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_193479_(color).m_7421_(uMax, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_193479_(color).m_7421_(uMax, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_193479_(color).m_7421_(uMin, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 1.0f, 0.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, minZ).m_193479_(color).m_7421_(uMin, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, -1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_193479_(color).m_7421_(uMax, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, -1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_193479_(color).m_7421_(uMax, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, -1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_193479_(color).m_7421_(uMin, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, -1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_193479_(color).m_7421_(uMin, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_193479_(color).m_7421_(uMax, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_193479_(color).m_7421_(uMax, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_193479_(color).m_7421_(uMin, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 1.0f, 0.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, minZ).m_193479_(color).m_7421_(uMin, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, -1.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_193479_(color).m_7421_(uMax, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, -1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_193479_(color).m_7421_(uMax, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, -1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_193479_(color).m_7421_(uMin, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, -1.0f).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_193479_(color).m_7421_(uMin, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_193479_(color).m_7421_(uMax, vMax).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_193479_(color).m_7421_(uMax, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, 1.0f).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_193479_(color).m_7421_(uMin, vMin).m_86008_(OverlayTexture.f_118083_).m_85969_(combinedLight).m_85977_(normal, 0.0f, 0.0f, 1.0f).m_5752_();
    }

    public static void drawColorLines(PoseStack poseStack, VertexConsumer builder, List<Vec2> points, int colorStart, int colorEnd, float width) {
        if (points.size() < 2) {
            return;
        }
        Matrix4f mat = poseStack.m_85850_().m_85861_();
        Vec2 lastPoint = points.get(0);
        Vec2 point = points.get(1);
        Vector3 vec = null;
        int sa = (colorStart >> 24) & 255;
        int sr = (colorStart >> 16) & 255;
        int sg = (colorStart >> 8) & 255;
        int sb = colorStart & 255;
        int ea = ((colorEnd >> 24) & 255) - sa;
        int er = ((colorEnd >> 16) & 255) - sr;
        int eg = ((colorEnd >> 8) & 255) - sg;
        int eb = (colorEnd & 255) - sb;
        for (int i = 1; i < points.size(); i++) {
            float s = (i - 1.0f) / points.size();
            float e = (i * 1.0f) / points.size();
            point = points.get(i);
            vec = new Vector3(point.f_82470_ - lastPoint.f_82470_, point.f_82471_ - lastPoint.f_82471_, 0.0d).rotate(Math.toRadians(90.0d), Vector3.Z).normalize().multiply(-width);
            builder.m_85982_(mat, lastPoint.f_82470_ + ((float) vec.x), lastPoint.f_82471_ + ((float) vec.y), 0.0f).m_85950_((sr + (er * s)) / 255.0f, (sg + (eg * s)) / 255.0f, (sb + (eb * s)) / 255.0f, (sa + (ea * s)) / 255.0f).m_5752_();
            vec.multiply(-1.0d);
            builder.m_85982_(mat, lastPoint.f_82470_ + ((float) vec.x), lastPoint.f_82471_ + ((float) vec.y), 0.0f).m_85950_((sr + (er * e)) / 255.0f, (sg + (eg * e)) / 255.0f, (sb + (eb * e)) / 255.0f, (sa + (ea * e)) / 255.0f).m_5752_();
            lastPoint = point;
        }
        vec.multiply(-1.0d);
        builder.m_85982_(mat, point.f_82470_ + ((float) vec.x), point.f_82471_ + ((float) vec.y), 0.0f).m_6122_(sr + er, sg + eg, sb + eb, sa + ea).m_5752_();
        vec.multiply(-1.0d);
        builder.m_85982_(mat, point.f_82470_ + ((float) vec.x), point.f_82471_ + ((float) vec.y), 0.0f).m_6122_(sr + er, sg + eg, sb + eb, sa + ea).m_5752_();
    }

    public static void drawColorTexLines(PoseStack poseStack, VertexConsumer builder, List<Vec2> points, int colorStart, int colorEnd, float width) {
        if (points.size() < 2) {
            return;
        }
        Matrix4f mat = poseStack.m_85850_().m_85861_();
        Vec2 lastPoint = points.get(0);
        Vec2 point = points.get(1);
        Vector3 vec = null;
        int sa = (colorStart >> 24) & 255;
        int sr = (colorStart >> 16) & 255;
        int sg = (colorStart >> 8) & 255;
        int sb = colorStart & 255;
        int ea = ((colorEnd >> 24) & 255) - sa;
        int er = ((colorEnd >> 16) & 255) - sr;
        int eg = ((colorEnd >> 8) & 255) - sg;
        int eb = (colorEnd & 255) - sb;
        for (int i = 1; i < points.size(); i++) {
            float s = (i - 1.0f) / points.size();
            float e = (i * 1.0f) / points.size();
            point = points.get(i);
            float u = (i - 1.0f) / points.size();
            vec = new Vector3(point.f_82470_ - lastPoint.f_82470_, point.f_82471_ - lastPoint.f_82471_, 0.0d).rotate(Math.toRadians(90.0d), Vector3.Z).normalize().multiply(-width);
            builder.m_85982_(mat, lastPoint.f_82470_ + ((float) vec.x), lastPoint.f_82471_ + ((float) vec.y), 0.0f).m_7421_(u, 0.0f).m_85950_((sr + (er * s)) / 255.0f, (sg + (eg * s)) / 255.0f, (sb + (eb * s)) / 255.0f, (sa + (ea * s)) / 255.0f).m_5752_();
            vec.multiply(-1.0d);
            builder.m_85982_(mat, lastPoint.f_82470_ + ((float) vec.x), lastPoint.f_82471_ + ((float) vec.y), 0.0f).m_7421_(u, 1.0f).m_85950_((sr + (er * e)) / 255.0f, (sg + (eg * e)) / 255.0f, (sb + (eb * e)) / 255.0f, (sa + (ea * e)) / 255.0f).m_5752_();
            lastPoint = point;
        }
        vec.multiply(-1.0d);
        builder.m_85982_(mat, (float) (point.f_82470_ + vec.x), (float) (point.f_82471_ + vec.y), 0.0f).m_7421_(1.0f, 0.0f).m_6122_(sr + er, sg + eg, sb + eb, sa + ea).m_5752_();
        vec.multiply(-1.0d);
        builder.m_85982_(mat, point.f_82470_ + ((float) vec.x), point.f_82471_ + ((float) vec.y), 0.0f).m_7421_(1.0f, 1.0f).m_6122_(sr + er, sg + eg, sb + eb, sa + ea).m_5752_();
    }
}
