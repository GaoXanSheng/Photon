package com.lowdragmc.lowdraglib.utils;

import com.google.common.base.MoreObjects;
import java.util.Objects;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/PositionedRect.class */
public class PositionedRect {
    public final Position position;
    public final Size size;

    public PositionedRect(int x, int y, int width, int height) {
        this(new Position(x, y), new Size(width, height));
    }

    public PositionedRect(Position position, Size size) {
        this.position = position;
        this.size = size;
    }

    public PositionedRect(Position pos1, Position pos2) {
        this.position = new Position(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y));
        this.size = new Size(Math.max(pos1.x, pos2.x) - this.position.x, Math.max(pos1.y, pos2.y) - this.position.y);
    }

    public Position getPosition() {
        return this.position;
    }

    public Size getSize() {
        return this.size;
    }

    public boolean intersects(Position other) {
        return this.position.x <= other.x && this.position.y <= other.y && this.position.x + this.size.width >= other.x && this.position.y + this.size.height >= other.y;
    }

    public boolean intersects(PositionedRect other) {
        return intersects(other.position) || intersects(other.position.add(other.size)) || intersects(other.position.add(new Size(other.size.width, 0))) || intersects(other.position.add(new Size(0, other.size.height)));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof PositionedRect) {
            PositionedRect that = (PositionedRect) o;
            return this.position.equals(that.position) && this.size.equals(that.size);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.position, this.size);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("position", this.position).add("size", this.size).toString();
    }
}
