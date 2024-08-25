package com.lowdragmc.lowdraglib.utils.curve;

import java.util.ArrayList;
import java.util.List;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/curve/Curve.class */
public abstract class Curve<T> {
    public abstract T getPoint(float f);

    public List<T> getPoints(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("size should be greater than 2.");
        }
        List<T> points = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            points.add(getPoint((i * 1.0f) / (size - 1)));
        }
        return points;
    }
}
