package com.lowdragmc.lowdraglib.networking.s2c;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.lowdragmc.lowdraglib.networking.both.PacketIntLocation;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.accessor.IManagedAccessor;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/networking/s2c/SPacketManagedPayload.class */
public class SPacketManagedPayload extends PacketIntLocation implements IPacket {
    private CompoundTag extra;
    private BlockEntityType<?> blockEntityType;
    private BitSet changed;
    private ITypedPayload<?>[] payloads;

    public SPacketManagedPayload() {
    }

    public SPacketManagedPayload(BlockEntityType<?> type, BlockPos pos, BitSet changed, ITypedPayload<?>[] payloads, CompoundTag extra) {
        super(pos);
        this.blockEntityType = type;
        this.changed = changed;
        this.payloads = payloads;
        this.extra = extra;
    }

    public SPacketManagedPayload(CompoundTag tag) {
        super(BlockPos.m_122022_(tag.m_128454_("p")));
        this.blockEntityType = (BlockEntityType) Registry.f_122830_.m_7745_(new ResourceLocation(tag.m_128461_("t")));
        this.changed = BitSet.valueOf(tag.m_128463_("c"));
        ListTag list = tag.m_128437_("l", 10);
        this.payloads = new ITypedPayload[list.size()];
        for (int i = 0; i < this.payloads.length; i++) {
            CompoundTag payloadTag = list.m_128728_(i);
            byte id = payloadTag.m_128445_("t");
            ITypedPayload<?> payload = TypedPayloadRegistries.create(id);
            payload.deserializeNBT(payloadTag.m_128423_("d"));
            this.payloads[i] = payload;
        }
        this.extra = tag.m_128469_("e");
    }

    public CompoundTag serializeNBT() {
        ITypedPayload<?>[] iTypedPayloadArr;
        CompoundTag tag = new CompoundTag();
        tag.m_128356_("p", this.pos.m_121878_());
        tag.m_128359_("t", ((ResourceLocation) Objects.requireNonNull(Registry.f_122830_.m_7981_(this.blockEntityType))).toString());
        tag.m_128382_("c", this.changed.toByteArray());
        ListTag list = new ListTag();
        for (ITypedPayload<?> payload : this.payloads) {
            CompoundTag payloadTag = new CompoundTag();
            payloadTag.m_128344_("t", payload.getType());
            Tag data = payload.serializeNBT();
            if (data != null) {
                payloadTag.m_128365_("d", data);
            }
            list.add(payloadTag);
        }
        tag.m_128365_("l", list);
        tag.m_128365_("e", this.extra);
        return tag;
    }

    public static SPacketManagedPayload of(IAutoSyncBlockEntity tile, boolean force) {
        BitSet changed = new BitSet();
        Map<ManagedKey, ITypedPayload<?>> payloads = new LinkedHashMap<>();
        IRef[] syncedFields = tile.getRootStorage().getSyncFields();
        for (int i = 0; i < syncedFields.length; i++) {
            IRef field = syncedFields[i];
            if (force || field.isSyncDirty()) {
                changed.set(i);
                ManagedKey key = field.getKey();
                payloads.put(key, key.readSyncedField(field, force));
                field.clearSyncDirty();
            }
        }
        CompoundTag extra = new CompoundTag();
        tile.writeCustomSyncData(extra);
        return new SPacketManagedPayload(tile.getBlockEntityType(), tile.getCurrentPos(), changed, (ITypedPayload[]) payloads.values().toArray(new ITypedPayload[0]), extra);
    }

    public void processPacket(@NotNull IAutoSyncBlockEntity blockEntity) {
        if (blockEntity.getSelf().m_58903_() != this.blockEntityType) {
            LDLib.LOGGER.warn("Block entity type mismatch in managed payload packet!");
            return;
        }
        IManagedStorage storage = blockEntity.getRootStorage();
        IRef[] syncedFields = storage.getSyncFields();
        IManagedAccessor.writeSyncedFields(storage, syncedFields, this.changed, this.payloads);
        blockEntity.readCustomSyncData(this.extra);
    }

    @Override // com.lowdragmc.lowdraglib.networking.both.PacketIntLocation, com.lowdragmc.lowdraglib.networking.IPacket
    public void encode(FriendlyByteBuf buf) {
        ITypedPayload<?>[] iTypedPayloadArr;
        super.encode(buf);
        buf.m_130085_((ResourceLocation) Objects.requireNonNull(Registry.f_122830_.m_7981_(this.blockEntityType)));
        buf.m_130087_(this.changed.toByteArray());
        for (ITypedPayload<?> payload : this.payloads) {
            buf.writeByte(payload.getType());
            payload.writePayload(buf);
        }
        buf.m_130079_(this.extra);
    }

    @Override // com.lowdragmc.lowdraglib.networking.both.PacketIntLocation, com.lowdragmc.lowdraglib.networking.IPacket
    public void decode(FriendlyByteBuf buffer) {
        super.decode(buffer);
        this.blockEntityType = (BlockEntityType) Registry.f_122830_.m_7745_(buffer.m_130281_());
        this.changed = BitSet.valueOf(buffer.m_130052_());
        this.payloads = new ITypedPayload[this.changed.cardinality()];
        for (int i = 0; i < this.payloads.length; i++) {
            byte id = buffer.readByte();
            ITypedPayload<?> payload = TypedPayloadRegistries.create(id);
            payload.readPayload(buffer);
            this.payloads[i] = payload;
        }
        this.extra = buffer.m_130260_();
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        ClientLevel level;
        if (handler.isClient() && (level = Minecraft.m_91087_().f_91073_) != null) {
            BlockEntity m_7702_ = level.m_7702_(this.pos);
            if (m_7702_ instanceof IAutoSyncBlockEntity) {
                IAutoSyncBlockEntity autoSyncBlockEntity = (IAutoSyncBlockEntity) m_7702_;
                processPacket(autoSyncBlockEntity);
            }
        }
    }
}
