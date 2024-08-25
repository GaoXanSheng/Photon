package com.lowdragmc.lowdraglib.json;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.ShaderTexture;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/SimpleIGuiTextureJsonUtils.class */
public class SimpleIGuiTextureJsonUtils {
    public static JsonObject toJson(IGuiTexture texture) {
        JsonObject jsonObject = new JsonObject();
        if (texture instanceof ResourceTexture) {
            ResourceTexture resourceTexture = (ResourceTexture) texture;
            jsonObject.addProperty("type", "resource");
            jsonObject.addProperty("res", resourceTexture.imageLocation.toString());
        } else {
            if (texture instanceof ItemStackTexture) {
                ItemStackTexture itemStackTexture = (ItemStackTexture) texture;
                if (itemStackTexture.itemStack.length > 0) {
                    jsonObject.addProperty("type", "item");
                    jsonObject.addProperty("res", Registry.f_122827_.m_7981_(itemStackTexture.itemStack[0].m_41720_()).toString());
                }
            }
            if (texture instanceof ShaderTexture) {
                ShaderTexture shaderTexture = (ShaderTexture) texture;
                if (shaderTexture.location != null) {
                    jsonObject.addProperty("type", "shader");
                    jsonObject.addProperty("res", shaderTexture.location.toString());
                }
            }
        }
        return jsonObject;
    }

    public static IGuiTexture fromJson(JsonObject jsonObject) {
        String asString = jsonObject.get("type").getAsString();
        boolean z = true;
        switch (asString.hashCode()) {
            case -903579675:
                if (asString.equals("shader")) {
                    z = true;
                    break;
                }
                break;
            case -341064690:
                if (asString.equals("resource")) {
                    z = false;
                    break;
                }
                break;
            case 3242771:
                if (asString.equals("item")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                return new ResourceTexture(jsonObject.get("res").getAsString());
            case true:
                return new ItemStackTexture((Item) Registry.f_122827_.m_7745_(new ResourceLocation(jsonObject.get("res").getAsString())));
            case true:
                return ShaderTexture.createShader(new ResourceLocation(jsonObject.get("res").getAsString()));
            default:
                return IGuiTexture.EMPTY;
        }
    }
}
