package com.lowdragmc.lowdraglib.utils;

import java.util.function.Consumer;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/LdUtils.class */
public class LdUtils {
    private LdUtils() {
        throw new UnsupportedOperationException("can't instantiate LdUtils");
    }

    public static <T> T make(T value, Consumer<T> operate) {
        operate.accept(value);
        return value;
    }
}
