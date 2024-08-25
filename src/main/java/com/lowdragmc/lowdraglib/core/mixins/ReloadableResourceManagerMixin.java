package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.model.custommodel.LDLMetadataSection;
import com.lowdragmc.lowdraglib.gui.texture.ShaderTexture;
import com.lowdragmc.lowdraglib.utils.CustomResourcePack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ReloadableResourceManager.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/ReloadableResourceManagerMixin.class */
public abstract class ReloadableResourceManagerMixin {
    @ModifyVariable(method = {"createReload"}, at = @At("HEAD"), index = 4, argsOnly = true)
    private List<PackResources> injectCreateReload(List<PackResources> resourcePacks) {
        LDLMetadataSection.clearCache();
        if (LDLib.isRemote()) {
            ShaderTexture.clearCache();
        }
        ArrayList<PackResources> mutableList = new ArrayList<>(resourcePacks);
        mutableList.add(new CustomResourcePack(LDLib.getLDLibDir(), "ldlib", PackType.CLIENT_RESOURCES));
        return mutableList;
    }
}
