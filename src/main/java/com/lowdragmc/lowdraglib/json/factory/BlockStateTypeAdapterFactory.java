package com.lowdragmc.lowdraglib.json.factory;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/factory/BlockStateTypeAdapterFactory.class */
public class BlockStateTypeAdapterFactory implements TypeAdapterFactory {
    public static final BlockStateTypeAdapterFactory INSTANCE = new BlockStateTypeAdapterFactory();

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (BlockState.class.isAssignableFrom(type.getRawType())) {
            return new IBlockStateTypeAdapter(gson);
        }
        return null;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/factory/BlockStateTypeAdapterFactory$IBlockStateTypeAdapter.class */
    private static final class IBlockStateTypeAdapter extends TypeAdapter<BlockState> {
        private final Gson gson;

        private IBlockStateTypeAdapter(Gson gson) {
            this.gson = gson;
        }

        public void write(JsonWriter out, BlockState value) {
            if (value == null) {
                this.gson.toJson(JsonNull.INSTANCE, out);
                return;
            }
            Registry.f_122824_.m_7981_(value.m_60734_());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", Registry.f_122824_.m_7981_(value.m_60734_()).toString());
            jsonObject.addProperty("meta", Integer.valueOf(value.m_60734_().m_49965_().m_61056_().indexOf(value)));
            this.gson.toJson(jsonObject, out);
        }

        /* renamed from: read */
        public BlockState m97read(JsonReader in) {
            JsonElement jsonElement = (JsonElement) this.gson.fromJson(in, JsonElement.class);
            if (jsonElement.isJsonNull()) {
                return null;
            }
            ResourceLocation id = new ResourceLocation(jsonElement.getAsJsonObject().get("id").getAsString());
            Block block = (Block) Registry.f_122824_.m_7745_(id);
            if (block != Blocks.f_50016_ || id.equals(new ResourceLocation("air"))) {
                if (jsonElement.getAsJsonObject().has("meta")) {
                    int meta = jsonElement.getAsJsonObject().get("meta").getAsInt();
                    return block.m_49965_().m_61056_().size() > meta ? (BlockState) block.m_49965_().m_61056_().get(meta) : block.m_49966_();
                }
                return block.m_49966_();
            }
            return null;
        }
    }
}
