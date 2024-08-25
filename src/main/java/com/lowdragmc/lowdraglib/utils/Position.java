package com.lowdragmc.lowdraglib.utils;

import com.google.common.base.MoreObjects;
import java.util.Objects;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/Position.class */
public class Position {
    public static final Position ORIGIN = new Position(0, 0);
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(Position other) {
        return new Position(this.x + other.x, this.y + other.y);
    }

    public Position subtract(Position other) {
        return new Position(this.x - other.x, this.y - other.y);
    }

    public Position add(Size size) {
        return new Position(this.x + size.width, this.y + size.height);
    }

    public Position addX(int x) {
        return new Position(this.x + x, this.y);
    }

    public Position addY(int y) {
        return new Position(this.x, this.y + y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Position) {
            Position position = (Position) o;
            return this.x == position.x && this.y == position.y;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.x), Integer.valueOf(this.y));
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.x).add("y", this.y).toString();
    }
}
