package com.lowdragmc.lowdraglib.syncdata.accessor;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/accessor/RecipeAccessor.class */
public class RecipeAccessor extends CustomObjectAccessor<Recipe> {
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public /* bridge */ /* synthetic */ Recipe deserialize(AccessorOp accessorOp, ITypedPayload iTypedPayload) {
        return deserialize(accessorOp, (ITypedPayload<?>) iTypedPayload);
    }

    public RecipeAccessor() {
        super(Recipe.class, true);
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public ITypedPayload<?> serialize(AccessorOp op, Recipe value) {
        ResourceLocation type = Registry.f_122864_.m_7981_(value.m_6671_());
        ResourceLocation id = value.m_6423_();
        CompoundTag tag = new CompoundTag();
        tag.m_128359_("type", type == null ? "null" : type.toString());
        tag.m_128359_("id", id.toString());
        return NbtTagPayload.of(tag);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor
    public Recipe deserialize(AccessorOp op, ITypedPayload<?> payload) {
        RecipeManager recipeManager;
        if (payload instanceof NbtTagPayload) {
            NbtTagPayload nbtTagPayload = (NbtTagPayload) payload;
            CompoundTag payload2 = nbtTagPayload.getPayload();
            if (payload2 instanceof CompoundTag) {
                CompoundTag tag = payload2;
                RecipeType<?> type = (RecipeType) Registry.f_122864_.m_7745_(new ResourceLocation(tag.m_128461_("type")));
                ResourceLocation id = new ResourceLocation(tag.m_128461_("id"));
                if (type != null) {
                    if (LDLib.isRemote()) {
                        recipeManager = Minecraft.m_91087_().m_91403_().m_105141_();
                    } else {
                        recipeManager = Platform.getMinecraftServer().m_129894_();
                    }
                    for (Recipe<?> recipe : recipeManager.m_44051_()) {
                        if (recipe.m_6671_() == type && recipe.m_6423_().equals(id)) {
                            return recipe;
                        }
                    }
                    return null;
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
