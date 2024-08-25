package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.Resource;
import net.minecraft.client.resources.language.I18n;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/LocalizationUtils.class */
public class LocalizationUtils {
    public static Resource<String> RESOURCE;

    public static void setResource(Resource<String> resource) {
        RESOURCE = resource;
    }

    public static void clearResource() {
        RESOURCE = null;
    }

    public static String format(String localisationKey, Object... substitutions) {
        if (!LDLib.isClient()) {
            return String.format(localisationKey, substitutions);
        }
        if (RESOURCE != null && RESOURCE.hasResource(localisationKey)) {
            return RESOURCE.getResource(localisationKey);
        }
        return I18n.m_118938_(localisationKey, substitutions);
    }

    public static boolean exist(String localisationKey) {
        if (LDLib.isClient()) {
            return I18n.m_118936_(localisationKey);
        }
        return false;
    }
}
