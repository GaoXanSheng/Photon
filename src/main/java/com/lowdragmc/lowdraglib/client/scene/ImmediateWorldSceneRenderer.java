package com.lowdragmc.lowdraglib.client.scene;

import com.lowdragmc.lowdraglib.utils.PositionedRect;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/scene/ImmediateWorldSceneRenderer.class */
public class ImmediateWorldSceneRenderer extends WorldSceneRenderer {
    public ImmediateWorldSceneRenderer(Level world) {
        super(world);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer
    public PositionedRect getPositionedRect(int x, int y, int width, int height) {
        Window window = Minecraft.m_91087_().m_91268_();
        int windowWidth = (int) ((width / (window.m_85445_() * 1.0d)) * window.m_85441_());
        int windowHeight = (int) ((height / (window.m_85446_() * 1.0d)) * window.m_85442_());
        int windowX = (int) ((x / (window.m_85445_() * 1.0d)) * window.m_85441_());
        int windowY = (window.m_85442_() - ((int) ((y / (window.m_85446_() * 1.0d)) * window.m_85442_()))) - windowHeight;
        return super.getPositionedRect(windowX, windowY, windowWidth, windowHeight);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer
    public void clearView(int x, int y, int width, int height) {
        int a = (this.clearColor & (-16777216)) >> 24;
        if (a == 0) {
            RenderSystem.m_69421_(256, Minecraft.f_91002_);
            return;
        }
        GL11.glEnable(3089);
        GL11.glScissor(x, y, width, height);
        super.clearView(x, y, width, height);
        GL11.glDisable(3089);
    }
}
