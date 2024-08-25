package com.lowdragmc.lowdraglib.client.scene;

import com.lowdragmc.lowdraglib.LDLib;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/scene/FBOWorldSceneRenderer.class */
public class FBOWorldSceneRenderer extends WorldSceneRenderer {
    private int resolutionWidth;
    private int resolutionHeight;
    private RenderTarget fbo;

    public FBOWorldSceneRenderer(Level world, int resolutionWidth, int resolutionHeight) {
        super(world);
        this.resolutionWidth = 1080;
        this.resolutionHeight = 1080;
        setFBOSize(resolutionWidth, resolutionHeight);
    }

    public FBOWorldSceneRenderer(Level world, RenderTarget fbo) {
        super(world);
        this.resolutionWidth = 1080;
        this.resolutionHeight = 1080;
        this.fbo = fbo;
    }

    public int getResolutionWidth() {
        return this.resolutionWidth;
    }

    public int getResolutionHeight() {
        return this.resolutionHeight;
    }

    public void setFBOSize(int resolutionWidth, int resolutionHeight) {
        this.resolutionWidth = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        releaseFBO();
        try {
            this.fbo = new MainTarget(resolutionWidth, resolutionHeight);
        } catch (Exception e) {
            LDLib.LOGGER.error("set FBO SIZE failed", e);
        }
    }

    public BlockHitResult screenPos2BlockPosFace(int mouseX, int mouseY) {
        int lastID = bindFBO();
        BlockHitResult looking = super.screenPos2BlockPosFace(mouseX, mouseY, 0, 0, this.resolutionWidth, this.resolutionHeight);
        unbindFBO(lastID);
        return looking;
    }

    public Vector3f blockPos2ScreenPos(BlockPos pos, boolean depth) {
        int lastID = bindFBO();
        Vector3f winPos = super.blockPos2ScreenPos(pos, depth, 0, 0, this.resolutionWidth, this.resolutionHeight);
        unbindFBO(lastID);
        return winPos;
    }

    public void render(PoseStack poseStack, float x, float y, float width, float height, float mouseX, float mouseY) {
        int lastID = bindFBO();
        super.render(new PoseStack(), 0.0f, 0.0f, this.resolutionWidth, this.resolutionHeight, (int) ((this.resolutionWidth * (mouseX - x)) / width), (int) (this.resolutionHeight * (1.0f - ((mouseY - y) / height))));
        unbindFBO(lastID);
        Tesselator tessellator = Tesselator.m_85913_();
        BufferBuilder bufferbuilder = tessellator.m_85915_();
        RenderSystem.m_157427_(GameRenderer::m_172817_);
        RenderSystem.m_157453_(0, this.fbo.m_83975_());
        bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85817_);
        Matrix4f pose = poseStack.m_85850_().m_85861_();
        bufferbuilder.m_85982_(pose, x + width, y + height, 0.0f).m_7421_(1.0f, 0.0f).m_5752_();
        bufferbuilder.m_85982_(pose, x + width, y, 0.0f).m_7421_(1.0f, 1.0f).m_5752_();
        bufferbuilder.m_85982_(pose, x, y, 0.0f).m_7421_(0.0f, 1.0f).m_5752_();
        bufferbuilder.m_85982_(pose, x, y + height, 0.0f).m_7421_(0.0f, 0.0f).m_5752_();
        tessellator.m_85914_();
    }

    @Override // com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer
    public void render(PoseStack poseStack, float x, float y, float width, float height, int mouseX, int mouseY) {
        render(poseStack, x, y, width, height, mouseX, mouseY);
    }

    private int bindFBO() {
        int lastID = GL11.glGetInteger(36006);
        this.fbo.m_83931_(0.0f, 0.0f, 0.0f, 0.0f);
        this.fbo.m_83954_(Minecraft.f_91002_);
        this.fbo.m_83947_(true);
        return lastID;
    }

    private void unbindFBO(int lastID) {
        this.fbo.m_83963_();
        GlStateManager.m_84486_(36160, lastID);
    }

    public void releaseFBO() {
        if (this.fbo != null) {
            this.fbo.m_83930_();
        }
        this.fbo = null;
    }
}
