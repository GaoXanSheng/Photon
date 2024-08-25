package com.lowdragmc.lowdraglib.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/BlockTypeAdapterFactory.class */
public class BlockTypeAdapterFactory implements TypeAdapterFactory {
    public static final BlockTypeAdapterFactory INSTANCE = new BlockTypeAdapterFactory();

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (Block.class.isAssignableFrom(type.getRawType())) {
            return new BlockTypeAdapter(gson);
        }
        return null;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/BlockTypeAdapterFactory$BlockTypeAdapter.class */
    private static final class BlockTypeAdapter extends TypeAdapter<Block> {
        private final Gson gson;

        private BlockTypeAdapter(Gson gson) {
            this.gson = gson;
        }

        public void write(JsonWriter out, Block value) {
            if (value == null) {
                this.gson.toJson(JsonNull.INSTANCE, out);
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", Registry.f_122824_.m_7981_(value).toString());
            this.gson.toJson(jsonObject, out);
        }

        /* renamed from: read */
        public Block m91read(JsonReader in) {
            JsonElement jsonElement = (JsonElement) this.gson.fromJson(in, JsonElement.class);
            if (jsonElement.isJsonNull()) {
                return null;
            }
            return (Block) Registry.f_122824_.m_7745_(new ResourceLocation(jsonElement.getAsJsonObject().get("id").getAsString()));
        }
    }
}
