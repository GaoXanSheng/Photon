package com.lowdragmc.lowdraglib.utils.interpolate;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/interpolate/Eases.class */
public enum Eases implements IEase {
    EaseLinear(input -> {
        return input;
    }),
    EaseQuadIn(input2 -> {
        return input2 * input2;
    }),
    EaseQuadInOut(input3 -> {
        float input3 = input3 / 0.5f;
        if (input3 < 1.0f) {
            return 0.5f * input3 * input3;
        }
        float input4 = input3 - 1.0f;
        return (-0.5f) * ((input4 * (input4 - 2.0f)) - 1.0f);
    }),
    EaseQuadOut(input4 -> {
        return (-input4) * (input4 - 2.0f);
    });
    
    final IEase ease;

    Eases(IEase ease) {
        this.ease = ease;
    }

    @Override // com.lowdragmc.lowdraglib.utils.interpolate.IEase
    public float getInterpolation(float t) {
        return this.ease.getInterpolation(t);
    }
}
