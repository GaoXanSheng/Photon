package com.lowdragmc.lowdraglib.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Array;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/JsonUtil.class */
public class JsonUtil {
    public static int[] getIntArray(JsonElement array) {
        if (array.isJsonArray()) {
            JsonArray jsonArray = array.getAsJsonArray();
            int[] result = new int[jsonArray.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = jsonArray.get(i).getAsInt();
            }
            return result;
        }
        return new int[0];
    }

    public static JsonArray setIntArray(int[] array) {
        JsonArray jsonArray = new JsonArray();
        for (int i : array) {
            jsonArray.add(Integer.valueOf(i));
        }
        return jsonArray;
    }

    public static <T extends Enum<T>> T[] getEnumArray(JsonElement array, Class<T> enumClass) {
        if (array.isJsonArray()) {
            JsonArray jsonArray = array.getAsJsonArray();
            T[] result = (T[]) ((Enum[]) Array.newInstance((Class<?>) enumClass, jsonArray.size()));
            for (int i = 0; i < result.length; i++) {
                result[i] = enumClass.getEnumConstants()[jsonArray.get(i).getAsInt()];
            }
            return result;
        }
        return (T[]) ((Enum[]) Array.newInstance((Class<?>) enumClass, 0));
    }

    public static <T extends Enum<T>> JsonArray setEnumArray(T[] array, Class<T> enumClass) {
        JsonArray jsonArray = new JsonArray();
        for (T i : array) {
            jsonArray.add(Integer.valueOf(i.ordinal()));
        }
        return jsonArray;
    }

    public static boolean[] getBooleanArray(JsonElement array) {
        if (array.isJsonArray()) {
            JsonArray jsonArray = array.getAsJsonArray();
            boolean[] result = new boolean[jsonArray.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = jsonArray.get(i).getAsBoolean();
            }
            return result;
        }
        return new boolean[0];
    }

    public static JsonArray setBooleanArray(boolean[] array) {
        JsonArray jsonArray = new JsonArray();
        for (boolean i : array) {
            jsonArray.add(Boolean.valueOf(i));
        }
        return jsonArray;
    }

    public static float[] getFloatArray(JsonElement array) {
        if (array.isJsonArray()) {
            JsonArray jsonArray = array.getAsJsonArray();
            float[] result = new float[jsonArray.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = jsonArray.get(i).getAsFloat();
            }
            return result;
        }
        return new float[0];
    }

    public static JsonArray setFloatArray(float[] array) {
        JsonArray jsonArray = new JsonArray();
        for (float i : array) {
            jsonArray.add(Float.valueOf(i));
        }
        return jsonArray;
    }

    public static <T extends Enum<T>> T getEnumOr(JsonObject jsonObject, String key, Class<T> enumClass, T io) {
        JsonElement jsonElement = jsonObject.get(key);
        if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
            T[] enumConstants = enumClass.getEnumConstants();
            if (primitive.isString()) {
                String name = primitive.getAsString();
                for (T t : enumConstants) {
                    if (t.name().equals(name)) {
                        return t;
                    }
                }
            } else if (primitive.isNumber()) {
                return enumConstants[jsonElement.getAsInt()];
            }
        }
        return io;
    }
}
