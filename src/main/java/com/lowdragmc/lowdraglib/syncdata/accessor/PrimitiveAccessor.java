package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import java.util.Objects;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor.class */
public abstract class PrimitiveAccessor extends ManagedAccessor {
    private final Class<?>[] operandTypes;

    protected PrimitiveAccessor(Class<?>... operandTypes) {
        this.operandTypes = operandTypes;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public Class<?>[] operandTypes() {
        return this.operandTypes;
    }

    protected PrimitiveTypedPayload<?> ensurePrimitive(ITypedPayload<?> payload) {
        if (!(payload instanceof PrimitiveTypedPayload)) {
            throw new IllegalArgumentException("Payload %s is not a primitive payload".formatted(new Object[]{payload}));
        }
        PrimitiveTypedPayload<?> primitivePayload = (PrimitiveTypedPayload) payload;
        return primitivePayload;
    }

    protected <T> IManagedVar<T> ensureType(IManagedVar<?> field, Class<T> clazz) {
        if (!clazz.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException("Field %s is not of type %s".formatted(new Object[]{field, clazz}));
        }
        return field;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$IntAccessor.class */
    public static class IntAccessor extends PrimitiveAccessor {
        public IntAccessor() {
            super(Integer.TYPE, Integer.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Int) {
                IManagedVar.Int intField = (IManagedVar.Int) field;
                return PrimitiveTypedPayload.ofInt(intField.intValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not an int field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Int) {
                IManagedVar.Int intField = (IManagedVar.Int) field;
                intField.setInt(primitivePayload.getAsInt());
                return;
            }
            ensureType(field, Integer.class).set(Integer.valueOf(primitivePayload.getAsInt()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$LongAccessor.class */
    public static class LongAccessor extends PrimitiveAccessor {
        public LongAccessor() {
            super(Long.TYPE, Long.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Long) {
                IManagedVar.Long longField = (IManagedVar.Long) field;
                return PrimitiveTypedPayload.ofLong(longField.longValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a long field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Long) {
                IManagedVar.Long longField = (IManagedVar.Long) field;
                longField.setLong(primitivePayload.getAsLong());
                return;
            }
            ensureType(field, Long.class).set(Long.valueOf(primitivePayload.getAsLong()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$FloatAccessor.class */
    public static class FloatAccessor extends PrimitiveAccessor {
        public FloatAccessor() {
            super(Float.TYPE, Float.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Float) {
                IManagedVar.Float floatField = (IManagedVar.Float) field;
                return PrimitiveTypedPayload.ofFloat(floatField.floatValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a float field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Float) {
                IManagedVar.Float floatField = (IManagedVar.Float) field;
                floatField.setFloat(primitivePayload.getAsFloat());
                return;
            }
            ensureType(field, Float.class).set(Float.valueOf(primitivePayload.getAsFloat()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$DoubleAccessor.class */
    public static class DoubleAccessor extends PrimitiveAccessor {
        public DoubleAccessor() {
            super(Double.TYPE, Double.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Double) {
                IManagedVar.Double doubleField = (IManagedVar.Double) field;
                return PrimitiveTypedPayload.ofDouble(doubleField.doubleValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a double field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Double) {
                IManagedVar.Double doubleField = (IManagedVar.Double) field;
                doubleField.setDouble(primitivePayload.getAsDouble());
                return;
            }
            ensureType(field, Double.class).set(Double.valueOf(primitivePayload.getAsDouble()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$BooleanAccessor.class */
    public static class BooleanAccessor extends PrimitiveAccessor {
        public BooleanAccessor() {
            super(Boolean.TYPE, Boolean.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Boolean) {
                IManagedVar.Boolean booleanField = (IManagedVar.Boolean) field;
                return PrimitiveTypedPayload.ofBoolean(booleanField.booleanValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a boolean field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Boolean) {
                IManagedVar.Boolean booleanField = (IManagedVar.Boolean) field;
                booleanField.setBoolean(primitivePayload.getAsBoolean());
                return;
            }
            ensureType(field, Boolean.class).set(Boolean.valueOf(primitivePayload.getAsBoolean()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$ByteAccessor.class */
    public static class ByteAccessor extends PrimitiveAccessor {
        public ByteAccessor() {
            super(Byte.TYPE, Byte.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Byte) {
                IManagedVar.Byte byteField = (IManagedVar.Byte) field;
                return PrimitiveTypedPayload.ofByte(byteField.byteValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a byte field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Byte) {
                IManagedVar.Byte byteField = (IManagedVar.Byte) field;
                byteField.setByte(primitivePayload.getAsByte());
                return;
            }
            ensureType(field, Byte.class).set(Byte.valueOf(primitivePayload.getAsByte()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$CharAccessor.class */
    public static class CharAccessor extends PrimitiveAccessor {
        public CharAccessor() {
            super(Character.TYPE, Character.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Char) {
                IManagedVar.Char charField = (IManagedVar.Char) field;
                return PrimitiveTypedPayload.ofChar(charField.charValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a char field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Char) {
                IManagedVar.Char charField = (IManagedVar.Char) field;
                charField.setChar(primitivePayload.getAsChar());
                return;
            }
            ensureType(field, Character.class).set(Character.valueOf(primitivePayload.getAsChar()));
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/PrimitiveAccessor$ShortAccessor.class */
    public static class ShortAccessor extends PrimitiveAccessor {
        public ShortAccessor() {
            super(Short.TYPE, Short.class);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
            if (field instanceof IManagedVar.Short) {
                IManagedVar.Short shortField = (IManagedVar.Short) field;
                return PrimitiveTypedPayload.ofShort(shortField.shortValue());
            }
            PrimitiveTypedPayload<?> result = PrimitiveTypedPayload.tryOfBoxed(field.value());
            return (ITypedPayload) Objects.requireNonNull(result, "Field %s is not a short field".formatted(new Object[]{field}));
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
        public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
            PrimitiveTypedPayload<?> primitivePayload = ensurePrimitive(payload);
            if (field instanceof IManagedVar.Short) {
                IManagedVar.Short shortField = (IManagedVar.Short) field;
                shortField.setShort(primitivePayload.getAsShort());
                return;
            }
            ensureType(field, Short.class).set(Short.valueOf(primitivePayload.getAsShort()));
        }
    }
}
