package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Collection;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ReadonlyArrayRef.class */
public class ReadonlyArrayRef extends ReadonlyRef implements IArrayRef {
    private final IntSet dirty;

    public ReadonlyArrayRef(boolean isLazy, Object value) {
        super(isLazy, value);
        this.dirty = new IntOpenHashSet();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ReadonlyRef
    protected void init() {
        IContentChangeAware[] iContentChangeAwareArr;
        Object value = readRaw();
        if ((value instanceof IContentChangeAware) || (value instanceof IManaged)) {
            super.init();
        } else if (!isLazy()) {
            Class<?> type = value.getClass();
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (IManaged.class.isAssignableFrom(componentType)) {
                    return;
                }
                if (!IContentChangeAware.class.isAssignableFrom(componentType)) {
                    throw new IllegalArgumentException("complex sync field must be an IContentChangeAware if not lazy!");
                }
                for (IContentChangeAware handler : (IContentChangeAware[]) value) {
                    replaceHandler(handler);
                }
            } else if (value instanceof Collection) {
                Collection<?> collection = (Collection) value;
                for (Object item : collection) {
                    if (item instanceof IContentChangeAware) {
                        IContentChangeAware handler2 = (IContentChangeAware) item;
                        replaceHandler(handler2);
                    } else if (!(item instanceof IManaged)) {
                        throw new IllegalArgumentException("complex sync field must be an IContentChangeAware if not lazy!");
                    }
                }
            } else {
                throw new IllegalArgumentException("Field must be an array or collection");
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ReadonlyRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void update() {
        IRef[] nonLazyFields;
        IRef[] nonLazyFields2;
        super.update();
        Object value = readRaw();
        Class<?> type = value.getClass();
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            if (IManaged.class.isAssignableFrom(componentType)) {
                IManaged[] values = (IManaged[]) value;
                for (int i = 0; i < values.length; i++) {
                    IManaged managed = values[i];
                    for (IRef field : managed.getSyncStorage().getNonLazyFields()) {
                        field.update();
                    }
                    if (managed.getSyncStorage().hasDirtySyncFields() || managed.getSyncStorage().hasDirtyPersistedFields()) {
                        setChanged(i);
                    }
                }
            }
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection) value;
            int i2 = 0;
            for (Object item : collection) {
                if (item instanceof IManaged) {
                    IManaged managed2 = (IManaged) item;
                    for (IRef field2 : managed2.getSyncStorage().getNonLazyFields()) {
                        field2.update();
                    }
                    if (managed2.getSyncStorage().hasDirtySyncFields() || managed2.getSyncStorage().hasDirtyPersistedFields()) {
                        setChanged(i2);
                    }
                }
                i2++;
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ReadonlyRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void markAsDirty() {
        super.markAsDirty();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IArrayRef
    public void setChanged(int index) {
        markAsDirty();
        this.dirty.add(index);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IArrayRef
    public IntSet getChanged() {
        return this.dirty;
    }
}
