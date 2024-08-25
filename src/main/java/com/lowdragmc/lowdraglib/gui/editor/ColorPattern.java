package com.lowdragmc.lowdraglib.gui.editor;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ColorPattern.class */
public enum ColorPattern {
    WHITE(-1),
    T_WHITE(-1996488705),
    BLACK(-14540254),
    T_BLACK(1143087650),
    GRAY(-10066330),
    T_GRAY(1717986918),
    GREEN(-13369600),
    T_GREEN(-2009858304),
    RED(-6487774),
    T_RED(-2002976478),
    YELLOW(-205),
    T_YELLOW(-1996488909),
    CYAN(-13404297),
    T_CYAN(-2009893001);
    
    public final int color;

    ColorPattern(int color) {
        this.color = color;
    }

    public ColorRectTexture rectTexture() {
        return new ColorRectTexture(this.color);
    }

    public ColorBorderTexture borderTexture(int border) {
        return new ColorBorderTexture(border, this.color);
    }
}
