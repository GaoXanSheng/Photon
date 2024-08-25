package com.lowdragmc.lowdraglib.gui.editor.data.resource;

import com.lowdragmc.lowdraglib.gui.editor.ui.ResourcePanel;
import com.lowdragmc.lowdraglib.gui.editor.ui.resource.ResourceContainer;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/data/resource/Resource.class */
public abstract class Resource<T> {
    protected final Map<String, T> data = new LinkedHashMap();

    public abstract String name();

    public abstract ResourceContainer<T, ? extends Widget> createContainer(ResourcePanel resourcePanel);

    @Nullable
    public abstract Tag serialize(T t);

    public abstract T deserialize(Tag tag);

    public Map<String, T> getData() {
        return this.data;
    }

    public void buildDefault() {
    }

    public void onLoad() {
    }

    public void unLoad() {
    }

    public T removeResource(String key) {
        return this.data.remove(key);
    }

    public boolean hasResource(String key) {
        return this.data.containsKey(key);
    }

    public void addResource(String key, T resource) {
        this.data.put(key, resource);
    }

    public Set<Map.Entry<String, T>> allResources() {
        return this.data.entrySet();
    }

    public T getResource(String key) {
        return this.data.get(key);
    }

    public T getResourceOrDefault(String key, T defaultValue) {
        return this.data.getOrDefault(key, defaultValue);
    }

    public void merge(Resource<T> resource) {
        resource.data.forEach(k, v -> {
            if (!this.data.containsKey(k)) {
                this.data.put(k, v);
            }
        });
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        this.data.forEach(key, value -> {
            Tag nbt = serialize(value);
            if (nbt != null) {
                tag.m_128365_(tag, nbt);
            }
        });
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.data.clear();
        for (String key : nbt.m_128431_()) {
            this.data.put(key, deserialize(nbt.m_128423_(key)));
        }
    }
}
