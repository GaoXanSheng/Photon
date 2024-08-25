package com.lowdragmc.lowdraglib.client.shader;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.shader.management.Shader;
import com.lowdragmc.lowdraglib.gui.texture.ShaderTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/Shaders.class */
public class Shaders {
    public static Shader IMAGE_F;
    public static Shader IMAGE_V;
    public static Shader GUI_IMAGE_V;
    public static Shader SCREEN_V;
    public static Shader ROUND_F;
    public static Shader PANEL_BG_F;
    public static Shader ROUND_BOX_F;
    public static Shader PROGRESS_ROUND_BOX_F;
    public static Shader FRAME_ROUND_BOX_F;
    public static Shader ROUND_LINE_F;
    private static ShaderInstance particleShader;
    private static ShaderInstance blitShader;
    private static ShaderInstance hsbShader;
    private static ShaderInstance compassLineShader;
    public static Map<ResourceLocation, Shader> CACHE = new HashMap();
    private static final VertexFormatElement HSB_Alpha = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.COLOR, 4);
    public static final VertexFormat HSB_VERTEX_FORMAT = new VertexFormat(ImmutableMap.builder().put("Position", DefaultVertexFormat.f_85804_).put("HSB_ALPHA", HSB_Alpha).build());

    public static void init() {
        IMAGE_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "image"));
        IMAGE_V = load(Shader.ShaderType.VERTEX, new ResourceLocation("ldlib", "image"));
        GUI_IMAGE_V = load(Shader.ShaderType.VERTEX, new ResourceLocation("ldlib", "gui_image"));
        SCREEN_V = load(Shader.ShaderType.VERTEX, new ResourceLocation("ldlib", "screen"));
        ROUND_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "round"));
        PANEL_BG_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "panel_bg"));
        ROUND_BOX_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "round_box"));
        PROGRESS_ROUND_BOX_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "progress_round_box"));
        FRAME_ROUND_BOX_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "frame_round_box"));
        ROUND_LINE_F = load(Shader.ShaderType.FRAGMENT, new ResourceLocation("ldlib", "round_line"));
    }

    public static void reload() {
        for (Shader shader : CACHE.values()) {
            if (shader != null) {
                shader.deleteShader();
            }
        }
        CACHE.clear();
        init();
        DrawerHelper.init();
        ShaderTexture.clearCache();
    }

    public static Shader load(Shader.ShaderType shaderType, ResourceLocation resourceLocation) {
        return CACHE.computeIfAbsent(new ResourceLocation(resourceLocation.m_135827_(), "shaders/" + resourceLocation.m_135815_() + shaderType.shaderExtension), key -> {
            try {
                Shader shader = Shader.loadShader(shaderType, key);
                LDLib.LOGGER.debug("load shader {} resource {} success", shaderType, resourceLocation);
                return shader;
            } catch (IOException e) {
                LDLib.LOGGER.error("load shader {} resource {} failed", shaderType, resourceLocation);
                LDLib.LOGGER.error("caused by ", e);
                return IMAGE_F;
            }
        });
    }

    public static ShaderInstance getParticleShader() {
        return particleShader;
    }

    public static ShaderInstance getBlitShader() {
        return blitShader;
    }

    public static ShaderInstance getHsbShader() {
        return hsbShader;
    }

    public static ShaderInstance getCompassLineShader() {
        return compassLineShader;
    }

    public static List<Pair<ShaderInstance, Consumer<ShaderInstance>>> registerShaders(ResourceManager resourceManager) {
        try {
            return List.of(Pair.of(new ShaderInstance(resourceManager, new ResourceLocation("ldlib", "particle").toString(), DefaultVertexFormat.f_85813_), shaderInstance -> {
                particleShader = shaderInstance;
            }), Pair.of(new ShaderInstance(resourceManager, new ResourceLocation("ldlib", "fast_blit").toString(), DefaultVertexFormat.f_85814_), shaderInstance2 -> {
                blitShader = shaderInstance2;
            }), Pair.of(new ShaderInstance(resourceManager, new ResourceLocation("ldlib", "hsb_block").toString(), HSB_VERTEX_FORMAT), shaderInstance3 -> {
                hsbShader = shaderInstance3;
            }), Pair.of(new ShaderInstance(resourceManager, new ResourceLocation("ldlib", "compass_line").toString(), DefaultVertexFormat.f_85819_), shaderInstance4 -> {
                compassLineShader = shaderInstance4;
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
