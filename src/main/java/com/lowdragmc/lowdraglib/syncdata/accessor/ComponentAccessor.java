package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.StringPayload;
import net.minecraft.network.chat.Component;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/ComponentAccessor.class */
public class ComponentAccessor extends CustomObjectAccessor<Component> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ Component deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public ComponentAccessor() {
        super(Component.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, Component value) {
        return StringPayload.of(Component.Serializer.m_130703_(value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public Component deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof StringPayload) {
            StringPayload stringPayload = (StringPayload) payload;
            String json = stringPayload.getPayload();
            return Component.Serializer.m_130701_(json);
        }
        return null;
    }
}
