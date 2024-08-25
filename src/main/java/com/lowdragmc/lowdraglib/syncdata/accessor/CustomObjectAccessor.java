package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import java.util.Objects;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/CustomObjectAccessor.class */
public abstract class CustomObjectAccessor<T> extends ManagedAccessor {
    private final Class<T> type;
    private final boolean includesChildren;
    private final Class<?>[] operandTypes;

    public abstract ITypedPayload<?> serialize(AccessorOp accessorOp, T t);

    public abstract T deserialize(AccessorOp accessorOp, ITypedPayload<?> iTypedPayload);

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public CustomObjectAccessor(Class<T> type, boolean includesChildren) {
        this.type = type;
        this.includesChildren = includesChildren;
        this.operandTypes = new Class[]{type};
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean hasPredicate() {
        return this.includesChildren;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
    public boolean test(Class<?> type) {
        return this.type.isAssignableFrom(type);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public Class<?>[] operandTypes() {
        return this.operandTypes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
        Object value = field.value();
        if (value != null) {
            if (!this.type.isAssignableFrom(value.getClass())) {
                throw new IllegalArgumentException("Value %s is not assignable to type %s".formatted(new Object[]{value, this.type}));
            }
            return serialize(op, value);
        }
        return PrimitiveTypedPayload.ofNull();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
        if (payload instanceof PrimitiveTypedPayload) {
            PrimitiveTypedPayload<?> primitive = (PrimitiveTypedPayload) payload;
            if (primitive.isNull()) {
                field.set(null);
                return;
            }
        }
        T result = deserialize(op, payload);
        Objects.requireNonNull(result, "Payload %s is not a valid payload for type %s".formatted(new Object[]{payload, this.type}));
        field.set(result);
    }
}
