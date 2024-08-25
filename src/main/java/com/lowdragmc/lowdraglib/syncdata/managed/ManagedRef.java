package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef.class */
public class ManagedRef implements IRef {
    protected final IManagedVar<?> field;
    protected boolean isSyncDirty;
    protected boolean isPersistedDirty;
    protected ManagedKey key;
    protected boolean lazy = false;
    protected BooleanConsumer onSyncListener = changed -> {
    };
    protected BooleanConsumer onPersistedListener = changed -> {
    };

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public boolean isSyncDirty() {
        return this.isSyncDirty;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public boolean isPersistedDirty() {
        return this.isPersistedDirty;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void setOnSyncListener(BooleanConsumer onSyncListener) {
        this.onSyncListener = onSyncListener;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void setOnPersistedListener(BooleanConsumer onPersistedListener) {
        this.onPersistedListener = onPersistedListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ManagedRef(IManagedVar<?> field) {
        this.field = field;
    }

    public static ManagedRef create(IManagedVar<?> field, boolean lazy) {
        if (field instanceof IManagedVar.Int) {
            return new IntRef((IManagedVar.Int) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Long) {
            return new LongRef((IManagedVar.Long) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Float) {
            return new FloatRef((IManagedVar.Float) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Double) {
            return new DoubleRef((IManagedVar.Double) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Boolean) {
            return new BooleanRef((IManagedVar.Boolean) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Byte) {
            return new ByteRef((IManagedVar.Byte) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Short) {
            return new ShortRef((IManagedVar.Short) field).setLazy(lazy);
        }
        if (field instanceof IManagedVar.Char) {
            return new CharRef((IManagedVar.Char) field).setLazy(lazy);
        }
        if (field instanceof ReadOnlyManagedField) {
            return new ReadOnlyManagedRef((ReadOnlyManagedField) field).setLazy(lazy);
        }
        return new SimpleObjectRef(field).setLazy(lazy);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public ManagedKey getKey() {
        return this.key;
    }

    public IRef setKey(ManagedKey key) {
        this.key = key;
        return this;
    }

    public <T extends IManagedVar<?>> T getField() {
        return (T) this.field;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void clearSyncDirty() {
        this.isSyncDirty = false;
        if (this.key.isDestSync()) {
            this.onSyncListener.accept(false);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void clearPersistedDirty() {
        this.isPersistedDirty = false;
        if (this.key.isPersist()) {
            this.onPersistedListener.accept(false);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void markAsDirty() {
        if (this.key.isDestSync()) {
            this.isSyncDirty = true;
            this.onSyncListener.accept(true);
        }
        if (this.key.isPersist()) {
            this.isPersistedDirty = true;
            this.onPersistedListener.accept(true);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public boolean isLazy() {
        return this.lazy;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public <T> T readRaw() {
        return (T) getField().value();
    }

    protected ManagedRef setLazy(boolean lazy) {
        this.lazy = lazy;
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IRef
    public void update() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$IntRef.class */
    public static class IntRef extends ManagedRef {
        private int oldValue;

        IntRef(IManagedVar.Int field) {
            super(field);
            this.oldValue = ((IManagedVar.Int) getField()).intValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            int newValue = ((IManagedVar.Int) getField()).intValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$LongRef.class */
    public static class LongRef extends ManagedRef {
        private long oldValue;

        LongRef(IManagedVar.Long field) {
            super(field);
            this.oldValue = ((IManagedVar.Long) getField()).longValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            long newValue = ((IManagedVar.Long) getField()).longValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$FloatRef.class */
    public static class FloatRef extends ManagedRef {
        private float oldValue;

        FloatRef(IManagedVar.Float field) {
            super(field);
            this.oldValue = ((IManagedVar.Float) getField()).floatValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            float newValue = ((IManagedVar.Float) getField()).floatValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$DoubleRef.class */
    public static class DoubleRef extends ManagedRef {
        private double oldValue;

        DoubleRef(IManagedVar.Double field) {
            super(field);
            this.oldValue = ((IManagedVar.Double) getField()).doubleValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            double newValue = ((IManagedVar.Double) getField()).doubleValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$BooleanRef.class */
    public static class BooleanRef extends ManagedRef {
        private boolean oldValue;

        BooleanRef(IManagedVar.Boolean field) {
            super(field);
            this.oldValue = ((IManagedVar.Boolean) getField()).booleanValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            boolean newValue = ((IManagedVar.Boolean) getField()).booleanValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$ByteRef.class */
    public static class ByteRef extends ManagedRef {
        private byte oldValue;

        ByteRef(IManagedVar.Byte field) {
            super(field);
            this.oldValue = ((IManagedVar.Byte) getField()).byteValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            byte newValue = ((IManagedVar.Byte) getField()).byteValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$ShortRef.class */
    public static class ShortRef extends ManagedRef {
        private short oldValue;

        ShortRef(IManagedVar.Short field) {
            super(field);
            this.oldValue = ((IManagedVar.Short) getField()).shortValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            short newValue = ((IManagedVar.Short) getField()).shortValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedRef$CharRef.class */
    public static class CharRef extends ManagedRef {
        private char oldValue;

        CharRef(IManagedVar.Char field) {
            super(field);
            this.oldValue = ((IManagedVar.Char) getField()).charValue();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedRef, com.lowdragmc.lowdraglib.syncdata.managed.IRef
        public void update() {
            char newValue = ((IManagedVar.Char) getField()).charValue();
            if (this.oldValue != newValue) {
                this.oldValue = newValue;
                markAsDirty();
            }
        }
    }
}
