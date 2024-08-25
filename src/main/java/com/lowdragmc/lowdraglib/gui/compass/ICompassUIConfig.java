package com.lowdragmc.lowdraglib.gui.compass;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import javax.annotation.Nonnull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/ICompassUIConfig.class */
public interface ICompassUIConfig {
    @Nonnull
    IGuiTexture getSectionBackground();

    @Nonnull
    IGuiTexture getListViewBackground();

    @Nonnull
    IGuiTexture getListItemBackground();

    @Nonnull
    IGuiTexture getListItemSelectedBackground();

    @Nonnull
    IGuiTexture getNodeBackground();

    @Nonnull
    IGuiTexture getNodeHoverBackground();

    static ICompassUIConfig getDefault() {
        return new ICompassUIConfig() { // from class: com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig.1
            public IGuiTexture sectionBackground = IGuiTexture.EMPTY;
            public IGuiTexture listViewBackground = ResourceBorderTexture.BORDERED_BACKGROUND_INVERSE;
            public IGuiTexture listItemBackground = ResourceBorderTexture.BUTTON_COMMON;
            public IGuiTexture listItemSelectedBackground = ResourceBorderTexture.BUTTON_COMMON.copy().setColor(-13402241);
            public IGuiTexture nodeBackground = ResourceBorderTexture.BUTTON_COMMON;
            public IGuiTexture nodeHoverBackground = ResourceBorderTexture.BUTTON_COMMON.copy().setColor(-13402241);

            @Override // com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig
            public IGuiTexture getSectionBackground() {
                return this.sectionBackground;
            }

            @Override // com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig
            public IGuiTexture getListViewBackground() {
                return this.listViewBackground;
            }

            @Override // com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig
            public IGuiTexture getListItemBackground() {
                return this.listItemBackground;
            }

            @Override // com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig
            public IGuiTexture getListItemSelectedBackground() {
                return this.listItemSelectedBackground;
            }

            @Override // com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig
            public IGuiTexture getNodeBackground() {
                return this.nodeBackground;
            }

            @Override // com.lowdragmc.lowdraglib.gui.compass.ICompassUIConfig
            public IGuiTexture getNodeHoverBackground() {
                return this.nodeHoverBackground;
            }
        };
    }
}
