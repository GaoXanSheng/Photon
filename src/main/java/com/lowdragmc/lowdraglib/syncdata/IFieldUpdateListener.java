package com.lowdragmc.lowdraglib.syncdata;

@FunctionalInterface
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/IFieldUpdateListener.class */
public interface IFieldUpdateListener<T> {
    void onFieldChanged(String str, T t, T t2);
}
