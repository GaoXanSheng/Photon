package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.photon.client.emitter.data.material.BlendMode;
import com.lowdragmc.photon.client.emitter.data.material.IMaterial;
import com.lowdragmc.photon.client.emitter.data.material.TextureMaterial;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/MaterialSetting.class */
public class MaterialSetting implements IConfigurable, ITagSerializable<CompoundTag> {
    @Configurable(name = "Blend Mode", subConfigurable = true)
    protected final BlendMode blendMode = new BlendMode();
    @Configurable
    protected boolean cull = true;
    @Configurable
    protected boolean depthTest = true;
    @Configurable
    protected boolean depthMask = false;
    @Nonnull
    protected IMaterial material = new TextureMaterial();

    public boolean isCull() {
        return this.cull;
    }

    public void setCull(boolean cull) {
        this.cull = cull;
    }

    public boolean isDepthTest() {
        return this.depthTest;
    }

    public void setDepthTest(boolean depthTest) {
        this.depthTest = depthTest;
    }

    public boolean isDepthMask() {
        return this.depthMask;
    }

    public void setDepthMask(boolean depthMask) {
        this.depthMask = depthMask;
    }

    @Nonnull
    public IMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(@Nonnull IMaterial material) {
        if (material == null) {
            throw new NullPointerException("material is marked non-null but is null");
        }
        this.material = material;
    }

    public void pre() {
        this.blendMode.apply();
        if (this.cull) {
            RenderSystem.m_69481_();
        } else {
            RenderSystem.m_69464_();
        }
        if (this.depthTest) {
            RenderSystem.m_69482_();
        } else {
            RenderSystem.m_69465_();
        }
        RenderSystem.m_69458_(this.depthMask);
    }

    public void post() {
        if (this.blendMode.getBlendFunc() != BlendMode.BlendFuc.ADD) {
            RenderSystem.m_69403_(BlendMode.BlendFuc.ADD.op);
        }
        if (!this.cull) {
            RenderSystem.m_69481_();
        }
        if (!this.depthTest) {
            RenderSystem.m_69482_();
        }
        if (!this.depthMask) {
            RenderSystem.m_69458_(true);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        super.buildConfigurator(father);
        ConfiguratorGroup setting = new ConfiguratorGroup("Setting");
        this.material.buildConfigurator(setting);
        ImageWidget preview = new ImageWidget(0, 0, 50, 50, () -> {
            return this.material.preview();
        }).setBorder(2, ColorPattern.T_WHITE.color);
        WrapperConfigurator wrapper = new WrapperConfigurator("preview", preview);
        preview.setDraggingConsumer(o -> {
            return o instanceof IMaterial;
        }, o2 -> {
            preview.setBorder(2, ColorPattern.GREEN.color);
        }, o3 -> {
            preview.setBorder(2, ColorPattern.T_WHITE.color);
        }, o4 -> {
            if (preview instanceof IMaterial) {
                IMaterial mat = (IMaterial) preview;
                this.material = mat.copy();
                setting.removeAllConfigurators();
                this.material.buildConfigurator(setting);
                setting.computeLayout();
                setting.setBorder(2, ColorPattern.T_WHITE.color);
            }
        });
        wrapper.setTips("photon.emitter.config.material.preview");
        father.addConfigurator(0, wrapper);
        father.addConfigurators(setting);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        PersistedParser.serializeNBT(nbt, getClass(), this);
        nbt.m_128365_("material", this.material.mo129serializeNBT());
        return nbt;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        PersistedParser.deserializeNBT(nbt, new HashMap(), getClass(), this);
        IMaterial material = IMaterial.deserializeWrapper(nbt.m_128469_("material"));
        this.material = material == null ? new TextureMaterial() : material;
    }
}
