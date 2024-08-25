package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem.class */
public class ManagedArrayItem<T> implements IManagedVar<T> {
    protected Object array;
    protected Class<T> type;
    protected int index;

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public T value() {
        return (T) ((Object[]) this.array)[this.index];
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public void set(T value) {
        ((Object[]) this.array)[this.index] = value;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public boolean isPrimitive() {
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public Class<T> getType() {
        return this.type;
    }

    public ManagedArrayItem(Object array, int index) {
        this.array = array;
        this.type = (Class<T>) array.getClass().getComponentType();
        this.index = index;
    }

    public static <T> ManagedArrayItem<T> of(Object array, int index) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Not an array");
        }
        Class<?> type = array.getClass().getComponentType();
        if (type == Integer.TYPE) {
            return new IntArrayItem(array, index);
        }
        if (type == Long.TYPE) {
            return new LongArrayItem(array, index);
        }
        if (type == Float.TYPE) {
            return new FloatArrayItem(array, index);
        }
        if (type == Double.TYPE) {
            return new DoubleArrayItem(array, index);
        }
        if (type == Boolean.TYPE) {
            return new BooleanArrayItem(array, index);
        }
        if (type == Byte.TYPE) {
            return new ByteArrayItem(array, index);
        }
        if (type == Character.TYPE) {
            return new CharArrayItem(array, index);
        }
        if (type == Short.TYPE) {
            return new ShortArrayItem(array, index);
        }
        return new ManagedArrayItem<>(array, index);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$IntArrayItem.class */
    static class IntArrayItem extends ManagedArrayItem<Integer> implements IManagedVar.Int {
        public IntArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Integer value() {
            return Integer.valueOf(intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Integer value) {
            setInt(value.intValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Int
        public void setInt(int value) {
            ((int[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Int
        public int intValue() {
            return ((int[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$LongArrayItem.class */
    static class LongArrayItem extends ManagedArrayItem<Long> implements IManagedVar.Long {
        public LongArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Long value() {
            return Long.valueOf(longValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Long value) {
            setLong(value.longValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Long
        public void setLong(long value) {
            ((long[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Long
        public long longValue() {
            return ((long[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$FloatArrayItem.class */
    static class FloatArrayItem extends ManagedArrayItem<Float> implements IManagedVar.Float {
        public FloatArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Float value() {
            return Float.valueOf(floatValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Float value) {
            setFloat(value.floatValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Float
        public void setFloat(float value) {
            ((float[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Float
        public float floatValue() {
            return ((float[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$ByteArrayItem.class */
    static class ByteArrayItem extends ManagedArrayItem<Byte> implements IManagedVar.Byte {
        public ByteArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Byte value() {
            return Byte.valueOf(byteValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Byte value) {
            setByte(value.byteValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Byte
        public void setByte(byte value) {
            ((byte[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Byte
        public byte byteValue() {
            return ((byte[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$DoubleArrayItem.class */
    static class DoubleArrayItem extends ManagedArrayItem<Double> implements IManagedVar.Double {
        public DoubleArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Double value() {
            return Double.valueOf(doubleValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Double value) {
            setDouble(value.doubleValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Double
        public void setDouble(double value) {
            ((double[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Double
        public double doubleValue() {
            return ((double[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$BooleanArrayItem.class */
    static class BooleanArrayItem extends ManagedArrayItem<Boolean> implements IManagedVar.Boolean {
        public BooleanArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Boolean value() {
            return Boolean.valueOf(booleanValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Boolean value) {
            setBoolean(value.booleanValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Boolean
        public void setBoolean(boolean value) {
            ((boolean[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Boolean
        public boolean booleanValue() {
            return ((boolean[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$ShortArrayItem.class */
    static class ShortArrayItem extends ManagedArrayItem<Short> implements IManagedVar.Short {
        public ShortArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Short value() {
            return Short.valueOf(shortValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Short value) {
            setShort(value.shortValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Short
        public void setShort(short value) {
            ((short[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Short
        public short shortValue() {
            return ((short[]) this.array)[this.index];
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedArrayItem$CharArrayItem.class */
    static class CharArrayItem extends ManagedArrayItem<Character> implements IManagedVar.Char {
        public CharArrayItem(Object array, int index) {
            super(array, index);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public Character value() {
            return Character.valueOf(charValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedArrayItem, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public void set(Character value) {
            setChar(value.charValue());
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Char
        public void setChar(char value) {
            ((char[]) this.array)[this.index] = value;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Char
        public char charValue() {
            return ((char[]) this.array)[this.index];
        }
    }
}
