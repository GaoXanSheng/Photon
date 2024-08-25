package com.lowdragmc.photon.client.emitter.data.number;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/NumberFunction3Config.class */
public @interface NumberFunction3Config {
    NumberFunctionConfig common() default @NumberFunctionConfig;

    NumberFunctionConfig[] xyz() default {};
}
