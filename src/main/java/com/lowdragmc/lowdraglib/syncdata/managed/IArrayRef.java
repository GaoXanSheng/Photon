package com.lowdragmc.lowdraglib.syncdata.managed;

import it.unimi.dsi.fastutil.ints.IntSet;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IArrayRef.class */
public interface IArrayRef extends IRef {
    IntSet getChanged();

    default void setChanged(int index) {
        markAsDirty();
    }
}
