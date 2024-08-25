package com.lowdragmc.lowdraglib.utils.curve;

import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.Interpolations;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.Vec2;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/curve/ExplicitCubicBezierCurve2.class */
public class ExplicitCubicBezierCurve2 extends Curve<Vec2> implements ITagSerializable<ListTag> {
    public Vec2 p0;
    public Vec2 c0;
    public Vec2 c1;
    public Vec2 p1;

    public ExplicitCubicBezierCurve2(Vec2 start, Vec2 control1, Vec2 control2, Vec2 end) {
        this.p0 = start;
        this.c0 = control1;
        this.c1 = control2;
        this.p1 = end;
    }

    public ExplicitCubicBezierCurve2(ListTag list) {
        deserializeNBT(list);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.utils.curve.Curve
    public Vec2 getPoint(float t) {
        if (this.c0.f_82470_ == this.p0.f_82470_) {
            return new Vec2(this.p0.f_82470_ + (t * (this.p1.f_82470_ - this.p0.f_82470_)), this.c0.f_82471_ > this.p0.f_82471_ ? this.p0.f_82471_ : this.p1.f_82471_);
        } else if (this.c1.f_82470_ == this.p1.f_82470_) {
            return new Vec2(this.p0.f_82470_ + (t * (this.p1.f_82470_ - this.p0.f_82470_)), this.c1.f_82471_ > this.p1.f_82471_ ? this.p1.f_82471_ : this.p0.f_82471_);
        } else {
            return new Vec2(this.p0.f_82470_ + (t * (this.p1.f_82470_ - this.p0.f_82470_)), (float) Interpolations.CubicBezier(t, this.p0.f_82471_, this.c0.f_82471_, this.c1.f_82471_, this.p1.f_82471_));
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public ListTag mo129serializeNBT() {
        ListTag list = new ListTag();
        list.add(FloatTag.m_128566_(this.p0.f_82470_));
        list.add(FloatTag.m_128566_(this.p0.f_82471_));
        list.add(FloatTag.m_128566_(this.c0.f_82470_));
        list.add(FloatTag.m_128566_(this.c0.f_82471_));
        list.add(FloatTag.m_128566_(this.c1.f_82470_));
        list.add(FloatTag.m_128566_(this.c1.f_82471_));
        list.add(FloatTag.m_128566_(this.p1.f_82470_));
        list.add(FloatTag.m_128566_(this.p1.f_82471_));
        return list;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(ListTag list) {
        this.p0 = new Vec2(list.m_128775_(0), list.m_128775_(1));
        this.c0 = new Vec2(list.m_128775_(2), list.m_128775_(3));
        this.c1 = new Vec2(list.m_128775_(4), list.m_128775_(5));
        this.p1 = new Vec2(list.m_128775_(6), list.m_128775_(7));
    }
}
