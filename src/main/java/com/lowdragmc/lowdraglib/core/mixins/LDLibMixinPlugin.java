package com.lowdragmc.lowdraglib.core.mixins;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/LDLibMixinPlugin.class */
public class LDLibMixinPlugin implements IMixinConfigPlugin, MixinPluginShared {
    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("com.lowdragmc.lowdraglib.core.mixins.jei")) {
            return IS_JEI_LOAD;
        }
        if (mixinClassName.contains("com.lowdragmc.lowdraglib.core.mixins.rei")) {
            return IS_REI_LOAD;
        }
        if (mixinClassName.contains("com.lowdragmc.lowdraglib.core.mixins.emi")) {
            return IS_MEI_LOAD;
        }
        return true;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
