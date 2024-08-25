package com.lowdragmc.lowdraglib.gui.compass;

import com.google.gson.JsonParser;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.gui.compass.component.BlankComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.CompassComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.HeaderComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.ImageComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.IngredientComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.RecipeComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.SceneComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.TextBoxComponent;
import com.lowdragmc.lowdraglib.gui.compass.component.animation.Action;
import com.lowdragmc.lowdraglib.gui.compass.component.animation.InformationAction;
import com.lowdragmc.lowdraglib.gui.compass.component.animation.SceneAction;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.mojang.realmsclient.util.JsonUtils;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/CompassManager.class */
public final class CompassManager implements ResourceManagerReloadListener {
    public static final CompassManager INSTANCE = new CompassManager();
    public static final int MAX_HOBER_TICK = 20;
    private int cHoverTick = 0;
    private long startedTick = Long.MAX_VALUE;
    private ItemStack lastStack = ItemStack.f_41583_;
    public boolean devMode = Platform.isDevEnv();
    private final Map<String, Supplier<ILayoutComponent>> COMPONENTS = new HashMap();
    private final Map<String, Function<Element, Action>> ACTION_CREATORS = new HashMap();
    private final Map<String, ICompassUIConfig> CONFIGS = new HashMap();
    private final Map<Item, Set<ResourceLocation>> itemLookup = new HashMap();
    public final Map<String, Map<ResourceLocation, CompassSection>> sections = new HashMap();
    public final Map<String, Map<ResourceLocation, CompassNode>> nodes = new HashMap();
    public final Map<ResourceLocation, Map<String, Document>> nodePages = new HashMap();

    private CompassManager() {
    }

    public void registerAction(String name, Function<Element, Action> creator) {
        this.ACTION_CREATORS.put(name, creator);
    }

    public void registerComponent(String name, Supplier<ILayoutComponent> clazz) {
        this.COMPONENTS.put(name, clazz);
    }

    public void registerUIConfig(String modID, ICompassUIConfig config) {
        this.CONFIGS.put(modID, config);
    }

    public void registerItemLookup(Item item, ResourceLocation nodeName) {
        this.itemLookup.computeIfAbsent(item, k -> {
            return new HashSet();
        }).add(nodeName);
    }

    @Nullable
    public ILayoutComponent createComponent(String name, Element element) {
        Supplier<ILayoutComponent> creator = this.COMPONENTS.get(name);
        if (creator == null) {
            return null;
        }
        return creator.get().fromXml(element);
    }

    @Nullable
    public Action createAction(Element element) {
        Function<Element, Action> creator = this.ACTION_CREATORS.get(element.getTagName());
        if (creator == null) {
            return null;
        }
        return creator.apply(element);
    }

    public ICompassUIConfig getUIConfig(String modID) {
        return this.CONFIGS.getOrDefault(modID, ICompassUIConfig.getDefault());
    }

    public void init() {
        HeaderComponent.Header[] values;
        registerComponent("text", TextBoxComponent::new);
        registerComponent("image", ImageComponent::new);
        for (HeaderComponent.Header header : HeaderComponent.Header.values()) {
            registerComponent(header.name(), HeaderComponent::new);
            registerComponent(header.name(), HeaderComponent::new);
            registerComponent(header.name(), HeaderComponent::new);
        }
        registerComponent("br", BlankComponent::new);
        registerComponent("recipe", RecipeComponent::new);
        registerComponent("scene", SceneComponent::new);
        registerComponent("ingredient", IngredientComponent::new);
        registerComponent("compass", CompassComponent::new);
        registerAction("scene", SceneAction::new);
        registerAction("information", InformationAction::new);
    }

    public void m_6213_(@Nonnull ResourceManager resourceManager) {
        this.sections.clear();
        this.nodes.clear();
        this.nodePages.clear();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.m_214159_("compass/sections", rl -> {
            return rl.m_135815_().endsWith(".json");
        }).entrySet()) {
            ResourceLocation key = entry.getKey();
            Resource resource = entry.getValue();
            try {
                InputStreamReader reader = new InputStreamReader(resource.m_215507_(), StandardCharsets.UTF_8);
                String path = key.m_135815_().replace("compass/sections/", "");
                CompassSection section = new CompassSection(new ResourceLocation(key.m_135827_(), path.substring(0, path.length() - 5)), JsonParser.parseReader(reader).getAsJsonObject());
                this.sections.computeIfAbsent(key.m_135827_(), k -> {
                    return new HashMap();
                }).put(section.sectionName, section);
                reader.close();
            } catch (Exception e) {
                LDLib.LOGGER.error("loading compass section {} failed", entry.getKey(), e);
            }
        }
        for (Map.Entry<ResourceLocation, Resource> entry2 : resourceManager.m_214159_("compass/nodes", rl2 -> {
            return rl2.m_135815_().endsWith(".json");
        }).entrySet()) {
            ResourceLocation key2 = entry2.getKey();
            Resource resource2 = entry2.getValue();
            try {
                InputStreamReader reader2 = new InputStreamReader(resource2.m_215507_(), StandardCharsets.UTF_8);
                String path2 = key2.m_135815_().replace("compass/nodes/", "");
                CompassNode node = new CompassNode(new ResourceLocation(key2.m_135827_(), path2.substring(0, path2.length() - 5)), JsonParser.parseReader(reader2).getAsJsonObject());
                this.nodes.computeIfAbsent(key2.m_135827_(), k2 -> {
                    return new HashMap();
                }).put(node.nodeName, node);
                reader2.close();
            } catch (Exception e2) {
                LDLib.LOGGER.error("loading compass node {} failed", entry2.getKey(), e2);
            }
        }
        for (Map.Entry<String, Map<ResourceLocation, CompassNode>> entry3 : this.nodes.entrySet()) {
            Iterator<Map.Entry<ResourceLocation, CompassNode>> iterator = entry3.getValue().entrySet().iterator();
            while (iterator.hasNext()) {
                CompassNode node2 = iterator.next().getValue();
                ResourceLocation sectionName = new ResourceLocation(JsonUtils.m_90161_("section", node2.getConfig(), "default"));
                CompassSection section2 = this.sections.getOrDefault(entry3.getKey(), Collections.emptyMap()).get(sectionName);
                if (section2 != null) {
                    node2.setSection(section2);
                    node2.getItems().forEach(item -> {
                        this.itemLookup.computeIfAbsent(node2, k3 -> {
                            return new HashSet();
                        }).add(node.nodeName);
                    });
                } else {
                    LDLib.LOGGER.error("node {}'s section {} not found", node2.getNodeName(), sectionName);
                    iterator.remove();
                }
            }
        }
        for (Map<ResourceLocation, CompassNode> nodes : this.nodes.values()) {
            nodes.values().forEach((v0) -> {
                v0.initRelation();
            });
        }
    }

    public static void onComponentClick(String link, ClickData cd) {
        if (ResourceLocation.m_135830_(link)) {
            INSTANCE.openCompass(new ResourceLocation(link));
        }
    }

    public void onCPressed(ItemStack itemStack) {
        long tick = Minecraft.m_91087_().f_91073_.m_46467_();
        if (!ItemStack.m_150942_(this.lastStack, itemStack)) {
            this.lastStack = itemStack;
            this.cHoverTick = 0;
            this.startedTick = tick;
        } else {
            this.cHoverTick = (int) (tick - this.startedTick);
        }
        if (this.cHoverTick < 0 || this.cHoverTick > 20) {
            this.lastStack = ItemStack.f_41583_;
            this.cHoverTick = 0;
            this.startedTick = tick;
        }
        if (this.cHoverTick == 20) {
            openCompass((CompassNode[]) getNodesByItem(itemStack.m_41720_()).toArray(new CompassNode[0]));
        }
    }

    public float getCHoverProgress() {
        return (this.cHoverTick * 1.0f) / 20.0f;
    }

    public void clearCPressed() {
        this.cHoverTick = 0;
        this.startedTick = Long.MAX_VALUE;
        this.lastStack = ItemStack.f_41583_;
    }

    public void openCompass(ResourceLocation nodeLocation) {
        CompassNode node = this.nodes.getOrDefault(nodeLocation.m_135827_(), Collections.emptyMap()).getOrDefault(nodeLocation, null);
        if (node != null) {
            openCompass(node);
        }
    }

    public void openCompass(CompassNode... compassNodes) {
        ModularUI uiTemplate;
        IUIHolder iUIHolder = new IUIHolder() { // from class: com.lowdragmc.lowdraglib.gui.compass.CompassManager.1
            @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
            public ModularUI createUI(Player entityPlayer) {
                return null;
            }

            @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
            public boolean isInvalid() {
                return true;
            }

            @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
            public boolean isRemote() {
                return true;
            }

            @Override // com.lowdragmc.lowdraglib.gui.modular.IUIHolder
            public void markAsDirty() {
            }
        };
        Minecraft minecraft = Minecraft.m_91087_();
        LocalPlayer entityPlayer = minecraft.f_91074_;
        if (compassNodes.length == 1) {
            uiTemplate = new ModularUI(iUIHolder, entityPlayer).widget(new CompassView(compassNodes[0]));
        } else if (compassNodes.length > 1) {
            uiTemplate = new ModularUI(210, 100, iUIHolder, entityPlayer).widget(new CompassSelectorWidget(Arrays.asList(compassNodes)));
        } else {
            return;
        }
        uiTemplate.initWidgets();
        ModularUIGuiContainer ModularUIGuiContainer = new ModularUIGuiContainer(uiTemplate, entityPlayer.f_36096_.f_38840_);
        minecraft.m_91152_(ModularUIGuiContainer);
        entityPlayer.f_36096_ = ModularUIGuiContainer.m_6262_();
    }

    public boolean hasCompass(Item item) {
        return !getNodesByItem(item).isEmpty();
    }

    public List<CompassNode> getNodesByItem(Item item) {
        return this.itemLookup.getOrDefault(item, Collections.emptySet()).stream().map(nodeName -> {
            return this.nodes.getOrDefault(nodeName.m_135827_(), Collections.emptyMap()).get(nodeName);
        }).toList();
    }

    @Nullable
    public CompassNode getNodeByName(ResourceLocation nodeName) {
        return this.nodes.getOrDefault(nodeName.m_135827_(), Collections.emptyMap()).get(nodeName);
    }

    @Nullable
    public CompassSection getSectionByName(ResourceLocation sectionName) {
        return this.sections.getOrDefault(sectionName.m_135827_(), Collections.emptyMap()).get(sectionName);
    }
}
