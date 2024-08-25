package com.lowdragmc.lowdraglib.syncdata.blockentity;

import com.google.common.base.Strings;
import com.lowdragmc.lowdraglib.syncdata.accessor.IManagedAccessor;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.utils.TagUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/blockentity/IAutoPersistBlockEntity.class */
public interface IAutoPersistBlockEntity extends IManagedBlockEntity {
    default void saveManagedPersistentData(CompoundTag tag, boolean forDrop) {
        IRef[] persistedFields = getRootStorage().getPersistedFields();
        for (IRef persistedField : persistedFields) {
            ManagedKey fieldKey = persistedField.getKey();
            if (!forDrop || fieldKey.isDrop()) {
                String key = fieldKey.getPersistentKey();
                if (Strings.isNullOrEmpty(key)) {
                    key = fieldKey.getName();
                }
                Tag nbt = fieldKey.readPersistedField(persistedField);
                if (nbt != null) {
                    TagUtils.setTagExtended(tag, key, nbt);
                }
            }
        }
        saveCustomPersistedData(tag, forDrop);
    }

    default void loadManagedPersistentData(CompoundTag tag) {
        IRef[] refs = getRootStorage().getPersistedFields();
        IManagedAccessor.writePersistedFields(tag, refs);
        loadCustomPersistedData(tag);
    }

    default void saveCustomPersistedData(CompoundTag tag, boolean forDrop) {
    }

    default void loadCustomPersistedData(CompoundTag tag) {
    }
}
