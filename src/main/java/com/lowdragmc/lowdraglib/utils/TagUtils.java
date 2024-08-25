package com.lowdragmc.lowdraglib.utils;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/TagUtils.class */
public class TagUtils {
    public static CompoundTag getOrCreateTag(CompoundTag compoundTag, String key) {
        if (!compoundTag.m_128441_(key)) {
            compoundTag.m_128365_(key, new CompoundTag());
        }
        return compoundTag.m_128469_(key);
    }

    public static Tag getTagExtended(CompoundTag compoundTag, String key) {
        return getTagExtended(compoundTag, key, false);
    }

    public static Tag getTagExtended(CompoundTag compoundTag, String key, boolean create) {
        CompoundTag m_128469_;
        if (compoundTag == null) {
            if (create) {
                throw new NullPointerException("CompoundTag is null");
            }
            return null;
        }
        String[] keys = key.split("\\.");
        CompoundTag current = compoundTag;
        for (int i = 0; i < keys.length - 1; i++) {
            if (create) {
                m_128469_ = getOrCreateTag(current, keys[i]);
            } else if (!current.m_128441_(keys[i])) {
                return null;
            } else {
                m_128469_ = current.m_128469_(keys[i]);
            }
            current = m_128469_;
        }
        return current.m_128423_(keys[keys.length - 1]);
    }

    public static <T extends Tag> T getTagExtended(CompoundTag compoundTag, String key, T defaultValue) {
        T t = (T) getTagExtended(compoundTag, key, false);
        if (t == null) {
            return defaultValue;
        }
        return t;
    }

    public static void setTagExtended(CompoundTag compoundTag, String key, Tag tag) {
        String[] keys = key.split("\\.");
        CompoundTag current = compoundTag;
        for (int i = 0; i < keys.length - 1; i++) {
            current = getOrCreateTag(current, keys[i]);
        }
        current.m_128365_(keys[keys.length - 1], tag);
    }

    @Nullable
    public static <T extends Tag> T removeDuplicates(T tag, T demo) {
        if (tag.equals(demo)) {
            return null;
        }
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag1 = (CompoundTag) tag;
            if (demo instanceof CompoundTag) {
                CompoundTag compoundTag2 = (CompoundTag) demo;
                for (String key : compoundTag2.m_128431_()) {
                    if (!key.startsWith("_")) {
                        Tag tag2 = compoundTag2.m_128423_(key);
                        Tag tag1 = compoundTag1.m_128423_(key);
                        if (tag1 != null && tag2 != null) {
                            Tag cleanTag = removeDuplicates(tag1, tag2);
                            if (cleanTag != null) {
                                compoundTag1.m_128365_(key, cleanTag);
                            } else {
                                compoundTag1.m_128473_(key);
                            }
                        }
                    }
                }
            }
        }
        return tag;
    }
}
