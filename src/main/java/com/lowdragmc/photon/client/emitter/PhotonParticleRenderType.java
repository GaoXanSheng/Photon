package com.lowdragmc.photon.client.emitter;

import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.utils.PositionedRect;
import com.lowdragmc.photon.Photon;
import com.lowdragmc.photon.client.emitter.data.RendererSetting;
import com.lowdragmc.photon.client.postprocessing.BloomEffect;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;

/**
 * @author KilaBash
 * @date 2023/6/5
 * @implNote IPhotonParticleRenderType
 */
@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public abstract class PhotonParticleRenderType implements ParticleRenderType {

    @Getter
    @Nullable
    private static Frustum FRUSTUM;

    @Getter
    private static RendererSetting.Layer LAYER = RendererSetting.Layer.Translucent;

    public static boolean bloomMark = false;

    public static void renderBloom() {
        if (LAYER == RendererSetting.Layer.Translucent && bloomMark && !Photon.isUsingShaderPack()) {
            // setup view port
            var lastViewport = new PositionedRect(GlStateManager.Viewport.x(), GlStateManager.Viewport.y(), GlStateManager.Viewport.width(), GlStateManager.Viewport.height());
            var input = BloomEffect.getInput();
            var output = BloomEffect.getOutput();
            var background = Minecraft.getInstance().getMainRenderTarget();
            if (lastViewport.position.x != 0 ||
                    lastViewport.position.y != 0 ||
                    lastViewport.size.width != background.width ||
                    lastViewport.size.height != background.height){
                RenderSystem.viewport(0, 0, background.width, background.height);
            }

            // render bloom effect
            BloomEffect.renderBloom(background.width, background.height,
                    background.getColorTextureId(),
                    input.getColorTextureId(),
                    output);

            // clean input
            input.bindWrite(false);
            GlStateManager._clearColor(0.0f, 0.0f, 0.0f, 0.0f);
            int i = GL11.GL_COLOR_BUFFER_BIT;
            GlStateManager._clear(i, Minecraft.ON_OSX);

            // draw effect back to main target
            GlStateManager._colorMask(true, true, true, true);
            GlStateManager._disableDepthTest();
            GlStateManager._depthMask(false);

            background.bindWrite(false);

            Shaders.getBlitShader().setSampler("DiffuseSampler", output.getColorTextureId());

            Shaders.getBlitShader().apply();
            GlStateManager._enableBlend();
            RenderSystem.defaultBlendFunc();

            var tesselator = Tesselator.getInstance();
            var bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            bufferbuilder.vertex(-1, 1, 0).endVertex();
            bufferbuilder.vertex(-1, -1, 0).endVertex();
            bufferbuilder.vertex(1, -1, 0).endVertex();
            bufferbuilder.vertex(1, 1, 0).endVertex();
            BufferUploader.end(bufferbuilder);
            Shaders.getBlitShader().clear();

            GlStateManager._depthMask(true);
            GlStateManager._colorMask(true, true, true, true);
            GlStateManager._enableDepthTest();

            // restore view port
            if (lastViewport.position.x != 0 ||
                    lastViewport.position.y != 0 ||
                    lastViewport.size.width != background.width ||
                    lastViewport.size.height != background.height){
                RenderSystem.viewport(lastViewport.position.x, lastViewport.position.y, lastViewport.size.width, lastViewport.size.height);
            }
            bloomMark = false;
        }
    }

    public static void prepareForParticleRendering(@Nullable Frustum cullingFrustum) {
        FRUSTUM = cullingFrustum;
        LAYER = RendererSetting.Layer.Opaque;
    }

    public static void finishRender() {
        renderBloom();
        if (LAYER == RendererSetting.Layer.Opaque) {
            LAYER = RendererSetting.Layer.Translucent;
        }
    }

    public void beginBloom() {
        if (!Photon.isUsingShaderPack()) {
            var input = BloomEffect.getInput();
            input.bindWrite(false);
            bloomMark = true;
        }
    }

    public void endBloom() {
        if (!Photon.isUsingShaderPack()) {
            var background = Minecraft.getInstance().getMainRenderTarget();
            background.bindWrite(false);
        }
    }

    public boolean isParallel() {
        return false;
    }

    @Override
    @Deprecated
    public final void begin(BufferBuilder builder, TextureManager textureManager) {
        prepareStatus();
        begin(builder);
    }

    @Override
    @Deprecated
    public final void end(Tesselator tesselator) {
        end(tesselator.getBuilder());
        releaseStatus();
    }

    /**
     * setup opengl environment, setup shaders, uniforms.
     */
    public void prepareStatus() {

    }

    /**
     * setup buffer builder, which may be called in async.
     */
    public void begin(BufferBuilder builder) {

    }

    /**
     * upload the buffer builder. In the render thread
     */
    public void end(BufferBuilder builder) {
        BufferUploader.end(builder);
    }

    /**
     * restore opengl environment.
     */
    public void releaseStatus() {

    }

    /**
     * check is specific layer
     */
    public static boolean checkLayer(RendererSetting.Layer layer) {
        return LAYER == layer;
    }

    /**
     * do cull checking
     */
    public static boolean checkFrustum(AABB aabb) {
        if (FRUSTUM == null) return true;
        return FRUSTUM.isVisible(aabb);
    }

    public static Comparator<ParticleRenderType> makeParticleRenderTypeComparator(List<ParticleRenderType> renderOrder) {
        Comparator<ParticleRenderType> vanillaComparator = Comparator.comparingInt(renderOrder::indexOf);
        return (typeOne, typeTwo) ->
        {
            boolean vanillaOne = renderOrder.contains(typeOne);
            boolean vanillaTwo = renderOrder.contains(typeTwo);

            if (vanillaOne && vanillaTwo)
            {
                return vanillaComparator.compare(typeOne, typeTwo);
            }
            if (!vanillaOne && !vanillaTwo)
            {
                return Integer.compare(System.identityHashCode(typeOne), System.identityHashCode(typeTwo));
            }
            return vanillaOne ? -1 : 1;
        };
    }

}
