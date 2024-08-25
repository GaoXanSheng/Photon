package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import java.util.function.Predicate;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/IAccessor.class */
public interface IAccessor extends Predicate<Class<?>> {
    ITypedPayload<?> readField(AccessorOp accessorOp, IRef iRef);

    void writeField(AccessorOp accessorOp, IRef iRef, ITypedPayload<?> iTypedPayload);

    boolean isManaged();

    void setDefaultType(byte b);

    byte getDefaultType();

    default boolean hasPredicate() {
        return false;
    }

    @Override // java.util.function.Predicate
    default boolean test(Class<?> type) {
        return false;
    }

    default Class<?>[] operandTypes() {
        return new Class[0];
    }

    default ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj) {
        throw new UnsupportedOperationException();
    }

    default void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload) {
        throw new UnsupportedOperationException();
    }

    default ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
        throw new UnsupportedOperationException();
    }

    default void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
        throw new UnsupportedOperationException();
    }
}
