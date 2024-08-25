package com.lowdragmc.lowdraglib.misc;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedHolder;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.PrimitiveTypedPayload;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/SyncableMap.class */
public abstract class SyncableMap<K, V> implements Map<K, V>, IContentChangeAware, ITagSerializable<Tag> {
    private final IAccessor keyAccessor;
    private final IAccessor valueAccessor;
    private final Class<?> keyType;
    private final Class<?> valueType;
    private boolean stringKey;
    private final Map<K, V> map = new HashMap();
    private Runnable onContentsChanged = () -> {
    };

    public SyncableMap() {
        this.stringKey = false;
        Type parent = getClass().getGenericSuperclass();
        Type keyType = ((ParameterizedType) parent).getActualTypeArguments()[0];
        Type valueType = ((ParameterizedType) parent).getActualTypeArguments()[1];
        this.keyType = ReflectionUtils.getRawType(keyType, Object.class);
        this.valueType = ReflectionUtils.getRawType(valueType, Object.class);
        this.stringKey = keyType == String.class;
        this.keyAccessor = TypedPayloadRegistries.findByType(keyType);
        this.valueAccessor = TypedPayloadRegistries.findByType(valueType);
        if (this.keyAccessor == null || this.valueAccessor == null) {
            throw new RuntimeException("Cannot find accessor for key or value type");
        }
        if (!this.keyAccessor.isManaged()) {
            throw new RuntimeException("Key accessor is not managed");
        }
        if (!this.valueAccessor.isManaged()) {
            throw new RuntimeException("Value accessor is not managed");
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
    public void setOnContentsChanged(Runnable onContentsChanged) {
        this.onContentsChanged = onContentsChanged;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
    public Runnable getOnContentsChanged() {
        return this.onContentsChanged;
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override // java.util.Map
    public V get(Object key) {
        return this.map.get(key);
    }

    @Override // java.util.Map
    @Nullable
    public V put(K key, V value) {
        V result = this.map.put(key, value);
        this.onContentsChanged.run();
        return result;
    }

    @Override // java.util.Map
    public V remove(Object key) {
        V result = this.map.remove(key);
        this.onContentsChanged.run();
        return result;
    }

    @Override // java.util.Map
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
        this.onContentsChanged.run();
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
        this.onContentsChanged.run();
    }

    @Override // java.util.Map
    @NotNull
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    @NotNull
    public Collection<V> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    @NotNull
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    private static Tag readVal(ManagedAccessor accessor, Object val) {
        return val == null ? PrimitiveTypedPayload.ofNull().serializeNBT() : accessor.readManagedField(AccessorOp.PERSISTED, ManagedHolder.of(val)).serializeNBT();
    }

    private static Object writeVal(ManagedAccessor accessor, Tag val, Class<?> type) {
        if (val == null) {
            return null;
        }
        ManagedHolder<?> holder = ManagedHolder.ofType(type);
        ITypedPayload<?> payload = TypedPayloadRegistries.create(accessor.getDefaultType());
        payload.deserializeNBT(val);
        accessor.writeManagedField(AccessorOp.PERSISTED, holder, payload);
        return holder.value();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT */
    public Tag mo129serializeNBT() {
        if (this.stringKey) {
            CompoundTag tag = new CompoundTag();
            for (Map.Entry<K, V> entry : this.map.entrySet()) {
                Tag valueTag = readVal((ManagedAccessor) this.valueAccessor, entry.getValue());
                tag.m_128365_((String) entry.getKey(), valueTag);
            }
            return tag;
        }
        ListTag list = new ListTag();
        this.map.forEach(k, v -> {
            CompoundTag tag2 = new CompoundTag();
            Tag keyTag = readVal((ManagedAccessor) this.keyAccessor, list);
            Tag valueTag2 = readVal((ManagedAccessor) this.valueAccessor, v);
            tag2.m_128365_("k", keyTag);
            if (valueTag2 != null) {
                tag2.m_128365_("v", valueTag2);
            }
            list.add(tag2);
        });
        return list;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(Tag nbt) {
        this.map.clear();
        if (nbt instanceof CompoundTag) {
            CompoundTag tag = (CompoundTag) nbt;
            for (String key : tag.m_128431_()) {
                Tag valueTag = tag.m_128423_(key);
                Object value = writeVal((ManagedAccessor) this.valueAccessor, valueTag, this.valueType);
                this.map.put(key, value);
            }
            return;
        }
        ((ListTag) nbt).forEach(tag2 -> {
            CompoundTag compound = (CompoundTag) tag2;
            Tag keyTag = compound.m_128423_("k");
            Tag valueTag2 = compound.m_128423_("v");
            Object key2 = writeVal((ManagedAccessor) this.keyAccessor, keyTag, this.keyType);
            Object value2 = writeVal((ManagedAccessor) this.valueAccessor, valueTag2, this.valueType);
            this.map.put(key2, value2);
        });
    }
}
