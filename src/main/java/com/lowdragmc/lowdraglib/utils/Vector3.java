package com.lowdragmc.lowdraglib.utils;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/Vector3.class */
public class Vector3 {
    public static final Vector3 X = new Vector3(1.0d, 0.0d, 0.0d);
    public static final Vector3 Y = new Vector3(0.0d, 1.0d, 0.0d);
    public static final Vector3 Z = new Vector3(0.0d, 0.0d, 1.0d);
    public static final Vector3 ZERO = new Vector3(0.0d, 0.0d, 0.0d);
    public static final Vector3 ONE = new Vector3(1.0d, 1.0d, 1.0d);
    public double x;
    public double y;
    public double z;

    public Vector3(double d, double d1, double d2) {
        this.x = d;
        this.y = d1;
        this.z = d2;
    }

    public Vector3(Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vector3(Vector3f vec) {
        this.x = vec.m_122239_();
        this.y = vec.m_122260_();
        this.z = vec.m_122269_();
    }

    public Vector3(Vec3 vec) {
        this.x = vec.m_7096_();
        this.y = vec.m_7098_();
        this.z = vec.m_7094_();
    }

    public Vector3(Vector3d vec) {
        this.x = vec.f_86214_;
        this.y = vec.f_86215_;
        this.z = vec.f_86216_;
    }

    public Vector3(Vec3i vec) {
        this.x = vec.m_123341_();
        this.y = vec.m_123342_();
        this.z = vec.m_123343_();
    }

    public static Vector3 fromNBT(CompoundTag tag) {
        return new Vector3(tag.m_128459_("x"), tag.m_128459_("y"), tag.m_128459_("z"));
    }

    public Vec3 vec3() {
        return new Vec3(this.x, this.y, this.z);
    }

    public BlockPos pos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public Vector3 rotate(double angle, Vector3 axis) {
        Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
        return this;
    }

    public Vector3 rotateYXY(Vector3 rotation) {
        return rotate(rotation.y, Y).rotate(rotation.x, X).rotate(rotation.z, Y);
    }

    public double angle(Vector3 vec) {
        return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
    }

    public double dotProduct(Vector3 vec) {
        double d = (vec.x * this.x) + (vec.y * this.y) + (vec.z * this.z);
        if (d > 1.0d && d < 1.00001d) {
            d = 1.0d;
        } else if (d < -1.0d && d > -1.00001d) {
            d = -1.0d;
        }
        return d;
    }

    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.m_128347_("x", this.x);
        tag.m_128347_("y", this.y);
        tag.m_128347_("z", this.z);
        return tag;
    }

    public Vector3f vector3f() {
        return new Vector3f((float) this.x, (float) this.y, (float) this.z);
    }

    public Vector4f vector4f() {
        return new Vector4f((float) this.x, (float) this.y, (float) this.z, 1.0f);
    }

    public Vector3 set(double x1, double y1, double z1) {
        this.x = x1;
        this.y = y1;
        this.z = z1;
        return this;
    }

    public Vector3 set(Vector3 vec) {
        return set(vec.x, vec.y, vec.z);
    }

    public Vector3 add(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        return this;
    }

    public Vector3 add(double d) {
        return add(d, d, d);
    }

    public Vector3 add(Vector3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(BlockPos pos) {
        return add(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public Vector3 subtract(double dx, double dy, double dz) {
        this.x -= dx;
        this.y -= dy;
        this.z -= dz;
        return this;
    }

    public Vector3 subtract(double d) {
        return subtract(d, d, d);
    }

    public Vector3 subtract(Vector3 vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public Vector3 subtract(BlockPos pos) {
        return subtract(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public Vector3 multiply(double fx, double fy, double fz) {
        this.x *= fx;
        this.y *= fy;
        this.z *= fz;
        return this;
    }

    public Vector3 multiply(double f) {
        return multiply(f, f, f);
    }

    public Vector3 multiply(Vector3 f) {
        return multiply(f.x, f.y, f.z);
    }

    public Vector3 divide(double fx, double fy, double fz) {
        this.x /= fx;
        this.y /= fy;
        this.z /= fz;
        return this;
    }

    public Vector3 divide(double f) {
        return divide(f, f, f);
    }

    public Vector3 divide(Vector3 vec) {
        return divide(vec.x, vec.y, vec.z);
    }

    public Vector3 divide(BlockPos pos) {
        return divide(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public Vector3 floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);
        return this;
    }

    public Vector3 ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        this.z = Math.ceil(this.z);
        return this;
    }

    public double mag() {
        return Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public double magSquared() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    public Vector3 xCrossProduct() {
        double d = this.z;
        double d1 = -this.y;
        this.x = 0.0d;
        this.y = d;
        this.z = d1;
        return this;
    }

    public Vector3 zCrossProduct() {
        double d = -this.y;
        double d1 = this.x;
        this.x = d;
        this.y = d1;
        this.z = 0.0d;
        return this;
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this.x);
        int i = (int) (l ^ (l >>> 32));
        long l2 = Double.doubleToLongBits(this.y);
        int i2 = (31 * i) + ((int) (l2 ^ (l2 >>> 32)));
        long l3 = Double.doubleToLongBits(this.z);
        return (31 * i2) + ((int) (l3 ^ (l3 >>> 32)));
    }

    public boolean equals(Object o) {
        if (!(o instanceof Vector3)) {
            return false;
        }
        Vector3 v = (Vector3) o;
        return this.x == v.x && this.y == v.y && this.z == v.z;
    }

    public boolean equalsT(Vector3 v) {
        return between(this.x - 1.0E-5d, v.x, this.x + 1.0E-5d) && between(this.y - 1.0E-5d, v.y, this.y + 1.0E-5d) && between(this.z - 1.0E-5d, v.z, this.z + 1.0E-5d);
    }

    public static boolean between(double min, double value, double max) {
        return min <= value && value <= max;
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector3(" + new BigDecimal(this.x, cont) + ", " + new BigDecimal(this.y, cont) + ", " + new BigDecimal(this.z, cont) + ")";
    }

    public Vector3 normalize() {
        double d = mag();
        if (d != 0.0d) {
            multiply(1.0d / d);
        }
        return this;
    }

    public Vector3 project(Vector3 b) {
        double l = b.magSquared();
        if (l == 0.0d) {
            set(0.0d, 0.0d, 0.0d);
        } else {
            double m = dotProduct(b) / l;
            set(b).multiply(m);
        }
        return this;
    }

    public Vector3 crossProduct(Vector3 vec) {
        double d = (this.y * vec.z) - (this.z * vec.y);
        double d1 = (this.z * vec.x) - (this.x * vec.z);
        double d2 = (this.x * vec.y) - (this.y * vec.x);
        this.x = d;
        this.y = d1;
        this.z = d2;
        return this;
    }

    public boolean isZero() {
        return this.x == 0.0d && this.y == 0.0d && this.z == 0.0d;
    }

    public double min() {
        return this.x < this.y ? Math.min(this.x, this.z) : Math.min(this.y, this.z);
    }

    public double max() {
        return this.x > this.y ? Math.max(this.x, this.z) : Math.max(this.y, this.z);
    }
}
