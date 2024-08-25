package com.lowdragmc.lowdraglib.syncdata.field;

import com.lowdragmc.lowdraglib.syncdata.IFieldUpdateListener;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/field/FieldManagedStorage.class */
public class FieldManagedStorage implements IManagedStorage {
    private final IManaged owner;
    private BitSet dirtySyncFields;
    private BitSet dirtyPersistedFields;
    private IRef[] syncFields;
    private IRef[] persistedFields;
    private IRef[] nonLazyFields;
    private Map<ManagedKey, IRef> fieldMap;
    private boolean initialized = false;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<ManagedKey, List<FieldUpdateSubscription>> listeners = new HashMap();

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public <T> ISubscription addSyncUpdateListener(ManagedKey key, IFieldUpdateListener<T> listener) {
        FieldUpdateSubscription fieldUpdateSubscription = new FieldUpdateSubscription(key, listener) { // from class: com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage.1
            @Override // com.lowdragmc.lowdraglib.syncdata.field.FieldUpdateSubscription, com.lowdragmc.lowdraglib.syncdata.ISubscription
            public void unsubscribe() {
                FieldManagedStorage.this.listeners.getOrDefault(this.key, new ArrayList()).remove(this);
            }
        };
        this.listeners.computeIfAbsent(key, k -> {
            return new ArrayList();
        }).add(fieldUpdateSubscription);
        return fieldUpdateSubscription;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public void removeAllSyncUpdateListener(ManagedKey key) {
        this.listeners.remove(key);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public boolean hasSyncListener(ManagedKey key) {
        List<FieldUpdateSubscription> list = this.listeners.get(key);
        return (list == null || list.isEmpty()) ? false : true;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public <T> void notifyFieldUpdate(ManagedKey key, T newVal, T oldVal) {
        List<FieldUpdateSubscription> list = this.listeners.get(key);
        if (list != null) {
            for (FieldUpdateSubscription sub : list) {
                try {
                    sub.listener.onFieldChanged(key.getName(), newVal, oldVal);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public void init() {
        this.lock.lock();
        try {
            if (this.initialized) {
                return;
            }
            ManagedKey[] fields = this.owner.getFieldHolder().getFields();
            ManagedFieldUtils.FieldRefs result = ManagedFieldUtils.getFieldRefs(fields, this.owner, ref, index, changed -> {
                if (this.dirtySyncFields != null && index >= 0) {
                    this.dirtySyncFields.set(index, changed);
                    this.owner.onSyncChanged(ref, changed);
                }
            }, ref2, index2, changed2 -> {
                if (this.dirtyPersistedFields != null && index2 >= 0) {
                    this.dirtyPersistedFields.set(index2, changed2);
                    this.owner.onPersistedChanged(ref2, changed2);
                }
            });
            this.syncFields = result.syncedRefs();
            this.persistedFields = result.persistedRefs();
            this.dirtySyncFields = new BitSet(this.syncFields.length);
            this.dirtyPersistedFields = new BitSet(result.persistedRefs().length);
            this.nonLazyFields = result.nonLazyFields();
            this.fieldMap = result.fieldRefMap();
            this.initialized = true;
        } finally {
            this.lock.unlock();
        }
    }

    public FieldManagedStorage(IManaged owner) {
        this.owner = owner;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef[] getSyncFields() {
        init();
        return this.syncFields;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public boolean hasDirtySyncFields() {
        return !this.dirtySyncFields.isEmpty();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public boolean hasDirtyPersistedFields() {
        return !this.dirtyPersistedFields.isEmpty();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef[] getPersistedFields() {
        init();
        return this.persistedFields;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IManaged[] getManaged() {
        return new IManaged[]{this.owner};
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef getFieldByKey(ManagedKey key) {
        init();
        return this.fieldMap.get(key);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IManagedStorage
    public IRef[] getNonLazyFields() {
        init();
        return this.nonLazyFields;
    }
}
