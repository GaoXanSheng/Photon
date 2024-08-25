package com.lowdragmc.lowdraglib.syncdata.field;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/field/ManagedFieldHolder.class */
public class ManagedFieldHolder {
    private final Map<String, ManagedKey> fieldNameMap;
    private ManagedKey[] fields;
    private Map<String, RPCMethodMeta> rpcMethodMap;
    private final Class<? extends IManaged> clazz;

    public ManagedFieldHolder(Class<? extends IManaged> clazz) {
        this.fieldNameMap = new HashMap();
        this.rpcMethodMap = new HashMap();
        this.clazz = clazz;
        initAll();
    }

    public void merge(ManagedFieldHolder other) {
        this.fields = (ManagedKey[]) ArrayUtils.addAll(this.fields, other.fields);
        resetSyncFieldIndexMap();
        this.rpcMethodMap.putAll(other.rpcMethodMap);
    }

    public ManagedFieldHolder(Class<? extends IManaged> clazz, ManagedFieldHolder parent) {
        this(clazz);
        merge(parent);
    }

    private void initAll() {
        this.fields = ManagedFieldUtils.getManagedFields(this.clazz);
        resetSyncFieldIndexMap();
        this.rpcMethodMap = ManagedFieldUtils.getRPCMethods(this.clazz);
    }

    private void resetSyncFieldIndexMap() {
        ManagedKey[] managedKeyArr;
        this.fieldNameMap.clear();
        for (ManagedKey key : this.fields) {
            if (this.fieldNameMap.containsKey(key.getName())) {
                LDLib.LOGGER.warn("Duplicate sync field name: " + key.getName());
            } else {
                this.fieldNameMap.put(key.getName(), key);
            }
        }
    }

    public ManagedKey[] getFields() {
        return this.fields;
    }

    public Map<String, RPCMethodMeta> getRpcMethodMap() {
        return this.rpcMethodMap;
    }

    public ManagedKey getSyncedFieldIndex(String name) {
        if (!this.fieldNameMap.containsKey(name)) {
            throw new IllegalArgumentException("No sync field with name " + name);
        }
        return this.fieldNameMap.get(name);
    }
}
