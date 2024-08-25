package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientLanguage.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/LanguageMixin.class */
public abstract class LanguageMixin {
    @Inject(method = {"getOrDefault"}, at = {@At("HEAD")}, cancellable = true)
    private void injectGet(String pId, CallbackInfoReturnable<String> cir) {
        if (LocalizationUtils.RESOURCE != null && LocalizationUtils.RESOURCE.hasResource(pId)) {
            cir.setReturnValue(LocalizationUtils.RESOURCE.getResource(pId));
        }
    }
}
