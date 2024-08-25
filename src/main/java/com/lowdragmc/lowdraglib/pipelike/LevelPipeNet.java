package com.lowdragmc.lowdraglib.pipelike;

import com.lowdragmc.lowdraglib.pipelike.PipeNet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/pipelike/LevelPipeNet.class */
public abstract class LevelPipeNet<NodeDataType, T extends PipeNet<NodeDataType>> extends SavedData {
    private final ServerLevel serverLevel;
    protected List<T> pipeNets;
    protected final Map<ChunkPos, List<T>> pipeNetsByChunk;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract T createNetInstance();

    public LevelPipeNet(ServerLevel serverLevel) {
        this.pipeNets = new ArrayList();
        this.pipeNetsByChunk = new HashMap();
        this.serverLevel = serverLevel;
    }

    public LevelPipeNet(ServerLevel serverLevel, CompoundTag tag) {
        this(serverLevel);
        this.pipeNets = new ArrayList();
        ListTag allEnergyNets = tag.m_128437_("PipeNets", 10);
        for (int i = 0; i < allEnergyNets.size(); i++) {
            CompoundTag pNetTag = allEnergyNets.m_128728_(i);
            T pipeNet = createNetInstance();
            pipeNet.deserializeNBT(pNetTag);
            addPipeNetSilently(pipeNet);
        }
        init();
    }

    public ServerLevel getWorld() {
        return this.serverLevel;
    }

    protected void init() {
        this.pipeNets.forEach((v0) -> {
            v0.onNodeConnectionsUpdate();
        });
    }

    public void addNode(BlockPos nodePos, NodeDataType nodeData, int mark, int openConnections, boolean isActive) {
        Direction[] values;
        T myPipeNet = null;
        Node<NodeDataType> node = new Node<>(nodeData, openConnections, mark, isActive);
        for (Direction facing : Direction.values()) {
            BlockPos offsetPos = nodePos.m_121945_(facing);
            T pipeNet = getNetFromPos(offsetPos);
            Node<NodeDataType> secondNode = pipeNet == null ? null : pipeNet.getAllNodes().get(offsetPos);
            if (pipeNet != null && pipeNet.canAttachNode(nodeData) && pipeNet.canNodesConnect(secondNode, facing.m_122424_(), node, null)) {
                if (myPipeNet == null) {
                    myPipeNet = pipeNet;
                    myPipeNet.addNode(nodePos, node);
                } else if (myPipeNet != pipeNet) {
                    myPipeNet.uniteNetworks(pipeNet);
                }
            }
        }
        if (myPipeNet == null) {
            T myPipeNet2 = createNetInstance();
            myPipeNet2.addNode(nodePos, node);
            addPipeNet(myPipeNet2);
            m_77762_();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addPipeNetToChunk(ChunkPos chunkPos, T pipeNet) {
        this.pipeNetsByChunk.computeIfAbsent(chunkPos, any -> {
            return new ArrayList();
        }).add(pipeNet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removePipeNetFromChunk(ChunkPos chunkPos, T pipeNet) {
        List<T> list = this.pipeNetsByChunk.get(chunkPos);
        if (list != null) {
            list.remove(pipeNet);
        }
        if (list.isEmpty()) {
            this.pipeNetsByChunk.remove(chunkPos);
        }
    }

    public void removeNode(BlockPos nodePos) {
        T pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.removeNode(nodePos);
        }
    }

    public void updateBlockedConnections(BlockPos nodePos, Direction side, boolean isBlocked) {
        T pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.updateBlockedConnections(nodePos, side, isBlocked);
        }
    }

    public void updateData(BlockPos nodePos, NodeDataType data) {
        T pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.updateNodeData(nodePos, data);
        }
    }

    public void updateMark(BlockPos nodePos, int newMark) {
        T pipeNet = getNetFromPos(nodePos);
        if (pipeNet != null) {
            pipeNet.updateMark(nodePos, newMark);
        }
    }

    public T getNetFromPos(BlockPos blockPos) {
        List<T> pipeNetsInChunk = this.pipeNetsByChunk.getOrDefault(new ChunkPos(blockPos), Collections.emptyList());
        for (T pipeNet : pipeNetsInChunk) {
            if (pipeNet.containsNode(blockPos)) {
                return pipeNet;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addPipeNet(T pipeNet) {
        addPipeNetSilently(pipeNet);
    }

    protected void addPipeNetSilently(T pipeNet) {
        this.pipeNets.add(pipeNet);
        pipeNet.getContainedChunks().forEach(chunkPos -> {
            addPipeNetToChunk(pipeNet, pipeNet);
        });
        pipeNet.isValid = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removePipeNet(T pipeNet) {
        this.pipeNets.remove(pipeNet);
        pipeNet.getContainedChunks().forEach(chunkPos -> {
            removePipeNetFromChunk(pipeNet, pipeNet);
        });
        pipeNet.isValid = false;
        m_77762_();
    }

    public CompoundTag m_7176_(CompoundTag compound) {
        ListTag allPipeNets = new ListTag();
        for (T pipeNet : this.pipeNets) {
            CompoundTag pNetTag = pipeNet.mo129serializeNBT();
            allPipeNets.add(pNetTag);
        }
        compound.m_128365_("PipeNets", allPipeNets);
        return compound;
    }
}
