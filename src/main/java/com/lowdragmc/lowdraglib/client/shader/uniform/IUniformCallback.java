package com.lowdragmc.lowdraglib.client.shader.uniform;

@FunctionalInterface
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/uniform/IUniformCallback.class */
public interface IUniformCallback {
    void apply(UniformCache uniformCache);

    default IUniformCallback with(IUniformCallback callback) {
        return cache -> {
            apply(callback);
            callback.apply(callback);
        };
    }
}
