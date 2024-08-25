package com.lowdragmc.lowdraglib.utils;

import java.util.function.Consumer;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ISearch.class */
public interface ISearch<T> {
    void search(String str, Consumer<T> consumer);

    default boolean isManualInterrupt() {
        return false;
    }
}
