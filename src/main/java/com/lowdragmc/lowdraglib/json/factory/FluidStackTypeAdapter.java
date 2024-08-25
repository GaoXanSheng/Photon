package com.lowdragmc.lowdraglib.json.factory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/factory/FluidStackTypeAdapter.class */
public class FluidStackTypeAdapter implements TypeAdapterFactory {
    public static final FluidStackTypeAdapter INSTANCE = new FluidStackTypeAdapter();

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (FluidStack.class.isAssignableFrom(type.getRawType())) {
            return new IFluidStackAdapter(gson);
        }
        return null;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/json/factory/FluidStackTypeAdapter$IFluidStackAdapter.class */
    private static final class IFluidStackAdapter extends TypeAdapter<FluidStack> {
        private final Gson gson;

        private IFluidStackAdapter(Gson gson) {
            this.gson = gson;
        }

        public void write(JsonWriter out, FluidStack value) {
            if (value == null) {
                this.gson.toJson(JsonNull.INSTANCE, out);
            } else {
                this.gson.toJson(new JsonPrimitive(value.saveToTag(new CompoundTag()).toString()), out);
            }
        }

        /* renamed from: read */
        public FluidStack m99read(JsonReader in) {
            JsonElement jsonElement = (JsonElement) this.gson.fromJson(in, JsonElement.class);
            if (jsonElement.isJsonObject()) {
                try {
                    return FluidStack.loadFromTag(TagParser.m_129359_(jsonElement.getAsString()));
                } catch (CommandSyntaxException e) {
                    return FluidStack.empty();
                }
            }
            return null;
        }
    }
}
