package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.IFieldUpdateListener;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/MultiManagedStorage.class */
public class MultiManagedStorage implements IManagedStorage {
    private final List<IManagedStorage> storages = new ArrayList();
    private final Map<ManagedKey, IRef> cacheFields = new HashMap();
    private IManaged[] cacheManaged = null;
    private IRef[] cacheNonLazyFields = null;
    private IRef[] cachePersistedFields = null;
    private IRef[] cacheSyncFields = null;

    public void attach(IManagedStorage storage) {
        clearCache();
        this.storages.add(storage);
    }

    public void detach(IManagedStorage storage) {
        clearCache();
        this.storages.remove(storage);
    }

    public void clearCache() {
        this.cacheFields.clear();
        this.cacheManaged = null;
        this.cacheNonLazyFields = null;
        this.cachePersistedFields = null;
        this.cacheSyncFields = null;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef getFieldByKey(ManagedKey key) {
        if (!this.cacheFields.containsKey(key)) {
            IRef ref = null;
            for (IManagedStorage storage : this.storages) {
                ref = storage.getFieldByKey(key);
                if (ref != null) {
                    break;
                }
            }
            this.cacheFields.put(key, ref);
        }
        return this.cacheFields.get(key);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IManaged[] getManaged() {
        if (this.cacheManaged == null) {
            this.cacheManaged = (IManaged[]) this.storages.stream().map((v0) -> {
                return v0.getManaged();
            }).flatMap((v0) -> {
                return Arrays.stream(v0);
            }).toArray(x$0 -> {
                return new IManaged[x$0];
            });
        }
        return this.cacheManaged;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef[] getNonLazyFields() {
        if (this.cacheNonLazyFields == null) {
            this.cacheNonLazyFields = (IRef[]) this.storages.stream().map((v0) -> {
                return v0.getNonLazyFields();
            }).flatMap((v0) -> {
                return Arrays.stream(v0);
            }).toArray(x$0 -> {
                return new IRef[x$0];
            });
        }
        return this.cacheNonLazyFields;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef[] getPersistedFields() {
        if (this.cachePersistedFields == null) {
            this.cachePersistedFields = (IRef[]) this.storages.stream().map((v0) -> {
                return v0.getPersistedFields();
            }).flatMap((v0) -> {
                return Arrays.stream(v0);
            }).toArray(x$0 -> {
                return new IRef[x$0];
            });
        }
        return this.cachePersistedFields;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef[] getSyncFields() {
        if (this.cacheSyncFields == null) {
            this.cacheSyncFields = (IRef[]) this.storages.stream().map((v0) -> {
                return v0.getSyncFields();
            }).flatMap((v0) -> {
                return Arrays.stream(v0);
            }).toArray(x$0 -> {
                return new IRef[x$0];
            });
        }
        return this.cacheSyncFields;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public boolean hasDirtySyncFields() {
        for (IManagedStorage storage : this.storages) {
            if (storage.hasDirtySyncFields()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public boolean hasDirtyPersistedFields() {
        for (IManagedStorage storage : this.storages) {
            if (storage.hasDirtyPersistedFields()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public <T> ISubscription addSyncUpdateListener(ManagedKey key, IFieldUpdateListener<T> listener) {
        throw new IllegalStateException("do not add listener for multi managed storage");
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public void removeAllSyncUpdateListener(ManagedKey key) {
        throw new IllegalStateException("do not remove listener for multi managed storage");
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public boolean hasSyncListener(ManagedKey key) {
        for (IManagedStorage storage : this.storages) {
            if (storage.hasSyncListener(key)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public <T> void notifyFieldUpdate(ManagedKey key, T newVal, T oldVal) {
        for (IManagedStorage storage : this.storages) {
            storage.notifyFieldUpdate(key, newVal, oldVal);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public void init() {
        this.storages.forEach((v0) -> {
            v0.init();
        });
    }
}
