package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedHolder;
import com.lowdragmc.lowdraglib.syncdata.payload.ArrayPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/CollectionAccessor.class */
public class CollectionAccessor extends ReadonlyAccessor implements IArrayLikeAccessor {
    private final IAccessor childAccessor;
    private final Class<?> childType;

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public byte getDefaultType() {
        return TypedPayloadRegistries.getId(ArrayPayload.class);
    }

    public CollectionAccessor(IAccessor childAccessor, Class<?> childType) {
        this.childAccessor = childAccessor;
        this.childType = childType;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean hasPredicate() {
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
    public boolean test(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj) {
        if (!(obj instanceof Collection)) {
            throw new IllegalArgumentException("Field %s is not Collection".formatted(new Object[]{obj}));
        }
        Collection<?> collection = (Collection) obj;
        Iterator<?> iter = collection.iterator();
        int size = collection.size();
        ITypedPayload[] result = new ITypedPayload[size];
        if (!this.childAccessor.isManaged()) {
            throw new IllegalArgumentException("Child accessor %s is not managed".formatted(new Object[]{this.childAccessor}));
        }
        for (int i = 0; i < size; i++) {
            Object element = iter.next();
            ManagedHolder<Object> holder = ManagedHolder.of(element);
            ITypedPayload<?> payload = this.childAccessor.readManagedField(op, holder);
            result[i] = payload;
        }
        return ArrayPayload.of(result);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload) {
        if (!(obj instanceof Collection)) {
            throw new IllegalArgumentException("Field %s is not Collection".formatted(new Object[]{obj}));
        }
        if (!(payload instanceof ArrayPayload)) {
            throw new IllegalArgumentException("Payload %s is not ArrayPayload".formatted(new Object[]{payload}));
        }
        ArrayPayload arrayPayload = (ArrayPayload) payload;
        Collection<Object> collection = (Collection) obj;
        ITypedPayload<?>[] array = arrayPayload.getPayload();
        if (!this.childAccessor.isManaged()) {
            throw new IllegalArgumentException("Child accessor %s is not managed".formatted(new Object[]{this.childAccessor}));
        }
        collection.clear();
        for (ITypedPayload<?> element : array) {
            ManagedHolder<?> holder = ManagedHolder.ofType(this.childType);
            this.childAccessor.writeManagedField(op, holder, element);
            collection.add(holder.value());
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.IArrayLikeAccessor
    public IAccessor getChildAccessor() {
        return this.childAccessor;
    }
}
