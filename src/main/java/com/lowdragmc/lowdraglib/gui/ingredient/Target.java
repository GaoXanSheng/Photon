package com.lowdragmc.lowdraglib.gui.ingredient;

import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.Rect2i;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/ingredient/Target.class */
public interface Target extends Consumer<Object> {
    @Nonnull
    Rect2i getArea();

    @Override // java.util.function.Consumer
    void accept(Object obj);
}
