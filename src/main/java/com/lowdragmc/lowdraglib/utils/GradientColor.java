package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.Vec2;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/GradientColor.class */
public class GradientColor implements ITagSerializable<CompoundTag> {
    protected List<Vec2> aP;
    protected List<Vec2> rP;
    protected List<Vec2> gP;
    protected List<Vec2> bP;

    public List<Vec2> getAP() {
        return this.aP;
    }

    public List<Vec2> getRP() {
        return this.rP;
    }

    public List<Vec2> getGP() {
        return this.gP;
    }

    public List<Vec2> getBP() {
        return this.bP;
    }

    public GradientColor() {
        this.aP = new ArrayList(List.of(new Vec2(0.0f, 1.0f), new Vec2(1.0f, 1.0f)));
        this.rP = new ArrayList(List.of(new Vec2(0.0f, 1.0f), new Vec2(1.0f, 1.0f)));
        this.gP = new ArrayList(List.of(new Vec2(0.0f, 1.0f), new Vec2(1.0f, 1.0f)));
        this.bP = new ArrayList(List.of(new Vec2(0.0f, 1.0f), new Vec2(1.0f, 1.0f)));
    }

    public GradientColor(int... colors) {
        this.aP = new ArrayList();
        this.rP = new ArrayList();
        this.gP = new ArrayList();
        this.bP = new ArrayList();
        if (colors.length == 1) {
            this.aP.add(new Vec2(0.5f, ColorUtils.alpha(colors[0])));
            this.rP.add(new Vec2(0.5f, ColorUtils.red(colors[0])));
            this.gP.add(new Vec2(0.5f, ColorUtils.green(colors[0])));
            this.bP.add(new Vec2(0.5f, ColorUtils.blue(colors[0])));
        }
        for (int i = 0; i < colors.length; i++) {
            float t = i / (colors.length - 1.0f);
            this.aP.add(new Vec2(t, ColorUtils.alpha(colors[i])));
            this.rP.add(new Vec2(t, ColorUtils.red(colors[i])));
            this.gP.add(new Vec2(t, ColorUtils.green(colors[i])));
            this.bP.add(new Vec2(t, ColorUtils.blue(colors[i])));
        }
    }

    public float get(List<Vec2> data, float t) {
        float value = data.get(0).f_82471_;
        boolean found = t < data.get(0).f_82470_;
        if (!found) {
            int i = 0;
            while (true) {
                if (i >= data.size() - 1) {
                    break;
                }
                Vec2 s = data.get(i);
                Vec2 e = data.get(i + 1);
                if (t < s.f_82470_ || t > e.f_82470_) {
                    i++;
                } else {
                    value = ((s.f_82471_ * (e.f_82470_ - t)) / (e.f_82470_ - s.f_82470_)) + ((e.f_82471_ * (t - s.f_82470_)) / (e.f_82470_ - s.f_82470_));
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            value = data.get(data.size() - 1).f_82471_;
        }
        return value;
    }

    public int getColor(float t) {
        return ColorUtils.color(get(this.aP, t), get(this.rP, t), get(this.gP, t), get(this.bP, t));
    }

    public int getRGBColor(float t) {
        return ColorUtils.color(1.0f, get(this.rP, t), get(this.gP, t), get(this.bP, t));
    }

    public int add(List<Vec2> data, float t, float value) {
        if (data.size() == 0) {
            data.add(new Vec2(t, value));
            return 0;
        } else if (t < data.get(0).f_82470_) {
            data.add(0, new Vec2(t, value));
            return 0;
        } else {
            for (int i = 0; i < data.size() - 1; i++) {
                if (t >= data.get(i).f_82470_ && t <= data.get(i + 1).f_82470_) {
                    data.add(i + 1, new Vec2(t, value));
                    return i + 1;
                }
            }
            data.add(new Vec2(t, value));
            return data.size() - 1;
        }
    }

    public int addAlpha(float t, float value) {
        return add(this.aP, t, value);
    }

    public int addRGB(float t, float r, float g, float b) {
        add(this.rP, t, r);
        add(this.gP, t, g);
        return add(this.bP, t, b);
    }

    private ListTag saveAsTag(List<Vec2> data) {
        ListTag list = new ListTag();
        for (Vec2 vec2 : data) {
            list.add(FloatTag.m_128566_(vec2.f_82470_));
            list.add(FloatTag.m_128566_(vec2.f_82471_));
        }
        return list;
    }

    private void loadFromTag(List<Vec2> data, ListTag list) {
        data.clear();
        for (int i = 0; i < list.size(); i += 2) {
            data.add(new Vec2(list.m_128775_(i), list.m_128775_(i + 1)));
        }
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.m_128365_("a", saveAsTag(this.aP));
        tag.m_128365_("r", saveAsTag(this.rP));
        tag.m_128365_("g", saveAsTag(this.gP));
        tag.m_128365_("b", saveAsTag(this.bP));
        return tag;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag nbt) {
        loadFromTag(this.aP, nbt.m_128437_("a", 5));
        loadFromTag(this.rP, nbt.m_128437_("r", 5));
        loadFromTag(this.gP, nbt.m_128437_("g", 5));
        loadFromTag(this.bP, nbt.m_128437_("b", 5));
    }
}
