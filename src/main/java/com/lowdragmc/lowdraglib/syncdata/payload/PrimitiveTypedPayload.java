package com.lowdragmc.lowdraglib.syncdata.payload;

import com.lowdragmc.lowdraglib.syncdata.SyncedFieldAccessors;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import javax.annotation.Nullable;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload.class */
public abstract class PrimitiveTypedPayload<T> implements ITypedPayload<T> {
    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    @Deprecated
    public abstract T getPayload();

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public byte getType() {
        return TypedPayloadRegistries.getId(getClass());
    }

    public int getAsInt() {
        if (this instanceof IntPayload) {
            return ((IntPayload) this).value;
        }
        if (this instanceof BytePayload) {
            return ((BytePayload) this).value;
        }
        if (this instanceof ShortPayload) {
            return ((ShortPayload) this).value;
        }
        if (this instanceof LongPayload) {
            return (int) ((LongPayload) this).value;
        }
        if (this instanceof FloatPayload) {
            return (int) ((FloatPayload) this).value;
        }
        if (this instanceof DoublePayload) {
            return (int) ((DoublePayload) this).value;
        }
        throw new IllegalStateException("Cannot get int value from " + getClass().getSimpleName());
    }

    public long getAsLong() {
        if (this instanceof LongPayload) {
            return ((LongPayload) this).value;
        }
        if (this instanceof IntPayload) {
            return ((IntPayload) this).value;
        }
        if (this instanceof BytePayload) {
            return ((BytePayload) this).value;
        }
        if (this instanceof ShortPayload) {
            return ((ShortPayload) this).value;
        }
        if (this instanceof FloatPayload) {
            return ((FloatPayload) this).value;
        }
        if (this instanceof DoublePayload) {
            return (long) ((DoublePayload) this).value;
        }
        throw new IllegalStateException("Cannot get long value from " + getClass().getSimpleName());
    }

    public float getAsFloat() {
        if (this instanceof FloatPayload) {
            return ((FloatPayload) this).value;
        }
        if (this instanceof IntPayload) {
            return ((IntPayload) this).value;
        }
        if (this instanceof BytePayload) {
            return ((BytePayload) this).value;
        }
        if (this instanceof ShortPayload) {
            return ((ShortPayload) this).value;
        }
        if (this instanceof LongPayload) {
            return (float) ((LongPayload) this).value;
        }
        if (this instanceof DoublePayload) {
            return (float) ((DoublePayload) this).value;
        }
        throw new IllegalStateException("Cannot get float value from " + getClass().getSimpleName());
    }

    public double getAsDouble() {
        if (this instanceof DoublePayload) {
            return ((DoublePayload) this).value;
        }
        if (this instanceof IntPayload) {
            return ((IntPayload) this).value;
        }
        if (this instanceof BytePayload) {
            return ((BytePayload) this).value;
        }
        if (this instanceof ShortPayload) {
            return ((ShortPayload) this).value;
        }
        if (this instanceof LongPayload) {
            return ((LongPayload) this).value;
        }
        if (this instanceof FloatPayload) {
            return ((FloatPayload) this).value;
        }
        throw new IllegalStateException("Cannot get double value from " + getClass().getSimpleName());
    }

    public byte getAsByte() {
        if (this instanceof BytePayload) {
            return ((BytePayload) this).value;
        }
        if (this instanceof IntPayload) {
            return (byte) ((IntPayload) this).value;
        }
        if (this instanceof ShortPayload) {
            return (byte) ((ShortPayload) this).value;
        }
        if (this instanceof LongPayload) {
            return (byte) ((LongPayload) this).value;
        }
        if (this instanceof FloatPayload) {
            return (byte) ((FloatPayload) this).value;
        }
        if (this instanceof DoublePayload) {
            return (byte) ((DoublePayload) this).value;
        }
        throw new IllegalStateException("Cannot get byte value from " + getClass().getSimpleName());
    }

    public short getAsShort() {
        if (this instanceof ShortPayload) {
            return ((ShortPayload) this).value;
        }
        if (this instanceof IntPayload) {
            return (short) ((IntPayload) this).value;
        }
        if (this instanceof BytePayload) {
            return ((BytePayload) this).value;
        }
        if (this instanceof LongPayload) {
            return (short) ((LongPayload) this).value;
        }
        if (this instanceof FloatPayload) {
            return (short) ((FloatPayload) this).value;
        }
        if (this instanceof DoublePayload) {
            return (short) ((DoublePayload) this).value;
        }
        throw new IllegalStateException("Cannot get short value from " + getClass().getSimpleName());
    }

    public boolean getAsBoolean() {
        if (this instanceof BooleanPayload) {
            return ((BooleanPayload) this).value;
        }
        throw new IllegalStateException("Cannot get boolean value from " + getClass().getSimpleName());
    }

    public char getAsChar() {
        if (this instanceof CharPayload) {
            return ((CharPayload) this).value;
        }
        throw new IllegalStateException("Cannot get char value from " + getClass().getSimpleName());
    }

    public boolean isNull() {
        return this instanceof NullPayload;
    }

    public static void registerAll() {
        TypedPayloadRegistries.register(NullPayload.class, () -> {
            return NullPayload.INSTANCE;
        }, null);
        TypedPayloadRegistries.register(BooleanPayload.class, BooleanPayload::new, SyncedFieldAccessors.BOOLEAN_ACCESSOR);
        TypedPayloadRegistries.register(BytePayload.class, BytePayload::new, SyncedFieldAccessors.BYTE_ACCESSOR);
        TypedPayloadRegistries.register(ShortPayload.class, ShortPayload::new, SyncedFieldAccessors.SHORT_ACCESSOR);
        TypedPayloadRegistries.register(IntPayload.class, IntPayload::new, SyncedFieldAccessors.INT_ACCESSOR);
        TypedPayloadRegistries.register(LongPayload.class, LongPayload::new, SyncedFieldAccessors.LONG_ACCESSOR);
        TypedPayloadRegistries.register(FloatPayload.class, FloatPayload::new, SyncedFieldAccessors.FLOAT_ACCESSOR);
        TypedPayloadRegistries.register(DoublePayload.class, DoublePayload::new, SyncedFieldAccessors.DOUBLE_ACCESSOR);
        TypedPayloadRegistries.register(CharPayload.class, CharPayload::new, SyncedFieldAccessors.CHAR_ACCESSOR);
    }

    public static PrimitiveTypedPayload<Integer> ofInt(int value) {
        IntPayload result = new IntPayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Long> ofLong(long value) {
        LongPayload result = new LongPayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Float> ofFloat(float value) {
        FloatPayload result = new FloatPayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Double> ofDouble(double value) {
        DoublePayload result = new DoublePayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Byte> ofByte(byte value) {
        BytePayload result = new BytePayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Short> ofShort(short value) {
        ShortPayload result = new ShortPayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Boolean> ofBoolean(boolean value) {
        BooleanPayload result = new BooleanPayload();
        result.value = value;
        return result;
    }

    public static PrimitiveTypedPayload<Character> ofChar(char value) {
        CharPayload result = new CharPayload();
        result.value = value;
        return result;
    }

    public static <T> PrimitiveTypedPayload<T> ofNull() {
        return NullPayload.INSTANCE;
    }

    @Nullable
    public static PrimitiveTypedPayload<?> tryOfBoxed(Object object) {
        if (object == null) {
            return ofNull();
        }
        if (object instanceof Integer) {
            Integer integer = (Integer) object;
            return ofInt(integer.intValue());
        } else if (object instanceof Long) {
            Long aLong = (Long) object;
            return ofLong(aLong.longValue());
        } else if (object instanceof Float) {
            Float aFloat = (Float) object;
            return ofFloat(aFloat.floatValue());
        } else if (object instanceof Double) {
            Double aDouble = (Double) object;
            return ofDouble(aDouble.doubleValue());
        } else if (object instanceof Byte) {
            Byte aByte = (Byte) object;
            return ofByte(aByte.byteValue());
        } else if (object instanceof Short) {
            Short aShort = (Short) object;
            return ofShort(aShort.shortValue());
        } else if (object instanceof Boolean) {
            Boolean aBoolean = (Boolean) object;
            return ofBoolean(aBoolean.booleanValue());
        } else if (object instanceof Character) {
            Character character = (Character) object;
            return ofChar(character.charValue());
        } else {
            return null;
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
    public boolean isPrimitive() {
        return true;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$NullPayload.class */
    public static class NullPayload extends PrimitiveTypedPayload<Object> {
        private static final NullPayload INSTANCE = new NullPayload();

        private NullPayload() {
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return null;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Object getPayload() {
            return null;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$IntPayload.class */
    public static class IntPayload extends PrimitiveTypedPayload<Integer> {
        private int value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.m_130130_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.m_130242_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return IntTag.m_128679_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((IntTag) tag).m_7047_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Integer getPayload() {
            return Integer.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$LongPayload.class */
    public static class LongPayload extends PrimitiveTypedPayload<Long> {
        private long value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.m_130103_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.m_130258_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return LongTag.m_128882_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((LongTag) tag).m_7046_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Long getPayload() {
            return Long.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$BooleanPayload.class */
    public static class BooleanPayload extends PrimitiveTypedPayload<Boolean> {
        private boolean value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.writeBoolean(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.readBoolean();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return ByteTag.m_128273_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((ByteTag) tag).m_7063_() != 0;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Boolean getPayload() {
            return Boolean.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$FloatPayload.class */
    public static class FloatPayload extends PrimitiveTypedPayload<Float> {
        private float value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.writeFloat(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.readFloat();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return FloatTag.m_128566_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((FloatTag) tag).m_7057_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Float getPayload() {
            return Float.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$DoublePayload.class */
    public static class DoublePayload extends PrimitiveTypedPayload<Double> {
        private double value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.writeDouble(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.readDouble();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return DoubleTag.m_128500_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((DoubleTag) tag).m_7061_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Double getPayload() {
            return Double.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$BytePayload.class */
    public static class BytePayload extends PrimitiveTypedPayload<Byte> {
        private byte value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.writeByte(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.readByte();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return ByteTag.m_128266_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((ByteTag) tag).m_7063_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Byte getPayload() {
            return Byte.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$ShortPayload.class */
    public static class ShortPayload extends PrimitiveTypedPayload<Short> {
        private short value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.writeShort(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.readShort();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return ShortTag.m_129258_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = ((ShortTag) tag).m_7053_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Short getPayload() {
            return Short.valueOf(this.value);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/payload/PrimitiveTypedPayload$CharPayload.class */
    public static class CharPayload extends PrimitiveTypedPayload<Character> {
        private char value;

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void writePayload(FriendlyByteBuf buf) {
            buf.writeChar(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void readPayload(FriendlyByteBuf buf) {
            this.value = buf.readChar();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Tag serializeNBT() {
            return IntTag.m_128679_(this.value);
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public void deserializeNBT(Tag tag) {
            this.value = (char) ((IntTag) tag).m_7047_();
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload, com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload
        public Character getPayload() {
            return Character.valueOf(this.value);
        }
    }
}
