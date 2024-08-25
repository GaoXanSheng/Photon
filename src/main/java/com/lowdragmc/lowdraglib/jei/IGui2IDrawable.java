package com.lowdragmc.lowdraglib.jei;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import mezz.jei.api.gui.drawable.IDrawable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/jei/IGui2IDrawable.class */
public interface IGui2IDrawable {
    static IDrawable toDrawable(final IGuiTexture guiTexture, final int width, final int height) {
        return new IDrawable() { // from class: com.lowdragmc.lowdraglib.jei.IGui2IDrawable.1
            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public void draw(@Nonnull PoseStack matrixStack, int x, int y) {
                if (guiTexture == null) {
                    return;
                }
                guiTexture.draw(matrixStack, 0, 0, x, y, width, height);
            }
        };
    }
}
