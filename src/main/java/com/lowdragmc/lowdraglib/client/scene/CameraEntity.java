package com.lowdragmc.lowdraglib.client.scene;

import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/scene/CameraEntity.class */
public class CameraEntity extends Entity {
    public CameraEntity(Level pLevel) {
        super(EntityType.f_20532_, pLevel);
    }

    protected void m_8097_() {
    }

    protected void m_7378_(@Nonnull CompoundTag pCompound) {
    }

    protected void m_7380_(@Nonnull CompoundTag pCompound) {
    }

    @Nonnull
    public Packet<?> m_5654_() {
        return new ClientboundAddEntityPacket(this);
    }
}
