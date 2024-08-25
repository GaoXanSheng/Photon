package com.lowdragmc.lowdraglib.gui.editor.accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/accessors/TypesAccessor.class */
public abstract class TypesAccessor<T> implements IConfiguratorAccessor<T> {
    public Set<Class<?>> types = new HashSet();

    public TypesAccessor(Class<?>... types) {
        this.types.addAll(List.of((Object[]) types));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.accessors.IConfiguratorAccessor
    public boolean test(Class<?> type) {
        return this.types.contains(type);
    }
}
