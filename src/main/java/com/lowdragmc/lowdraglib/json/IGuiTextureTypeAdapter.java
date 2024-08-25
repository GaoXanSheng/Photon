package com.lowdragmc.lowdraglib.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/IGuiTextureTypeAdapter.class */
public class IGuiTextureTypeAdapter implements TypeAdapterFactory {
    public static final IGuiTextureTypeAdapter INSTANCE = new IGuiTextureTypeAdapter();

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (IGuiTexture.class.isAssignableFrom(type.getRawType())) {
            return new IGuiTextureAdapter(gson);
        }
        return null;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/IGuiTextureTypeAdapter$IGuiTextureAdapter.class */
    private static final class IGuiTextureAdapter extends TypeAdapter<IGuiTexture> {
        private final Gson gson;

        private IGuiTextureAdapter(Gson gson) {
            this.gson = gson;
        }

        public void write(JsonWriter out, IGuiTexture value) {
            if (value == null) {
                this.gson.toJson(JsonNull.INSTANCE, out);
            } else {
                this.gson.toJson(SimpleIGuiTextureJsonUtils.toJson(value), out);
            }
        }

        /* renamed from: read */
        public IGuiTexture m93read(JsonReader in) {
            JsonElement jsonElement = (JsonElement) this.gson.fromJson(in, JsonElement.class);
            if (jsonElement.isJsonObject()) {
                return SimpleIGuiTextureJsonUtils.fromJson(jsonElement.getAsJsonObject());
            }
            return null;
        }
    }
}
