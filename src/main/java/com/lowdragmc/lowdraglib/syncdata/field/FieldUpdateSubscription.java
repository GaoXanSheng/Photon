package com.lowdragmc.lowdraglib.syncdata.field;

import com.lowdragmc.lowdraglib.syncdata.IFieldUpdateListener;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/field/FieldUpdateSubscription.class */
public abstract class FieldUpdateSubscription implements ISubscription {
    @NotNull
    public final ManagedKey key;
    @NotNull
    public final IFieldUpdateListener<?> listener;

    @Override // com.lowdragmc.lowdraglib.syncdata.ISubscription
    public abstract void unsubscribe();

    public FieldUpdateSubscription(@NotNull ManagedKey key, @NotNull IFieldUpdateListener<?> listener) {
        this.key = key;
        this.listener = listener;
    }
}
