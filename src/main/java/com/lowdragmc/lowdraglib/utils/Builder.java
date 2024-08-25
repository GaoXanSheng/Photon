package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.utils.Builder;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/Builder.class */
public abstract class Builder<T, B extends Builder<T, B>> {
    protected List<String[]> shape = new ArrayList();
    protected Map<Character, T> symbolMap = new LinkedHashMap();

    public B aisle(String... data) {
        this.shape.add(data);
        return this;
    }

    public B where(char symbol, T value) {
        this.symbolMap.put(Character.valueOf(symbol), value);
        return this;
    }

    public T[][][] bakeArray(Class<T> clazz, T defaultValue) {
        T[][][] Ts = (T[][][]) ((Object[][][]) Array.newInstance((Class<?>) clazz, this.shape.get(0)[0].length(), this.shape.get(0).length, this.shape.size()));
        for (int z = 0; z < Ts.length; z++) {
            String[] aisleEntry = this.shape.get(z);
            for (int y = 0; y < this.shape.get(0).length; y++) {
                String columnEntry = aisleEntry[y];
                for (int x = 0; x < columnEntry.length(); x++) {
                    T info = this.symbolMap.getOrDefault(Character.valueOf(columnEntry.charAt(x)), defaultValue);
                    Ts[x][y][z] = info;
                }
            }
        }
        return Ts;
    }

    public B shallowCopy() {
        try {
            B b = (B) getClass().newInstance();
            b.shape = new ArrayList(this.shape);
            b.symbolMap = new HashMap(this.symbolMap);
            return b;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
