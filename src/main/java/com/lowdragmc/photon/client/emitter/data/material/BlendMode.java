package com.lowdragmc.photon.client.emitter.data.material;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/material/BlendMode.class */
public class BlendMode {
    @Configurable
    private boolean enableBlend;
    @Configurable
    private GlStateManager.SourceFactor srcColorFactor;
    @Configurable
    private GlStateManager.DestFactor dstColorFactor;
    @Configurable
    private GlStateManager.SourceFactor srcAlphaFactor;
    @Configurable
    private GlStateManager.DestFactor dstAlphaFactor;
    @Configurable
    private BlendFuc blendFunc;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/material/BlendMode$BlendFuc.class */
    public enum BlendFuc {
        ADD(32774),
        SUBTRACT(32778),
        REVERSE_SUBTRACT(32779),
        MIN(32775),
        MAX(32776);
        
        public final int op;

        BlendFuc(int op) {
            this.op = op;
        }
    }

    public boolean isEnableBlend() {
        return this.enableBlend;
    }

    public void setEnableBlend(boolean enableBlend) {
        this.enableBlend = enableBlend;
    }

    public GlStateManager.SourceFactor getSrcColorFactor() {
        return this.srcColorFactor;
    }

    public void setSrcColorFactor(GlStateManager.SourceFactor srcColorFactor) {
        this.srcColorFactor = srcColorFactor;
    }

    public GlStateManager.DestFactor getDstColorFactor() {
        return this.dstColorFactor;
    }

    public void setDstColorFactor(GlStateManager.DestFactor dstColorFactor) {
        this.dstColorFactor = dstColorFactor;
    }

    public GlStateManager.SourceFactor getSrcAlphaFactor() {
        return this.srcAlphaFactor;
    }

    public void setSrcAlphaFactor(GlStateManager.SourceFactor srcAlphaFactor) {
        this.srcAlphaFactor = srcAlphaFactor;
    }

    public GlStateManager.DestFactor getDstAlphaFactor() {
        return this.dstAlphaFactor;
    }

    public void setDstAlphaFactor(GlStateManager.DestFactor dstAlphaFactor) {
        this.dstAlphaFactor = dstAlphaFactor;
    }

    public BlendFuc getBlendFunc() {
        return this.blendFunc;
    }

    public void setBlendFunc(BlendFuc blendFunc) {
        this.blendFunc = blendFunc;
    }

    private BlendMode(boolean enableBlend, GlStateManager.SourceFactor srcColorFactor, GlStateManager.DestFactor dstColorFactor, GlStateManager.SourceFactor srcAlphaFactor, GlStateManager.DestFactor dstAlphaFactor, BlendFuc blendFunc) {
        this.srcColorFactor = srcColorFactor;
        this.dstColorFactor = dstColorFactor;
        this.srcAlphaFactor = srcAlphaFactor;
        this.dstAlphaFactor = dstAlphaFactor;
        this.enableBlend = enableBlend;
        this.blendFunc = blendFunc;
    }

    public BlendMode() {
        this(true, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, BlendFuc.ADD);
    }

    public BlendMode(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor, BlendFuc blendFunc) {
        this(true, srcFactor, dstFactor, srcFactor, dstFactor, blendFunc);
    }

    public BlendMode(GlStateManager.SourceFactor srcColorFactor, GlStateManager.DestFactor dstColorFactor, GlStateManager.SourceFactor srcAlphaFactor, GlStateManager.DestFactor dstAlphaFactor, BlendFuc blendFunc) {
        this(true, srcColorFactor, dstColorFactor, srcAlphaFactor, dstAlphaFactor, blendFunc);
    }

    public void apply() {
        if (!this.enableBlend) {
            RenderSystem.m_69461_();
            return;
        }
        RenderSystem.m_69478_();
        RenderSystem.m_69453_();
        RenderSystem.m_69403_(this.blendFunc.op);
        RenderSystem.m_69416_(this.srcColorFactor, this.dstColorFactor, this.srcAlphaFactor, this.dstAlphaFactor);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof BlendMode) {
            BlendMode blendMode = (BlendMode) object;
            return this.blendFunc == blendMode.blendFunc && this.dstAlphaFactor == blendMode.dstAlphaFactor && this.dstColorFactor == blendMode.dstColorFactor && this.enableBlend == blendMode.enableBlend && this.srcAlphaFactor == blendMode.srcAlphaFactor && this.srcColorFactor == blendMode.srcColorFactor;
        }
        return false;
    }

    public int hashCode() {
        int i = this.srcColorFactor.f_84751_;
        return (31 * ((31 * ((31 * ((31 * ((31 * i) + this.srcAlphaFactor.f_84751_)) + this.dstColorFactor.f_84646_)) + this.dstAlphaFactor.f_84646_)) + this.blendFunc.op)) + (this.enableBlend ? 1 : 0);
    }
}
