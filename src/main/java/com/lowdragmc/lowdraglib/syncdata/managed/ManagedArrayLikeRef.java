package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.SyncUtils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.lang.reflect.Array;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayLikeRef.class */
public class ManagedArrayLikeRef extends ManagedRef implements IArrayRef {
    private final IntSet dirty;
    protected Object oldValue;
    protected int oldLength;
    protected final boolean isArray;

    public ManagedArrayLikeRef(IManagedVar<?> field, boolean lazy) {
        super(field);
        this.dirty = new IntOpenHashSet();
        this.lazy = lazy;
        this.isArray = field.getType().isArray();
        if (!this.isArray && !Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException("Field %s is not an array or collection".formatted(new Object[]{field}));
        }
        Object value = getField().value();
        if (value != null) {
            Object array = SyncUtils.copyArrayLike(value, this.isArray);
            this.oldValue = array;
            this.oldLength = Array.getLength(array);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void update() {
        Object newValue = getField().value();
        if ((this.oldValue == null && newValue != null) || ((this.oldValue != null && newValue == null) || (this.oldValue != null && checkArrayLikeChanges(this.oldValue, newValue)))) {
            if (newValue != null) {
                Object array = SyncUtils.copyArrayLike(newValue, this.isArray);
                this.oldValue = array;
                this.oldLength = Array.getLength(array);
            } else {
                this.oldValue = null;
                this.oldLength = 0;
            }
            markAsDirty();
        }
    }

    protected boolean checkArrayLikeChanges(@NotNull Object oldValue, @NotNull Object newValue) {
        if (this.isArray) {
            if (Array.getLength(newValue) != this.oldLength) {
                markAsDirty();
                this.dirty.clear();
                return true;
            }
            Object[] a = (Object[]) oldValue;
            Object[] b = (Object[]) newValue;
            boolean dirty = false;
            for (int i = 0; i < a.length; i++) {
                if (SyncUtils.isChanged(a[i], b[i])) {
                    setChanged(i);
                    dirty = true;
                }
            }
            return dirty;
        } else if (newValue instanceof Collection) {
            Collection<?> collection = (Collection) newValue;
            if (collection.size() != this.oldLength) {
                markAsDirty();
                this.dirty.clear();
                return true;
            }
            Object[] array = (Object[]) oldValue;
            int i2 = 0;
            boolean dirty2 = false;
            for (Object item : collection) {
                Object oldItem = array[i2];
                if ((oldItem == null && item != null) || ((oldItem != null && item == null) || (oldItem != null && SyncUtils.isChanged(oldItem, item)))) {
                    setChanged(i2);
                    dirty2 = true;
                }
                i2++;
            }
            return dirty2;
        } else {
            throw new IllegalArgumentException("Value %s is not an array or collection".formatted(new Object[]{newValue}));
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
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
