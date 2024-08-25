package com.lowdragmc.lowdraglib.syncdata.managed;

import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import java.lang.reflect.Field;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField.class */
public class ManagedField<T> implements IManagedVar<T> {
    protected Field field;
    protected Class<T> type;
    protected Object instance;

    public static <T> IManagedVar<T> of(Field field, Object instance) {
        Class<?> type = field.getType();
        if (type == Integer.TYPE) {
            return new Int(field, instance);
        }
        if (type == java.lang.Long.TYPE) {
            return new Long(field, instance);
        }
        if (type == java.lang.Float.TYPE) {
            return new Float(field, instance);
        }
        if (type == java.lang.Double.TYPE) {
            return new Double(field, instance);
        }
        if (type == java.lang.Boolean.TYPE) {
            return new Boolean(field, instance);
        }
        if (type == java.lang.Byte.TYPE) {
            return new Byte(field, instance);
        }
        if (type == Character.TYPE) {
            return new Char(field, instance);
        }
        if (type == java.lang.Short.TYPE) {
            return new Short(field, instance);
        }
        return new ManagedField(field, instance);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ManagedField(Field field, Object instance) {
        field.setAccessible(true);
        this.type = (Class<T>) field.getType();
        this.field = field;
        this.instance = instance;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public Class<T> getType() {
        return this.type;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public T value() {
        try {
            return (T) this.field.get(this.instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
    public void set(T value) {
        try {
            this.field.set(this.instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Int.class */
    static class Int extends ManagedField<Integer> implements IManagedVar.Int {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Int
        public /* bridge */ /* synthetic */ void set(Integer num) {
            super.set((Int) num);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ Integer value() {
            return (Integer) super.value();
        }

        private Int(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Int
        public int intValue() {
            try {
                return this.field.getInt(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Int
        public void setInt(int value) {
            try {
                this.field.setInt(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Long.class */
    static class Long extends ManagedField<java.lang.Long> implements IManagedVar.Long {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Long
        public /* bridge */ /* synthetic */ void set(java.lang.Long l) {
            super.set((Long) l);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ java.lang.Long value() {
            return (java.lang.Long) super.value();
        }

        private Long(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Long
        public long longValue() {
            try {
                return this.field.getLong(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Long
        public void setLong(long value) {
            try {
                this.field.setLong(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Float.class */
    static class Float extends ManagedField<java.lang.Float> implements IManagedVar.Float {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Float
        public /* bridge */ /* synthetic */ void set(java.lang.Float f) {
            super.set((Float) f);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ java.lang.Float value() {
            return (java.lang.Float) super.value();
        }

        private Float(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Float
        public float floatValue() {
            try {
                return this.field.getFloat(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Float
        public void setFloat(float value) {
            try {
                this.field.setFloat(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Byte.class */
    static class Byte extends ManagedField<java.lang.Byte> implements IManagedVar.Byte {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Byte
        public /* bridge */ /* synthetic */ void set(java.lang.Byte b) {
            super.set((Byte) b);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ java.lang.Byte value() {
            return (java.lang.Byte) super.value();
        }

        private Byte(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Byte
        public byte byteValue() {
            try {
                return this.field.getByte(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Byte
        public void setByte(byte value) {
            try {
                this.field.setByte(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Double.class */
    static class Double extends ManagedField<java.lang.Double> implements IManagedVar.Double {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Double
        public /* bridge */ /* synthetic */ void set(java.lang.Double d) {
            super.set((Double) d);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ java.lang.Double value() {
            return (java.lang.Double) super.value();
        }

        private Double(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Double
        public double doubleValue() {
            try {
                return this.field.getDouble(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Double
        public void setDouble(double value) {
            try {
                this.field.setDouble(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Boolean.class */
    static class Boolean extends ManagedField<java.lang.Boolean> implements IManagedVar.Boolean {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Boolean
        public /* bridge */ /* synthetic */ void set(java.lang.Boolean bool) {
            super.set((Boolean) bool);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ java.lang.Boolean value() {
            return (java.lang.Boolean) super.value();
        }

        private Boolean(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Boolean
        public boolean booleanValue() {
            try {
                return this.field.getBoolean(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Boolean
        public void setBoolean(boolean value) {
            try {
                this.field.setBoolean(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Short.class */
    static class Short extends ManagedField<java.lang.Short> implements IManagedVar.Short {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Short
        public /* bridge */ /* synthetic */ void set(java.lang.Short sh) {
            super.set((Short) sh);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ java.lang.Short value() {
            return (java.lang.Short) super.value();
        }

        private Short(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Short
        public short shortValue() {
            try {
                return this.field.getShort(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Short
        public void setShort(short value) {
            try {
                this.field.setShort(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/managed/ManagedField$Char.class */
    static class Char extends ManagedField<Character> implements IManagedVar.Char {
        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Char
        public /* bridge */ /* synthetic */ void set(Character ch) {
            super.set((Char) ch);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.ManagedField, com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar
        public /* bridge */ /* synthetic */ Character value() {
            return (Character) super.value();
        }

        private Char(Field field, Object instance) {
            super(field, instance);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Char
        public char charValue() {
            try {
                return this.field.getChar(this.instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar.Char
        public void setChar(char value) {
            try {
                this.field.setChar(this.instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
