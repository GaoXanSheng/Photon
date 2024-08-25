package com.lowdragmc.lowdraglib.pipelike;

import net.minecraft.core.Direction;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/pipelike/Node.class */
public final class Node<NodeDataType> {
    public static final int DEFAULT_MARK = 0;
    public static final int ALL_OPENED = 63;
    public static final int ALL_CLOSED = 0;
    public NodeDataType data;
    public int openConnections;
    public int mark;
    public boolean isActive;

    public Node(NodeDataType data, int openConnections, int mark, boolean isActive) {
        this.data = data;
        this.openConnections = openConnections;
        this.mark = mark;
        this.isActive = isActive;
    }

    public boolean isBlocked(Direction facing) {
        return (this.openConnections & (1 << facing.ordinal())) == 0;
    }
}
