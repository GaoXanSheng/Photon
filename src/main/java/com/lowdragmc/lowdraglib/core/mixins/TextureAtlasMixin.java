package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.client.model.custommodel.LDLMetadataSection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({TextureAtlas.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/TextureAtlasMixin.class */
public abstract class TextureAtlasMixin {
    @Inject(method = {"getBasicSpriteInfos"}, at = {@At("HEAD")})
    protected void injectStateToModelLocation(ResourceManager resourceManager, Set<ResourceLocation> spriteNames, CallbackInfoReturnable<Collection<TextureAtlasSprite.Info>> cir) {
        Set<ResourceLocation> append = new HashSet<>();
        for (ResourceLocation spriteName : spriteNames) {
            LDLMetadataSection data = LDLMetadataSection.getMetadata(LDLMetadataSection.spriteToAbsolute(spriteName));
            if (data.connection != null) {
                append.add(data.connection);
            }
        }
        if (!append.isEmpty()) {
            spriteNames.addAll(append);
        }
    }
}
