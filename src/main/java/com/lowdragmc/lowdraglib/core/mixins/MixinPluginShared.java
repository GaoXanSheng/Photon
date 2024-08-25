package com.lowdragmc.lowdraglib.core.mixins;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/MixinPluginShared.class */
public interface MixinPluginShared {
    public static final boolean IS_OPT_LOAD = isClassFound("optifine.OptiFineTranformationService");
    public static final boolean IS_DASH_LOADER = isClassFound("dev.quantumfusion.dashloader.mixin.MixinPlugin");
    public static final boolean IS_SODIUM_LOAD = isClassFound("me.jellysquid.mods.sodium.mixin.SodiumMixinPlugin");
    public static final boolean IS_JEI_LOAD = isClassFound("mezz.jei.core.config.GiveMode");
    public static final boolean IS_REI_LOAD = isClassFound("me.shedaniel.rei.api.common.entry.EntryStack");
    public static final boolean IS_MEI_LOAD = isClassFound("dev.emi.emi.api.EmiFillAction");
    public static final boolean IS_RUBIDIUM_LOAD = IS_SODIUM_LOAD;

    static boolean isClassFound(String className) {
        try {
            Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
