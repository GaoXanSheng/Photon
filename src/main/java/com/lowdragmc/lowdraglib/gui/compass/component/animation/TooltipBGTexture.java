package com.lowdragmc.lowdraglib.gui.compass.component.animation;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.mojang.blaze3d.vertex.PoseStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/animation/TooltipBGTexture.class */
public class TooltipBGTexture extends TransformTexture {
    public static TooltipBGTexture INSTANCE = new TooltipBGTexture();

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        ColorPattern.BLACK.rectTexture().draw(stack, mouseX, mouseY, x - 1.0f, y - 1.0f, width + 2, height + 2);
        ColorPattern.WHITE.borderTexture(1).draw(stack, mouseX, mouseY, x - 1.0f, y - 1.0f, width + 2, height + 2);
    }
}
