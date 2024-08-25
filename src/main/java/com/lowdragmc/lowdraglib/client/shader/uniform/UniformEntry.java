package com.lowdragmc.lowdraglib.client.shader.uniform;

import com.mojang.math.Matrix4f;
import java.nio.FloatBuffer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformEntry.class */
public abstract class UniformEntry<T> {
    public static final Predicate<UniformEntry<?>> IS_INT = uniformEntry -> {
        return uniformEntry instanceof IntUniformEntry;
    };
    public static final Predicate<UniformEntry<?>> IS_FLOAT = uniformEntry -> {
        return uniformEntry instanceof FloatUniformEntry;
    };
    public static final Predicate<UniformEntry<?>> IS_MATRIX = uniformEntry -> {
        return uniformEntry instanceof MatrixUniformEntry;
    };
    public static final Predicate<UniformEntry<?>> IS_MATRIX4F = uniformEntry -> {
        return uniformEntry instanceof Matrix4FUniformEntry;
    };
    public static final Predicate<UniformEntry<?>> IS_BOOLEAN = uniformEntry -> {
        return uniformEntry instanceof BooleanUniformEntry;
    };

    public abstract boolean check(T t);

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformEntry$IntUniformEntry.class */
    public static class IntUniformEntry extends UniformEntry<int[]> {
        public static Function<int[], UniformEntry<int[]>> NEW = IntUniformEntry::new;
        private final int[] cache;

        public IntUniformEntry(int... cache) {
            this.cache = cache;
        }

        @Override // com.lowdragmc.lowdraglib.client.shader.uniform.UniformEntry
        public boolean check(int... other) {
            if (this.cache.length != other.length) {
                return false;
            }
            for (int i = 0; i < this.cache.length; i++) {
                if (this.cache[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformEntry$FloatUniformEntry.class */
    public static class FloatUniformEntry extends UniformEntry<float[]> {
        public static Function<float[], UniformEntry<float[]>> NEW = FloatUniformEntry::new;
        private final float[] cache;

        public FloatUniformEntry(float... cache) {
            this.cache = cache;
        }

        @Override // com.lowdragmc.lowdraglib.client.shader.uniform.UniformEntry
        public boolean check(float... other) {
            if (this.cache.length != other.length) {
                return false;
            }
            for (int i = 0; i < this.cache.length; i++) {
                if (this.cache[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformEntry$MatrixUniformEntry.class */
    public static class MatrixUniformEntry extends UniformEntry<Pair<FloatBuffer, Boolean>> {
        public static Function<Pair<FloatBuffer, Boolean>, UniformEntry<Pair<FloatBuffer, Boolean>>> NEW = MatrixUniformEntry::new;
        private final FloatBuffer matrix;
        private final boolean transpose;

        public MatrixUniformEntry(Pair<FloatBuffer, Boolean> other) {
            this.matrix = (FloatBuffer) other.getKey();
            this.transpose = ((Boolean) other.getValue()).booleanValue();
        }

        @Override // com.lowdragmc.lowdraglib.client.shader.uniform.UniformEntry
        public boolean check(Pair<FloatBuffer, Boolean> other) {
            return this.matrix.equals(other.getKey()) && this.transpose == ((Boolean) other.getValue()).booleanValue();
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformEntry$Matrix4FUniformEntry.class */
    public static class Matrix4FUniformEntry extends UniformEntry<Matrix4f> {
        public static Function<Matrix4f, UniformEntry<Matrix4f>> NEW = Matrix4FUniformEntry::new;
        private final Matrix4f matrix;

        public Matrix4FUniformEntry(Matrix4f other) {
            this.matrix = other.m_27658_();
        }

        @Override // com.lowdragmc.lowdraglib.client.shader.uniform.UniformEntry
        public boolean check(Matrix4f other) {
            return this.matrix.equals(other);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformEntry$BooleanUniformEntry.class */
    public static class BooleanUniformEntry extends UniformEntry<Boolean> {
        public static Function<Boolean, UniformEntry<Boolean>> NEW = (v1) -> {
            return new BooleanUniformEntry(v1);
        };
        private final boolean bool;

        public BooleanUniformEntry(boolean bool) {
            this.bool = bool;
        }

        @Override // com.lowdragmc.lowdraglib.client.shader.uniform.UniformEntry
        public boolean check(Boolean other) {
            return this.bool == other.booleanValue();
        }
    }
}
