package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.accessor.SimpleObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ArrayPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.BlockPosPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.EnumValuePayload;
import com.lowdragmc.lowdraglib.syncdata.payload.FluidStackPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.FriendlyBufPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ItemStackPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.StringPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.UUIDPayload;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/TypedPayloadRegistries.class */
public class TypedPayloadRegistries {
    private static final Byte2ObjectMap<Supplier<? extends ITypedPayload<?>>> factories = new Byte2ObjectOpenHashMap();
    private static final Object2ByteMap<Class<?>> idMap = new Object2ByteOpenHashMap();
    private static final Map<Class<?>, IAccessor> accessorMap = new ConcurrentHashMap();
    private static List<Pair<Integer, IAccessor>> accessorSearchListUnsorted = new ArrayList();
    private static List<IAccessor> accessorSearchList = null;
    private static boolean loaded = false;
    private static byte currId = 0;

    private static byte nextId() {
        byte id = currId;
        currId = (byte) (id + 1);
        if (currId == Byte.MAX_VALUE) {
            throw new IllegalStateException("Too many typed payloads registered!");
        }
        return id;
    }

    private static byte registerPayload(Class<?> clazz, Supplier<? extends ITypedPayload<?>> factory) {
        if (loaded) {
            throw new IllegalStateException("Cannot register new typed payloads after loading!");
        }
        Objects.requireNonNull(factory);
        if (idMap.containsKey(clazz)) {
            return idMap.getByte(clazz);
        }
        byte id = nextId();
        factories.put(id, factory);
        idMap.put(clazz, id);
        return id;
    }

    public static <P, T extends ITypedPayload<P>> void register(Class<T> clazz, Supplier<T> factory, @Nullable IAccessor accessor) {
        register(clazz, factory, accessor, 10);
    }

    public static <T extends ITypedPayload<?>> void register(Class<T> clazz, Supplier<T> factory, @Nullable IAccessor accessor, int priority) {
        byte type = registerPayload(clazz, factory);
        if (accessor != null) {
            accessor.setDefaultType(type);
            Class<?>[] operandTypes = accessor.operandTypes();
            if (operandTypes != null) {
                for (Class<?> operandType : operandTypes) {
                    accessorMap.put(operandType, accessor);
                }
            }
            if (accessor.hasPredicate()) {
                accessorSearchListUnsorted.add(Pair.of(Integer.valueOf(priority), accessor));
            }
        }
        loaded = false;
    }

    public static <P, T extends ObjectTypedPayload<P>> void registerSimple(Class<T> clazz, Supplier<T> factory, Class<P> objType, int priority) {
        register(clazz, factory, SimpleObjectAccessor.create(objType, priority > 0, factory), priority);
    }

    public static byte getId(Class<?> clazz) {
        return idMap.getOrDefault(clazz, (byte) -1);
    }

    public static ITypedPayload<?> create(byte type) {
        if (!factories.containsKey(type)) {
            throw new IllegalArgumentException("Unknown payload type: " + type);
        }
        return (ITypedPayload) ((Supplier) factories.get(type)).get();
    }

    public static ITypedPayload<?> ofNull() {
        return PrimitiveTypedPayload.ofNull();
    }

    public static ITypedPayload<?> of(byte type) {
        return (ITypedPayload) ((Supplier) factories.get(type)).get();
    }

    public static IAccessor findByType(Type clazz) {
        if (clazz instanceof GenericArrayType) {
            GenericArrayType array = (GenericArrayType) clazz;
            Type componentType = array.getGenericComponentType();
            IAccessor childAccessor = findByType(componentType);
            Class<?> rawType = ReflectionUtils.getRawType(componentType);
            return SyncedFieldAccessors.arrayAccessor(childAccessor, rawType == null ? Object.class : rawType);
        }
        Class<?> rawType2 = ReflectionUtils.getRawType(clazz);
        if (rawType2 != null) {
            if (rawType2.isArray()) {
                Class<?> componentType2 = rawType2.getComponentType();
                IAccessor childAccessor2 = findByType(componentType2);
                return SyncedFieldAccessors.arrayAccessor(childAccessor2, componentType2);
            } else if (Collection.class.isAssignableFrom(rawType2)) {
                Type componentType3 = ((ParameterizedType) clazz).getActualTypeArguments()[0];
                IAccessor childAccessor3 = findByType(componentType3);
                Class<?> rawComponentType = ReflectionUtils.getRawType(componentType3);
                return SyncedFieldAccessors.collectionAccessor(childAccessor3, rawComponentType == null ? Object.class : rawComponentType);
            } else {
                return findByClass(rawType2);
            }
        }
        throw new IllegalArgumentException("No payload found for class " + clazz.getTypeName());
    }

    public static IAccessor findByClass(Class<?> clazz) {
        if (!loaded) {
            throw new IllegalStateException("Payload registries not loaded!");
        }
        IAccessor result = accessorMap.computeIfAbsent(clazz, c -> {
            for (IAccessor accessor : accessorSearchList) {
                if (accessor.test((Class<?>) c)) {
                    return accessor;
                }
            }
            return null;
        });
        if (result == null) {
            throw new IllegalArgumentException("No payload found for class " + clazz.getName());
        }
        if (result.hasPredicate() && !result.test(clazz)) {
            throw new IllegalStateException("Accessor " + result + " does not match class " + clazz);
        }
        return result;
    }

    public static void init() {
        PrimitiveTypedPayload.registerAll();
        registerPayload(ArrayPayload.class, ArrayPayload::new);
        registerPayload(FriendlyBufPayload.class, FriendlyBufPayload::new);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.TAG_SERIALIZABLE_ACCESSOR, 100);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.MANAGED_ACCESSOR, 100);
        register(EnumValuePayload.class, EnumValuePayload::new, SyncedFieldAccessors.ENUM_ACCESSOR, 1000);
        registerSimple(NbtTagPayload.class, NbtTagPayload::new, Tag.class, 99);
        registerSimple(BlockPosPayload.class, BlockPosPayload::new, BlockPos.class, 1);
        registerSimple(FluidStackPayload.class, FluidStackPayload::new, FluidStack.class, -1);
        registerSimple(StringPayload.class, StringPayload::new, String.class, -1);
        registerSimple(UUIDPayload.class, UUIDPayload::new, UUID.class, -1);
        registerSimple(ItemStackPayload.class, ItemStackPayload::new, ItemStack.class, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.BLOCK_STATE_ACCESSOR, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.RECIPE_ACCESSOR, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.POSITION_ACCESSOR, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.VECTOR3_ACCESSOR, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.RANGE_ACCESSOR, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.SIZE_ACCESSOR, -1);
        register(StringPayload.class, StringPayload::new, SyncedFieldAccessors.RESOURCE_LOCATION_ACCESSOR, -1);
        register(NbtTagPayload.class, NbtTagPayload::new, SyncedFieldAccessors.GUI_TEXTURE_ACCESSOR, 1000);
        register(StringPayload.class, StringPayload::new, SyncedFieldAccessors.COMPONENT_ACCESSOR, 1000);
    }

    public static void postInit() {
        if (loaded) {
            return;
        }
        accessorSearchList = accessorSearchListUnsorted.stream().sorted(Comparator.comparingInt((v0) -> {
            return v0.first();
        })).map((v0) -> {
            return v0.second();
        }).toList();
        accessorSearchListUnsorted = null;
        loaded = true;
    }
}
