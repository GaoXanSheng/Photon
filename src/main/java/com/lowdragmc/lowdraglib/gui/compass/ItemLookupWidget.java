package com.lowdragmc.lowdraglib.gui.compass;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

/**
 * @author KilaBash
 * @date 2023/7/30
 * @implNote ItemLookupWidget
 */
@OnlyIn(Dist.CLIENT)
public class ItemLookupWidget extends Widget {
    private final String text;

    public ItemLookupWidget(String text) {
        super(0, 0, Minecraft.getInstance().font.width(I18n.get(text)),10);
        this.text = text;
    }

    @Override
    public void drawInBackground(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        var pos = this.getPosition();
        var size = this.getSize();
        long id = Minecraft.getInstance().getWindow().getWindow();
        var isCPressed = InputConstants.isKeyDown(id, GLFW.GLFW_KEY_C);
        if (isCPressed) {
            ColorPattern.WHITE.borderTexture(-1).draw(poseStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
            ColorPattern.GREEN.rectTexture().draw(poseStack, mouseX, mouseY, pos.x + 2, pos.y + 2, (int)((size.width - 4) * CompassManager.INSTANCE.getCHoverProgress()), size.height - 4);
        } else {
            new TextTexture(text).setType(TextTexture.TextType.LEFT).setDropShadow(false).setColor(0xff555555).draw(poseStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
    }
}

