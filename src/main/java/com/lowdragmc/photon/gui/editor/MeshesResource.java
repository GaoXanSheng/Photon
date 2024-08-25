package com.lowdragmc.photon.gui.editor;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.editor.ui.ConfigPanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.photon.client.emitter.data.shape.MeshData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/MeshesResource.class */
public class MeshesResource extends Resource<MeshData> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String name() {
        return "mesh";
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void buildDefault() {
        addModelMesh("pedestal");
    }

    public void addModelMesh(String model) {
        MeshData mesh = new MeshData(LDLib.location("block/" + model));
        mesh.meshName = model;
        this.data.put(model, mesh);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public ResourceContainer<MeshData, ? extends Widget> createContainer(ResourcePanel panel) {
        ResourceContainer<MeshData, ImageWidget> container = new ResourceContainer<MeshData, ImageWidget>(this, panel) { // from class: com.lowdragmc.photon.gui.editor.MeshesResource.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer
            public void renameResource() {
                if (this.selected != null) {
                    DialogWidget.showStringEditorDialog(Editor.INSTANCE, LocalizationUtils.format("ldlib.gui.editor.tips.rename", new Object[0]) + " " + LocalizationUtils.format(this.resource.name(), new Object[0]), this.selected, s -> {
                        if (this.resource.hasResource(s)) {
                            return false;
                        }
                        if (this.renamePredicate != null) {
                            return this.renamePredicate.test(s);
                        }
                        return true;
                    }, s2 -> {
                        if (s2 == null) {
                            return;
                        }
                        MeshData stored = (MeshData) this.resource.removeResource(this.selected);
                        stored.meshName = s2;
                        this.resource.addResource(s2, stored);
                        reBuild();
                    });
                }
            }
        };
        container.setWidgetSupplier(k -> {
            return new ImageWidget(0, 0, 30, 30, Icons.MESH.copy());
        }).setDragging(this::getResource, r -> {
            return Icons.MESH.copy();
        }).setOnEdit(k2 -> {
            panel.getEditor().getConfigPanel().openConfigurator(ConfigPanel.Tab.RESOURCE, getResource(panel));
        }).setOnAdd(key -> {
            return new MeshData();
        });
        return container;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public Tag serialize(MeshData value) {
        return value.mo129serializeNBT();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public MeshData deserialize(Tag nbt) {
        if (nbt instanceof CompoundTag) {
            CompoundTag tag = (CompoundTag) nbt;
            MeshData mesh = new MeshData();
            mesh.deserializeNBT(tag);
            return mesh;
        }
        return null;
    }
}
