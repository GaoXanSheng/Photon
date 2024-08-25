package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.utils.forge.LDLItemGroupImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/LDLItemGroup.class */
public class LDLItemGroup extends CreativeModeTab {
    protected String domain;
    protected String id;
    protected Supplier<ItemStack> iconSupplier;

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static int expandArrayAndGetId() {
        return LDLItemGroupImpl.expandArrayAndGetId();
    }

    public LDLItemGroup(String domain, String id, Supplier<ItemStack> iconSupplier) {
        super(expandArrayAndGetId(), domain + "." + id);
        this.domain = domain;
        this.id = id;
        this.iconSupplier = iconSupplier;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getGroupId() {
        return this.id;
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack m_6976_() {
        return this.iconSupplier.get();
    }
}
