package com.lowdragmc.photon.forge.core;

import com.lowdragmc.lowdraglib.core.mixins.MixinPluginShared;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/forge/core/PhotonMixinPlugin.class */
public class PhotonMixinPlugin implements IMixinConfigPlugin, MixinPluginShared {
    boolean IS_IRIS_LOAD = MixinPluginShared.isClassFound("net.irisshaders.iris.api.v0.IrisApi");

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return (mixinClassName.contains("com.lowdragmc.photon.forge.core.mixins.no_iris") && this.IS_IRIS_LOAD) ? false : true;
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
