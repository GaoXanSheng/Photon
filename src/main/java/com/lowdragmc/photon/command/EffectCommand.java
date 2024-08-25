package com.lowdragmc.photon.command;

import com.lowdragmc.lowdraglib.networking.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/command/EffectCommand.class */
public abstract class EffectCommand implements IPacket {
    protected ResourceLocation location;
    protected Vec3 offset = Vec3.f_82478_;
    protected Vec3 rotation = Vec3.f_82478_;
    protected int delay;
    protected boolean forcedDeath;
    protected boolean allowMulti;

    public void setLocation(ResourceLocation location) {
        this.location = location;
    }

    public void setOffset(Vec3 offset) {
        this.offset = offset;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setForcedDeath(boolean forcedDeath) {
        this.forcedDeath = forcedDeath;
    }

    public void setAllowMulti(boolean allowMulti) {
        this.allowMulti = allowMulti;
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void encode(FriendlyByteBuf buf) {
        buf.m_130085_(this.location);
        buf.writeDouble(this.offset.f_82479_);
        buf.writeDouble(this.offset.f_82480_);
        buf.writeDouble(this.offset.f_82481_);
        buf.writeDouble(this.rotation.f_82479_);
        buf.writeDouble(this.rotation.f_82480_);
        buf.writeDouble(this.rotation.f_82481_);
        buf.m_130130_(this.delay);
        buf.writeBoolean(this.forcedDeath);
        buf.writeBoolean(this.allowMulti);
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    public void decode(FriendlyByteBuf buf) {
        this.location = buf.m_130281_();
        this.offset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.rotation = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.delay = buf.m_130242_();
        this.forcedDeath = buf.readBoolean();
        this.allowMulti = buf.readBoolean();
    }
}
