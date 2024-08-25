package com.lowdragmc.lowdraglib.utils;

import com.google.common.base.MoreObjects;
import java.util.Objects;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/Size.class */
public class Size {
    public static final Size ZERO = new Size(0, 0);
    public final int width;
    public final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Size) {
            Size size = (Size) o;
            return this.width == size.width && this.height == size.height;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.width), Integer.valueOf(this.height));
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("width", this.width).add("height", this.height).toString();
    }
}
