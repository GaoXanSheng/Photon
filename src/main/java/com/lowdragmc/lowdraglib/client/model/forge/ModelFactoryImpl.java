package com.lowdragmc.lowdraglib.client.model.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/model/forge/ModelFactoryImpl.class */
public class ModelFactoryImpl {
    public static ModelBakery getModeBakery() {
        return Minecraft.m_91087_().m_91304_().getModelBakery();
    }

    public static UnbakedModel getLDLibModel(UnbakedModel vanilla) {
        return vanilla;
    }
}
