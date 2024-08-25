package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/ManagedAccessor.class */
public abstract class ManagedAccessor implements IAccessor {
    private byte defaultType = -1;

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public abstract ITypedPayload<?> readManagedField(AccessorOp accessorOp, IManagedVar<?> iManagedVar);

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public abstract void writeManagedField(AccessorOp accessorOp, IManagedVar<?> iManagedVar, ITypedPayload<?> iTypedPayload);

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public byte getDefaultType() {
        return this.defaultType;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void setDefaultType(byte defaultType) {
        this.defaultType = defaultType;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean isManaged() {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readField(AccessorOp op, IRef field) {
        if (!(field instanceof ManagedRef)) {
            throw new IllegalArgumentException("Field %s is not a managed field".formatted(new Object[]{field}));
        }
        ManagedRef syncedField = (ManagedRef) field;
        IManagedVar<?> managedField = syncedField.getField();
        if (!managedField.isPrimitive() && managedField.value() == null) {
            return PrimitiveTypedPayload.ofNull();
        }
        return readManagedField(op, managedField);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeField(AccessorOp op, IRef field, ITypedPayload<?> payload) {
        if (!(field instanceof ManagedRef)) {
            throw new IllegalArgumentException("Field %s is not a managed field".formatted(new Object[]{field}));
        }
        ManagedRef syncedField = (ManagedRef) field;
        IManagedVar<?> managedField = syncedField.getField();
        writeManagedField(op, managedField, payload);
    }
}
