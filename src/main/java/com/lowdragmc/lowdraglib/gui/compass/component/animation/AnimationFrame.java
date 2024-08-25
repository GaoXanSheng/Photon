package com.lowdragmc.lowdraglib.gui.compass.component.animation;

import com.lowdragmc.lowdraglib.gui.compass.CompassManager;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/animation/AnimationFrame.class */
public class AnimationFrame {
    private int duration;
    private int delay;
    private final List<Action> actions;
    private final List<Component> tooltips;
    private int[] actionTime;

    public AnimationFrame duration(int duration) {
        this.duration = duration;
        return this;
    }

    public int delay() {
        return this.delay;
    }

    public AnimationFrame delay(int delay) {
        this.delay = delay;
        return this;
    }

    public List<Action> actions() {
        return this.actions;
    }

    public List<Component> tooltips() {
        return this.tooltips;
    }

    public AnimationFrame() {
        this.duration = -1;
        this.actions = new ArrayList();
        this.tooltips = new ArrayList();
        this.actionTime = new int[0];
    }

    public AnimationFrame addActions(Action... actions) {
        this.actions.addAll(Arrays.asList(actions));
        calculateActionTime();
        return this;
    }

    public AnimationFrame(int frameIndex, Element element) {
        this.duration = -1;
        this.actions = new ArrayList();
        this.tooltips = new ArrayList();
        this.actionTime = new int[0];
        this.duration = XmlUtils.getAsInt(element, "duration", -1);
        this.delay = XmlUtils.getAsInt(element, "delay", 0);
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element actionElement = (Element) node;
                if (actionElement.getNodeName().equals("description")) {
                    this.tooltips.addAll(XmlUtils.getComponents(actionElement, Style.f_131099_));
                }
                Action action = CompassManager.INSTANCE.createAction(actionElement);
                if (action != null) {
                    this.actions.add(action);
                }
            }
        }
        calculateActionTime();
    }

    protected void calculateActionTime() {
        this.actionTime = new int[this.actions.size()];
        int i = 0;
        while (i < this.actions.size()) {
            Action action = this.actions.get(i);
            Action lastAction = i == 0 ? null : this.actions.get(i - 1);
            this.actionTime[i] = action.delay + (i == 0 ? 0 : this.actionTime[i - 1]);
            if (!action.startBeforeLast) {
                int[] iArr = this.actionTime;
                int i2 = i;
                iArr[i2] = iArr[i2] + (lastAction == null ? 0 : lastAction.getDuration());
            }
            i++;
        }
    }

    public int getDuration() {
        if (this.duration > 0) {
            return this.duration;
        }
        if (this.actionTime.length == 0) {
            return 0;
        }
        return this.actionTime[this.actionTime.length - 1] + this.actions.get(this.actions.size() - 1).getDuration();
    }

    protected void onFrameStart(CompassScene scene) {
    }

    protected void onFrameEnd(CompassScene scene) {
    }

    public boolean onFrameTick(CompassScene scene, int frameTick) {
        if (frameTick == 0) {
            onFrameStart(scene);
        }
        if (frameTick >= getDuration()) {
            onFrameEnd(scene);
            return true;
        }
        for (int i = 0; i < this.actionTime.length; i++) {
            if (this.actionTime[i] == frameTick) {
                this.actions.get(i).performAction(this, scene, true);
            } else if (this.actionTime[i] > frameTick) {
                return false;
            }
        }
        return false;
    }

    public void performFrameResult(CompassScene compassScene) {
        for (Action action : this.actions) {
            action.performAction(this, compassScene, false);
        }
    }
}
