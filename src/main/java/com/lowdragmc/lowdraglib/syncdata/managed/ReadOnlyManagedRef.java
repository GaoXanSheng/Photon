package com.lowdragmc.lowdraglib.syncdata.managed;

import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ReadOnlyManagedRef.class */
public class ReadOnlyManagedRef extends ManagedRef {
    private boolean wasNull;
    private CompoundTag lastUid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReadOnlyManagedRef(ReadOnlyManagedField field) {
        super(field);
        Object current = getField().value();
        this.wasNull = current == null;
        if (current != null) {
            this.lastUid = getReadOnlyField().serializeUid(current);
        }
    }

    public ReadOnlyManagedField getReadOnlyField() {
        return (ReadOnlyManagedField) this.field;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void update() {
        Object newValue = getField().value();
        if ((this.wasNull && newValue != null) || (!this.wasNull && newValue == null)) {
            markAsDirty();
        }
        this.wasNull = newValue == null;
        if (newValue != null) {
            CompoundTag newUid = getReadOnlyField().serializeUid(newValue);
            if (!newUid.equals(this.lastUid) || getReadOnlyField().isDirty(newValue)) {
                markAsDirty();
            }
            this.lastUid = newUid;
        }
    }
}
