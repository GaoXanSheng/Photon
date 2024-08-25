package com.lowdragmc.photon.gui.editor;

import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.photon.client.emitter.data.material.CustomShaderMaterial;
import com.lowdragmc.photon.client.emitter.data.material.IMaterial;
import com.lowdragmc.photon.client.emitter.data.material.TextureMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/gui/editor/MaterialsResource.class */
public class MaterialsResource extends Resource<IMaterial> {
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String name() {
        return "material";
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void buildDefault() {
        addVanillaTextureMaterial("angry");
        addVanillaTextureMaterial("bubble");
        addVanillaTextureMaterial("damage");
        addVanillaTextureMaterial("flame");
        addVanillaTextureMaterial("glow");
        addVanillaTextureMaterial("heart");
        addVanillaTextureMaterial("lava");
        addVanillaTextureMaterial("note");
        addBuiltinTextureMaterial("kila_tail");
        addBuiltinTextureMaterial("laser");
        addBuiltinTextureMaterial("smoke");
        addBuiltinTextureMaterial("thaumcraft");
        addBuiltinTextureMaterial("ring");
        addBuiltinShaderMaterial("circle");
    }

    private void addVanillaTextureMaterial(String name) {
        this.data.put(name, new TextureMaterial(new ResourceLocation("textures/particle/%s.png".formatted(new Object[]{name}))));
    }

    private void addBuiltinTextureMaterial(String name) {
        this.data.put(name, new TextureMaterial(new ResourceLocation("photon:textures/particle/%s.png".formatted(new Object[]{name}))));
    }

    private void addBuiltinShaderMaterial(String name) {
        this.data.put(name, new CustomShaderMaterial(new ResourceLocation("photon:%s".formatted(new Object[]{name}))));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public ResourceContainer<IMaterial, ? extends Widget> createContainer(ResourcePanel panel) {
        return new MaterialsResourceContainer(this, panel);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public Tag serialize(IMaterial value) {
        return value.mo129serializeNBT();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public IMaterial deserialize(Tag nbt) {
        if (nbt instanceof CompoundTag) {
            CompoundTag tag = (CompoundTag) nbt;
            String type = tag.m_128461_("_type");
            for (Class<? extends IMaterial> clazz : IMaterial.MATERIALS) {
                if (type.equals(clazz.getSimpleName())) {
                    try {
                        IMaterial mat = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                        mat.deserializeNBT(tag);
                        return mat;
                    } catch (Throwable th) {
                    }
                }
            }
            return null;
        }
        return null;
    }
}
