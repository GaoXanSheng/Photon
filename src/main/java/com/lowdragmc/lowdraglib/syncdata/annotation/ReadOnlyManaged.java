package com.lowdragmc.lowdraglib.syncdata.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/annotation/ReadOnlyManaged.class */
public @interface ReadOnlyManaged {
    String onDirtyMethod();

    String serializeMethod();

    String deserializeMethod();
}
