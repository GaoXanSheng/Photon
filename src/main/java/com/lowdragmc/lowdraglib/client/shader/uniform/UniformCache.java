package com.lowdragmc.lowdraglib.client.shader.uniform;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.shader.uniform.UniformEntry;
import com.mojang.math.Matrix4f;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import net.minecraft.util.FastColor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/UniformCache.class */
public class UniformCache {
    protected static final FloatBuffer MATRIX4F_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private final Int2ObjectMap<UniformEntry<?>> entryCache = new Int2ObjectOpenHashMap();
    private final Object2IntMap<String> locationCache = new Object2IntOpenHashMap();
    private final int programId;

    public UniformCache(int programId) {
        this.programId = programId;
    }

    public void invalidate() {
        this.entryCache.clear();
        this.locationCache.clear();
    }

    public void glUniform1F(String location, float v0) {
        glUniformF(location, loc -> {
            GL20.glUniform1f(loc, v0);
        }, v0);
    }

    public void glUniform2F(String location, float v0, float v1) {
        glUniformF(location, loc -> {
            GL20.glUniform2f(loc, v0, v1);
        }, v0, v1);
    }

    public void glUniform3F(String location, float v0, float v1, float v2) {
        glUniformF(location, loc -> {
            GL20.glUniform3f(loc, v0, v1, v2);
        }, v0, v1, v2);
    }

    public void glUniform4F(String location, float v0, float v1, float v2, float v3) {
        glUniformF(location, loc -> {
            GL20.glUniform4f(loc, v0, v1, v2, v3);
        }, v0, v1, v2, v3);
    }

    public void fillRGBAColor(String location, int color) {
        glUniform4F(location, FastColor.ARGB32.m_13665_(color) / 255.0f, FastColor.ARGB32.m_13667_(color) / 255.0f, FastColor.ARGB32.m_13669_(color) / 255.0f, FastColor.ARGB32.m_13655_(color) / 255.0f);
    }

    private void glUniformF(String location, IntConsumer callback, float... values) {
        glUniform(location, UniformEntry.IS_FLOAT, UniformEntry.FloatUniformEntry.NEW, callback, values);
    }

    public void glUniform1I(String location, int v0) {
        glUniformI(location, loc -> {
            GL20.glUniform1i(loc, v0);
        }, v0);
    }

    public void glUniform2I(String location, int v0, int v1) {
        glUniformI(location, loc -> {
            GL20.glUniform2i(loc, v0, v1);
        }, v0, v1);
    }

    public void glUniform3I(String location, int v0, int v1, int v2) {
        glUniformI(location, loc -> {
            GL20.glUniform3i(loc, v0, v1, v2);
        }, v0, v1, v2);
    }

    public void glUniform4I(String location, int v0, int v1, int v2, int v3) {
        glUniformI(location, loc -> {
            GL20.glUniform4i(loc, v0, v1, v2, v3);
        }, v0, v1, v2, v3);
    }

    private void glUniformI(String location, IntConsumer callback, int... values) {
        glUniform(location, UniformEntry.IS_INT, UniformEntry.IntUniformEntry.NEW, callback, values);
    }

    public void glUniformMatrix2(String location, boolean transpose, FloatBuffer matrix) {
        glUniformMatrix(location, loc -> {
            GL20.glUniformMatrix2fv(loc, transpose, matrix);
        }, transpose, matrix);
    }

    public void glUniformMatrix4(String location, boolean transpose, FloatBuffer matrix) {
        glUniformMatrix(location, loc -> {
            GL20.glUniformMatrix4fv(loc, transpose, matrix);
        }, transpose, matrix);
    }

    public void glUniformMatrix(String location, IntConsumer callback, boolean transpose, FloatBuffer matrix) {
        glUniform(location, UniformEntry.IS_MATRIX, UniformEntry.MatrixUniformEntry.NEW, callback, ImmutablePair.of(matrix, Boolean.valueOf(transpose)));
    }

    public void glUniformBoolean(String location, boolean value) {
        glUniform(location, UniformEntry.IS_BOOLEAN, UniformEntry.BooleanUniformEntry.NEW, loc -> {
            GL20.glUniform1i(loc, value ? 1 : 0);
        }, Boolean.valueOf(value));
    }

    public void glUniformMatrix4F(String location, Matrix4f matrix4f) {
        glUniform(location, UniformEntry.IS_MATRIX4F, UniformEntry.Matrix4FUniformEntry.NEW, loc -> {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                FloatBuffer buffer = stack.mallocFloat(16);
                matrix4f.m_27650_(buffer);
                buffer.rewind();
                GL20.glUniformMatrix4fv(loc, false, buffer);
                if (stack != null) {
                    stack.close();
                }
            } catch (Throwable th) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }, matrix4f);
    }

    private int getUniformLocation(String name) {
        int uniformLocation;
        if (this.locationCache.containsKey(name)) {
            uniformLocation = this.locationCache.get(name).intValue();
        } else {
            uniformLocation = GL20.glGetUniformLocation(this.programId, name);
            this.locationCache.put(name, uniformLocation);
        }
        return uniformLocation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> void glUniform(String location, Predicate<UniformEntry<?>> isType, Function<T, UniformEntry<T>> createUniform, IntConsumer applyCallback, T value) {
        int loc = getUniformLocation(location);
        if (loc == -1) {
            if (Platform.isDevEnv()) {
            }
            return;
        }
        boolean update = true;
        if (this.entryCache.containsKey(loc)) {
            UniformEntry uniformEntry = (UniformEntry) this.entryCache.get(loc);
            if (isType.test(uniformEntry)) {
                update = !uniformEntry.check(value);
            }
        }
        if (update) {
            UniformEntry<T> entry = createUniform.apply(value);
            applyCallback.accept(loc);
            this.entryCache.put(loc, entry);
        }
    }
}
