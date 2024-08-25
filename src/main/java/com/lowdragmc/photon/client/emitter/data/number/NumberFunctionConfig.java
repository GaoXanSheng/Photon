package com.lowdragmc.photon.client.emitter.data.number;

import com.lowdragmc.photon.client.emitter.data.number.curve.CurveConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/NumberFunctionConfig.class */
public @interface NumberFunctionConfig {
    Class<? extends NumberFunction>[] types() default {Constant.class};

    float min() default -2.1474836E9f;

    float max() default 2.1474836E9f;

    float wheelDur() default 0.1f;

    float defaultValue() default 0.0f;

    boolean isDecimals() default true;

    CurveConfig curveConfig() default @CurveConfig;
}
