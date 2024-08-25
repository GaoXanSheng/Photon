package com.lowdragmc.photon.client.emitter.data.number.curve;

import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.curve.ExplicitCubicBezierCurve2;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec2;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/number/curve/ECBCurves.class */
public class ECBCurves extends ArrayList<ExplicitCubicBezierCurve2> implements ITagSerializable<ListTag> {
    public ECBCurves() {
        add(new ExplicitCubicBezierCurve2(new Vec2(0.0f, 0.5f), new Vec2(0.1f, 0.5f), new Vec2(0.9f, 0.5f), new Vec2(1.0f, 0.5f)));
    }

    public ECBCurves(float... data) {
        for (int i = 0; i < data.length; i += 8) {
            add(new ExplicitCubicBezierCurve2(new Vec2(data[i], data[i + 1]), new Vec2(data[i + 2], data[i + 3]), new Vec2(data[i + 4], data[i + 5]), new Vec2(data[i + 6], data[i + 7])));
        }
    }

    public float getCurveY(float x) {
        float value = get(0).p0.f_82471_;
        boolean found = x < get(0).p0.f_82470_;
        if (!found) {
            Iterator<ExplicitCubicBezierCurve2> it = iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ExplicitCubicBezierCurve2 curve = it.next();
                if (x >= curve.p0.f_82470_ && x <= curve.p1.f_82470_) {
                    value = curve.getPoint((x - curve.p0.f_82470_) / (curve.p1.f_82470_ - curve.p0.f_82470_)).f_82471_;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            value = get(size() - 1).p1.f_82471_;
        }
        return value;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public ListTag mo129serializeNBT() {
        ListTag list = new ListTag();
        Iterator<ExplicitCubicBezierCurve2> it = iterator();
        while (it.hasNext()) {
            ExplicitCubicBezierCurve2 curve = it.next();
            list.add(curve.mo129serializeNBT());
        }
        return list;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(ListTag list) {
        clear();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ListTag listTag = (Tag) it.next();
            if (listTag instanceof ListTag) {
                ListTag curve = listTag;
                add(new ExplicitCubicBezierCurve2(curve));
            }
        }
    }
}
