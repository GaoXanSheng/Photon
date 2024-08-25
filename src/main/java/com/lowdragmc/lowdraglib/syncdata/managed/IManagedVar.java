package com.lowdragmc.lowdraglib.syncdata.managed;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar.class */
public interface IManagedVar<T> {
    T value();

    void set(T t);

    boolean isPrimitive();

    Class<T> getType();

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Int.class */
    public interface Int extends IManagedVar<Integer> {
        void setInt(int i);

        int intValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default Integer value() {
            return Integer.valueOf(intValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(Integer value) {
            setInt(value.intValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Long.class */
    public interface Long extends IManagedVar<java.lang.Long> {
        void setLong(long j);

        long longValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default java.lang.Long value() {
            return java.lang.Long.valueOf(longValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(java.lang.Long value) {
            setLong(value.longValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Float.class */
    public interface Float extends IManagedVar<java.lang.Float> {
        void setFloat(float f);

        float floatValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default java.lang.Float value() {
            return java.lang.Float.valueOf(floatValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(java.lang.Float value) {
            setFloat(value.floatValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Double.class */
    public interface Double extends IManagedVar<java.lang.Double> {
        void setDouble(double d);

        double doubleValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default java.lang.Double value() {
            return java.lang.Double.valueOf(doubleValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(java.lang.Double value) {
            setDouble(value.doubleValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Boolean.class */
    public interface Boolean extends IManagedVar<java.lang.Boolean> {
        void setBoolean(boolean z);

        boolean booleanValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default java.lang.Boolean value() {
            return java.lang.Boolean.valueOf(booleanValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(java.lang.Boolean value) {
            setBoolean(value.booleanValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Byte.class */
    public interface Byte extends IManagedVar<java.lang.Byte> {
        void setByte(byte b);

        byte byteValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default java.lang.Byte value() {
            return java.lang.Byte.valueOf(byteValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(java.lang.Byte value) {
            setByte(value.byteValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Short.class */
    public interface Short extends IManagedVar<java.lang.Short> {
        void setShort(short s);

        short shortValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default java.lang.Short value() {
            return java.lang.Short.valueOf(shortValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(java.lang.Short value) {
            setShort(value.shortValue());
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/IManagedVar$Char.class */
    public interface Char extends IManagedVar<Character> {
        void setChar(char c);

        char charValue();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default Character value() {
            return Character.valueOf(charValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        @Deprecated
        default void set(Character value) {
            setChar(value.charValue());
        }
    }
}
