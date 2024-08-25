package com.lowdragmc.lowdraglib.gui.editor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/annotation/Configurable.class */
public @interface Configurable {
    String name() default "";

    String[] tips() default {};

    boolean collapse() default true;

    boolean canCollapse() default true;

    boolean forceUpdate() default true;

    String key() default "";

    boolean subConfigurable() default false;

    boolean persisted() default true;
}
