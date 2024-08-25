package com.lowdragmc.lowdraglib.client.utils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import expr.Expr;
import java.util.Stack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/utils/RenderUtils.class */
public class RenderUtils {
    private static final Stack<int[]> scissorFrameStack = new Stack<>();

    @Deprecated
    public static void useScissor(int x, int y, int width, int height, Runnable codeBlock) {
        pushScissorFrame(x, y, width, height);
        try {
            codeBlock.run();
        } finally {
            popScissorFrame();
        }
    }

    public static void useScissor(PoseStack poseStack, int x, int y, int width, int height, Runnable codeBlock) {
        Matrix4f pose = poseStack.m_85850_().m_85861_();
        Vector4f pos = new Vector4f(x, y, 0.0f, 1.0f);
        pos.m_123607_(pose);
        Vector4f size = new Vector4f(x + width, y + height, 0.0f, 1.0f);
        size.m_123607_(pose);
        int x2 = (int) pos.m_123601_();
        int y2 = (int) pos.m_123615_();
        int width2 = (int) (size.m_123601_() - x2);
        int height2 = (int) (size.m_123615_() - y2);
        pushScissorFrame(x2, y2, width2, height2);
        try {
            codeBlock.run();
        } finally {
            popScissorFrame();
        }
    }

    private static int[] peekFirstScissorOrFullScreen() {
        int[] currentTopFrame = scissorFrameStack.isEmpty() ? null : scissorFrameStack.peek();
        if (currentTopFrame == null) {
            Window window = Minecraft.m_91087_().m_91268_();
            return new int[]{0, 0, window.m_85441_(), window.m_85442_()};
        }
        return currentTopFrame;
    }

    private static void pushScissorFrame(int x, int y, int width, int height) {
        int[] parentScissor = peekFirstScissorOrFullScreen();
        int parentX = parentScissor[0];
        int parentY = parentScissor[1];
        int parentWidth = parentScissor[2];
        int parentHeight = parentScissor[3];
        boolean pushedFrame = false;
        if (x <= parentX + parentWidth && y <= parentY + parentHeight) {
            int newX = Math.max(x, parentX);
            int newY = Math.max(y, parentY);
            int newWidth = width - (newX - x);
            int newHeight = height - (newY - y);
            if (newWidth > 0 && newHeight > 0) {
                int maxWidth = parentWidth - (x - parentX);
                int maxHeight = parentHeight - (y - parentY);
                int newWidth2 = Math.min(maxWidth, newWidth);
                int newHeight2 = Math.min(maxHeight, newHeight);
                applyScissor(newX, newY, newWidth2, newHeight2);
                if (scissorFrameStack.isEmpty()) {
                    GL11.glEnable(3089);
                }
                scissorFrameStack.push(new int[]{newX, newY, newWidth2, newHeight2});
                pushedFrame = true;
            }
        }
        if (!pushedFrame) {
            if (scissorFrameStack.isEmpty()) {
                GL11.glEnable(3089);
            }
            scissorFrameStack.push(new int[]{parentX, parentY, parentWidth, parentHeight});
        }
    }

    private static void popScissorFrame() {
        scissorFrameStack.pop();
        int[] parentScissor = peekFirstScissorOrFullScreen();
        int parentX = parentScissor[0];
        int parentY = parentScissor[1];
        int parentWidth = parentScissor[2];
        int parentHeight = parentScissor[3];
        applyScissor(parentX, parentY, parentWidth, parentHeight);
        if (scissorFrameStack.isEmpty()) {
            GL11.glDisable(3089);
        }
    }

    private static void applyScissor(int x, int y, int w, int h) {
        Window window = Minecraft.m_91087_().m_91268_();
        double s = window.m_85449_();
        int translatedY = (window.m_85446_() - y) - h;
        GL11.glScissor((int) (x * s), (int) (translatedY * s), (int) (w * s), (int) (h * s));
    }

    public static void useStencil(Runnable mask, Runnable renderInMask, boolean shouldRenderMask) {
        GL11.glStencilMask(255);
        GL11.glClearStencil(0);
        GL11.glClear(1024);
        GL11.glEnable(2960);
        GL11.glStencilFunc(519, 1, 255);
        GL11.glStencilOp(7680, 7680, 7681);
        if (!shouldRenderMask) {
            GL11.glColorMask(false, false, false, false);
            GL11.glDepthMask(false);
        }
        mask.run();
        if (!shouldRenderMask) {
            GL11.glColorMask(true, true, true, true);
            GL11.glDepthMask(true);
        }
        GL11.glStencilMask(0);
        GL11.glStencilFunc(514, 1, 255);
        GL11.glStencilOp(7680, 7680, 7680);
        renderInMask.run();
        GL11.glDisable(2960);
    }

    public static void renderBlockOverLay(PoseStack poseStack, BlockPos pos, float r, float g, float b, float scale) {
        if (pos == null) {
            return;
        }
        RenderSystem.m_69478_();
        RenderSystem.m_69405_(770, 1);
        poseStack.m_85836_();
        poseStack.m_85837_(pos.m_123341_() + 0.5d, pos.m_123342_() + 0.5d, pos.m_123343_() + 0.5d);
        poseStack.m_85841_(scale, scale, scale);
        Tesselator tessellator = Tesselator.m_85913_();
        RenderSystem.m_69472_();
        BufferBuilder buffer = tessellator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172811_);
        buffer.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85815_);
        renderCubeFace(poseStack, buffer, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, r, g, b, 1.0f);
        tessellator.m_85914_();
        poseStack.m_85849_();
        RenderSystem.m_69493_();
        RenderSystem.m_69405_(770, 771);
        RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void renderCubeFace(PoseStack matrixStack, BufferBuilder buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a) {
        Matrix4f mat = matrixStack.m_85850_().m_85861_();
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, minZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, minY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, maxX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
        buffer.m_85982_(mat, minX, maxY, maxZ).m_85950_(r, g, b, a).m_5752_();
    }

    public static void moveToFace(PoseStack matrixStack, double x, double y, double z, Direction face) {
        matrixStack.m_85837_(x + 0.5d + (face.m_122429_() * 0.5d), y + 0.5d + (face.m_122430_() * 0.5d), z + 0.5d + (face.m_122431_() * 0.5d));
    }

    /* renamed from: com.lowdragmc.lowdraglib.client.utils.RenderUtils$1  reason: invalid class name */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/utils/RenderUtils$1.class */
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
                $SwitchMap$net$minecraft$core$Direction[Direction.EAST.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.NORTH.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.SOUTH.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public static void rotateToFace(PoseStack matrixStack, Direction face, @Nullable Direction spin) {
        int angle = spin == Direction.EAST ? 90 : spin == Direction.SOUTH ? 180 : spin == Direction.WEST ? -90 : 0;
        switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction[face.ordinal()]) {
            case 1:
                matrixStack.m_85841_(1.0f, -1.0f, 1.0f);
                matrixStack.m_85845_(Vector3f.f_122223_.m_122240_(90.0f));
                matrixStack.m_85845_(Vector3f.f_122227_.m_122240_(angle));
                return;
            case 2:
                matrixStack.m_85841_(1.0f, -1.0f, 1.0f);
                matrixStack.m_85845_(Vector3f.f_122223_.m_122240_(-90.0f));
                matrixStack.m_85845_(Vector3f.f_122227_.m_122240_(spin == Direction.EAST ? 90.0f : spin == Direction.NORTH ? 180.0f : spin == Direction.WEST ? -90.0f : 0.0f));
                return;
            case 3:
                matrixStack.m_85841_(-1.0f, -1.0f, -1.0f);
                matrixStack.m_85845_(Vector3f.f_122225_.m_122240_(-90.0f));
                matrixStack.m_85845_(Vector3f.f_122227_.m_122240_(angle));
                return;
            case 4:
                matrixStack.m_85841_(-1.0f, -1.0f, -1.0f);
                matrixStack.m_85845_(Vector3f.f_122225_.m_122240_(90.0f));
                matrixStack.m_85845_(Vector3f.f_122227_.m_122240_(angle));
                return;
            case Expr.ATAN2 /* 5 */:
                matrixStack.m_85841_(-1.0f, -1.0f, -1.0f);
                matrixStack.m_85845_(Vector3f.f_122227_.m_122240_(angle));
                return;
            case Expr.MAX /* 6 */:
                matrixStack.m_85841_(-1.0f, -1.0f, -1.0f);
                matrixStack.m_85845_(Vector3f.f_122225_.m_122240_(180.0f));
                matrixStack.m_85845_(Vector3f.f_122227_.m_122240_(angle));
                return;
            default:
                return;
        }
    }
}
