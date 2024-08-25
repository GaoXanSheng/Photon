package com.lowdragmc.lowdraglib.pipelike;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.pipelike.PipeNet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/pipelike/PipeNetWalker.class */
public abstract class PipeNetWalker<NodeDataType, Net extends PipeNet<NodeDataType>> {
    protected final Net pipeNet;
    protected List<PipeNetWalker<NodeDataType, Net>> walkers;
    protected final BlockPos.MutableBlockPos currentPos;
    private int walkedBlocks;
    private boolean invalid;
    private boolean running;
    protected final Set<Long> walked = new HashSet();
    protected final List<Direction> pipes = new ArrayList();
    protected PipeNetWalker<NodeDataType, Net> root = this;

    @Nonnull
    protected abstract PipeNetWalker<NodeDataType, Net> createSubWalker(Net net, BlockPos blockPos, int i);

    public int getWalkedBlocks() {
        return this.walkedBlocks;
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    protected PipeNetWalker(Net pipeNet, BlockPos sourcePipe, int walkedBlocks) {
        this.pipeNet = pipeNet;
        this.walkedBlocks = walkedBlocks;
        this.currentPos = sourcePipe.m_122032_();
    }

    protected void checkNeighbour(Node<NodeDataType> pipeNode, BlockPos pipePos, Direction faceToNeighbour) {
    }

    protected boolean checkPipe(Node<NodeDataType> pipeNode, BlockPos pos) {
        return true;
    }

    protected void onRemoveSubWalker(PipeNetWalker<NodeDataType, Net> subWalker) {
    }

    public void traversePipeNet() {
        traversePipeNet(32768);
    }

    public void traversePipeNet(int maxWalks) {
        if (this.invalid) {
            throw new IllegalStateException("This walker already walked. Create a new one if you want to walk again");
        }
        int i = 0;
        this.running = true;
        while (this.running && !walk()) {
            int i2 = i;
            i++;
            if (i2 >= maxWalks) {
                break;
            }
        }
        this.running = false;
        this.root.walked.clear();
        if (i >= maxWalks) {
            LDLib.LOGGER.warn("The walker reached the maximum amount of walks {}", Integer.valueOf(i));
        }
        this.invalid = true;
    }

    private boolean walk() {
        if (this.walkers == null) {
            checkPos();
            if (this.pipes.size() == 0) {
                return true;
            }
            if (this.pipes.size() == 1) {
                this.currentPos.m_122173_(this.pipes.get(0));
                this.walkedBlocks++;
                return !isRunning();
            }
            this.walkers = new ArrayList();
            for (Direction side : this.pipes) {
                PipeNetWalker<NodeDataType, Net> walker = createSubWalker(this.pipeNet, this.currentPos.m_121945_(side), this.walkedBlocks + 1);
                walker.root = this.root;
                this.walkers.add(walker);
            }
        }
        Iterator<PipeNetWalker<NodeDataType, Net>> iterator = this.walkers.iterator();
        while (iterator.hasNext()) {
            PipeNetWalker<NodeDataType, Net> walker2 = iterator.next();
            if (walker2.walk()) {
                onRemoveSubWalker(walker2);
                iterator.remove();
            }
        }
        return !isRunning() || this.walkers.size() == 0;
    }

    private void checkPos() {
        Direction[] values;
        this.pipes.clear();
        Node<NodeDataType> pipeNode = this.pipeNet.getNodeAt(this.currentPos);
        if (pipeNode == null || !checkPipe(this.pipeNet.getNodeAt(this.currentPos), this.currentPos)) {
            return;
        }
        this.root.walked.add(Long.valueOf(this.currentPos.m_121878_()));
        for (Direction accessSide : Direction.values()) {
            if (!isWalked(this.currentPos.m_121945_(accessSide)) && !pipeNode.isBlocked(accessSide)) {
                if (this.pipeNet.isNodeConnectedTo(this.currentPos, accessSide)) {
                    this.pipes.add(accessSide);
                } else {
                    checkNeighbour(pipeNode, this.currentPos, accessSide);
                }
            }
        }
    }

    protected boolean isWalked(BlockPos pos) {
        return this.root.walked.contains(Long.valueOf(pos.m_121878_()));
    }

    public void stop() {
        this.root.running = false;
    }

    public boolean isRunning() {
        return this.root.running;
    }

    public ServerLevel getLevel() {
        return this.pipeNet.getLevel();
    }

    public BlockPos getCurrentPos() {
        return this.currentPos.m_7949_();
    }
}
