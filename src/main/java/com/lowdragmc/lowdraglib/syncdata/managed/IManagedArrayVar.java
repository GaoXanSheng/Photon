package com.lowdragmc.lowdraglib.syncdata.managed;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedArrayVar.class */
public interface IManagedArrayVar<T> extends IManagedVar<T> {
    T value(int i);

    void set(int i, T t);

    Class<T> getChildrenType();
}
