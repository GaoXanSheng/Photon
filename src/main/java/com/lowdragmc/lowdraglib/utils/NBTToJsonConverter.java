package com.lowdragmc.lowdraglib.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/NBTToJsonConverter.class */
public class NBTToJsonConverter {
    public static JsonElement getObject(CompoundTag tag) {
        int[] m_128648_;
        JsonArray jsonArray;
        Set<String> keys = tag.m_128431_();
        JsonObject jsonRoot = new JsonObject();
        for (String key : keys) {
            CompoundTag m_128423_ = tag.m_128423_(key);
            if (m_128423_ instanceof CompoundTag) {
                jsonArray = getObject(m_128423_);
            } else if (m_128423_ instanceof NumericTag) {
                jsonArray = new JsonPrimitive(Double.valueOf(((NumericTag) m_128423_).m_7061_()));
            } else if (m_128423_ instanceof StringTag) {
                jsonArray = new JsonPrimitive(m_128423_.m_7916_());
            } else if (m_128423_ instanceof ListTag) {
                ListTag tagList = (ListTag) m_128423_;
                JsonArray array = new JsonArray();
                for (int i = 0; i < tagList.size(); i++) {
                    if (tagList.m_7264_() == 10) {
                        array.add(getObject(tagList.m_128728_(i)));
                    } else if (tagList.m_7264_() == 8) {
                        array.add(new JsonPrimitive(tagList.m_128778_(i)));
                    }
                }
                jsonArray = array;
            } else if (!(m_128423_ instanceof IntArrayTag)) {
                byte var10002 = m_128423_.m_7060_();
                throw new IllegalArgumentException("NBT to JSON converter doesn't support the nbt tag: " + var10002 + ", tag: " + m_128423_);
            } else {
                IntArrayTag intArray = (IntArrayTag) m_128423_;
                JsonArray array2 = new JsonArray();
                for (int i2 : intArray.m_128648_()) {
                    array2.add(new JsonPrimitive(Integer.valueOf(i2)));
                }
                jsonArray = array2;
            }
            jsonRoot.add(key, jsonArray);
        }
        return jsonRoot;
    }
}
