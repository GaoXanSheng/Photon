package com.lowdragmc.lowdraglib.utils;

import java.lang.reflect.Array;
import java.util.List;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ArrayUtils.class */
public class ArrayUtils {
    public static <T> T[] fromList(List<T> list) {
        return (T[]) create(list, list.get(0).getClass());
    }

    public static <T> T[] create(List<T> list, Class<T> type) {
        T[] array = (T[]) ((Object[]) Array.newInstance((Class<?>) type, list.size()));
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
