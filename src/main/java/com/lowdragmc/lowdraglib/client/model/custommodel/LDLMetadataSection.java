package com.lowdragmc.lowdraglib.client.model.custommodel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/model/custommodel/LDLMetadataSection.class */
public class LDLMetadataSection {
    public static final String SECTION_NAME = "ldlib";
    private static final Map<ResourceLocation, LDLMetadataSection> METADATA_CACHE = new ConcurrentHashMap();
    public static final LDLMetadataSection MISSING = new LDLMetadataSection(false, null);
    public final boolean emissive;
    public final ResourceLocation connection;

    public LDLMetadataSection(boolean emissive, ResourceLocation connection) {
        this.emissive = emissive;
        this.connection = connection;
    }

    public static void clearCache() {
        METADATA_CACHE.clear();
    }

    public boolean isMissing() {
        return this == MISSING;
    }

    @Nonnull
    public static LDLMetadataSection getMetadata(ResourceLocation res) {
        if (METADATA_CACHE.containsKey(res)) {
            return METADATA_CACHE.get(res);
        }
        LDLMetadataSection ret = MISSING;
        Optional<Resource> resourceOptional = Minecraft.m_91087_().m_91098_().m_213713_(res);
        if (resourceOptional.isPresent()) {
            Resource resource = resourceOptional.get();
            try {
                ret = (LDLMetadataSection) resource.m_215509_().m_214059_(Serializer.INSTANCE).get();
            } catch (Exception e) {
            }
        }
        METADATA_CACHE.put(res, ret);
        return ret;
    }

    @Nonnull
    public static LDLMetadataSection getMetadata(TextureAtlasSprite sprite) {
        return getMetadata(spriteToAbsolute(sprite.m_118413_()));
    }

    public static boolean isEmissive(TextureAtlasSprite sprite) {
        LDLMetadataSection ret = getMetadata(spriteToAbsolute(sprite.m_118413_()));
        return ret.emissive;
    }

    @Nullable
    public static TextureAtlasSprite getConnection(TextureAtlasSprite sprite) {
        LDLMetadataSection ret = getMetadata(spriteToAbsolute(sprite.m_118413_()));
        if (ret.connection == null) {
            return null;
        }
        return ModelFactory.getBlockSprite(ret.connection);
    }

    public static ResourceLocation spriteToAbsolute(ResourceLocation sprite) {
        if (!sprite.m_135815_().startsWith("textures/")) {
            sprite = new ResourceLocation(sprite.m_135827_(), "textures/" + sprite.m_135815_());
        }
        if (!sprite.m_135815_().endsWith(".png")) {
            sprite = new ResourceLocation(sprite.m_135827_(), sprite.m_135815_() + ".png");
        }
        return sprite;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/model/custommodel/LDLMetadataSection$Serializer.class */
    public static class Serializer implements MetadataSectionSerializer<LDLMetadataSection> {
        static Serializer INSTANCE = new Serializer();

        @Nonnull
        public String m_7991_() {
            return "ldlib";
        }

        @Nonnull
        /* renamed from: fromJson */
        public LDLMetadataSection m_6322_(@Nonnull JsonObject json) {
            boolean emissive = false;
            ResourceLocation connection = null;
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                if (obj.has("emissive")) {
                    JsonElement element = obj.get("emissive");
                    if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                        emissive = element.getAsBoolean();
                    }
                }
                if (obj.has("connection")) {
                    JsonElement element2 = obj.get("connection");
                    if (element2.isJsonPrimitive() && element2.getAsJsonPrimitive().isString()) {
                        connection = new ResourceLocation(element2.getAsString());
                    }
                }
            }
            return new LDLMetadataSection(emissive, connection);
        }
    }
}
