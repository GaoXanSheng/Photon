package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.google.common.base.Strings;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.lowdraglib.utils.TagUtils;
import java.util.BitSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/IManagedAccessor.class */
public class IManagedAccessor extends ReadonlyAccessor {
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor
    public boolean hasPredicate() {
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.IAccessor, java.util.function.Predicate
    public boolean test(Class<?> type) {
        return IManaged.class.isAssignableFrom(type);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj) {
        CompoundTag tag;
        if (!(obj instanceof IManaged)) {
            throw new IllegalArgumentException("Field %s is not ITagSerializable".formatted(new Object[]{obj}));
        }
        IManaged managed = (IManaged) obj;
        if (op == AccessorOp.SYNCED || op == AccessorOp.FORCE_SYNCED) {
            tag = readSyncedFields(managed, new CompoundTag(), op == AccessorOp.FORCE_SYNCED);
        } else {
            tag = readManagedFields(managed, new CompoundTag());
        }
        return new NbtTagPayload().setPayload(tag);
    }

    public static CompoundTag readSyncedFields(IManaged managed, CompoundTag tag, boolean force) {
        BitSet changed = new BitSet();
        IRef[] syncedFields = managed.getSyncStorage().getSyncFields();
        ListTag list = new ListTag();
        for (int i = 0; i < syncedFields.length; i++) {
            IRef field = syncedFields[i];
            if (force || field.isSyncDirty()) {
                changed.set(i);
                ManagedKey key = field.getKey();
                ITypedPayload<?> payload = key.readSyncedField(field, force);
                CompoundTag payloadTag = new CompoundTag();
                payloadTag.m_128344_("t", payload.getType());
                Tag data = payload.serializeNBT();
                if (data != null) {
                    payloadTag.m_128365_("d", data);
                }
                list.add(payloadTag);
                field.clearSyncDirty();
            }
        }
        tag.m_128382_("c", changed.toByteArray());
        tag.m_128365_("l", list);
        return tag;
    }

    public static CompoundTag readManagedFields(IManaged managed, CompoundTag tag) {
        IRef[] persistedFields = managed.getSyncStorage().getPersistedFields();
        for (IRef persistedField : persistedFields) {
            ManagedKey fieldKey = persistedField.getKey();
            String key = fieldKey.getPersistentKey();
            if (Strings.isNullOrEmpty(key)) {
                key = fieldKey.getName();
            }
            Tag nbt = fieldKey.readPersistedField(persistedField);
            if (nbt != null) {
                TagUtils.setTagExtended(tag, key, nbt);
            }
        }
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.ReadonlyAccessor, com.lowdragmc.lowdraglib.syncdata.IAccessor
    public void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload) {
        if (!(obj instanceof IManaged)) {
            throw new IllegalArgumentException("Field %s is not ITagSerializable".formatted(new Object[]{obj}));
        }
        IManaged managed = (IManaged) obj;
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtPayload = (NbtTagPayload) payload;
            CompoundTag payload2 = nbtPayload.getPayload();
            if (payload2 instanceof CompoundTag) {
                CompoundTag tag = payload2;
                if (op != AccessorOp.SYNCED && op != AccessorOp.FORCE_SYNCED) {
                    if (op == AccessorOp.PERSISTED) {
                        IRef[] refs = managed.getSyncStorage().getPersistedFields();
                        writePersistedFields(tag, refs);
                        return;
                    }
                    throw new IllegalArgumentException("Payload %s does not match op %s".formatted(new Object[]{payload, op}));
                }
                IManagedStorage storage = managed.getSyncStorage();
                IRef[] syncedFields = storage.getSyncFields();
                BitSet changed = BitSet.valueOf(tag.m_128463_("c"));
                ListTag list = tag.m_128437_("l", 10);
                ITypedPayload<?>[] payloads = new ITypedPayload[list.size()];
                for (int i = 0; i < payloads.length; i++) {
                    CompoundTag payloadTag = list.m_128728_(i);
                    byte id = payloadTag.m_128445_("t");
                    ITypedPayload<?> p = TypedPayloadRegistries.create(id);
                    p.deserializeNBT(payloadTag.m_128423_("d"));
                    payloads[i] = p;
                }
                writeSyncedFields(storage, syncedFields, changed, payloads);
                return;
            }
        }
        throw new IllegalArgumentException("Payload %s is not NbtTagPayload".formatted(new Object[]{payload}));
    }

    public static void writePersistedFields(CompoundTag tag, IRef[] refs) {
        for (IRef ref : refs) {
            ManagedKey fieldKey = ref.getKey();
            String key = fieldKey.getPersistentKey();
            if (Strings.isNullOrEmpty(key)) {
                key = fieldKey.getName();
            }
            Tag nbt = TagUtils.getTagExtended(tag, key);
            if (nbt != null) {
                fieldKey.writePersistedField(ref, nbt);
            }
        }
    }

    public static void writeSyncedFields(IManagedStorage storage, IRef[] syncedFields, BitSet changed, ITypedPayload<?>[] payloads) {
        int j = 0;
        for (int i = 0; i < changed.length(); i++) {
            if (changed.get(i)) {
                IRef field = syncedFields[i];
                ManagedKey key = field.getKey();
                boolean hasListener = storage.hasSyncListener(key);
                Object oldValue = null;
                if (hasListener) {
                    oldValue = field.readRaw();
                }
                key.writeSyncedField(field, payloads[j]);
                if (hasListener) {
                    storage.notifyFieldUpdate(key, field.readRaw(), oldValue);
                }
                j++;
            }
        }
    }
}
