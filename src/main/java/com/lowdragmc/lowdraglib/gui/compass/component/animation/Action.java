package com.lowdragmc.lowdraglib.gui.compass.component.animation;

import com.lowdragmc.lowdraglib.utils.XmlUtils;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/animation/Action.class */
public abstract class Action {
    protected int delay;
    protected boolean startBeforeLast;

    public abstract int getDuration();

    public abstract void performAction(AnimationFrame animationFrame, CompassScene compassScene, boolean z);

    public int delay() {
        return this.delay;
    }

    public Action delay(int delay) {
        this.delay = delay;
        return this;
    }

    public boolean startBeforeLast() {
        return this.startBeforeLast;
    }

    public Action startBeforeLast(boolean startBeforeLast) {
        this.startBeforeLast = startBeforeLast;
        return this;
    }

    public Action() {
    }

    public Action(Element element) {
        this.delay = XmlUtils.getAsInt(element, "delay", 0);
        this.startBeforeLast = XmlUtils.getAsBoolean(element, "start-before-last", false);
    }
}
