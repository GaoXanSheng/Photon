package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.UIResourceTexture;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/IGuiTextureAccessor.class */
public class IGuiTextureAccessor extends CustomObjectAccessor<IGuiTexture> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ IGuiTexture deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public IGuiTextureAccessor() {
        super(IGuiTexture.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, IGuiTexture value) {
        CompoundTag tag = IGuiTexture.serializeWrapper(value);
        if (tag == null) {
            tag = new CompoundTag();
            if (value instanceof UIResourceTexture) {
                UIResourceTexture uiResourceTexture = (UIResourceTexture) value;
                tag.m_128359_("type", "ui_resource");
                tag.m_128359_("key", uiResourceTexture.key);
            } else {
                tag.m_128359_("type", "empty");
            }
        }
        return NbtTagPayload.of(tag);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public IGuiTexture deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag tag = nbtTagPayload.getPayload();
            String type = tag.m_128461_("type");
            if (type.equals("ui_resource") && tag.m_128441_("key")) {
                String key = tag.m_128461_("key");
                Resource<IGuiTexture> resource = UIResourceTexture.getProjectResource();
                if (resource == null) {
                    return new UIResourceTexture(key);
                }
                if (UIResourceTexture.isProject()) {
                    return new UIResourceTexture(resource, key);
                }
                return resource.getResourceOrDefault(key, IGuiTexture.MISSING_TEXTURE);
            }
            return IGuiTexture.deserializeWrapper(tag);
        }
        return null;
    }
}
