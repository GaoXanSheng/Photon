package com.lowdragmc.lowdraglib.pipelike;

import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/pipelike/PipeNet.class */
public abstract class PipeNet<NodeDataType> implements ITagSerializable<CompoundTag> {
    protected final LevelPipeNet<NodeDataType, PipeNet<NodeDataType>> worldData;
    private long lastUpdate;
    private final Map<BlockPos, Node<NodeDataType>> nodeByBlockPos = new HashMap();
    private final Map<BlockPos, Node<NodeDataType>> unmodifiableNodeByBlockPos = Collections.unmodifiableMap(this.nodeByBlockPos);
    private final Map<ChunkPos, Integer> ownedChunks = new HashMap();
    boolean isValid = false;

    protected abstract void writeNodeData(NodeDataType nodedatatype, CompoundTag compoundTag);

    protected abstract NodeDataType readNodeData(CompoundTag compoundTag);

    /* JADX WARN: Multi-variable type inference failed */
    public PipeNet(LevelPipeNet<NodeDataType, ? extends PipeNet> Level) {
        this.worldData = Level;
    }

    public Set<ChunkPos> getContainedChunks() {
        return Collections.unmodifiableSet(this.ownedChunks.keySet());
    }

    public LevelPipeNet<NodeDataType, PipeNet<NodeDataType>> getWorldData() {
        return this.worldData;
    }

    public ServerLevel getLevel() {
        return this.worldData.getWorld();
    }

    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public boolean isValid() {
        return this.isValid;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onNodeConnectionsUpdate() {
        this.lastUpdate = System.currentTimeMillis();
    }

    protected void onNodeDataUpdate() {
    }

    protected void onPipeConnectionsUpdate() {
    }

    public void onNeighbourUpdate(BlockPos fromPos) {
    }

    public Map<BlockPos, Node<NodeDataType>> getAllNodes() {
        return this.unmodifiableNodeByBlockPos;
    }

    public Node<NodeDataType> getNodeAt(BlockPos blockPos) {
        return this.nodeByBlockPos.get(blockPos);
    }

    public boolean containsNode(BlockPos blockPos) {
        return this.nodeByBlockPos.containsKey(blockPos);
    }

    public boolean isNodeConnectedTo(BlockPos pos, Direction side) {
        Node<NodeDataType> nodeSecond;
        Node<NodeDataType> nodeFirst = getNodeAt(pos);
        if (nodeFirst == null || (nodeSecond = getNodeAt(pos.m_121945_(side))) == null) {
            return false;
        }
        return canNodesConnect(nodeFirst, side, nodeSecond, this);
    }

    protected void addNodeSilently(BlockPos nodePos, Node<NodeDataType> node) {
        this.nodeByBlockPos.put(nodePos, node);
        checkAddedInChunk(nodePos);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addNode(BlockPos nodePos, Node<NodeDataType> node) {
        addNodeSilently(nodePos, node);
        onNodeConnectionsUpdate();
        this.worldData.m_77762_();
    }

    protected Node<NodeDataType> removeNodeWithoutRebuilding(BlockPos nodePos) {
        Node<NodeDataType> removedNode = this.nodeByBlockPos.remove(nodePos);
        ensureRemovedFromChunk(nodePos);
        this.worldData.m_77762_();
        return removedNode;
    }

    public void removeNode(BlockPos nodePos) {
        if (this.nodeByBlockPos.containsKey(nodePos)) {
            Node<NodeDataType> selfNode = removeNodeWithoutRebuilding(nodePos);
            rebuildNetworkOnNodeRemoval(nodePos, selfNode);
        }
    }

    protected void checkAddedInChunk(BlockPos nodePos) {
        ChunkPos chunkPos = new ChunkPos(nodePos);
        int newValue = this.ownedChunks.compute(chunkPos, pos, old -> {
            return Integer.valueOf((old == null ? 0 : old.intValue()) + 1);
        }).intValue();
        if (newValue == 1 && isValid()) {
            this.worldData.addPipeNetToChunk(chunkPos, this);
        }
    }

    protected void ensureRemovedFromChunk(BlockPos nodePos) {
        ChunkPos chunkPos = new ChunkPos(nodePos);
        int newValue = this.ownedChunks.compute(chunkPos, pos, old -> {
            return Integer.valueOf(old == null ? 0 : old.intValue() - 1);
        }).intValue();
        if (newValue == 0) {
            this.ownedChunks.remove(chunkPos);
            if (isValid()) {
                this.worldData.removePipeNetFromChunk(chunkPos, this);
            }
        }
    }

    public void updateBlockedConnections(BlockPos nodePos, Direction facing, boolean isBlocked) {
        if (!containsNode(nodePos)) {
            return;
        }
        Node<NodeDataType> selfNode = getNodeAt(nodePos);
        if (selfNode.isBlocked(facing) == isBlocked) {
            return;
        }
        setBlocked(selfNode, facing, isBlocked);
        BlockPos offsetPos = nodePos.m_121945_(facing);
        PipeNet<NodeDataType> pipeNetAtOffset = this.worldData.getNetFromPos(offsetPos);
        if (pipeNetAtOffset == null) {
            onNodeConnectionsUpdate();
            onPipeConnectionsUpdate();
            this.worldData.m_77762_();
            return;
        }
        if (pipeNetAtOffset == this) {
            if (isBlocked) {
                setBlocked(selfNode, facing, false);
                if (canNodesConnect(selfNode, facing, getNodeAt(offsetPos), this)) {
                    setBlocked(selfNode, facing, true);
                    HashMap<BlockPos, Node<NodeDataType>> thisENet = findAllConnectedBlocks(nodePos);
                    if (!getAllNodes().equals(thisENet)) {
                        PipeNet<NodeDataType> newPipeNet = this.worldData.createNetInstance();
                        thisENet.keySet().forEach(this::removeNodeWithoutRebuilding);
                        newPipeNet.transferNodeData(thisENet, this);
                        this.worldData.addPipeNet(newPipeNet);
                    }
                }
            }
        } else if (!isBlocked) {
            Node<NodeDataType> neighbourNode = pipeNetAtOffset.getNodeAt(offsetPos);
            if (canNodesConnect(selfNode, facing, neighbourNode, pipeNetAtOffset) && pipeNetAtOffset.canNodesConnect(neighbourNode, facing.m_122424_(), selfNode, this)) {
                uniteNetworks(pipeNetAtOffset);
            }
        }
        onNodeConnectionsUpdate();
        onPipeConnectionsUpdate();
        this.worldData.m_77762_();
    }

    public void updateNodeData(BlockPos nodePos, NodeDataType data) {
        if (containsNode(nodePos)) {
            Node<NodeDataType> selfNode = getNodeAt(nodePos);
            selfNode.data = data;
            onNodeDataUpdate();
            this.worldData.m_77762_();
        }
    }

    public void updateMark(BlockPos nodePos, int newMark) {
        Direction[] values;
        if (!containsNode(nodePos)) {
            return;
        }
        HashMap<BlockPos, Node<NodeDataType>> selfConnectedBlocks = null;
        Node<NodeDataType> selfNode = getNodeAt(nodePos);
        int oldMark = selfNode.mark;
        selfNode.mark = newMark;
        for (Direction facing : Direction.values()) {
            BlockPos offsetPos = nodePos.m_121945_(facing);
            PipeNet<NodeDataType> otherPipeNet = this.worldData.getNetFromPos(offsetPos);
            Node<NodeDataType> secondNode = otherPipeNet == null ? null : otherPipeNet.getNodeAt(offsetPos);
            if (secondNode != null && areNodeBlockedConnectionsCompatible(selfNode, facing, secondNode) && areNodesCustomContactable(selfNode.data, secondNode.data, otherPipeNet) && areMarksCompatible(oldMark, secondNode.mark) != areMarksCompatible(newMark, secondNode.mark)) {
                if (areMarksCompatible(newMark, secondNode.mark)) {
                    if (otherPipeNet != this) {
                        uniteNetworks(otherPipeNet);
                    }
                } else if (otherPipeNet == this) {
                    if (selfConnectedBlocks == null) {
                        selfConnectedBlocks = findAllConnectedBlocks(nodePos);
                    }
                    if (!getAllNodes().equals(selfConnectedBlocks)) {
                        HashMap<BlockPos, Node<NodeDataType>> offsetConnectedBlocks = findAllConnectedBlocks(offsetPos);
                        if (!offsetConnectedBlocks.equals(selfConnectedBlocks)) {
                            offsetConnectedBlocks.keySet().forEach(this::removeNodeWithoutRebuilding);
                            PipeNet<NodeDataType> offsetPipeNet = this.worldData.createNetInstance();
                            offsetPipeNet.transferNodeData(offsetConnectedBlocks, this);
                            this.worldData.addPipeNet(offsetPipeNet);
                        }
                    }
                }
            }
        }
        onNodeConnectionsUpdate();
        this.worldData.m_77762_();
    }

    private void setBlocked(Node<NodeDataType> selfNode, Direction facing, boolean isBlocked) {
        if (!isBlocked) {
            selfNode.openConnections |= 1 << facing.ordinal();
        } else {
            selfNode.openConnections &= (1 << facing.ordinal()) ^ (-1);
        }
    }

    public boolean markNodeAsActive(BlockPos nodePos, boolean isActive) {
        if (containsNode(nodePos) && getNodeAt(nodePos).isActive != isActive) {
            getNodeAt(nodePos).isActive = isActive;
            this.worldData.m_77762_();
            onNodeConnectionsUpdate();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void uniteNetworks(PipeNet<NodeDataType> unitedPipeNet) {
        Map<BlockPos, Node<NodeDataType>> allNodes = new HashMap<>((Map<? extends BlockPos, ? extends Node<NodeDataType>>) unitedPipeNet.getAllNodes());
        this.worldData.removePipeNet(unitedPipeNet);
        Set<BlockPos> keySet = allNodes.keySet();
        Objects.requireNonNull(unitedPipeNet);
        keySet.forEach(this::removeNodeWithoutRebuilding);
        transferNodeData(allNodes, unitedPipeNet);
    }

    private boolean areNodeBlockedConnectionsCompatible(Node<NodeDataType> first, Direction firstFacing, Node<NodeDataType> second) {
        return (first.isBlocked(firstFacing) || second.isBlocked(firstFacing.m_122424_())) ? false : true;
    }

    private boolean areMarksCompatible(int mark1, int mark2) {
        return mark1 == mark2 || mark1 == 0 || mark2 == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean canNodesConnect(Node<NodeDataType> first, Direction firstFacing, Node<NodeDataType> second, PipeNet<NodeDataType> secondPipeNet) {
        return areNodeBlockedConnectionsCompatible(first, firstFacing, second) && areMarksCompatible(first.mark, second.mark) && areNodesCustomContactable(first.data, second.data, secondPipeNet);
    }

    protected HashMap<BlockPos, Node<NodeDataType>> findAllConnectedBlocks(BlockPos startPos) {
        HashMap<BlockPos, Node<NodeDataType>> observedSet = new HashMap<>();
        observedSet.put(startPos, getNodeAt(startPos));
        Node<NodeDataType> firstNode = getNodeAt(startPos);
        BlockPos.MutableBlockPos currentPos = startPos.m_122032_();
        Stack<Direction> moveStack = new Stack<>();
        while (true) {
            Direction[] values = Direction.values();
            int length = values.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    Direction facing = values[i];
                    currentPos.m_122173_(facing);
                    Node<NodeDataType> secondNode = getNodeAt(currentPos);
                    if (secondNode != null && canNodesConnect(firstNode, facing, secondNode, this) && !observedSet.containsKey(currentPos)) {
                        observedSet.put(currentPos.m_7949_(), getNodeAt(currentPos));
                        firstNode = secondNode;
                        moveStack.push(facing.m_122424_());
                        break;
                    }
                    currentPos.m_122173_(facing.m_122424_());
                    i++;
                } else if (!moveStack.isEmpty()) {
                    currentPos.m_122173_(moveStack.pop());
                    firstNode = getNodeAt(currentPos);
                } else {
                    return observedSet;
                }
            }
        }
    }

    protected void rebuildNetworkOnNodeRemoval(BlockPos nodePos, Node<NodeDataType> selfNode) {
        Direction[] values;
        int amountOfConnectedSides = 0;
        for (Direction facing : Direction.values()) {
            if (containsNode(nodePos.m_121945_(facing))) {
                amountOfConnectedSides++;
            }
        }
        if (amountOfConnectedSides >= 2) {
            for (Direction facing2 : Direction.values()) {
                BlockPos offsetPos = nodePos.m_121945_(facing2);
                Node<NodeDataType> secondNode = getNodeAt(offsetPos);
                if (secondNode != null && canNodesConnect(selfNode, facing2, secondNode, this)) {
                    HashMap<BlockPos, Node<NodeDataType>> thisENet = findAllConnectedBlocks(offsetPos);
                    if (getAllNodes().equals(thisENet)) {
                        break;
                    }
                    PipeNet<NodeDataType> energyNet = this.worldData.createNetInstance();
                    thisENet.keySet().forEach(this::removeNodeWithoutRebuilding);
                    energyNet.transferNodeData(thisENet, this);
                    this.worldData.addPipeNet(energyNet);
                }
            }
        }
        if (getAllNodes().isEmpty()) {
            this.worldData.removePipeNet(this);
        }
        onNodeConnectionsUpdate();
        this.worldData.m_77762_();
    }

    protected boolean areNodesCustomContactable(NodeDataType first, NodeDataType second, PipeNet<NodeDataType> secondNodePipeNet) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canAttachNode(NodeDataType nodeData) {
        return true;
    }

    protected void transferNodeData(Map<BlockPos, Node<NodeDataType>> transferredNodes, PipeNet<NodeDataType> parentNet) {
        transferredNodes.forEach(this::addNodeSilently);
        onNodeConnectionsUpdate();
        this.worldData.m_77762_();
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.m_128365_("Nodes", serializeAllNodeList(this.nodeByBlockPos));
        return compound;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        this.nodeByBlockPos.clear();
        this.ownedChunks.clear();
        deserializeAllNodeList(nbt.m_128469_("Nodes"));
    }

    protected void deserializeAllNodeList(CompoundTag compound) {
        ListTag allNodesList = compound.m_128437_("NodeIndexes", 10);
        ListTag wirePropertiesList = compound.m_128437_("WireProperties", 10);
        Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
        for (int i = 0; i < wirePropertiesList.size(); i++) {
            CompoundTag propertiesTag = wirePropertiesList.m_128728_(i);
            int wirePropertiesIndex = propertiesTag.m_128451_("index");
            NodeDataType nodeData = readNodeData(propertiesTag);
            int2ObjectOpenHashMap.put(wirePropertiesIndex, nodeData);
        }
        for (int i2 = 0; i2 < allNodesList.size(); i2++) {
            CompoundTag nodeTag = allNodesList.m_128728_(i2);
            int x = nodeTag.m_128451_("x");
            int y = nodeTag.m_128451_("y");
            int z = nodeTag.m_128451_("z");
            int wirePropertiesIndex2 = nodeTag.m_128451_("index");
            BlockPos blockPos = new BlockPos(x, y, z);
            Object obj = int2ObjectOpenHashMap.get(wirePropertiesIndex2);
            int openConnections = nodeTag.m_128451_("open");
            int mark = nodeTag.m_128451_("mark");
            boolean isNodeActive = nodeTag.m_128471_("active");
            addNodeSilently(blockPos, new Node<>(obj, openConnections, mark, isNodeActive));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected CompoundTag serializeAllNodeList(Map<BlockPos, Node<NodeDataType>> allNodes) {
        CompoundTag compound = new CompoundTag();
        ListTag allNodesList = new ListTag();
        ListTag wirePropertiesList = new ListTag();
        Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
        int currentIndex = 0;
        for (Map.Entry<BlockPos, Node<NodeDataType>> entry : allNodes.entrySet()) {
            BlockPos nodePos = entry.getKey();
            Node<NodeDataType> node = entry.getValue();
            CompoundTag nodeTag = new CompoundTag();
            nodeTag.m_128405_("x", nodePos.m_123341_());
            nodeTag.m_128405_("y", nodePos.m_123342_());
            nodeTag.m_128405_("z", nodePos.m_123343_());
            int wirePropertiesIndex = object2IntOpenHashMap.getOrDefault(node.data, -1);
            if (wirePropertiesIndex == -1) {
                wirePropertiesIndex = currentIndex;
                object2IntOpenHashMap.put(node.data, wirePropertiesIndex);
                currentIndex++;
            }
            nodeTag.m_128405_("index", wirePropertiesIndex);
            if (node.mark != 0) {
                nodeTag.m_128405_("mark", node.mark);
            }
            if (node.openConnections > 0) {
                nodeTag.m_128405_("open", node.openConnections);
            }
            if (node.isActive) {
                nodeTag.m_128379_("active", true);
            }
            allNodesList.add(nodeTag);
        }
        ObjectIterator it = object2IntOpenHashMap.keySet().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            int wirePropertiesIndex2 = object2IntOpenHashMap.getInt(next);
            CompoundTag propertiesTag = new CompoundTag();
            propertiesTag.m_128405_("index", wirePropertiesIndex2);
            writeNodeData(next, propertiesTag);
            wirePropertiesList.add(propertiesTag);
        }
        compound.m_128365_("NodeIndexes", allNodesList);
        compound.m_128365_("WireProperties", wirePropertiesList);
        return compound;
    }
}
