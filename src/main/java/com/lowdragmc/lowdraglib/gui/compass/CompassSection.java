package com.lowdragmc.lowdraglib.gui.compass;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.json.SimpleIGuiTextureJsonUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/CompassSection.class */
public class CompassSection {
    private final JsonObject config;
    public ResourceLocation sectionName;
    public int priority;
    public final Map<ResourceLocation, CompassNode> nodes = new HashMap();
    protected IGuiTexture buttonTexture;
    protected IGuiTexture backgroundTexture;

    public JsonObject getConfig() {
        return this.config;
    }

    public ResourceLocation getSectionName() {
        return this.sectionName;
    }

    public void setSectionName(ResourceLocation sectionName) {
        this.sectionName = sectionName;
    }

    public IGuiTexture getButtonTexture() {
        return this.buttonTexture;
    }

    public void setButtonTexture(IGuiTexture buttonTexture) {
        this.buttonTexture = buttonTexture;
    }

    public IGuiTexture getBackgroundTexture() {
        return this.backgroundTexture;
    }

    public void setBackgroundTexture(IGuiTexture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public CompassSection(ResourceLocation sectionName, JsonObject config) {
        this.config = config;
        this.sectionName = sectionName;
        this.priority = JsonUtils.m_90153_("priority", config, 0);
        setButtonTexture(SimpleIGuiTextureJsonUtils.fromJson(config.get("button_texture").getAsJsonObject()));
        setBackgroundTexture(config.has("background_texture") ? SimpleIGuiTextureJsonUtils.fromJson(config.get("background_texture").getAsJsonObject()) : null);
    }

    public JsonObject updateJson() {
        this.config.addProperty("priority", Integer.valueOf(this.priority));
        this.config.add("button_texture", SimpleIGuiTextureJsonUtils.toJson(this.buttonTexture));
        if (this.backgroundTexture != null) {
            this.config.add("background_texture", SimpleIGuiTextureJsonUtils.toJson(this.backgroundTexture));
        }
        return this.config;
    }

    public void addNode(CompassNode compassNode) {
        this.nodes.put(compassNode.getNodeName(), compassNode);
    }

    public final String toString() {
        return this.sectionName.toString();
    }

    public CompassNode getNode(ResourceLocation nodeName) {
        return this.nodes.get(nodeName);
    }

    public Component getChatComponent() {
        return Component.m_237115_(this.sectionName.m_214296_("compass.section"));
    }
}
