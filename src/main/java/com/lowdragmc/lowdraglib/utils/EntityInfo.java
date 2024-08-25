package com.lowdragmc.lowdraglib.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/EntityInfo.class */
public class EntityInfo {
    private int id;
    @Nullable
    private CompoundTag tag;
    @Nullable
    private EntityType<?> entityType;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTag(@Nullable CompoundTag tag) {
        this.tag = tag;
    }

    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }

    public void setEntityType(@Nullable EntityType<?> entityType) {
        this.entityType = entityType;
    }

    @Nullable
    public EntityType<?> getEntityType() {
        return this.entityType;
    }

    public EntityInfo(int id) {
        this.id = id;
    }

    public EntityInfo(int id, @Nullable EntityType<?> entityType) {
        this.id = id;
        this.entityType = entityType;
    }

    public EntityInfo(int id, @Nullable EntityType<?> entityType, @Nullable CompoundTag tag) {
        this.id = id;
        this.entityType = entityType;
        this.tag = tag;
    }
}
