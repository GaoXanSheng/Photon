package com.lowdragmc.lowdraglib.utils;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/Range.class */
public class Range {
    protected Number a;
    protected Number b;

    public Number getA() {
        return this.a;
    }

    public Number getB() {
        return this.b;
    }

    public void setA(Number a) {
        this.a = a;
    }

    public void setB(Number b) {
        this.b = b;
    }

    public Range(Number a, Number b) {
        this.a = a;
        this.b = b;
    }

    public Number getMin() {
        return Double.valueOf(Math.min(this.a.doubleValue(), this.b.doubleValue()));
    }

    public Number getMax() {
        return Double.valueOf(Math.min(this.a.doubleValue(), this.b.doubleValue()));
    }
}
