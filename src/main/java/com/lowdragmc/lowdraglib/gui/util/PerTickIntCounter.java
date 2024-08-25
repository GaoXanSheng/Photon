package com.lowdragmc.lowdraglib.gui.util;

import net.minecraft.world.level.Level;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/PerTickIntCounter.class */
public class PerTickIntCounter {
    private final int defaultValue;
    private long lastUpdatedWorldTime;
    private int currentValue;

    public PerTickIntCounter(int defaultValue) {
        this.defaultValue = defaultValue;
        this.currentValue = defaultValue;
    }

    private void checkValueState(Level world) {
        long currentWorldTime = world.m_46467_();
        if (currentWorldTime != this.lastUpdatedWorldTime) {
            this.lastUpdatedWorldTime = currentWorldTime;
            this.currentValue = this.defaultValue;
        }
    }

    public int get(Level world) {
        checkValueState(world);
        return this.currentValue;
    }

    public void increment(Level world, int value) {
        checkValueState(world);
        this.currentValue += value;
    }
}
