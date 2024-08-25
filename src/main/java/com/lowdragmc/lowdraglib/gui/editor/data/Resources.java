package com.lowdragmc.lowdraglib.gui.editor.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/data/Resources.class */
public class Resources {
    public final Map<String, Resource<?>> resources;

    protected Resources() {
        this.resources = new LinkedHashMap();
        for (AnnotationDetector.Wrapper<LDLRegister, Resource> wrapper : AnnotationDetector.REGISTER_RESOURCES) {
            Resource resource = wrapper.creator().get();
            this.resources.put(resource.name(), resource);
        }
    }

    public Resources(Map<String, Resource<?>> resources) {
        this.resources = resources;
    }

    public static Resources emptyResource() {
        return new Resources(new LinkedHashMap());
    }

    public static Resources fromNBT(CompoundTag tag) {
        Resources resource = new Resources();
        resource.deserializeNBT(tag);
        return resource;
    }

    public static Resources defaultResource() {
        Resources resources = new Resources();
        resources.resources.values().forEach((v0) -> {
            v0.buildDefault();
        });
        return resources;
    }

    public void merge(Resources resources) {
        this.resources.forEach(k, v -> {
            if (resources.resources.containsKey(k)) {
                Resource f = resources.resources.get(k);
                v.merge(f);
            }
        });
    }

    public void load() {
        this.resources.values().forEach((v0) -> {
            v0.onLoad();
        });
    }

    public void dispose() {
        this.resources.values().forEach((v0) -> {
            v0.unLoad();
        });
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        this.resources.forEach(key, resource -> {
            tag.m_128365_(key, resource.serializeNBT());
        });
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.resources.forEach(k, v -> {
            v.deserializeNBT(nbt.m_128469_(k));
        });
    }
}
