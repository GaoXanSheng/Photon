package com.lowdragmc.photon.client.emitter.data.number.curve;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/curve/CurveTexture.class */
public class CurveTexture extends TransformTexture {
    private final ECBCurves curves;
    private int color = ColorPattern.T_RED.color;
    private float width = 0.5f;

    public void setWidth(float width) {
        this.width = width;
    }

    public CurveTexture(ECBCurves curves) {
        this.curves = curves;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public CurveTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack poseStack, int mouseX, int mouseY, float x, float y, int width, int height) {
        List<Vec2> points = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            float coordX = (i * 1.0f) / width;
            points.add(new Vec2(coordX, this.curves.getCurveY(coordX)));
        }
        points.add(new Vec2(1.0f, this.curves.getCurveY(1.0f)));
        DrawerHelper.drawLines(poseStack, points.stream().map(coord -> {
            return new Vec2(x + (width * coord.f_82470_), y + (height * (1.0f - coord.f_82471_)));
        }).toList(), this.color, this.color, this.width);
    }
}
