package com.lowdragmc.lowdraglib.gui.editor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Target({ElementType.TYPE})
@OnlyIn(Dist.CLIENT)
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/annotation/LDLRegisterClient.class */
public @interface LDLRegisterClient {
    String name();

    String group();

    String modID() default "";

    int priority() default 0;
}
