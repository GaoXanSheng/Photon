package com.lowdragmc.photon.gui.editor;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveLineWidget;
import com.lowdragmc.photon.client.emitter.data.number.curve.CurveTexture;
import com.lowdragmc.photon.client.emitter.data.number.curve.ECBCurves;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurveLineWidget;
import com.lowdragmc.photon.client.emitter.data.number.curve.RandomCurveTexture;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/CurvesResource.class */
public class CurvesResource extends Resource<Curves> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String name() {
        return "curves";
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void buildDefault() {
        this.data.put("middle", new Curves(new ECBCurves()));
        this.data.put("linear up", new Curves(new ECBCurves(0.0f, 0.0f, 0.1f, 0.3f, 0.9f, 0.7f, 1.0f, 1.0f)));
        this.data.put("linear down", new Curves(new ECBCurves(0.0f, 1.0f, 0.1f, 0.7f, 0.9f, 0.3f, 1.0f, 0.0f)));
        this.data.put("smooth up", new Curves(new ECBCurves(0.0f, 0.0f, 0.1f, 0.0f, 0.9f, 1.0f, 1.0f, 1.0f)));
        this.data.put("smooth down", new Curves(new ECBCurves(0.0f, 1.0f, 0.1f, 1.0f, 0.9f, 0.0f, 1.0f, 0.0f)));
        this.data.put("concave", new Curves(new ECBCurves(0.0f, 1.0f, 0.1f, 1.0f, 0.4f, 0.0f, 0.5f, 0.0f, 0.5f, 0.0f, 0.6f, 0.0f, 0.9f, 1.0f, 1.0f, 1.0f)));
        this.data.put("convex", new Curves(new ECBCurves(0.0f, 0.0f, 0.1f, 0.0f, 0.4f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.6f, 1.0f, 0.9f, 0.0f, 1.0f, 0.0f)));
        this.data.put("random full", new Curves(new ECBCurves(0.0f, 1.0f, 0.1f, 1.0f, 0.9f, 1.0f, 1.0f, 1.0f), new ECBCurves(0.0f, 0.0f, 0.1f, 0.0f, 0.9f, 0.0f, 1.0f, 0.0f)));
        this.data.put("random up", new Curves(new ECBCurves(0.0f, 1.0f, 0.1f, 1.0f, 0.9f, 1.0f, 1.0f, 1.0f), new ECBCurves(0.0f, 0.0f, 0.1f, 0.0f, 0.9f, 1.0f, 1.0f, 1.0f)));
        this.data.put("random down", new Curves(new ECBCurves(0.0f, 1.0f, 0.1f, 1.0f, 0.9f, 1.0f, 1.0f, 1.0f), new ECBCurves(0.0f, 1.0f, 0.1f, 1.0f, 0.9f, 0.0f, 1.0f, 0.0f)));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    @Nullable
    public Tag serialize(Curves curves) {
        return curves.mo129serializeNBT();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public Curves deserialize(Tag tag) {
        Curves curves = new Curves();
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            curves.deserializeNBT(compoundTag);
        }
        return curves;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public ResourceContainer<Curves, ? extends Widget> createContainer(ResourcePanel panel) {
        ResourceContainer<Curves, ImageWidget> container = new ResourceContainer<Curves, ImageWidget>(this, panel) { // from class: com.lowdragmc.photon.gui.editor.CurvesResource.1
            @Override // com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer
            protected TreeBuilder.Menu getMenu() {
                TreeBuilder.Menu menu = TreeBuilder.Menu.start();
                if (this.onEdit != null) {
                    menu.leaf(Icons.EDIT_FILE, "ldlib.gui.editor.menu.edit", () -> {
                        this.editResource();
                    });
                }
                menu.leaf("ldlib.gui.editor.menu.rename", () -> {
                    this.renameResource();
                });
                menu.crossLine();
                menu.leaf(Icons.COPY, "ldlib.gui.editor.menu.copy", () -> {
                    this.copy();
                });
                menu.leaf(Icons.PASTE, "ldlib.gui.editor.menu.paste", () -> {
                    this.paste();
                });
                menu.leaf(Icons.ADD_FILE, "add curve", () -> {
                    String randomName = genNewFileName();
                    this.resource.addResource(randomName, new Curves());
                    reBuild();
                });
                menu.leaf(Icons.ADD_FILE, "add random curve", () -> {
                    String randomName = genNewFileName();
                    this.resource.addResource(randomName, new Curves(new ECBCurves(), new ECBCurves(0.0f, 0.2f, 0.1f, 0.2f, 0.9f, 0.2f, 1.0f, 0.2f)));
                    reBuild();
                });
                menu.leaf(Icons.REMOVE_FILE, "ldlib.gui.editor.menu.remove", () -> {
                    this.removeSelectedResource();
                });
                return menu;
            }
        };
        container.setWidgetSupplier(k -> {
            IGuiTexture[] iGuiTextureArr = new IGuiTexture[2];
            iGuiTextureArr[0] = ColorPattern.T_WHITE.rectTexture();
            iGuiTextureArr[1] = getResource(k).isRandomCurve() ? new RandomCurveTexture(getResource(k).curves0, getResource(k).curves1) : new CurveTexture(getResource(k).curves0);
            return new ImageWidget(0, 0, 60, 15, new GuiTextureGroup(iGuiTextureArr));
        }).setDragging(this::getResource, curves -> {
            IGuiTexture[] iGuiTextureArr = new IGuiTexture[2];
            iGuiTextureArr[0] = ColorPattern.T_WHITE.rectTexture();
            iGuiTextureArr[1] = curves.isRandomCurve() ? new RandomCurveTexture(curves.curves0, curves.curves1) : new CurveTexture(curves.curves0);
            return new GuiTextureGroup(iGuiTextureArr);
        }).setOnEdit(k2 -> {
            openConfigurator(container, container);
        });
        return container;
    }

    private void openConfigurator(ResourceContainer<Curves, ImageWidget> container, final String key) {
        container.getPanel().getEditor().getConfigPanel().openConfigurator(ConfigPanel.Tab.RESOURCE, new IConfigurable() { // from class: com.lowdragmc.photon.gui.editor.CurvesResource.2
            @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
            public void buildConfigurator(ConfiguratorGroup father) {
                Curves curves = CurvesResource.this.getResource(key);
                if (curves.isRandomCurve()) {
                    RandomCurveLineWidget curveLine = new RandomCurveLineWidget(0, 0, 180, 60, curves.curves0, curves.curves1);
                    curveLine.setGridSize(new Size(6, 2));
                    curveLine.setHoverTips(coord -> {
                        return Component.m_237113_("x: %f, y:%f".formatted(new Object[]{Float.valueOf(coord.f_82470_), Float.valueOf(coord.f_82471_)}));
                    });
                    curveLine.setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
                    WrapperConfigurator configurator = new WrapperConfigurator("color", curveLine);
                    father.addConfigurators(configurator);
                    return;
                }
                CurveLineWidget curveLine2 = new CurveLineWidget(0, 0, 180, 60, curves.curves0);
                curveLine2.setGridSize(new Size(6, 2));
                curveLine2.setHoverTips(coord2 -> {
                    return Component.m_237113_("x: %f, y:%f".formatted(new Object[]{Float.valueOf(coord2.f_82470_), Float.valueOf(coord2.f_82471_)}));
                });
                curveLine2.setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
                WrapperConfigurator configurator2 = new WrapperConfigurator("color", curveLine2);
                father.addConfigurators(configurator2);
            }
        });
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/CurvesResource$Curves.class */
    public static class Curves implements ITagSerializable<CompoundTag> {
        @Nonnull
        public ECBCurves curves0;
        @Nullable
        public ECBCurves curves1;

        public Curves(@Nonnull ECBCurves curves0, @Nullable ECBCurves curves1) {
            this.curves0 = curves0;
            this.curves1 = curves1;
        }

        public Curves(@Nonnull ECBCurves curves0) {
            this(curves0, null);
        }

        public Curves() {
            this(new ECBCurves(), null);
        }

        public boolean isRandomCurve() {
            return this.curves1 != null;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
        /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
        public CompoundTag mo129serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.m_128365_("a", this.curves0.mo129serializeNBT());
            if (this.curves1 != null) {
                tag.m_128365_("b", this.curves1.mo129serializeNBT());
            }
            return tag;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
        public void deserializeNBT(CompoundTag nbt) {
            ListTag m_128423_ = nbt.m_128423_("a");
            if (m_128423_ instanceof ListTag) {
                ListTag list = m_128423_;
                this.curves0.deserializeNBT(list);
            }
            ListTag m_128423_2 = nbt.m_128423_("b");
            if (m_128423_2 instanceof ListTag) {
                ListTag list2 = m_128423_2;
                if (this.curves1 == null) {
                    this.curves1 = new ECBCurves();
                }
                this.curves1.deserializeNBT(list2);
            }
        }
    }
}
