package com.lowdragmc.lowdraglib.gui.editor.data.resource;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.TexturesResourceContainer;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.TabContainer;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@LDLRegister(name = TexturesResource.RESOURCE_NAME, group = "resource")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/data/resource/TexturesResource.class */
public class TexturesResource extends Resource<IGuiTexture> {
    public static final String RESOURCE_NAME = "ldlib.gui.editor.group.textures";

    public TexturesResource() {
        this.data.put("empty", IGuiTexture.EMPTY);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void buildDefault() {
        this.data.put("border background", ResourceBorderTexture.BORDERED_BACKGROUND);
        this.data.put("button", ResourceBorderTexture.BUTTON_COMMON);
        this.data.put("slot", new ResourceTexture("ldlib:textures/gui/slot.png"));
        this.data.put("fluid slot", new ResourceTexture("ldlib:textures/gui/fluid_slot.png"));
        this.data.put("tab", TabContainer.TABS_LEFT.getSubTexture(0.0f, 0.0f, 0.5f, 0.33333334f));
        this.data.put("tab pressed", TabContainer.TABS_LEFT.getSubTexture(0.5f, 0.0f, 0.5f, 0.33333334f));
        for (AnnotationDetector.Wrapper<LDLRegister, IGuiTexture> wrapper : AnnotationDetector.REGISTER_TEXTURES) {
            this.data.put("ldlib.gui.editor.register.texture." + wrapper.annotation().name(), wrapper.creator().get());
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String name() {
        return RESOURCE_NAME;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public ResourceContainer<IGuiTexture, ? extends Widget> createContainer(ResourcePanel panel) {
        return new TexturesResourceContainer(this, panel);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public Tag serialize(IGuiTexture value) {
        return IGuiTexture.serializeWrapper(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public IGuiTexture deserialize(Tag nbt) {
        if (nbt instanceof CompoundTag) {
            CompoundTag tag = (CompoundTag) nbt;
            return IGuiTexture.deserializeWrapper(tag);
        }
        return null;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void deserializeNBT(CompoundTag nbt) {
        this.data.clear();
        this.data.put("empty", IGuiTexture.EMPTY);
        for (String key : nbt.m_128431_()) {
            this.data.put(key, deserialize(nbt.m_128423_(key)));
        }
        for (IGuiTexture texture : this.data.values()) {
            texture.setUIResource(this);
        }
    }
}
