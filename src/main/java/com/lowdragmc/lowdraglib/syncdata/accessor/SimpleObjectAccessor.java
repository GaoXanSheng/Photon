package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/SimpleObjectAccessor.class */
public abstract class SimpleObjectAccessor extends ManagedAccessor {
    private final Class<?>[] operandTypes;

    public abstract ObjectTypedPayload<?> createEmpty();

    public static <T> SimpleObjectAccessor create(@NotNull final Class<T> type, boolean inherited, final Supplier<? extends ObjectTypedPayload<T>> payloadSupplier) {
        if (inherited) {
            return new SimpleObjectAccessor(new Class[]{type}) { // from class: com.lowdragmc.lowdraglib.syncdata.accessor.SimpleObjectAccessor.1
                @Override // com.lowdragmc.lowdraglib.syncdata.accessor.SimpleObjectAccessor
                public ObjectTypedPayload<?> createEmpty() {
                    return (ObjectTypedPayload) payloadSupplier.get();
                }

                @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
                public boolean hasPredicate() {
                    return true;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
                public boolean test(Class<?> test) {
                    return type.isAssignableFrom(test);
                }
            };
        }
        return new SimpleObjectAccessor(new Class[]{type}) { // from class: com.lowdragmc.lowdraglib.syncdata.accessor.SimpleObjectAccessor.2
            @Override // com.lowdragmc.lowdraglib.syncdata.accessor.SimpleObjectAccessor
            public ObjectTypedPayload<?> createEmpty() {
                return (ObjectTypedPayload) payloadSupplier.get();
            }
        };
    }

    protected SimpleObjectAccessor(Class<?>... operandTypes) {
        this.operandTypes = operandTypes;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public Class<?>[] operandTypes() {
        return this.operandTypes;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
        Object value = field.value();
        if (value != null) {
            return createEmpty().setPayload(value);
        }
        return PrimitiveTypedPayload.ofNull();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
        if (payload instanceof ObjectTypedPayload) {
            ObjectTypedPayload<?> object = (ObjectTypedPayload) payload;
            field.set(object.getPayload());
        }
        if (payload instanceof PrimitiveTypedPayload) {
            PrimitiveTypedPayload<?> primitive = (PrimitiveTypedPayload) payload;
            if (primitive.isNull()) {
                field.set(null);
            }
        }
    }
}
