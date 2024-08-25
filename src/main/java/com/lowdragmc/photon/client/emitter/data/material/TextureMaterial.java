package com.lowdragmc.photon.client.emitter.data.material;

import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.photon.Photon;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.File;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/material/TextureMaterial.class */
public class TextureMaterial extends ShaderInstanceMaterial {
    @Configurable
    public ResourceLocation texture;
    @Configurable
    @NumberRange(range = {0.0d, 1.0d})
    public float discardThreshold;

    public TextureMaterial() {
        this.texture = new ResourceLocation("textures/particle/glow.png");
        this.discardThreshold = 0.01f;
    }

    public TextureMaterial(ResourceLocation texture) {
        this.texture = new ResourceLocation("textures/particle/glow.png");
        this.discardThreshold = 0.01f;
        this.texture = texture;
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.m_128359_("texture", this.texture.toString());
        tag.m_128350_("discardThreshold", this.discardThreshold);
        return tag;
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public IMaterial copy() {
        TextureMaterial mat = new TextureMaterial(this.texture);
        mat.discardThreshold = this.discardThreshold;
        return mat;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        this.texture = new ResourceLocation(nbt.m_128461_("texture"));
        this.discardThreshold = nbt.m_128457_("discardThreshold");
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.ShaderInstanceMaterial
    public ShaderInstance getShader() {
        return Shaders.getParticleShader();
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.ShaderInstanceMaterial
    public void setupUniform() {
        RenderSystem.m_157456_(0, this.texture);
        Shaders.getParticleShader().m_173356_("DiscardThreshold").m_5985_(this.discardThreshold);
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.ShaderInstanceMaterial, com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public void begin(boolean isInstancing) {
        if (Photon.isUsingShaderPack() && Editor.INSTANCE == null) {
            RenderSystem.m_157456_(0, this.texture);
            return;
        }
        RenderSystem.m_157427_(this::getShader);
        setupUniform();
    }

    @Override // com.lowdragmc.photon.client.emitter.data.material.ShaderInstanceMaterial, com.lowdragmc.photon.client.emitter.data.material.IMaterial
    public IGuiTexture preview() {
        return new ResourceTexture(this.texture.toString());
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        WidgetGroup widgetGroup = new WidgetGroup(0, 0, 100, 100);
        widgetGroup.addWidget(new ImageWidget(0, 0, 100, 100, () -> {
            return new ResourceTexture(this.texture.toString());
        }).setBorder(2, ColorPattern.T_WHITE.color));
        widgetGroup.addWidget(new ButtonWidget(0, 0, 100, 100, IGuiTexture.EMPTY, cd -> {
            if (Editor.INSTANCE == null) {
                return;
            }
            File path = new File(Editor.INSTANCE.getWorkSpace(), "assets/ldlib/textures");
            DialogWidget.showFileDialog(Editor.INSTANCE, "ldlib.gui.editor.tips.select_image", path, true, DialogWidget.suffixFilter(".png"), r -> {
                if (path != null && path.isFile()) {
                    this.texture = new ResourceLocation("ldlib:" + path.getPath().replace(path.getPath(), "textures").replace('\\', '/'));
                }
            });
        }));
        WrapperConfigurator base = new WrapperConfigurator("ldlib.gui.editor.group.base_image", widgetGroup);
        base.setTips("ldlib.gui.editor.tips.click_select_image");
        father.addConfigurators(base);
        super.buildConfigurator(father);
    }
}
