package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.SyncUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/SimpleObjectRef.class */
public class SimpleObjectRef extends ManagedRef {
    private Object oldValue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleObjectRef(IManagedVar<?> field) {
        super(field);
        this.oldValue = getField().value();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void update() {
        Object newValue = getField().value();
        if ((this.oldValue == null && newValue != null) || ((this.oldValue != null && newValue == null) || (this.oldValue != null && SyncUtils.isChanged(this.oldValue, newValue)))) {
            this.oldValue = SyncUtils.copyWhenNecessary(newValue);
            markAsDirty();
        }
    }
}
