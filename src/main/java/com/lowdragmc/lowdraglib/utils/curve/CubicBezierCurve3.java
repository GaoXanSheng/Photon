package com.lowdragmc.lowdraglib.utils.curve;

import com.lowdragmc.lowdraglib.utils.Interpolations;
import com.lowdragmc.lowdraglib.utils.Vector3;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/curve/CubicBezierCurve3.class */
public class CubicBezierCurve3 extends Curve<Vector3> {
    public Vector3 p0;
    public Vector3 c0;
    public Vector3 c1;
    public Vector3 p1;

    public CubicBezierCurve3(Vector3 start, Vector3 control1, Vector3 control2, Vector3 end) {
        this.p0 = start;
        this.c0 = control1;
        this.c1 = control2;
        this.p1 = end;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.utils.curve.Curve
    public Vector3 getPoint(float t) {
        return new Vector3(Interpolations.CubicBezier(t, this.p0.x, this.c0.x, this.c1.x, this.p1.x), Interpolations.CubicBezier(t, this.p0.y, this.c0.y, this.c1.y, this.p1.y), Interpolations.CubicBezier(t, this.p0.z, this.c0.z, this.c1.z, this.p1.z));
    }
}
