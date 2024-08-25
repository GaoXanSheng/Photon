package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/IManaged.class */
public interface IManaged {
    ManagedFieldHolder getFieldHolder();

    IManagedStorage getSyncStorage();

    void onChanged();

    default void onSyncChanged(IRef ref, boolean isDirty) {
    }

    default void onPersistedChanged(IRef ref, boolean isDirty) {
        if (isDirty) {
            onChanged();
            ref.clearPersistedDirty();
        }
    }

    default <T> ISubscription addSyncUpdateListener(String name, IFieldUpdateListener<T> listener) {
        return getSyncStorage().addSyncUpdateListener(getFieldHolder().getSyncedFieldIndex(name), listener);
    }

    default void markDirty(String name) {
        getSyncStorage().getFieldByKey(getFieldHolder().getSyncedFieldIndex(name)).markAsDirty();
    }
}
