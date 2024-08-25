package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.compass.component.animation.AnimationFrame;
import com.lowdragmc.lowdraglib.gui.compass.component.animation.CompassScene;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/CompassComponent.class */
public class CompassComponent extends AbstractComponent {
    List<AnimationFrame> frames = new ArrayList();
    boolean useScene = true;
    boolean tickScene = false;
    float zoom = -1.0f;
    int range = 5;
    int height = 250;
    boolean draggable = false;
    boolean scalable = false;
    boolean ortho = false;
    float yaw = 25.0f;

    public List<AnimationFrame> getFrames() {
        return this.frames;
    }

    public boolean isUseScene() {
        return this.useScene;
    }

    public boolean isTickScene() {
        return this.tickScene;
    }

    public float getZoom() {
        return this.zoom;
    }

    public int getRange() {
        return this.range;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isDraggable() {
        return this.draggable;
    }

    public boolean isScalable() {
        return this.scalable;
    }

    public boolean isOrtho() {
        return this.ortho;
    }

    public float getYaw() {
        return this.yaw;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        NodeList frameNodes = element.getElementsByTagName("frame");
        for (int i = 0; i < frameNodes.getLength(); i++) {
            Element frameElement = (Element) frameNodes.item(i);
            this.frames.add(new AnimationFrame(i, frameElement));
        }
        this.useScene = XmlUtils.getAsBoolean(element, "scene", this.useScene);
        this.zoom = XmlUtils.getAsFloat(element, "zoom", this.zoom);
        this.height = XmlUtils.getAsInt(element, "height", this.height);
        this.tickScene = XmlUtils.getAsBoolean(element, "tick-scene", this.tickScene);
        this.draggable = XmlUtils.getAsBoolean(element, "draggable", this.draggable);
        this.scalable = XmlUtils.getAsBoolean(element, "scalable", this.scalable);
        this.ortho = !XmlUtils.getAsString(element, "camera", "ortho").equals("perspective");
        this.zoom = XmlUtils.getAsFloat(element, "zoom", this.zoom);
        this.range = Math.abs(XmlUtils.getAsInt(element, "range", this.range));
        this.yaw = XmlUtils.getAsFloat(element, "yaw", this.yaw);
        this.bottomMargin = 10;
        this.topMargin = 10;
        return super.fromXml(element);
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    protected LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        return currentPage.addStreamWidget(wrapper(new CompassScene(width(currentPage), this)));
    }
}
