package com.lowdragmc.lowdraglib.syncdata;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/IContentChangeAware.class */
public interface IContentChangeAware {
    void setOnContentsChanged(Runnable runnable);

    Runnable getOnContentsChanged();
}
