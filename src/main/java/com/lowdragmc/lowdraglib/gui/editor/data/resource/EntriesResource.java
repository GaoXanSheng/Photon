package com.lowdragmc.lowdraglib.gui.editor.data.resource;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.EntriesResourceContainer;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import javax.annotation.Nullable;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

@LDLRegister(name = EntriesResource.RESOURCE_NAME, group = "resource")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/data/resource/EntriesResource.class */
public class EntriesResource extends Resource<String> {
    public static final String RESOURCE_NAME = "ldlib.gui.editor.group.entries";

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String name() {
        return RESOURCE_NAME;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void buildDefault() {
        this.data.put("ldlib.author", "Hello KilaBash!");
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void onLoad() {
        LocalizationUtils.setResource(this);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public void unLoad() {
        LocalizationUtils.clearResource();
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public ResourceContainer<String, ? extends Widget> createContainer(ResourcePanel panel) {
        return new EntriesResourceContainer(this, panel);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    @Nullable
    public Tag serialize(String value) {
        return StringTag.m_129297_(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource
    public String deserialize(Tag nbt) {
        if (nbt instanceof StringTag) {
            StringTag stringTag = (StringTag) nbt;
            return stringTag.m_7916_();
        }
        return "missing value";
    }
}
