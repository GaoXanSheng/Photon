package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import java.util.HashMap;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/IGuiTexture.class */
public interface IGuiTexture extends IConfigurable {
    public static final IGuiTexture EMPTY = new IGuiTexture() { // from class: com.lowdragmc.lowdraglib.gui.texture.IGuiTexture.1
        @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
        @OnlyIn(Dist.CLIENT)
        public void draw(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        }
    };
    public static final IGuiTexture MISSING_TEXTURE = new IGuiTexture() { // from class: com.lowdragmc.lowdraglib.gui.texture.IGuiTexture.2
        @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
        @OnlyIn(Dist.CLIENT)
        public void draw(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
            Tesselator tessellator = Tesselator.m_85913_();
            BufferBuilder bufferbuilder = tessellator.m_85915_();
            RenderSystem.m_157427_(GameRenderer::m_172820_);
            RenderSystem.m_157456_(0, TextureManager.f_118466_);
            Matrix4f matrix4f = stack.m_85850_().m_85861_();
            bufferbuilder.m_166779_(VertexFormat.Mode.QUADS, DefaultVertexFormat.f_85817_);
            bufferbuilder.m_85982_(matrix4f, x, y + height, 0.0f).m_7421_(0.0f, 1.0f).m_5752_();
            bufferbuilder.m_85982_(matrix4f, x + width, y + height, 0.0f).m_7421_(1.0f, 1.0f).m_5752_();
            bufferbuilder.m_85982_(matrix4f, x + width, y, 0.0f).m_7421_(1.0f, 0.0f).m_5752_();
            bufferbuilder.m_85982_(matrix4f, x, y, 0.0f).m_7421_(0.0f, 0.0f).m_5752_();
            tessellator.m_85914_();
        }
    };
    public static final Function<String, AnnotationDetector.Wrapper<LDLRegister, IGuiTexture>> CACHE = Util.m_143827_(type -> {
        for (AnnotationDetector.Wrapper<LDLRegister, IGuiTexture> wrapper : AnnotationDetector.REGISTER_TEXTURES) {
            if (wrapper.annotation().name().equals(type)) {
                return wrapper;
            }
        }
        return null;
    });

    @OnlyIn(Dist.CLIENT)
    void draw(PoseStack poseStack, int i, int i2, float f, float f2, int i3, int i4);

    default IGuiTexture setColor(int color) {
        return this;
    }

    default IGuiTexture rotate(float degree) {
        return this;
    }

    default IGuiTexture scale(float scale) {
        return this;
    }

    default IGuiTexture transform(int xOffset, int yOffset) {
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    default void updateTick() {
    }

    @OnlyIn(Dist.CLIENT)
    default void drawSubArea(PoseStack stack, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        draw(stack, 0, 0, x, y, (int) width, (int) height);
    }

    default void createPreview(ConfiguratorGroup father) {
        father.addConfigurators(new WrapperConfigurator("ldlib.gui.editor.group.preview", new ImageWidget(0, 0, 100, 100, this).setBorder(2, ColorPattern.T_WHITE.color)));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    default void buildConfigurator(ConfiguratorGroup father) {
        createPreview(father);
        super.buildConfigurator(father);
    }

    @Nullable
    static CompoundTag serializeWrapper(IGuiTexture texture) {
        if (texture.isLDLRegister()) {
            CompoundTag tag = new CompoundTag();
            tag.m_128359_("type", texture.name());
            CompoundTag data = new CompoundTag();
            PersistedParser.serializeNBT(data, texture.getClass(), texture);
            tag.m_128365_("data", data);
            return tag;
        }
        return null;
    }

    @NotNull
    static IGuiTexture deserializeWrapper(CompoundTag tag) {
        String type = tag.m_128461_("type");
        CompoundTag data = tag.m_128469_("data");
        AnnotationDetector.Wrapper<LDLRegister, IGuiTexture> wrapper = CACHE.apply(type);
        IGuiTexture value = wrapper == null ? EMPTY : wrapper.creator().get();
        PersistedParser.deserializeNBT(data, new HashMap(), value.getClass(), value);
        return value;
    }

    default void setUIResource(Resource<IGuiTexture> texturesResource) {
    }
}
