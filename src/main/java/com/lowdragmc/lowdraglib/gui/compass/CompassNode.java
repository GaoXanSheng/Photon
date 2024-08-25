package com.lowdragmc.lowdraglib.gui.compass;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.json.SimpleIGuiTextureJsonUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/CompassNode.class */
public class CompassNode {
    protected final JsonObject config;
    protected ResourceLocation nodeName;
    protected CompassSection section;
    protected Position position;
    protected int size;
    @Nullable
    protected IGuiTexture background;
    @Nullable
    protected IGuiTexture hoverBackground;
    protected IGuiTexture buttonTexture;
    protected Set<CompassNode> preNodes = new HashSet();
    protected Set<CompassNode> childNodes = new HashSet();
    protected List<Item> items;

    public JsonObject getConfig() {
        return this.config;
    }

    public ResourceLocation getNodeName() {
        return this.nodeName;
    }

    public CompassNode setNodeName(ResourceLocation nodeName) {
        this.nodeName = nodeName;
        return this;
    }

    public CompassSection getSection() {
        return this.section;
    }

    public Position getPosition() {
        return this.position;
    }

    public CompassNode setPosition(Position position) {
        this.position = position;
        return this;
    }

    public int getSize() {
        return this.size;
    }

    @Nullable
    public IGuiTexture getBackground() {
        return this.background;
    }

    @Nullable
    public IGuiTexture getHoverBackground() {
        return this.hoverBackground;
    }

    public CompassNode setBackground(@Nullable IGuiTexture background) {
        this.background = background;
        return this;
    }

    public CompassNode setHoverBackground(@Nullable IGuiTexture hoverBackground) {
        this.hoverBackground = hoverBackground;
        return this;
    }

    public IGuiTexture getButtonTexture() {
        return this.buttonTexture;
    }

    public CompassNode setButtonTexture(IGuiTexture buttonTexture) {
        this.buttonTexture = buttonTexture;
        return this;
    }

    public Set<CompassNode> getPreNodes() {
        return this.preNodes;
    }

    public Set<CompassNode> getChildNodes() {
        return this.childNodes;
    }

    public CompassNode(ResourceLocation nodeName, JsonObject config) {
        this.config = config;
        this.nodeName = nodeName;
        this.background = config.has("background") ? SimpleIGuiTextureJsonUtils.fromJson(config.get("background").getAsJsonObject()) : null;
        this.hoverBackground = config.has("hover_background") ? SimpleIGuiTextureJsonUtils.fromJson(config.get("hover_background").getAsJsonObject()) : null;
        this.buttonTexture = SimpleIGuiTextureJsonUtils.fromJson(config.get("button_texture").getAsJsonObject());
        JsonArray position = config.get("position").getAsJsonArray();
        this.position = new Position(position.get(0).getAsInt(), position.get(1).getAsInt());
        this.size = GsonHelper.m_13824_(config, "size", 24);
    }

    public JsonObject updateJson() {
        JsonArray pos = new JsonArray();
        pos.add(Integer.valueOf(this.position.x));
        pos.add(Integer.valueOf(this.position.y));
        this.config.add("position", pos);
        this.config.addProperty("section", this.section.sectionName.toString());
        if (this.size != 24) {
            this.config.addProperty("size", Integer.valueOf(this.size));
        } else {
            this.config.remove("size");
        }
        if (this.background != null) {
            this.config.add("background", SimpleIGuiTextureJsonUtils.toJson(this.background));
        } else {
            this.config.remove("background");
        }
        if (this.hoverBackground != null) {
            this.config.add("hover_background", SimpleIGuiTextureJsonUtils.toJson(this.hoverBackground));
        } else {
            this.config.remove("hover_background");
        }
        this.config.add("button_texture", SimpleIGuiTextureJsonUtils.toJson(this.buttonTexture));
        if (this.preNodes.isEmpty()) {
            this.config.remove("pre_nodes");
        } else {
            JsonArray pre = new JsonArray();
            for (CompassNode node : this.preNodes) {
                pre.add(node.getNodeName().toString());
            }
            this.config.add("pre_nodes", pre);
        }
        return this.config;
    }

    public void setSection(CompassSection section) {
        this.section = section;
        this.section.addNode(this);
    }

    public void initRelation() {
        if (this.config.has("pre_nodes")) {
            JsonArray pre = this.config.get("pre_nodes").getAsJsonArray();
            Iterator it = pre.iterator();
            while (it.hasNext()) {
                JsonElement element = (JsonElement) it.next();
                ResourceLocation nodeName = new ResourceLocation(element.getAsString());
                CompassNode node = CompassManager.INSTANCE.getNodeByName(nodeName);
                if (node != null) {
                    this.preNodes.add(node);
                    node.childNodes.add(this);
                }
            }
        }
    }

    public final String toString() {
        return this.nodeName.toString();
    }

    public ResourceLocation getPage() {
        return new ResourceLocation(GsonHelper.m_13851_(this.config, "page", "ldlib:missing"));
    }

    public List<Item> getItems() {
        if (this.items == null) {
            this.items = new ArrayList();
            JsonArray items = GsonHelper.m_13832_(this.config, "items", new JsonArray());
            Iterator it = items.iterator();
            while (it.hasNext()) {
                JsonElement element = (JsonElement) it.next();
                String data = element.getAsString();
                if (ResourceLocation.m_135830_(data)) {
                    Item item = (Item) Registry.f_122827_.m_7745_(new ResourceLocation(data));
                    if (item != Items.f_41852_) {
                        this.items.add(item);
                    }
                } else if (data.startsWith("#") && ResourceLocation.m_135830_(data.substring(1))) {
                    TagKey<Item> tag = TagKey.m_203882_(Registry.f_122904_, new ResourceLocation(data.substring(1)));
                    Optional<HolderSet.Named<Item>> tagCollection = Registry.f_122827_.m_203431_(tag);
                    tagCollection.ifPresent(named -> {
                        named.forEach(holder -> {
                            this.items.add((Item) holder.m_203334_());
                        });
                    });
                }
            }
        }
        return this.items;
    }

    public Component getChatComponent() {
        return Component.m_237115_(this.nodeName.m_214296_("compass.node"));
    }
}
