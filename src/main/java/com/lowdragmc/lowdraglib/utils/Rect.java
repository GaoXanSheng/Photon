package com.lowdragmc.lowdraglib.utils;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/Rect.class */
public class Rect {
    public final int left;
    public final int right;
    public final int up;
    public final int down;

    protected Rect(int left, int right, int up, int down) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    public static Rect ofAbsolute(int left, int right, int up, int down) {
        return new Rect(left, right, up, down);
    }

    public static Rect ofRelative(int left, int width, int up, int height) {
        return new Rect(left, left + width, up, up + height);
    }

    public static Rect of(Position position, Size size) {
        return new Rect(position.x, position.x + size.width, position.y, position.y + size.height);
    }

    public Position toLeftUp() {
        return new Position(this.left, this.up);
    }

    public Position toLeftCenter() {
        return new Position(this.left, (this.up + this.down) / 2);
    }

    public Position toLeftDown() {
        return new Position(this.left, this.down);
    }

    public Position toDownCenter() {
        return new Position((this.left + this.right) / 2, this.down);
    }

    public Position toRightDown() {
        return new Position(this.right, this.down);
    }

    public Position toRightCenter() {
        return new Position(this.right, (this.up + this.down) / 2);
    }

    public Position toRightUp() {
        return new Position(this.right, this.up);
    }

    public Position toUpCenter() {
        return new Position((this.left + this.right) / 2, this.up);
    }

    public Position upAnd(int x) {
        return new Position(x, this.up);
    }

    public Position rightAnd(int y) {
        return new Position(this.right, y);
    }

    public Position downAnd(int x) {
        return new Position(x, this.down);
    }

    public Position leftAnd(int y) {
        return new Position(this.left, y);
    }

    public Rect expand(int expand) {
        return expand(expand, expand);
    }

    public Rect expand(int x, int y) {
        return new Rect(this.left - x, this.right + x, this.up - y, this.down + y);
    }

    public Rect horizontalExpand(int x) {
        return expand(x, 0);
    }

    public Rect horizontalExpand(int left, int right) {
        return new Rect(this.left - left, this.right + right, this.up, this.down);
    }

    public Rect verticalExpand(int y) {
        return expand(0, y);
    }

    public Rect verticalExpand(int up, int down) {
        return new Rect(this.left, this.right, this.up - up, this.down + down);
    }

    public Rect expandLeft(int expand) {
        return new Rect(this.left - expand, this.right, this.up, this.down);
    }

    public Rect expandRight(int expand) {
        return new Rect(this.left, this.right + expand, this.up, this.down);
    }

    public Rect expandUp(int expand) {
        return new Rect(this.left, this.right, this.up - expand, this.down);
    }

    public Rect expandDown(int expand) {
        return new Rect(this.left, this.right, this.up, this.down + expand);
    }

    public int getWidth() {
        return this.right - this.left;
    }

    public int getHeight() {
        return this.down - this.up;
    }

    public int getWidthCenter() {
        return (this.right + this.left) / 2;
    }

    public int getHeightCenter() {
        return (this.down + this.up) / 2;
    }

    public Rect withLeft(int left) {
        return new Rect(left, this.right, this.up, this.down);
    }

    public Rect withRight(int right) {
        return new Rect(this.left, right, this.up, this.down);
    }

    public Rect withUp(int up) {
        return new Rect(this.left, this.right, up, this.down);
    }

    public Rect withDown(int down) {
        return new Rect(this.left, this.right, this.up, down);
    }

    public Rect withLeftFixedWidth(int width) {
        return new Rect(this.left, this.left + width, this.up, this.down);
    }

    public Rect withRightFixedWidth(int width) {
        return new Rect(this.right - width, this.right, this.up, this.down);
    }

    public Rect withUpFixedHeight(int height) {
        return new Rect(this.left, this.right, this.up, this.up + height);
    }

    public Rect withDownFixedHeight(int height) {
        return new Rect(this.left, this.right, this.down - height, this.down);
    }

    public Rect moveHorizontal(int delta) {
        return new Rect(this.left + delta, this.right + delta, this.up, this.down);
    }

    public Rect moveVertical(int delta) {
        return new Rect(this.left, this.right, this.up + delta, this.down + delta);
    }
}
