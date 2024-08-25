package com.lowdragmc.lowdraglib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lowdragmc.lowdraglib.json.IGuiTextureTypeAdapter;
import com.lowdragmc.lowdraglib.json.ItemStackTypeAdapter;
import com.lowdragmc.lowdraglib.json.factory.FluidStackTypeAdapter;
import java.io.File;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/LDLib.class */
public class LDLib {
    public static final String MOD_ID = "ldlib";
    public static final String MODID_JEI = "jei";
    public static final String MODID_RUBIDIUM = "rubidium";
    public static final String MODID_REI = "roughlyenoughitems";
    public static final String MODID_EMI = "emi";
    @Deprecated
    public static File location;
    public static final String NAME = "LowDragLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final Random random = new Random();
    public static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(IGuiTextureTypeAdapter.INSTANCE).registerTypeAdapterFactory(FluidStackTypeAdapter.INSTANCE).registerTypeAdapter(ItemStack.class, ItemStackTypeAdapter.INSTANCE).registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

    public static void init() {
        LOGGER.info("{} is initializing on platform: {}", NAME, Platform.platformName());
        getLDLibDir();
    }

    public static File getLDLibDir() {
        if (location == null) {
            location = new File(Platform.getGamePath().toFile(), "ldlib");
            if (location.mkdir()) {
                LOGGER.info("create ldlib config folder");
            }
        }
        return location;
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation("ldlib", path);
    }

    public static boolean isClient() {
        return Platform.isClient();
    }

    public static boolean isRemote() {
        if (isClient()) {
            return Minecraft.m_91087_().m_18695_();
        }
        return false;
    }

    public static boolean isModLoaded(String mod) {
        return Platform.isModLoaded(mod);
    }

    public static boolean isJeiLoaded() {
        return (isEmiLoaded() || isReiLoaded() || !isModLoaded(MODID_JEI)) ? false : true;
    }

    public static boolean isReiLoaded() {
        return isModLoaded(MODID_REI);
    }

    public static boolean isEmiLoaded() {
        return isModLoaded(MODID_EMI);
    }
}
