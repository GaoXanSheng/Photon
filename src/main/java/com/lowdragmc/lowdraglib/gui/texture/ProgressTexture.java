package com.lowdragmc.lowdraglib.gui.texture;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "progress_texture", group = "texture")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ProgressTexture.class */
public class ProgressTexture extends TransformTexture {
    @Configurable
    protected FillDirection fillDirection;
    @Configurable
    protected IGuiTexture emptyBarArea;
    @Configurable
    protected IGuiTexture filledBarArea;
    protected double progress;
    private boolean demo;

    public IGuiTexture getEmptyBarArea() {
        return this.emptyBarArea;
    }

    public IGuiTexture getFilledBarArea() {
        return this.filledBarArea;
    }

    public ProgressTexture() {
        this(new ResourceTexture("ldlib:textures/gui/progress_bar_fuel.png").getSubTexture(0.0d, 0.0d, 1.0d, 0.5d), new ResourceTexture("ldlib:textures/gui/progress_bar_fuel.png").getSubTexture(0.0d, 0.5d, 1.0d, 0.5d));
        this.fillDirection = FillDirection.DOWN_TO_UP;
        this.demo = true;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void updateTick() {
        if (this.emptyBarArea != null) {
            this.emptyBarArea.updateTick();
        }
        if (this.filledBarArea != null) {
            this.filledBarArea.updateTick();
        }
        if (this.demo) {
            this.progress = Math.abs(System.currentTimeMillis() % 2000) / 2000.0d;
        }
    }

    public ProgressTexture(IGuiTexture emptyBarArea, IGuiTexture filledBarArea) {
        this.fillDirection = FillDirection.LEFT_TO_RIGHT;
        this.emptyBarArea = emptyBarArea;
        this.filledBarArea = filledBarArea;
    }

    public void setProgress(double progress) {
        this.progress = Mth.m_14008_(0.0d, progress, 1.0d);
    }

    public ProgressTexture setFillDirection(FillDirection fillDirection) {
        this.fillDirection = fillDirection;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.TransformTexture
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(PoseStack stack, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (this.emptyBarArea != null) {
            this.emptyBarArea.draw(stack, mouseX, mouseY, x, y, width, height);
        }
        if (this.filledBarArea != null) {
            float drawnU = (float) this.fillDirection.getDrawnU(this.progress);
            float drawnV = (float) this.fillDirection.getDrawnV(this.progress);
            float drawnWidth = (float) this.fillDirection.getDrawnWidth(this.progress);
            float drawnHeight = (float) this.fillDirection.getDrawnHeight(this.progress);
            float X = x + (drawnU * width);
            float Y = y + (drawnV * height);
            float W = width * drawnWidth;
            float H = height * drawnHeight;
            this.filledBarArea.drawSubArea(stack, X, Y, W, H, drawnU, drawnV, (drawnWidth * width) / width, (drawnHeight * height) / height);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
    public void setUIResource(Resource<IGuiTexture> texturesResource) {
        if (this.emptyBarArea != null) {
            this.emptyBarArea.setUIResource(texturesResource);
        }
        if (this.filledBarArea != null) {
            this.filledBarArea.setUIResource(texturesResource);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ProgressTexture$Auto.class */
    public static class Auto extends ProgressTexture {
        public Auto(IGuiTexture emptyBarArea, IGuiTexture filledBarArea) {
            super(emptyBarArea, filledBarArea);
        }

        @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture, com.lowdragmc.lowdraglib.gui.texture.IGuiTexture
        public void updateTick() {
            this.progress = Math.abs(System.currentTimeMillis() % 2000) / 2000.0d;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/texture/ProgressTexture$FillDirection.class */
    public enum FillDirection {
        LEFT_TO_RIGHT { // from class: com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.1
            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnHeight(double progress) {
                return 1.0d;
            }
        },
        RIGHT_TO_LEFT { // from class: com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.2
            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnU(double progress) {
                return 1.0d - progress;
            }

            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnHeight(double progress) {
                return 1.0d;
            }
        },
        UP_TO_DOWN { // from class: com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.3
            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnWidth(double progress) {
                return 1.0d;
            }
        },
        DOWN_TO_UP { // from class: com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.4
            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnV(double progress) {
                return 1.0d - progress;
            }

            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnWidth(double progress) {
                return 1.0d;
            }
        },
        ALWAYS_FULL { // from class: com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.5
            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnHeight(double progress) {
                return 1.0d;
            }

            @Override // com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection
            public double getDrawnWidth(double progress) {
                return 1.0d;
            }
        };

        public double getDrawnU(double progress) {
            return 0.0d;
        }

        public double getDrawnV(double progress) {
            return 0.0d;
        }

        public double getDrawnWidth(double progress) {
            return progress;
        }

        public double getDrawnHeight(double progress) {
            return progress;
        }
    }
}
