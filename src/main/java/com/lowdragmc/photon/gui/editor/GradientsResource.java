package com.lowdragmc.photon.gui.editor;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.util.TreeBuilder;
import com.lowdragmc.lowdraglib.gui.widget.GradientColorWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.GradientColor;
import com.lowdragmc.photon.client.emitter.data.number.color.GradientColorTexture;
import com.lowdragmc.photon.client.emitter.data.number.color.RandomGradientColorTexture;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/GradientsResource.class */
public class GradientsResource extends Resource<Gradients> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String name() {
        return "gradients";
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void buildDefault() {
        this.data.put("black white", new Gradients(new GradientColor(-16777216, -1)));
        this.data.put("gradient", new Gradients(new GradientColor(16777215, -1, 16777215)));
        this.data.put("rainbow", new Gradients(new GradientColor(-65536, -23296, -256, -16711936, -16744449, -16776961, -7667457)));
        this.data.put("random", new Gradients(new GradientColor(-1, -1), new GradientColor(-16777216, -16777216)));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    @Nullable
    public Tag serialize(Gradients gradients) {
        return gradients.mo129serializeNBT();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public Gradients deserialize(Tag tag) {
        Gradients gradients = new Gradients();
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            gradients.deserializeNBT(compoundTag);
        }
        return gradients;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public ResourceContainer<Gradients, ? extends Widget> createContainer(ResourcePanel panel) {
        ResourceContainer<Gradients, ImageWidget> container = new ResourceContainer<Gradients, ImageWidget>(this, panel) { // from class: com.lowdragmc.photon.gui.editor.GradientsResource.1
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
                menu.leaf(Icons.ADD_FILE, "add gradient", () -> {
                    String randomName = genNewFileName();
                    this.resource.addResource(randomName, new Gradients());
                    reBuild();
                });
                menu.leaf(Icons.ADD_FILE, "add random gradient", () -> {
                    String randomName = genNewFileName();
                    this.resource.addResource(randomName, new Gradients(new GradientColor(), new GradientColor(-16777216)));
                    reBuild();
                });
                menu.leaf(Icons.REMOVE_FILE, "ldlib.gui.editor.menu.remove", () -> {
                    this.removeSelectedResource();
                });
                return menu;
            }
        };
        container.setWidgetSupplier(k -> {
            return new ImageWidget(0, 0, 60, 15, getResource(k).isRandomGradient() ? new RandomGradientColorTexture(getResource(k).gradient0, getResource(k).gradient1) : new GradientColorTexture(getResource(k).gradient0));
        }).setDragging(this::getResource, gradients -> {
            return gradients.isRandomGradient() ? new RandomGradientColorTexture(gradients.gradient0, gradients.gradient1) : new GradientColorTexture(gradients.gradient0);
        }).setOnEdit(k2 -> {
            openConfigurator(container, container);
        });
        return container;
    }

    private void openConfigurator(ResourceContainer<Gradients, ImageWidget> container, final String key) {
        container.getPanel().getEditor().getConfigPanel().openConfigurator(ConfigPanel.Tab.RESOURCE, new IConfigurable() { // from class: com.lowdragmc.photon.gui.editor.GradientsResource.2
            @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
            public void buildConfigurator(ConfiguratorGroup father) {
                Gradients curves = GradientsResource.this.getResource(key);
                if (curves.isRandomGradient()) {
                    father.addConfigurators(new WrapperConfigurator("gradient0", new GradientColorWidget(0, 0, 180, curves.gradient0)), new WrapperConfigurator("gradient1", new GradientColorWidget(0, 0, 180, curves.gradient1)));
                } else {
                    father.addConfigurators(new WrapperConfigurator("gradient", new GradientColorWidget(0, 0, 180, curves.gradient0)));
                }
            }
        });
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/GradientsResource$Gradients.class */
    public static class Gradients implements ITagSerializable<CompoundTag> {
        @Nonnull
        public GradientColor gradient0;
        @Nullable
        public GradientColor gradient1;

        public Gradients(@Nonnull GradientColor gradient0, @Nullable GradientColor gradient1) {
            this.gradient0 = gradient0;
            this.gradient1 = gradient1;
        }

        public Gradients(@Nonnull GradientColor gradient0) {
            this(gradient0, null);
        }

        public Gradients() {
            this(new GradientColor(), null);
        }

        public boolean isRandomGradient() {
            return this.gradient1 != null;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
        /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
        public CompoundTag mo129serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.m_128365_("a", this.gradient0.mo129serializeNBT());
            if (this.gradient1 != null) {
                tag.m_128365_("b", this.gradient1.mo129serializeNBT());
            }
            return tag;
        }

        @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
        public void deserializeNBT(CompoundTag nbt) {
            CompoundTag m_128423_ = nbt.m_128423_("a");
            if (m_128423_ instanceof CompoundTag) {
                CompoundTag tag = m_128423_;
                this.gradient0.deserializeNBT(tag);
            }
            CompoundTag m_128423_2 = nbt.m_128423_("b");
            if (m_128423_2 instanceof CompoundTag) {
                CompoundTag tag2 = m_128423_2;
                if (this.gradient1 == null) {
                    this.gradient1 = new GradientColor();
                }
                this.gradient1.deserializeNBT(tag2);
            }
        }
    }
}
