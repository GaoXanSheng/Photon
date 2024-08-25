package com.lowdragmc.lowdraglib.networking.both;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IRPCBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.RPCMethodMeta;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.rpc.RPCSender;
import java.util.Arrays;
import java.util.Objects;

import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

/**
 * a packet that contains payload for managed fields
 */
@NoArgsConstructor
public class PacketRPCMethodPayload extends PacketIntLocation implements IPacket {
    private BlockEntityType<?> blockEntityType;
    private ITypedPayload<?>[] payloads;
    private String methodName;
    private int managedId;

    public PacketRPCMethodPayload(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public PacketRPCMethodPayload(int managedId, BlockEntityType<?> type, BlockPos pos, String methodName, ITypedPayload<?>[] payloads) {
        super(pos);
        this.managedId = managedId;
        this.blockEntityType = type;
        this.methodName = methodName;
        this.payloads = payloads;
    }

    public static PacketRPCMethodPayload of(IManaged managed, IRPCBlockEntity tile, String methodName, Object... args) {
        int index = Arrays.stream(tile.getRootStorage().getManaged()).toList().indexOf(managed);
        if (index < 0) {
            throw new IllegalArgumentException("No such rpc managed: " + methodName);
        }
        RPCMethodMeta rpcMethod = tile.getRPCMethod(managed, methodName);
        if (rpcMethod == null) {
            throw new IllegalArgumentException("No such RPC method: " + methodName);
        }
        ITypedPayload<?>[] payloads = rpcMethod.serializeArgs(args);
        return new PacketRPCMethodPayload(index, tile.getBlockEntityType(), tile.getCurrentPos(), methodName, payloads);
    }

    public void processPacket(@NotNull BlockEntity blockEntity, RPCSender sender) {
        if (blockEntity.getType() != blockEntityType) {
            LDLib.LOGGER.warn("Block entity type mismatch in rpc payload packet!");
            return;
        }
        if (!(blockEntity instanceof IRPCBlockEntity tile)) {
            LDLib.LOGGER.error("Received managed payload packet for block entity that does not implement IRPCBlockEntity: " + blockEntity);
            return;
        }
        if (tile.getRootStorage().getManaged().length <= managedId) {
            LDLib.LOGGER.error("Received managed couldn't be found in IRPCBlockEntity: " + blockEntity);
            return;
        }
        var managed = tile.getRootStorage().getManaged()[managedId];
        var rpcMethod = tile.getRPCMethod(managed, methodName);
        if (rpcMethod == null) {
            LDLib.LOGGER.error("Cannot find RPC method: " + methodName);
            return;
        }

        rpcMethod.invoke(managed, sender, payloads);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        super.encode(buf);
        buf.writeResourceLocation(Objects.requireNonNull(Registry.BLOCK_ENTITY_TYPE.getKey(blockEntityType)));
        buf.writeUtf(methodName);
        buf.writeVarInt(payloads.length);
        for (ITypedPayload<?> payload : payloads) {
            buf.writeByte(payload.getType());
            payload.writePayload(buf);
        }
    }


    @Override
    public void decode(FriendlyByteBuf buffer) {
        super.decode(buffer);
        blockEntityType = Registry.BLOCK_ENTITY_TYPE.get(buffer.readResourceLocation());
        methodName = buffer.readUtf();
        payloads = new ITypedPayload<?>[buffer.readVarInt()];
        for (int i = 0; i < payloads.length; i++) {
            byte id = buffer.readByte();
            var payload = TypedPayloadRegistries.create(id);
            payload.readPayload(buffer);
            payloads[i] = payload;
        }
    }

    @Override
    public void execute(IHandlerContext handler) {
        if (!handler.isClient()) {
            var player = handler.getPlayer();
            if (player == null) {
                LDLib.LOGGER.error("Received rpc payload packet from client with no player!");
                return;
            }
            var level = player.getLevel();
            if (!level.isLoaded(pos)) return;
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile == null) {
                return;
            }
            processPacket(tile, RPCSender.ofClient(player));

        } else {
            if (handler.getLevel() == null) {
                return;
            }
            BlockEntity tile = handler.getLevel().getBlockEntity(pos);
            if (tile == null) {
                return;
            }
            processPacket(tile, RPCSender.ofServer());
        }

    }
}
