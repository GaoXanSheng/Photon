package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import com.lowdragmc.lowdraglib.syncdata.payload.EnumValuePayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.util.StringRepresentable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/EnumAccessor.class */
public class EnumAccessor extends ManagedAccessor {
    private static final WeakHashMap<Class<? extends Enum<?>>, Enum<?>[]> enumCache = new WeakHashMap<>();
    private static final WeakHashMap<Class<? extends Enum<?>>, Map<String, Enum<?>>> enumNameCache = new WeakHashMap<>();

    public static <T extends Enum<T>> T getEnum(Class<T> type, String name) {
        Map<String, Enum<?>> values = enumNameCache.computeIfAbsent(type, t -> {
            Enum<?>[] enumArr;
            WeakHashMap<String, Enum<?>> map = new WeakHashMap<>();
            for (Enum<?> value : (Enum[]) t.getEnumConstants()) {
                String enumName = getEnumName(value);
                map.put(enumName, value);
            }
            return map;
        });
        Enum<?> r0 = values.get(name);
        if (r0 == null) {
            return null;
        }
        return type.cast(r0);
    }

    public static String getEnumName(Enum<?> enumValue) {
        if (enumValue instanceof StringRepresentable) {
            StringRepresentable provider = (StringRepresentable) enumValue;
            return provider.m_7912_();
        }
        return enumValue.name();
    }

    public static <T extends Enum<T>> T getEnum(Class<T> type, int ordinal) {
        Enum<?>[] values = enumCache.computeIfAbsent(type, (v0) -> {
            return v0.getEnumConstants();
        });
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IllegalArgumentException("Invalid ordinal for enum type " + type.getName() + ": " + ordinal);
        }
        return type.cast(values[ordinal]);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean hasPredicate() {
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
    public boolean test(Class<?> type) {
        return type.isEnum();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
        if (!field.getType().isEnum()) {
            throw new IllegalArgumentException("Field is not an enum");
        }
        Object value = field.value();
        if (value != null) {
            Enum<? extends Enum<?>> enumVal = (Enum) value;
            String name = getEnumName(enumVal);
            int ordinal = enumVal.ordinal();
            return EnumValuePayload.of(name, ordinal);
        }
        return PrimitiveTypedPayload.ofNull();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
        Enum value;
        if (payload instanceof PrimitiveTypedPayload) {
            PrimitiveTypedPayload<?> primitive = (PrimitiveTypedPayload) payload;
            if (primitive.isNull()) {
                field.set(null);
                return;
            }
        }
        if (!field.getType().isEnum()) {
            throw new IllegalArgumentException("Field is not an enum");
        }
        if (!(payload instanceof EnumValuePayload)) {
            throw new IllegalArgumentException("Payload is not an enum value");
        }
        EnumValuePayload enumValue = (EnumValuePayload) payload;
        int ordinal = enumValue.getPayload().ordinal;
        String name = enumValue.getPayload().name;
        if (ordinal >= 0) {
            value = getEnum((Class<Enum>) field.getType(), ordinal);
        } else {
            value = getEnum((Class<Enum>) field.getType(), name);
        }
        if (value == null) {
            value = getEnum((Class<Enum>) field.getType(), 0);
        }
        if (value == null) {
            throw new IllegalArgumentException("Invalid enum value %s (%d) for field %s".formatted(new Object[]{name, Integer.valueOf(ordinal), field}));
        }
        field.set(value);
    }
}
