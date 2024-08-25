package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedHolder;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ArrayPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import java.lang.reflect.Array;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/ArrayAccessor.class */
public class ArrayAccessor implements IAccessor, IArrayLikeAccessor {
    private final IAccessor childAccessor;
    private final Class<?> childType;

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public byte getDefaultType() {
        return TypedPayloadRegistries.getId(ArrayPayload.class);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void setDefaultType(byte defaultType) {
        throw new UnsupportedOperationException("Cannot set default type for array accessor");
    }

    public ArrayAccessor(IAccessor childAccessor, Class<?> childType) {
        this.childAccessor = childAccessor;
        this.childType = childType;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readField(AccessorOp op, IRef field) {
        if (field instanceof ManagedRef) {
            ManagedRef managedRef = (ManagedRef) field;
            IManagedVar<?> managedField = managedRef.getField();
            if (!managedField.isPrimitive() && managedField.value() == null) {
                return PrimitiveTypedPayload.ofNull();
            }
            return readManagedField(op, managedField);
        }
        Object obj = field.readRaw();
        if (obj == null) {
            throw new IllegalArgumentException("readonly field %s has a null reference".formatted(new Object[]{field}));
        }
        return readFromReadonlyField(op, obj);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeField(AccessorOp op, IRef field, ITypedPayload<?> payload) {
        if (field instanceof ManagedRef) {
            ManagedRef syncedField = (ManagedRef) field;
            IManagedVar<?> managedField = syncedField.getField();
            writeManagedField(op, managedField, payload);
        }
        Object obj = field.readRaw();
        if (obj == null) {
            throw new IllegalArgumentException("readonly field %s has a null reference".formatted(new Object[]{field}));
        }
        writeToReadonlyField(op, obj, payload);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean hasPredicate() {
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
    public boolean test(Class<?> type) {
        return type.isArray();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean isManaged() {
        return this.childAccessor.isManaged();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
        Object value = field.value();
        if (value == null) {
            return PrimitiveTypedPayload.ofNull();
        }
        if (!value.getClass().isArray()) {
            throw new IllegalArgumentException("Value %s is not an array".formatted(new Object[]{value}));
        }
        int size = Array.getLength(value);
        ITypedPayload[] result = new ITypedPayload[size];
        if (!this.childAccessor.isManaged()) {
            for (int i = 0; i < size; i++) {
                Object obj = Array.get(value, i);
                ITypedPayload<?> payload = this.childAccessor.readFromReadonlyField(op, obj);
                result[i] = payload;
            }
        } else {
            for (int i2 = 0; i2 < size; i2++) {
                ManagedArrayItem<Object> holder = ManagedArrayItem.of(value, i2);
                ITypedPayload<?> payload2 = this.childAccessor.readManagedField(op, holder);
                result[i2] = payload2;
            }
        }
        return ArrayPayload.of(result);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
        if (payload instanceof PrimitiveTypedPayload) {
            PrimitiveTypedPayload<?> primitive = (PrimitiveTypedPayload) payload;
            if (primitive.isNull()) {
                field.set(null);
                return;
            }
        }
        if (!(payload instanceof ArrayPayload)) {
            throw new IllegalArgumentException("Payload %s is not ArrayPayload".formatted(new Object[]{payload}));
        }
        ArrayPayload arrayPayload = (ArrayPayload) payload;
        boolean isManaged = this.childAccessor.isManaged();
        Object result = field.value();
        if (result == null || Array.getLength(result) != arrayPayload.getPayload().length) {
            if (!isManaged) {
                throw new IllegalArgumentException("The array of %s should not be changed".formatted(new Object[]{this.childType}));
            }
            result = Array.newInstance(this.childType, arrayPayload.getPayload().length);
        }
        ITypedPayload<?>[] payloads = arrayPayload.getPayload();
        if (!isManaged) {
            for (int i = 0; i < payloads.length; i++) {
                Object obj = Array.get(result, i);
                ITypedPayload<?> item = payloads[i];
                this.childAccessor.writeToReadonlyField(op, obj, item);
            }
        } else {
            for (int i2 = 0; i2 < payloads.length; i2++) {
                ManagedArrayItem<Object> holder = ManagedArrayItem.of(result, i2);
                this.childAccessor.writeManagedField(op, holder, payloads[i2]);
            }
        }
        if (isManaged) {
            field.set(result);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj) {
        ManagedHolder<Object> holder = ManagedHolder.of(obj);
        return readManagedField(op, holder);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload) {
        ManagedHolder<Object> holder = ManagedHolder.of(obj);
        writeManagedField(op, holder, payload);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.IArrayLikeAccessor
    public IAccessor getChildAccessor() {
        return this.childAccessor;
    }
}
