package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/IManagedStorage.class */
public interface IManagedStorage {
    IManaged[] getManaged();

    IRef getFieldByKey(ManagedKey managedKey);

    IRef[] getNonLazyFields();

    boolean hasDirtySyncFields();

    boolean hasDirtyPersistedFields();

    IRef[] getPersistedFields();

    IRef[] getSyncFields();

    <T> ISubscription addSyncUpdateListener(ManagedKey managedKey, IFieldUpdateListener<T> iFieldUpdateListener);

    void removeAllSyncUpdateListener(ManagedKey managedKey);

    boolean hasSyncListener(ManagedKey managedKey);

    <T> void notifyFieldUpdate(ManagedKey managedKey, T t, T t2);

    void init();

    default void markDirty(ManagedKey key) {
        getFieldByKey(key).markAsDirty();
    }

    default void markAllDirty() {
        IRef[] syncFields;
        for (IRef syncField : getSyncFields()) {
            syncField.markAsDirty();
        }
    }
}
